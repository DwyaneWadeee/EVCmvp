package com.evc.toolkit.util;


/**
 * @Description：描述信息
 * @Author：Sai
 * @Date：2019/3/11 13:31
 */
public class HttpUrlConstant {
    public static String APPURL;
    public static String H5_URL;
    public static final String KEY_CURRENT_ENVIRONMENT = "current_environment";
    public static void initGlobalHost(String url){
        APPURL = url;
    }
    public static void initH5Host(String url){
        H5_URL = url;
    }
    public static String getH5Url() {
        return H5_URL;
    }

    public static String getAPPURL() {
        return APPURL;
    }

    public static void setAPPURL(String APPURL) {
        HttpUrlConstant.APPURL = APPURL;
    }

    ////app更新////
    public static final String APPID = "1";
    public static final String SYSTEMID = "1";
    /////////
}
