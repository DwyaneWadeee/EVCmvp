package com.evc.foundation.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.evc.foundation.BaseAlertDialogFragment;
import com.evc.foundation.R;


/**
 * @Description：描述信息
 * @Author：Sai
 * @Date：2019/4/12 16:44
 */
public class LoadingDialogFragment extends BaseAlertDialogFragment implements View.OnClickListener {
    public static final String MSG = "msg";
    public static final String CANCELABLE = "cancelable";
    private boolean cancelable = false;
    private View loBackgound;
    private View lLoadingBg;
    private TextView tvMsg;

    public static LoadingDialogFragment newInstance(String msg, boolean cancelable) {
        Bundle args = new Bundle();
        args.putString(MSG, msg);
        args.putBoolean(CANCELABLE, cancelable);
        LoadingDialogFragment fragment = new LoadingDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        cancelable = bundle.getBoolean(CANCELABLE);
        String msg = bundle.getString(MSG);
        if(!TextUtils.isEmpty(msg)) {
            tvMsg.setText(msg);
            lLoadingBg.setBackgroundResource(R.drawable.bg_white_radius_10);
        } else {

        }
    }

    @Override
    protected void initView() {
        loBackgound = findViewById(R.id.loBackgound);
        tvMsg = findViewById(R.id.tvLoadingMsg);
        lLoadingBg = findViewById(R.id.lLoadingBg);
//        LottieAnimationView loadingView = findViewById(R.id.loading_progress);
//        loadingView.playAnimation();
        //lottie动画暂时使用progressbar代替
        ProgressBar loadingView = findViewById(R.id.loading_progress);
    }

    @Override
    protected void initListener() {
        super.initListener();
        if(cancelable)
            loBackgound.setOnClickListener(this);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.dialog_custom_loading;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.loBackgound){
            if(cancelable)
                dismiss();
            return;
        }

    }
}
