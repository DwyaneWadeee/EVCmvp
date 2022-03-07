package com.evc.foundation.net

import android.accounts.NetworkErrorException
import android.util.Log
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException


/**
 *
 * @Package: com.evc.foundation.net
 * @Description: 网络结果预处理
 * @Author: EvanChan
 * @CreateDate: 9/1/21 11:07 AM
 * @m-mail: dadaintheair@gmail.com
 */
open class HttpResultObserver<T>: Observer<HttpResult<T>> {
    override fun onComplete() {
    }

    override fun onSubscribe(disposable: Disposable) {
    }

    override fun onNext(result: HttpResult<T>) {
        if (result==null){
            //通常是服务器出错返回了非约定格式
            onHttpFail(HttpStatusConstants.CODE_DEFAULT,"网络错误,返回非正常格式，请稍后再试")
        } else {
            //正确返回约定的CODE_SUCCESS码
            if (result.code == HttpStatusConstants.CODE_SUCCESS){
                onHttpSuccess(result.data,result.message)
            }else{
                //返回约定的其他类型码，可根据返回码进行相对应的操作,这里屏蔽掉被挤下线掉toast
                if (result.code != HttpStatusConstants.HTTPCODE_STATUS_KICK){
                    onHttpFail(result.code, result.message)
                }
            }
        }
    }

    override fun onError(e: Throwable) {
        try {
            if (e is ConnectException
                || e is TimeoutException
                || e is NetworkErrorException
                || e is HttpException
                || e is UnknownHostException){
                var msg = e.message
                //401token校验失败的，不toast
                if (msg != null && msg.contains("HTTP 401")) {
                    return
                }
                Log.e("网络异常为什么弹出1", e.message)
                Log.e("网络异常为什么弹出2", e.localizedMessage)

                onNetWorkError("网络异常")
            }else if (e is SocketTimeoutException){
                onNetWorkError("网络连接超时")
            }else{
                onNetWorkError(e.message)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            e.printStackTrace()
            onComplete()
        }
    }

    /**
     * 正常返回结果
     *
     * @param result 结果
     * @param msg    附带消息
     */
    open fun onHttpSuccess(result: T, msg: String?) {}

    /**
     * 正常返回但code不是CODE_SUCCESS
     *
     * @param code 约定的错误码
     * @param msg  附带消息
     */
    open fun onHttpFail(code: Int, msg: String?) {}

    /**
     * 非正常返回，通常是网络异常问题
     *
     * @param msg 异常描述
     */
    open fun onNetWorkError(msg: String?) {}
}