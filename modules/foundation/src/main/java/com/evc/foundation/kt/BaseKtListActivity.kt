package com.evc.foundation.kt

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.loadmore.LoadMoreView
import com.evc.foundation.R
import com.evc.foundation.mvppresnter.BaseKtListPresenter
import com.evc.foundation.mvpview.BaseKtListView
import com.evc.foundation.utils.LoadingDialogUtil
import com.evc.foundation.utils.TUtil
import com.evc.foundation.view.*
import kotlinx.android.synthetic.main.activity_toolbar_recycler_view.*

/**
 *
 * @Package: com.evc.foundation.kt
 * @Description: BaseListActivity基类
 * @Author: EvanChan
 * @CreateDate: 9/15/21 4:21 PM
 * @m-mail: dadaintheair@gmail.com
 */
abstract class BaseKtListActivity<P:BaseKtListPresenter<*>> :BaseActivity(),BaseKtListView{
    protected var presenter: P? = null
    protected var mLayoutManager: RecyclerView.LayoutManager? = null


    protected var refreshable = true
    protected var recyclerView: RecyclerView? = null
    var adapter: BaseQuickAdapter<*, *>? = null
    protected var loadMoreView: LoadMoreView? = null

    //用于修改不需要endView的场景，默认需要
    protected var enviewGone = false
    protected var loadingMoreGone = false //默认没有加载更多

    protected abstract fun setContentViewToRoot(): View

    protected open fun setToolbarTitle(): Int? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initPresenter()
        super.onCreate(savedInstanceState)

    }

    protected open fun initPresenter() {
        presenter = TUtil.getT(this, 0)
        if (presenter != null) {
            presenter!!.setVM(this as Nothing, this)
        }
    }

    override fun onDestroy() {
        if (presenter != null) {
            presenter!!.onDestroy()
        }
        super.onDestroy()
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_toolbar_recycler_view
    }

    override fun initView() {
        val params = ViewGroup.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )

        multipleStatusView.addView(setContentViewToRoot(), params)
        recyclerView = findViewById(R.id.recyclerView)
        adapter = getCurrentAdapter()

        if (recyclerView != null) {
            recyclerView?.layoutManager = presenter?.getLayoutManager()
        }


        if (refreshable) {
            smart_refresh_layout.setOnRefreshListener {
                refresh()
            }
        } else {
            smart_refresh_layout.setEnableRefresh(false)
        }

        loadMoreView = if (enviewGone) {
            LoadMoreViewNoEndViewText()
        } else {
            CommonLoadMoreView()
        }
        if (loadingMoreGone) {
            loadMoreView = NoLoadMoreLoadingView()
        }

        adapter?.setLoadMoreView(loadMoreView)
        Log.e(TAG, "recycler view $recyclerView")
        adapter?.bindToRecyclerView(recyclerView)
        recyclerView?.setAdapter(adapter)
    }

    override fun initData() {
        setToolbarTitle()?.let {
            topBarView.titleText = getString(it)
        }
    }

    override fun initListener() {
        super.initListener()
        presenter?.let { presenter ->
            adapter?.setOnLoadMoreListener(presenter.getLoadingMoreListener(), recyclerView)
            adapter?.onItemClickListener = presenter.getOnItemClickListener()
            adapter?.onItemChildClickListener = presenter.getOnItemChildClickListener()
        }
    }

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
    }


    override fun onItemChildClick(
        adapter: BaseQuickAdapter<*, *>?,
        view: View?,
        position: Int
    ): Boolean {
        return true
    }

    override fun onLoadingMore(isLoadingMore: Boolean) {
        if (!isLoadingMore) {
            if (adapter!!.isLoading) {
                adapter!!.loadMoreComplete()
            }
        }
        if (!presenter?.isHasMore!!) {
            adapter!!.loadMoreEnd()
        }
    }

    override fun onRefreshing(refreshing: Boolean) {
        if (!refreshable) return
        if (refreshing) {
        } else {
            smart_refresh_layout.finishRefresh()
        }
    }

    override fun onHttpEmptySuccess(msg: String?) {
        multipleStatusView.showEmpty()
    }

    fun showEmpty(resId: Int) {
        multipleStatusView.showEmpty(resId, MultipleStatusView.DEFAULT_LAYOUT_PARAMS)
    }

    override fun onStatusLoading() {
        multipleStatusView.showLoading()
    }

    override fun onHttpError(code: Int, msg: String?) {
        multipleStatusView.showError()
    }

    override fun onHttpNetworkError(msg: String?) {
        multipleStatusView.showNoNetwork()
    }

    override fun onHttpDataGet(data: Any?) {
    }

    override fun onHttpCompleted() {
        if (adapter?.isLoading == true) {
            adapter?.loadMoreComplete()
        }
        if (!presenter?.isHasMore!!) {
            adapter?.loadMoreEnd()
        }
    }

    override fun showContent() {
        multipleStatusView.showContent()
    }

    override fun showLoading(msg: String?) {
        LoadingDialogUtil.showLoadingDialog(this, msg)
    }

    override fun dismissLoading() {
        LoadingDialogUtil.dismissLoadingDialog(this)
    }

    protected abstract fun getCurrentAdapter(): BaseQuickAdapter<*, *>?

    open fun refresh() {
        adapter?.data?.clear()
        (presenter as BaseKtListPresenter<BaseKtListView>).refresh()
    }


    open fun getLayoutManager(): RecyclerView.LayoutManager? {
        if (mLayoutManager == null) {
            mLayoutManager = XLinearLayoutManager(currActivity)
        }
        return mLayoutManager
    }

    protected fun addToContent(view: View?, index: Int) {
        linear_content.addView(view, index)
    }

    /**
     * 当前recyclerView 带headerView
     *
     * @param position
     */
    open fun scrollToPosition(position: Int) {
        if (null != currActivity) {
            val smoothScroller: LinearSmoothScroller = object : LinearSmoothScroller(currActivity) {
                override fun getVerticalSnapPreference(): Int {
                    return SNAP_TO_END
                }
            }
            smoothScroller.targetPosition = position
            recyclerView?.layoutManager?.startSmoothScroll(smoothScroller)
        }
    }
}