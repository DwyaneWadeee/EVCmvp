package com.evc.foundation.rx

import com.trello.rxlifecycle3.RxLifecycle
import com.trello.rxlifecycle3.android.ActivityEvent
import io.reactivex.FlowableTransformer
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 *
 * @Package: com.evc.foundation.rx
 * @Description: java类作用描述
 * @Author: EvanChan
 * @CreateDate: 8/26/21 2:39 PM
 * @m-mail: dadaintheair@gmail.com
 */
object RxJavaUtils {


    /**
     * 延迟x秒执行
     */
    @JvmStatic
    fun observableTimeDelay(seconds: Int): Observable<Long?> {
        return Observable.just(0L).delay(seconds.toLong(), TimeUnit.SECONDS).compose(RxJavaUtils.observableSchedulers())
    }

    /**
     * [ObservableTransformer]线程调度器
     */
    @JvmStatic
    fun <T> observableSchedulers(): ObservableTransformer<T, T>? {
        return ObservableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    /**
     * [FlowableTransformer]线程调度器
     */
    @JvmStatic
    fun <T> flowableSchedulers(): FlowableTransformer<T, T>? {
        return FlowableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    /**
     * 防止内存泄露
     */
    fun <T,R>observableBindLifecycle(ob:Observable<R>):ObservableTransformer<T,T>{
        return ObservableTransformer { upstream ->
            upstream.compose(
                RxLifecycle.bindUntilEvent(
                    ob , ActivityEvent.DESTROY as R))
        }
    }


}