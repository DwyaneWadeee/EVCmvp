package com.evc.foundation.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.evc.foundation.R
import java.util.*

/**
 *
 * @Package: com.evc.foundation.view
 * @Description: 多种状态类型的View
 * @Author: EvanChan
 * @CreateDate: 9/3/21 10:13 AM
 * @m-mail: dadaintheair@gmail.com
 */
class MultipleStatusView :RelativeLayout,View.OnClickListener {
    companion object{
        val DEFAULT_LAYOUT_PARAMS = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
    }


    val STATUS_CONTENT = 0x00
    val STATUS_LOADING = 0x01
    val STATUS_EMPTY = 0x02
    val STATUS_ERROR = 0x03
    val STATUS_NO_NETWORK = 0x04

    private val NULL_RESOURCE_ID = -1

    private var mEmptyViewResId = 0
    private var mErrorViewResId = 0
    private var mLoadingViewResId = 0
    private var mNoNetworkViewResId = 0
    private var mContentViewResId = 0


    private var vEmpty: View? = null
    private var vError: View? = null
    private var vLoading: View? = null
    private var vNetWorkError: View? = null
    private var vContent: View? = null
    private var mOtherIds = ArrayList<Int>()


    private var mInflater: LayoutInflater? = null

    private var onStatusViewItemClickListener: OnStatusViewItemClickListener? = null
    private lateinit var clickItemIds: IntArray


    var mViewStatus :Int? = null

    constructor(context:Context): this(context,null)

    constructor(context:Context, attrs: AttributeSet?):this(context,attrs,0)

    constructor(context: Context,attrs: AttributeSet?,defStyleAttr:Int):super(context, attrs, defStyleAttr){
        val a =
            context.obtainStyledAttributes(attrs, R.styleable.MultipleStatusView, defStyleAttr, 0)
        mEmptyViewResId =
            a.getResourceId(R.styleable.MultipleStatusView_emptyView, R.layout.stateview_empty)
        mErrorViewResId =
            a.getResourceId(R.styleable.MultipleStatusView_errorView, R.layout.stateview_error)
        mLoadingViewResId =
            a.getResourceId(R.styleable.MultipleStatusView_loadingView, R.layout.stateview_loading)
        mNoNetworkViewResId = a.getResourceId(
            R.styleable.MultipleStatusView_networkerrorView,
            R.layout.stateview_networkerror
        )
        mContentViewResId = a.getResourceId(
            R.styleable.MultipleStatusView_contentView,
            NULL_RESOURCE_ID
        )
        a.recycle()
        mInflater = LayoutInflater.from(getContext())
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        showContent()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        clear(vEmpty, vLoading, vError, vNetWorkError)
        if (null != mOtherIds && !mOtherIds.isEmpty()) {
            mOtherIds.clear()
        }
        if (null != onStatusViewItemClickListener) {
            onStatusViewItemClickListener = null
        }
    }

    fun clear(vararg views:View?){
        if (views == null){
            return
        }
        try {
            for (view in views){
                if(null != view){
                    removeView(view)
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    /**
     * 获取当前状态
     */
    fun getViewStatus(): Int {
        return mViewStatus!!
    }

    /**
     * 显示空视图
     */
    fun showEmpty(){
            showEmpty(mEmptyViewResId, DEFAULT_LAYOUT_PARAMS)
    }

    fun showEmpty(layoutId: Int, layoutParams: ViewGroup.LayoutParams?) {
        showEmpty(inflateView(layoutId), layoutParams)
    }

    /**
     * 显示空视图
     * @param view 自定义视图
     * @param layoutParams 布局参数
     */
    fun showEmpty(view: View?, layoutParams: ViewGroup.LayoutParams?) {
        if (mViewStatus == STATUS_EMPTY) return
        checkNull(view, "Empty view is null!")
        mViewStatus = STATUS_EMPTY
        if (null == vEmpty) {
            vEmpty = view
            vEmpty!!.id = mErrorViewResId
            //这里设0是希望在empty的时候，列表还能下拉刷新
            addView(vEmpty, layoutParams)
        }
        addItemClickListener(vEmpty!!)
        showViewById(vEmpty!!.id)
    }

    /**
     * 显示错误视图
     */
    fun showError() {
        showError(mErrorViewResId, DEFAULT_LAYOUT_PARAMS)
    }

    /**
     * 显示错误视图
     * @param layoutId 自定义布局文件
     * @param layoutParams 布局参数
     */
    fun showError(layoutId: Int, layoutParams: ViewGroup.LayoutParams?) {
        showError(inflateView(layoutId), layoutParams)
    }

    /**
     * 显示错误视图
     * @param view 自定义视图
     * @param layoutParams 布局参数
     */
    fun showError(view: View?, layoutParams: ViewGroup.LayoutParams?) {
        if (mViewStatus == STATUS_ERROR) return
        checkNull(view, "Error view is null!")
        mViewStatus = STATUS_ERROR
        if (null == vError) {
            vError = view
            vError!!.id = mErrorViewResId
            addView(vError, layoutParams)
        }
        addItemClickListener(vError!!)
        showViewById(vError!!.id)
    }

    /**
     * 显示加载中视图
     */
    fun showLoading() {
        showLoading(mLoadingViewResId, DEFAULT_LAYOUT_PARAMS)
    }

    /**
     * 显示加载中视图
     * @param layoutId 自定义布局文件
     * @param layoutParams 布局参数
     */
    fun showLoading(layoutId: Int, layoutParams: ViewGroup.LayoutParams?) {
        showLoading(inflateView(layoutId)!!, layoutParams)
    }

    /**
     * 显示加载中视图
     * @param view 自定义视图
     * @param layoutParams 布局参数
     */
    fun showLoading(view: View, layoutParams: ViewGroup.LayoutParams?) {
        if (mViewStatus == STATUS_LOADING) return
        checkNull(view, "Loading view is null!")
        mViewStatus = STATUS_LOADING
        if (null == vLoading) {
            vLoading = view
            vLoading!!.id = mLoadingViewResId
            addView(vLoading, layoutParams)
        }
        addItemClickListener(vLoading!!)
        showViewById(vLoading!!.id)
    }

    /**
     * 显示无网络视图
     */
    fun showNoNetwork() {
        showNoNetwork(mNoNetworkViewResId, DEFAULT_LAYOUT_PARAMS)
    }

    /**
     * 显示无网络视图
     * @param layoutId 自定义布局文件
     * @param layoutParams 布局参数
     */
    fun showNoNetwork(layoutId: Int, layoutParams: ViewGroup.LayoutParams?) {
        showNoNetwork(inflateView(layoutId)!!, layoutParams)
    }

    /**
     * 显示无网络视图
     * @param view 自定义视图
     * @param layoutParams 布局参数
     */
    fun showNoNetwork(view: View, layoutParams: ViewGroup.LayoutParams?) {
        if (mViewStatus == STATUS_NO_NETWORK) return
        checkNull(view, "No network view is null!")
        mViewStatus = STATUS_NO_NETWORK
        if (null == vNetWorkError) {
            vNetWorkError = view
            vNetWorkError!!.id = mNoNetworkViewResId
            addView(vNetWorkError, layoutParams)
        }
        addItemClickListener(vNetWorkError!!)
        showViewById(vNetWorkError!!.id)
    }


    /**
     * 显示内容视图
     */
    fun showContent() {
        if (mViewStatus == STATUS_CONTENT) return
        mViewStatus = STATUS_CONTENT
        if (null == vContent && mContentViewResId != NULL_RESOURCE_ID) {
            vContent = mInflater!!.inflate(mContentViewResId, null)
            addView(vContent, 0, DEFAULT_LAYOUT_PARAMS)
        }
        showContentView()
    }

    private fun showContentView() {
        val childCount = childCount
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (mOtherIds.contains(view.id)) view.visibility = View.GONE
        }
    }

    private fun inflateView(layoutId: Int): View? {
        return mInflater!!.inflate(layoutId, null)
    }

    private fun showViewById(viewId: Int) {
        if (!mOtherIds.contains(viewId)) mOtherIds.add(viewId)
        val childCount = childCount
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (mOtherIds.contains(view.id)) view.visibility = View.GONE
            if (view.id == viewId) view.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View?) {
        if (null != onStatusViewItemClickListener) {
            onStatusViewItemClickListener?.onStatusViewItemClick(v!!.id)
        }
    }

    interface OnStatusViewItemClickListener {
        fun onStatusViewItemClick(viewId: Int)
    }

    fun addStatusViewItemClickListener(onStatusViewItemClickListener:OnStatusViewItemClickListener,vararg ids:Int){
        this.onStatusViewItemClickListener = onStatusViewItemClickListener
        clickItemIds = ids
    }

    private fun addItemClickListener(view: View) {
        if (clickItemIds == null) return
        for (id in clickItemIds) {
            val clickView = view.findViewById<View>(id)
            clickView?.setOnClickListener(this)
        }
    }

    private fun checkNull(obj: Any?, hint: String) {
        if (null == obj) {
            throw NullPointerException(hint)
        }
    }

}

