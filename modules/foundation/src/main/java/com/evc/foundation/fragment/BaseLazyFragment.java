package com.evc.foundation.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;

/**
 * Created by Sai on 2018/4/16.
 */

public abstract class BaseLazyFragment extends BaseFragment {
    protected boolean isVisible = true;
    protected boolean isLoadData = false;
    private boolean isPrepared;
    protected boolean canAutoLoadData = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.getUserVisibleHint()) {
            this.isVisible = true;
            this.lazyLoad();
            this.onVisible();
        } else {
            this.isVisible = false;
            this.onInVisible();
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.isPrepared = true;
        this.lazyLoad();
    }

    public void onVisible() {
    }

    public void onInVisible() {
    }

    protected void lazyLoad() {
        if(!canAutoLoadData)
            return;
        if (this.isPrepared && this.isVisible && !this.isLoadData) {
            this.initData();
            this.isLoadData = true;
        }
    }

}