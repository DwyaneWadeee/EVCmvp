package com.evc.toolkit.util;

import android.content.Context;

/**
 * Created by Sai on 2018/3/20.
 * App相关数据管理类
 */

public class AppInfoManager {
    private AppInfoManager() {
    }

    private Context context;
    public int mainTabBarHeight=0;
    /**
     * 当前版本号
     */
    public static int versionCode;
    /**
     * 当前版本名
     */
    public static String versionName;
    /**
     * 设备型号，如mi 3
     */
    private String mobileModel = AppUtil.getMobileModel();
    /**
     * 渠道来源，如xiaomi
     */
    private static String channelSource;

    public void initAppVersionInfo(int code, String name) {
        versionCode = code;
        versionName = name;
    }


    private static class AppInfoManagerInstance {
        private static final AppInfoManager INSTANCE = new AppInfoManager();
    }

    public static AppInfoManager getInstance() {
        return AppInfoManagerInstance.INSTANCE;
    }

    public void init(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public Context getAppContext() {
        return context.getApplicationContext();
    }

    public String getMobileModel() {
        return mobileModel;
    }

    public String getChannelSource() {
        return channelSource;
    }

    public void setChannelSource(String channelSource) {
        AppInfoManager.channelSource = channelSource;
    }
}
