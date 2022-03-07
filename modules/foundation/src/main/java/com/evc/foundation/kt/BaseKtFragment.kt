package com.evc.foundation.kt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.evc.foundation.mvppresnter.BaseDataPresenter
import com.evc.foundation.R
import com.evc.foundation.fragment.BaseLazyFragment
import com.evc.foundation.mvpview.BaseKtView
import com.evc.foundation.utils.LoadingDialogUtil
import com.evc.foundation.utils.TUtil
import com.evc.foundation.view.MultipleStatusView
import kotlinx.android.synthetic.main.fragment_kt_root.*

/**
 * @Package: com.evc.foundation.kt
 * @Description: Fragment基类
 * @Author: EvanChan
 * @CreateDate: 9/15/21 4:03 PM
 * @m-mail: dadaintheair@gmail.com
 */
abstract class BaseKtFragment<P: BaseDataPresenter<*>>: BaseLazyFragment(),BaseKtView {
    var presenter: P? = null
    protected abstract fun setContentViewToRoot(): View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPresenter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_kt_root
    }

    override fun initView() {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        multipleStatusView.addView(setContentViewToRoot(), params)
    }

    override fun initData() {

    }

    override fun initListener() {
        super.initListener()
        multipleStatusView.addStatusViewItemClickListener(object :
            MultipleStatusView.OnStatusViewItemClickListener {
            override fun onStatusViewItemClick(viewId: Int) {
                initData()
                showToast("refrs")
            }
        }, R.id.tvReload)
    }

    protected fun initPresenter() {
        presenter = TUtil.getT(this, 0)
        presenter?.setVM(this as Nothing, this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDestroyView() {
        if (presenter != null) {
            presenter!!.onDestroy()
        }
        super.onDestroyView()
    }

    /*************From BaseKtView**************/
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
        activity?.let {
            LoadingDialogUtil.showLoadingDialog(it, msg)
        }
    }

    override fun dismissLoading() {
        activity?.let {
            LoadingDialogUtil.dismissLoadingDialog(it)
        }
    }
    /*************From BaseKtView**************/

}