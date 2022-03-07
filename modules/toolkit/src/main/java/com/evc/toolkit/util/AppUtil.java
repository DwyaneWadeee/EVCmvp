package com.evc.toolkit.util;

/**
 * Created by Sai on 2018/3/21.
 * 提供涉及App等级的资料
 */

public class AppUtil {

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getMobileModel() {
        return android.os.Build.MODEL;
    }
}