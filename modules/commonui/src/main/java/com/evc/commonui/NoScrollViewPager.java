package com.evc.commonui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * @package： com.gac.nioapp.view
 * @describe： 保险起见，首页的viewpager 加入滑动开关 ViewPager禁止左右滑动切换，去除点击切换效果
 * @author： liming
 * @time： 2019/4/30 10:30 AM
 * @e-mail： liming@gac-nio.com
 */
public class NoScrollViewPager extends ViewPager {
    private boolean noScroll = true;

    /**
     * 重写onTouchEvent和onInterceptTouchEvent是为了屏蔽左右滑动
     * <p>
     * 重写setCurrentItem是为了取消点击切换效果。
     */

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        /* return false;//super.onTouchEvent(arg0); */
        if (noScroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (noScroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, false);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }


}
