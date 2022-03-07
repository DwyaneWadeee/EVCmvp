package com.evc.foundation.utils

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.util.DisplayMetrics

/**
 *
 * @Package: com.evc.foundation.utils
 * @Description: 参考今日头条的屏幕适配方案，ui给过来的屏幕宽度是375dp,目前只用到UED_WIDTH，部分页面用到UED_HEIGHT
 * @Author: EvanChan
 * @CreateDate: 8/10/21 10:36 AM
 * @m-mail: dadaintheair@gmail.com
 */
object DensityUtil {
    private var appDensity = 0f
    private var appScaledDensity = 0f
    lateinit  var appDisplayMetrics: DisplayMetrics
    private var barHeight = 0
    const val WIDTH = "width"
    const val HEIGHT = "height"
    const val UED_HEIGHT = 640f
    const val UED_WIDTH = 360f

    /**
     * 在Application里初始化一下
     *
     * @param application
     */
    fun setDensity(application: Application) {
        //获取application的DisplayMetrics
        appDisplayMetrics = application.resources.displayMetrics
        //获取状态栏高度
        barHeight = getStatusBarHeight(application)
        if (appDensity == 0f) {
            //初始化的时候赋值
            appDensity = appDisplayMetrics.density
            appScaledDensity = appDisplayMetrics.scaledDensity
            //添加字体变化的监听
            application.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onConfigurationChanged(newConfig: Configuration) {
                    //字体改变后,将appScaledDensity重新赋值
                    if (newConfig != null && newConfig.fontScale > 0) {
                        appScaledDensity =
                            application.resources.displayMetrics.scaledDensity
                    }
                }

                override fun onLowMemory() {}
            })
        }
    }

    /**
     * 此方法在BaseActivity中做初始化(如果不封装BaseActivity的话,直接用下面那个方法就好)
     * 在setContentView()之前设置
     *
     * @param activity
     */
    fun setDefault(activity: Activity?) {
        setAppOrientation(activity, WIDTH)
    }

    /**
     * 此方法用于在某一个Activity里面更改适配的方向
     * 在setContentView()之前设置
     *
     * @param activity
     * @param orientation
     */
    fun setOrientation(activity: Activity?, orientation: String) {
        setAppOrientation(activity, orientation)
    }

    /**
     * targetDensity
     * targetScaledDensity
     * targetDensityDpi
     * 这三个参数是统一修改过后的值
     * orientation:方向值,传入width或height
     */
    private fun setAppOrientation(activity: Activity?, orientation: String) {
        val targetDensity: Float
        targetDensity = if (orientation == HEIGHT) {
            //设计图的高度 单位:dp
            (appDisplayMetrics!!.heightPixels - barHeight) / UED_HEIGHT
        } else {
            //设计图的宽度 单位:dp
            appDisplayMetrics!!.widthPixels / UED_WIDTH
        }
        val targetScaledDensity =
            targetDensity * (appScaledDensity / appDensity)
        val targetDensityDpi = (160 * targetDensity).toInt()

        /**
         *
         * 最后在这里将修改过后的值赋给系统参数
         * 只修改Activity的density值
         */
        val activityDisplayMetrics = activity!!.resources.displayMetrics
        activityDisplayMetrics.density = targetDensity
        activityDisplayMetrics.scaledDensity = targetScaledDensity
        activityDisplayMetrics.densityDpi = targetDensityDpi
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}
