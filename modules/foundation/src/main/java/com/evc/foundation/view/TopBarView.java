package com.evc.foundation.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.DisplayCutout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;


import com.evc.foundation.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Created by Sai on 2018/3/27.
 * 顶部导航栏
 * colorMode 有 Light 和 Dark模式
 * titleText 设置标题
 * rightText 设置右侧文字按钮
 * rightBackground 设置右侧文字按钮背景
 * showBack 控制左侧图标显示和隐藏
 * TopBarViewShowDivider 控制底部分割线显示和隐藏
 * rightIcon 左侧返回键更换图标
 * rightIcon 右侧图标
 * 在 Activity 中 写 onTopBarBack(View view) 函数响应左侧按钮
 * 在 Activity 中 写 onTopBarRightClick(View view) 函数响应右侧按钮,含文字和图标，通过id判断
 * 其他 请看get set方法
 */
public class TopBarView extends Toolbar {
    public static final int TOPBARMODE_LIGHT = 0;
    public static final int TOPBARMODE_DARK = 1;
    private int mode = TOPBARMODE_LIGHT;
    private String titleText;
    private String rightText;
    private TextView tvTopBarTitle;
    private TextView tvTopBarRight;
    private ImageView ivTopBarBack;
    private ImageView ivTopBarRight;
    private View dividerView;
    private int topbarBackground;
    private int rightBackground;
    private boolean showBack;
    private boolean showDivider;
    private int leftIcon;
    private int rightIcon;
    private int rightSecondIcon;
    private View loTopBar;
    private View loTopBarBackgound;
    public static final int NULLRES = -1;//不设置资源
    private ImageView ivSecondTopBarRight;
    private int statusBarHeight = 0;

    public TopBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setContentInsetsRelative(0, 0);
        setContentInsetsAbsolute(0, 0);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TopBarView);
        mode = typedArray.getColor(R.styleable.TopBarView_colorMode, mode);
        titleText = typedArray.getString(R.styleable.TopBarView_titleText);
        rightText = typedArray.getString(R.styleable.TopBarView_rightText);
        topbarBackground = typedArray.getColor(R.styleable.TopBarView_topbarBackground, getResources().getColor(R.color.topbar_color_topbarBackground));
        rightBackground = typedArray.getColor(R.styleable.TopBarView_rightBackground, getResources().getColor(R.color.topbar_color_topbarBackground));
        showBack = typedArray.getBoolean(R.styleable.TopBarView_showBack, true);
        leftIcon = typedArray.getResourceId(R.styleable.TopBarView_leftIcon, NULLRES);
        rightIcon = typedArray.getResourceId(R.styleable.TopBarView_rightIcon, NULLRES);
        rightSecondIcon = typedArray.getResourceId(R.styleable.TopBarView_rightSecondIcon, NULLRES);
        showDivider = typedArray.getBoolean(R.styleable.TopBarView_TopBarViewShowDivider, true);

        typedArray.recycle();
        initView(context);
        initData();
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.topbarview, this);
        loTopBarBackgound = findViewById(R.id.loTopBarBackgound);
        loTopBar = findViewById(R.id.loTopBar);
        tvTopBarTitle = findViewById(R.id.tvTopBarTitle);
        tvTopBarRight = findViewById(R.id.tvTopBarRight);
        ivTopBarBack = findViewById(R.id.ivTopBarBack);
        ivTopBarRight = findViewById(R.id.ivTopBarRight);
        dividerView = findViewById(R.id.dividerView);
        ivSecondTopBarRight = findViewById(R.id.ivSecondTopBarRight);

        ivTopBarBack.setVisibility(showBack ? VISIBLE : INVISIBLE);
        ivTopBarRight.setVisibility(rightIcon != NULLRES ? VISIBLE : GONE);
        ivSecondTopBarRight.setVisibility(rightSecondIcon != NULLRES ? VISIBLE : GONE);
        dividerView.setVisibility(showDivider ? VISIBLE : INVISIBLE);
        tvTopBarRight.setVisibility(!TextUtils.isEmpty(rightText) ? VISIBLE : INVISIBLE);
        switch (mode) {
            case TOPBARMODE_LIGHT:
                tvTopBarTitle.setTextColor(context.getResources().getColor(R.color.topbar_color_title_light));
                tvTopBarRight.setTextColor(context.getResources().getColor(R.color.topbar_color_righttext_light));
                ivTopBarBack.setImageResource(R.drawable.ic_topbar_back_black);
                break;
            case TOPBARMODE_DARK:
                tvTopBarTitle.setTextColor(context.getResources().getColor(R.color.topbar_color_title_dark));
                tvTopBarRight.setTextColor(context.getResources().getColor(R.color.text_white));
                ivTopBarBack.setImageResource(R.drawable.ic_topbar_back_white);
                break;
        }

        loTopBarBackgound.setBackgroundColor(topbarBackground);
        //暂时去掉，统一使用波纹效果
//        tvTopBarRight.setBackgroundColor(rightBackground);
        if (leftIcon != NULLRES)
            ivTopBarBack.setImageResource(leftIcon);
        if (rightIcon != NULLRES)
            ivTopBarRight.setImageResource(rightIcon);

        if (rightSecondIcon != NULLRES) {
            ivSecondTopBarRight.setImageResource(rightSecondIcon);
        }

        //适配沉浸式，主动加padding顶部
        statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (resourceId > 0) {
                statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            }
        } else {
            //低版本 直接设置0
            statusBarHeight = 0;
        }
        loTopBar.setPadding(getPaddingLeft(), statusBarHeight, getPaddingRight(), getPaddingBottom());
    }

    public int getStatusBarHeight() {
        return statusBarHeight;
    }

    private void initData() {
        if (!TextUtils.isEmpty(titleText))
            tvTopBarTitle.setText(titleText);
        if (!TextUtils.isEmpty(rightText))
            tvTopBarRight.setText(rightText);
    }

    public void setTopbarBackground(int resId) {
        loTopBarBackgound.setBackgroundColor(resId);
    }

    public String getTitleText() {
        return titleText;
    }

    /**
     * 设置中间标题
     *
     * @param titleText
     */
    public void setTitleText(String titleText) {
        this.titleText = titleText;
        if (!TextUtils.isEmpty(titleText)) {
            tvTopBarTitle.setText(titleText);
        } else {
            tvTopBarTitle.setText("");
        }
    }

    public String getRightText() {
        return rightText;
    }

    /**
     * 设置右侧文字
     *
     * @param rightText
     */
    public void setRightText(String rightText) {
        this.rightText = rightText;
        if (!TextUtils.isEmpty(rightText)) {
            tvTopBarRight.setText(rightText);
            tvTopBarRight.setVisibility(VISIBLE);
        } else {
            tvTopBarRight.setText("");
            tvTopBarRight.setVisibility(INVISIBLE);
        }
    }

    public boolean isShowBack() {
        return showBack;
    }

    public void setShowBack(boolean showBack) {
        this.showBack = showBack;
        ivTopBarBack.setVisibility(showBack ? VISIBLE : INVISIBLE);
    }

    /**
     * 设置左侧图标
     *
     * @param resId
     */
    public void setLeftIcon(int resId) {
        ivTopBarBack.setVisibility(showBack ? VISIBLE : INVISIBLE);
        ivTopBarBack.setImageResource(resId);
    }

    /**
     * 设置右侧图标
     *
     * @param resId
     */
    public void setRightIcon(int resId) {
        ivTopBarRight.setVisibility(resId != NULLRES ? VISIBLE : GONE);
        if (resId != NULLRES) {
            ivTopBarRight.setImageResource(resId);
        }
    }

    /**
     * 设置右侧第二个图标
     */
    public void setSecondRightIcon(int resId) {
        ivSecondTopBarRight.setVisibility(View.VISIBLE);
        ivSecondTopBarRight.setImageResource(resId);
    }


    public boolean isShowDivider() {
        return showDivider;
    }

    /**
     * 设置分割线显示或隐藏
     *
     * @param showDivider
     */
    public void setShowDivider(boolean showDivider) {
        this.showDivider = showDivider;
        dividerView.setVisibility(showDivider ? VISIBLE : INVISIBLE);
    }

    public TextView getTopBarTitleView() {
        return tvTopBarTitle;
    }

    public View getTopBarBackgoundView() {
        return loTopBarBackgound;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int cutoutHeight = getDisplayCutoutHeight();
        if (cutoutHeight != 0) {
            int height = MeasureSpec.getSize(heightMeasureSpec);
            height = height + cutoutHeight / 2;
            int mode = MeasureSpec.getMode(heightMeasureSpec);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, mode);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private static final String KEY_VERSION_MIUI = "ro.miui.ui.version.name";

    /**
     * Android P 刘海屏高度
     *
     * @return
     */
    public int getDisplayCutoutHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                if (!TextUtils.isEmpty(getProp(KEY_VERSION_MIUI))) {//只要小米适配
                    DisplayCutout displayCutout = getRootWindowInsets().getDisplayCutout();
                    return displayCutout.getSafeInsetTop();
                }
            } catch (Exception e) {
            } catch (NoSuchMethodError e) {
            }
        }
        return 0;
    }

    //
    public ImageView getBackView() {
        return ivTopBarBack;
    }

    public View getDividerView() {
        return dividerView;
    }

    public ImageView getRightIconView() {
        return ivTopBarRight;
    }

    public ImageView getSecondRightIconView() {
        return ivSecondTopBarRight;
    }

    public TextView getRightTextView() {
        return tvTopBarRight;
    }

    public static String getProp(String name) {
        String line = null;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + name);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return line;
    }

}
