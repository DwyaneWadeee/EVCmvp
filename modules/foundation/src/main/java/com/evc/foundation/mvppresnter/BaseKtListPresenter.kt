package com.evc.foundation.mvppresnter

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bigkoo.katafoundation.model.ResponseList
import com.chad.library.adapter.base.BaseQuickAdapter
import com.evc.foundation.mvpview.BaseKtListView
import com.evc.foundation.net.HttpResult
import com.evc.foundation.net.HttpResultObserver
import com.evc.foundation.view.XLinearLayoutManager
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

/**
 *
 * @Package: com.evc.foundation.mvppresnter
 * @Description: java类作用描述
 * @Author: EvanChan
 * @CreateDate: 9/15/21 4:24 PM
 * @m-mail: dadaintheair@gmail.com
 */
open class BaseKtListPresenter<V:BaseKtListView>:BaseDataPresenter<V>()  {
    protected var mLoadingMoreListener: BaseQuickAdapter.RequestLoadMoreListener? = null
    protected var mOnItemClickListener: BaseQuickAdapter.OnItemClickListener? = null
    protected var mOnItemChildClickListener: BaseQuickAdapter.OnItemChildClickListener? = null
    protected var mLayoutManager: RecyclerView.LayoutManager? = null

    companion object {
        //默认开始页码
        const val firstPage = 1
    }

    var page = firstPage

    //默认一页的请求数量
    var pageSize = 10

    var isHasMore = false
    var isLoadingMore = false

    var requestFail = false

    open fun loadData(){

    }

    fun onLoadMore() {
        page += 1

        loadData()
    }

    fun refresh() {
        page = firstPage

        loadData()
    }


    open fun getLoadingMoreListener(): BaseQuickAdapter.RequestLoadMoreListener? {
        if (mLoadingMoreListener == null) {
            mLoadingMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
                if (!isLoadingMore && !isRefreshing() && isHasMore) {
                    onLoadMore()
                }
            }
        }
        return mLoadingMoreListener
    }
    open fun getOnItemClickListener(): BaseQuickAdapter.OnItemClickListener? {
        if (mOnItemClickListener == null) {
            mOnItemClickListener =
                BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
                    if (null != getThatView()) {
                        getThatView()?.onItemClick(adapter, view, position)
                    }
                }
        }
        return mOnItemClickListener
    }

    open fun getOnItemChildClickListener(): BaseQuickAdapter.OnItemChildClickListener? {
        if (mOnItemChildClickListener == null) {
            mOnItemChildClickListener =
                BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
                    if (null != getThatView()) {
                        getThatView()?.onItemChildClick(adapter, view, position)
                    }
                }
        }
        return mOnItemChildClickListener
    }

    open fun getLayoutManager(): RecyclerView.LayoutManager? {
        if (mLayoutManager == null) {
            mLayoutManager = XLinearLayoutManager(context)
        }
        return mLayoutManager
    }


    /**
     * list 网络请求处理
     */
    protected fun <DATA : Any?> Observable<HttpResult<ResponseList<DATA?>>>.responseList(
        loadingAnim: Boolean = true,
        showLoading: Boolean = false,
        onHttpFail: (code: Int, msg: String?) -> Unit = { code, msg -> },
        onNetWorkError: (msg: String?) -> Unit = {},
        onDisposeEmptyResult: (result: ResponseList<DATA?>?, msg: String?) -> Unit = { result, msg -> },
        onHttpSuccess: (ResponseList<DATA?>) -> Unit = {}
    ) = subscribe(object : HttpResultObserver<ResponseList<DATA?>>() {
        override fun onSubscribe(d: Disposable) {
            super.onSubscribe(d)
            if (loadingAnim) {
                if (page == firstPage) {
                    setStatusLoading()
                }
            }

            if (showLoading) {
                getThatView()?.showLoading()
            }
        }

        override fun onHttpSuccess(result: ResponseList<DATA?>, msg: String?) {
            super.onHttpSuccess(result, msg)

            isHasMore = result?.pages != result?.pageNum
            getThatView()?.onLoadingMore(isHasMore)

            if (loadingAnim) {
                getThatView()?.showContent()
            }

            if (showLoading) {
                getThatView()?.dismissLoading()
            }

            if (result != null) {
                onHttpSuccess(result)
            } else {
                onDisposeEmptyResult(result, msg)

                if (loadingAnim) {
                    setStatusEmpty(msg)
                }
            }

        }

        override fun onHttpFail(code: Int, msg: String?) {
            Log.d("BaseDetailPresenter", "code= $code  msg:$msg")
            if (loadingAnim) {
                setStatusError(code, msg)
                onHttpFail(code, msg)
            } else {
                onHttpFail(code, msg)
            }

            if (showLoading) {
                getThatView()?.dismissLoading()
            }
        }

        override fun onNetWorkError(msg: String?) {
            Log.d("BaseDetailPresenter", "msg:$msg")
            // 这里需要过滤一下这个，不知道为什么会抛出： failed to connect to /139.9.48.179 (port 443) from /10.10.85.223 (port 46446) after 15000ms: isConnected failed: ETIMEDOUT (Connection timed out)
            val customMsg = "failed to connect to"
            if (msg != null && !msg.startsWith(customMsg)) {
                setStatusNetworkError(msg)
            }
            onNetWorkError(msg)

            if (showLoading) {
                getThatView()?.dismissLoading()
            }
        }

        override fun onComplete() {
            setOnce(true)
            setRefreshing(false)
            onHttpCompleted()

            if (showLoading) {
                getThatView()?.dismissLoading()
            }
        }
    })
}