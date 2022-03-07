package com.evc.evcapp.application

import android.app.Application
import com.evc.evcapp.BuildConfig
import com.evc.toolkit.util.AppConstantInitUtil
import com.evc.foundation.utils.DensityUtil

/**
 * Created by Android Studio.
 * User: evan
 * Date: 2020/11/16
 * Time: 10:50 AM
 */
class EvcApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DensityUtil.setDensity(this)
        AppConstantInitUtil.initGlobalAppContant(this,
            BuildConfig.VERSION_CODE,
            BuildConfig.VERSION_NAME,
            BuildConfig.API_HOST
        )

    }
}