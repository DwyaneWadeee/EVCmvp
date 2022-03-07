package com.evc.foundation.view;


import com.evc.foundation.R;

/**
 * @package： com.bigkoo.katafoundation.view
 * @describe：
 * @author： liming
 * @time： 2019/4/29 5:20 PM
 * @e-mail： liming@gac-nio.com
 */
public class NoLoadMoreLoadingView extends com.chad.library.adapter.base.loadmore.LoadMoreView {
    @Override
    public int getLayoutId() {
        return R.layout.customer_quick_view_no_loading;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
