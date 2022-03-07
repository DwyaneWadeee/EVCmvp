package com.evc.foundation.manager

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.util.Log
import java.util.*

/**
 *
 * @Package: com.evc.foundation.manager
 * @Description: 管理Activity栈
 * @Author: EvanChan
 * @CreateDate: 8/10/21 10:50 AM
 * @m-mail: dadaintheair@gmail.com
 */
class AppManager private constructor() {
    companion object {
        private var activityStack: Stack<Activity?>? = null
        @Volatile
        private var instance: AppManager? = null

        /**
         * 单一实例
         */
        val appManager: AppManager?
            get() {
                if (instance == null) {
                    synchronized(AppManager::class.java) {
                        if (instance == null) {
                            instance = AppManager()
                            activityStack = Stack<Activity?>()
                        }
                    }
                }
                return instance
            }
    }
    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity?) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack!!.add(activity)
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity? {
        return try {
            activityStack!!.lastElement()
        } catch (e: Exception) {
            //            e.printStackTrace();
            null
        }
    }

    /**
     * 获取当前Activity的前一个Activity
     */
    fun preActivity(): Activity? {
        val index = activityStack!!.size - 2
        return if (index < 0) {
            null
        } else activityStack!![index]
    }

    /**
     * 获取当前Activity的前一个Activity
     */
    fun prePreActivity(): Activity? {
        val index = activityStack!!.size - 3
        Log.e("AppManager", "" + index)
        return if (index < 0) {
            null
        } else activityStack!![index]
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        val activity = activityStack!!.lastElement()
        finishActivity(activity)
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        var activity = activity
        if (activity != null) {
            activityStack!!.remove(activity)
            activity.finish()
            activity = null
        }
    }

    /**
     * 移除指定的Activity
     */
    fun removeActivity(activity: Activity?) {
        var activity = activity
        if (activity != null) {
            activityStack!!.remove(activity)
            activity = null
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        try {
            for (activity in activityStack!!) {
                if (activity!!.javaClass == cls) {
                    finishActivity(activity)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        var i = 0
        val size = activityStack!!.size
        while (i < size) {
            if (null != activityStack!![i]) {
                activityStack!![i]!!.finish()
            }
            i++
        }
        activityStack!!.clear()
    }

    /**
     * 返回到指定的activity
     *
     * @param cls
     */
    fun returnToActivity(cls: Class<*>) {
        while (activityStack!!.size != 0) if (activityStack!!.peek()!!.javaClass == cls) {
            break
        } else {
            finishActivity(activityStack!!.peek())
        }
    }

    fun getActivityCount(): Int {
        return if (null != activityStack) {
            activityStack!!.size
        } else 0
    }

    /**
     * 是否已经打开指定的activity
     *
     * @param cls
     * @return
     */
    fun isOpenActivity(cls: Class<*>): Boolean {
        if (activityStack != null) {
            var i = 0
            val size = activityStack!!.size
            while (i < size) {
                if (cls == activityStack!!.peek()!!.javaClass) {
                    return true
                }
                i++
            }
        }
        return false
    }

    /**
     * 退出应用程序
     *
     * @param context      上下文
     * @param isBackground 是否开开启后台运行
     */
    fun AppExit(context: Context, isBackground: Boolean?) {
        try {
            finishAllActivity()
            val activityMgr = context
                .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityMgr.restartPackage(context.packageName)
        } catch (e: Exception) {
        } finally {
            // 注意，如果你有后台程序运行，请不要支持此句子
            if (!isBackground!!) {
                System.exit(0)
            }
        }
    }


}