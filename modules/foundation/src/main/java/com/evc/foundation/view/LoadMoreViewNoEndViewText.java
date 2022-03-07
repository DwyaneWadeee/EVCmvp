package com.evc.foundation.view;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.evc.foundation.R;

/**
 * Created by Android Studio.
 * User: evan
 * Date: 2020/11/2
 * Time: 11:05 AM
 */
public class LoadMoreViewNoEndViewText extends LoadMoreView {
    @Override
    public int getLayoutId() {
        return R.layout.customer_quick_view_load_more_no_end_text;
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
