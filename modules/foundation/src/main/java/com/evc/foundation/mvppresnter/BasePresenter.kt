package com.evc.foundation.mvppresnter

import android.content.Context
import com.evc.foundation.mvpview.BaseKtView

/**
 * @Package: com.evc.foundation
 * @Description: BasePresenter
 * @Author: EvanChan
 * @CreateDate: 8/10/21 11:56 AM
 * @m-mail: dadaintheair@gmail.com
 */
abstract class BasePresenter<V:BaseKtView> {
    protected var context: Context? = null
    protected var view: V? = null
    protected var TAG = this.javaClass.simpleName
    fun setVM(v: V, mContext: Context?) {
        context = mContext
        view = v
    }

    fun getThatView(): V? {
        return view
    }

    open fun onDestroy() {
        context = null
        view = null
    }
}
