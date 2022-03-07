package com.evc.evcapp.activity

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.os.Message
import com.evc.evcapp.R
import com.evc.foundation.kt.BaseActivity
import java.lang.ref.WeakReference

/**
 *
 * @Package: com.evc.evcapp.activity
 * @Description: java类作用描述
 * @Author: EvanChan
 * @CreateDate: 1/7/22 10:11 AM
 * @m-mail: dadaintheair@gmail.com
 */
class SplashActivity : BaseActivity() {
    //延时时间
    private val DELAY = 1200L
    private var handler: FinishHandler? = null


    override fun getLayoutResID(): Int {
        return R.layout.activity_splash
    }

    override fun initView() {
    }

    override fun initData() {
        handler = FinishHandler(this)
        handler?.postDelayed(object :Runnable{
            override fun run() {
                startActivity(Intent(this@SplashActivity,MainActivity::class.java))
                finish()
            }
        },DELAY)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler
    }


    internal class FinishHandler(activity: Activity) :
        Handler() {
        var mWeakReference: WeakReference<Activity>
        override fun handleMessage(msg: Message) {
            val activity = mWeakReference.get()
            activity?.finish()
        }

        init {
            mWeakReference = WeakReference(activity)
        }
    }
}