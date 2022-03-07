package com.evc.foundation.mvppresnter

import android.util.Log
import com.evc.foundation.mvpview.BaseKtView
import com.evc.foundation.net.HttpResult
import com.evc.foundation.net.HttpResultObserver
import com.evc.foundation.rx.MainThread
import com.trello.rxlifecycle3.RxLifecycle
import com.trello.rxlifecycle3.android.ActivityEvent
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

/**
 *
 * @Package: com.evc.foundation.mvppresnter
 * @Description: Presenter基类
 * @Author: EvanChan
 * @CreateDate: 8/31/21 4:18 PM
 * @m-mail: dadaintheair@gmail.com
 */
open class BaseKtPresenter<V:BaseKtView>:
    BaseDataPresenter<V>() {
    /**
     * 封装网络请求
     */
    protected fun <R> requset(requestApoi : Observable<R>):Observable<R>{
//        RxJavaUtils.observableBindLifecycle(lifecycleSubject as Observable<T>)
        var bindingLifecycle = RxLifecycle.bindUntilEvent<R, ActivityEvent>(
            lifecycleSubject as Observable<ActivityEvent>,
            ActivityEvent.DESTROY
        )
        return requestApoi.compose(MainThread.io<R>()).compose(bindingLifecycle)
    }

    fun <K : Any?> Observable<HttpResult<K?>>.response(
        loadingAnim: Boolean = true,
        showLoading: Boolean = false,
        loadingMsg: String? = null,
        onHttpFail: (code:Int, msg: String?) -> Unit = {code,msg->},
        onNetworkError: (msg: String?) -> Unit = {},
        onDisposeEmptyResult: (result: K?, msg: String?) -> Unit = { result, msg -> },
        onComplete: () -> Unit = {},
        showErrorMsg: Boolean = true,
        onHttpSuccess: (K) -> Unit = {}
    ) = subscribe(object :HttpResultObserver<K?>(){
        override fun onSubscribe(disposable: Disposable) {
            super.onSubscribe(disposable)
            if(loadingAnim){
                setStatusLoading()
            }
            if (showLoading){
                val loadingMessage = loadingMsg?:"加载中"
                view?.showLoading(loadingMessage)
            }
        }

        override fun onHttpSuccess(result: K?, msg: String?) {
            super.onHttpSuccess(result, msg)
            if (loadingAnim){
                view?.showContent()
            }

            if (showLoading){
                view?.dismissLoading()
            }

            if (result!=null){
                onHttpSuccess(result)
            }else{
                if (loadingAnim){
                    setStatusEmpty(msg)
                }
                onDisposeEmptyResult(result, msg)
            }
        }

        override fun onHttpFail(code:Int,msg:String?){
            Log.d("BaseKtPresenter", "code= $code  msg:$msg")
            if (loadingAnim){
                setStatusError(code, msg)
                onHttpFail(code,msg)
            }else{
                onHttpFail(code, msg)
                if (showErrorMsg){
                    var failmsg = msg
                    if(msg?.contains("未知异常")!!){
                        failmsg = "服务器出了小差，请稍候再试"
                    }
                    view?.showToast(failmsg)
                }
            }

            if (showLoading) {
                view?.dismissLoading()
            }
        }

        override fun onNetWorkError(msg: String?) {
            Log.d("BaseKtPresenter", "msg:$msg")
            // 这里需要过滤一下这个，不知道为什么会抛出： failed to connect to /139.9.48.179 (port 443) from /10.10.85.223 (port 46446) after 15000ms: isConnected failed: ETIMEDOUT (Connection timed out)
            val customMsg = "failed to connect to"
            if (loadingAnim) {
                setStatusNetworkError(msg)
            } else {
//                onNetWorkError(msg)
            }
            if (msg != null && !msg.startsWith(customMsg)) {
                view?.showToast(msg)
            }
            if (showLoading) {
                view?.dismissLoading()
            }
        }

        override fun onComplete() {
            setOnce(true)
            setRefreshing(false)
            onHttpCompleted()
            if (showLoading){
                view?.dismissLoading()
            }
        }
    })


}