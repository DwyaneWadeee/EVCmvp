package com.evc.foundation.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.evc.foundation.utils.EventListener;
import com.evc.foundation.view.GlobalNativeToast;


/**
 * Created by sai on 2018/3/18.
 */

public abstract class BaseFragment extends Fragment {
    protected View rootView;
    protected Context currContext;
    protected String TAG = "cyf" + this.getClass().getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        currContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutResID(), container, false);
            initViewOnCreateView();//临时解决地址选择问题
        }

        return rootView;
    }

    protected void initViewOnCreateView() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
    }

    public <T extends View> T findViewById(int id) {
        return rootView.findViewById(id);
    }

    protected abstract void initData();

    protected abstract void initView();

    protected void initListener() {
    }

    protected abstract int getLayoutResID();

    public void showToast(String msg, int duration) {
        /**
         * HYCAN wifi经常访问生产环境会出现异常，老板总是在此wifi下使用，未知异常是后台返回的一个异常之外的异常，前端不要显示出来。
         */
        if (TextUtils.isEmpty(msg) || msg.equals("未知异常") || msg.startsWith("HTTP 504")) {
            return;
        }
        View view = Toast.makeText(getContext(), "", duration).getView();
        Toast toast = new Toast(getContext());
        toast.setView(view);
        toast.setText(msg);
        toast.setDuration(duration);
        toast.show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        currContext = null;
    }


    public void showToast(String msg) {
        GlobalNativeToast.show(getActivity().getApplication(), msg, Toast.LENGTH_SHORT);

    }

    public void showToast(int msgId) {
        GlobalNativeToast.show(getActivity().getApplication(), getActivity().getApplicationContext().getString(msgId), Toast.LENGTH_SHORT);
    }

    public void showToastLong(int msgId) {
        GlobalNativeToast.show(getActivity().getApplication(), getActivity().getApplicationContext().getString(msgId), Toast.LENGTH_LONG);
    }

    public void showToastLong(String msg) {
        GlobalNativeToast.show(getActivity().getApplication(), msg, Toast.LENGTH_LONG);
    }

    /**
     * Find出来的View，自带防抖功能
     */
    public <T extends View> T findClickView(int id) {

        T view = (T) findViewById(id);
        view.setOnClickListener(new EventListener(this));
        return view;
    }
}