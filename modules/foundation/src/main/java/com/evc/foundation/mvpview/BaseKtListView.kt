package com.evc.foundation.mvpview

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 *
 * @Package: com.evc.foundation.mvpview
 * @Description: java类作用描述
 * @Author: EvanChan
 * @CreateDate: 9/15/21 4:33 PM
 * @m-mail: dadaintheair@gmail.com
 */
interface BaseKtListView : BaseKtView {
    fun onItemClick(
        adapter: BaseQuickAdapter<*, *>?,
        view: View?,
        position: Int
    )

    fun onItemChildClick(
        adapter: BaseQuickAdapter<*, *>?,
        view: View?,
        position: Int
    ): Boolean

    fun onLoadingMore(isLoadingMore: Boolean)
}