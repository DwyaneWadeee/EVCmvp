package com.evc.toolkit.util;

import android.content.Context;

import com.evc.toolkit.storage.SPUtils;


/******************************
 ** @author
 ** @email
 ** @date 2019-07-25 12:14
 ** @describe
 *******************************/
public class AppConstantInitUtil {
    /**
     * @param context
     * @param code          versionCode
     * @param name          versionName
     * @param url           网关
     */
    public static void initGlobalAppContant(Context context, int code, String name, String url) {
        AppInfoManager.getInstance().init(context);
        AppInfoManager.getInstance().initAppVersionInfo(code, name);
        HttpUrlConstant.initGlobalHost(url);
        SPUtils.init(context);
    }

}
