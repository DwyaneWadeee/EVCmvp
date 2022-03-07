package com.evc.foundation.kt

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.evc.foundation.manager.AppManager
import com.evc.foundation.utils.DensityUtil
import com.evc.foundation.view.GlobalNativeToast

/**
 * @Package: com.evc.foundation.utils
 * @Description: BaseActivity
 * @Author: EvanChan
 * @CreateDate: 8/10/21 10:36 AM
 * @m-mail: dadaintheair@gmail.com
 */
abstract class BaseActivity :AppCompatActivity() {
    var currActivity : BaseActivity? =null
    protected var TAG = "cyf" + this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //处理状态栏

        currActivity = this

        DensityUtil.setDefault(this)

        setContentView(getLayoutResID())
        AppManager.appManager?.addActivity(this)

        initView()
        initData()
        initListener()

    }

    /**
     * 布局resId
     *
     * @return
     */
    protected abstract fun getLayoutResID():Int
    /**
     * 初始化view
     */
    protected abstract fun initView()
    /**
     * 初始化数据，在initView之后
     */
    protected abstract fun initData()

    protected open fun initListener(){}

    override fun onDestroy() {
        super.onDestroy()
        AppManager.appManager?.finishActivity(this)
        currActivity = null
    }

    open fun showToast(msg: String?) {
        GlobalNativeToast.show(application, msg, Toast.LENGTH_SHORT)
    }

    open fun showToast(msgId: Int) {
        GlobalNativeToast.show(
            application,
            applicationContext.getString(msgId),
            Toast.LENGTH_SHORT
        )
    }

    open fun showToastLong(msgId: Int) {
        GlobalNativeToast.show(
            application,
            applicationContext.getString(msgId),
            Toast.LENGTH_LONG
        )
    }

    open fun showToastLong(msg: String?) {
        GlobalNativeToast.show(application, msg, Toast.LENGTH_LONG)
    }

    open fun onTopBarBack(view: View?) {
        finish()
    }

    open fun onTopBarRightClick(v: View?) {}

}