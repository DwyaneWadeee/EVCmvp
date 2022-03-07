package com.evc.foundation.kt

import android.view.View
import android.widget.LinearLayout
import com.evc.foundation.BaseDataActivity
import com.evc.foundation.R
import com.evc.foundation.mvppresnter.BaseKtPresenter
import com.evc.foundation.mvpview.BaseKtView
import com.evc.foundation.utils.LoadingDialogUtil
import com.evc.foundation.view.MultipleStatusView
import com.evc.foundation.view.TopBarView
import kotlinx.android.synthetic.main.activity_toolbar.*

/**
 *
 * @Package: com.evc.foundation.kt
 * @Description: Activity基类
 * @Author: EvanChan
 * @CreateDate: 9/1/21 5:46 PM
 * @m-mail: dadaintheair@gmail.com
 */
abstract class BaseKtActivity<P:BaseKtPresenter<*>> : BaseDataActivity<P>(), BaseKtView {

    abstract fun setContentViewToRoot(): View

    open fun setToolbarTitle(): Int? {
        return null
    }

    override fun getLayoutResID(): Int {
        return R.layout.activity_toolbar
    }

    override fun initView() {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        multipleStatusView.addView(setContentViewToRoot(), params)
    }

    override fun initData() {
        setToolbarTitle()?.let {
            topBarView.titleText = getString(it)
        }
    }

    override fun initListener() {
        multipleStatusView.addStatusViewItemClickListener(object:
            MultipleStatusView.OnStatusViewItemClickListener{
            override fun onStatusViewItemClick(viewId: Int) {
                initData()
            }
        },R.id.tvReload)
    }

    /**
     * 对子类提供topbarView引用
     */
    protected fun getTopBarView(): TopBarView {
        return topBarView
    }
    /*************From BaseKtView**************/

    override fun onRefreshing(refreshing: Boolean) {
    }

    override fun onHttpEmptySuccess(msg: String?) {
        multipleStatusView.showEmpty()
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

    }
    override fun showContent() {
        multipleStatusView.showContent()
    }

    override fun showLoading(msg: String?) {
        LoadingDialogUtil.showLoadingDialog(this,msg)
    }

    override fun dismissLoading() {
        LoadingDialogUtil.dismissLoadingDialog(this)
    }

    /*************From BaseKtView**************/


    override fun onTopBarBack(view: View?) {

    }
    override fun onBackPressed() {
        super.onBackPressed()
    }









}