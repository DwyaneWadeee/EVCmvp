package com.evc.foundation;

import android.os.Bundle;

import com.evc.foundation.kt.BaseActivity;
import com.evc.foundation.mvppresnter.BaseDataPresenter;
import com.evc.foundation.mvpview.BaseKtView;
import com.evc.foundation.utils.TUtil;

/**
 * @Package: com.evc.foundation
 * @Description: java类作用描述
 * @Author: EvanChan
 * @CreateDate: 9/26/21 2:34 PM
 * @m-mail: dadaintheair@gmail.com
 */
public abstract class BaseDataActivity<P extends BaseDataPresenter> extends BaseActivity {
    protected P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initPresenter();
        super.onCreate(savedInstanceState);
    }

    protected void initPresenter() {
        presenter = TUtil.getT(this, 0);
        if (presenter != null) {
            presenter.setVM((BaseKtView) this, this);
        }
    }

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.onDestroy();
        }
        super.onDestroy();
    }


}
