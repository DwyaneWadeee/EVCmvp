package com.evc.foundation;


import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.evc.foundation.fragment.BaseDialogFragment;
import com.evc.foundation.fragment.BaseLazyFragment;

/**
 * @Description：描述信息
 * @Author：Sai
 * @Date：2019/4/11 18:54
 */
public abstract class BaseAlertDialogFragment extends BaseLazyFragment {
    public void setShowing(boolean showing) {
        isShowing = showing;
    }

    private boolean isShowing;
    public BaseDialogFragment.OnDialogHandleListener onDialogHandleListener;

    public void show(FragmentManager manager, String tag) {
        isShowing = true;
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(android.R.id.content,this, tag);
        // 这里吧原来的commit()方法换成了commitAllowingStateLoss()
        ft.commitAllowingStateLoss();
//        ft.commit();
    }

    public void replace(FragmentManager manager, String tag) {
        isShowing = true;
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(android.R.id.content,this, tag);
        ft.commitAllowingStateLoss();
    }

    public boolean isShowing() {
        return isShowing;
    }
    public void dismiss() {
        if (isShowing){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.remove(this);
            ft.commitAllowingStateLoss();
            isShowing = false;
            onDialogHandleListener = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onDialogHandleListener = null;
    }
}
