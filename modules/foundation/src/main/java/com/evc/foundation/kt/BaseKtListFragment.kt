package com.evc.foundation.kt

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.loadmore.LoadMoreView
import com.evc.foundation.R
import com.evc.foundation.fragment.BaseLazyFragment
import com.evc.foundation.mvppresnter.BaseKtListPresenter
import com.evc.foundation.mvpview.BaseKtListView
import com.evc.foundation.utils.LoadingDialogUtil
import com.evc.foundation.utils.TUtil
import com.evc.foundation.view.CommonLoadMoreView
import com.evc.foundation.view.LoadMoreViewNoEndViewText
import com.evc.foundation.view.NoLoadMoreLoadingView
import kotlinx.android.synthetic.main.fragment_recycler_view.*

/**
 *
 * @Package: com.evc.foundation.kt
 * @Description: ListFragment基类
 * @Author: EvanChan
 * @CreateDate: 9/17/21 3:30 PM
 * @m-mail: dadaintheair@gmail.com
 */
abstract class BaseKtListFragment<P : BaseKtListPresenter<*>> : BaseLazyFragment(), BaseKtListView {
    protected var presenter: P? = null
    protected var recyclerView: RecyclerView? = null
    protected var adapter: BaseQuickAdapter<*, *>? = null
    protected var loadMoreView: LoadMoreView? = null

    protected var refreshable = true
    //用于修改不需要endView的场景，默认需要
    protected var enviewGone = false
    protected var loadingMoreGone = true //默认没有加载更多


    protected abstract fun setContentViewToRoot(): View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPresenter()
    }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_recycler_view
    }

    override fun initData() {
    }

    override fun initView() {
        val params = ViewGroup.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )

        multipleStatusView?.addView(setContentViewToRoot(), params)

        recyclerView = findViewById(R.id.recyclerView)
        adapter = getCurrentAdapter()


        if (recyclerView != null) {
            recyclerView?.layoutManager = presenter?.getLayoutManager()
        }

        smart_refresh_layout.setEnableRefresh(refreshable)
        smart_refresh_layout.setOnRefreshListener {
            refresh()
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

    override fun onStatusLoading() {
//        multipleStatusView.showLoading()
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
        activity?.let {
            LoadingDialogUtil.showLoadingDialog(it, msg)
        }
    }

    override fun dismissLoading() {
        activity?.let {
            LoadingDialogUtil.dismissLoadingDialog(it)
        }
    }

    protected fun initPresenter() {
        presenter = TUtil.getT(this, 0)
        if (presenter != null) {
            presenter!!.setVM(this as Nothing, this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        if (presenter != null) {
            presenter?.onDestroy()
        }
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    protected abstract fun getCurrentAdapter(): BaseQuickAdapter<*, *>?

    public open fun refresh() {
        adapter?.data?.clear()
        adapter?.notifyDataSetChanged()
        (presenter as BaseKtListPresenter<BaseKtListView>).refresh()
    }

    /**
     * 当前recyclerView 带headerView
     *
     * @param position
     */
    open fun scrollToPosition(position: Int) {
        activity?.let {
            val smoothScroller: LinearSmoothScroller = object : LinearSmoothScroller(it) {
                override fun getVerticalSnapPreference(): Int {
                    return SNAP_TO_END
                }
            }
            smoothScroller.targetPosition = position
            recyclerView?.layoutManager?.startSmoothScroll(smoothScroller)
        }
    }

}