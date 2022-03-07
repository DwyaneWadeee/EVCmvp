package com.evc.foundation.mvppresnter

import com.evc.foundation.R
import com.evc.foundation.mvpview.BaseKtView
import com.evc.foundation.rx.MainThread
import com.evc.foundation.rx.RxJavaUtils
import com.evc.toolkit.rx.RxBus
import com.trello.rxlifecycle3.RxLifecycle
import com.trello.rxlifecycle3.android.ActivityEvent
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject

/**
 *
 * @Package: com.evc.foundation
 * @Description: BaseDataPresenter
 * @Author: EvanChan
 * @CreateDate: 8/10/21 2:49 PM
 * @m-mail: dadaintheair@gmail.com
 */
abstract class BaseDataPresenter<V:BaseKtView> :
    BasePresenter<V>(){
    protected val lifecycleSubject: BehaviorSubject<*>? = BehaviorSubject.create<BehaviorSubject<*>>()

    //刷新状态
    private var refreshing = false

    //加载中状态
    private var statusLoading = false

    //控制loading状态只有一次,对于列表的loading概念，就是首次加载数据，其余加载是刷新
    private var once = false

    open fun isOnce(): Boolean {
        return once
    }

    open fun setOnce(once: Boolean) {
        this.once = once
    }

    open fun isRefreshing(): Boolean {
        return refreshing
    }

    open fun setRefreshing(refreshing: Boolean) {
        this.refreshing = refreshing
        if (view != null) {
            view!!.onRefreshing(refreshing)
        }
    }

    open fun setStatusEmpty(msg: String?) {
        view!!.onHttpEmptySuccess(msg)
    }

    open fun isStatusLoading(): Boolean {
        return statusLoading
    }

    open fun setStatusLoading() {
        statusLoading = true
        view!!.onStatusLoading()
    }

    open fun setStatusError(code: Int, msg: String?) {
        view!!.onHttpError(code, msg)
        view!!.showToast(msg)
    }

    open fun setStatusNetworkError(msg: String?) {
        view!!.onHttpNetworkError(msg)
        view!!.showToast(msg)
    }

    open fun onHttpCompleted() {
        if (view != null) {
            view!!.onHttpCompleted()
        }
    }

    override fun onDestroy() {
//        lifecycleSubject?.onNext(ActivityEvent.DESTROY as Nothing)
        super.onDestroy()
    }


    open fun <T> toObservable(
        eventType: Class<T>?,
        onNext: Consumer<in T>?
    ) {
        RxBus.getInstance().toObservable(eventType).compose(MainThread.io())
            .compose(RxLifecycle.bindUntilEvent(lifecycleSubject!! as BehaviorSubject<ActivityEvent> , ActivityEvent.DESTROY))
            .subscribe(onNext)
    }

    open fun post(event: Any?) {
        RxBus.getInstance().post(event)
    }


//    abstract fun <Data> onLoadDataHttpRequest(): Observable<HttpResult<Data>?>?

//    abstract fun onLoadData()

    protected fun <R> onCallHttpRequest(observable: Observable<R>,callBack : Observer<R>){
        observable.compose(MainThread.io())
            .compose(RxLifecycle.bindUntilEvent<R,ActivityEvent>(lifecycleSubject as Observable<ActivityEvent>, ActivityEvent.DESTROY ) )
            .subscribe(callBack)
    }

    protected fun <T> onCallOvservableDelay(seconds:Int,callBack:Observer<T>){
        RxJavaUtils.observableTimeDelay(seconds)
            .compose(RxJavaUtils.observableBindLifecycle(lifecycleSubject as Observable<R>))
            .subscribe(callBack  as Observer<Long?>)
    }

}

