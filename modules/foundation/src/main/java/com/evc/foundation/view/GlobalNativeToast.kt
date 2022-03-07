package com.evc.foundation.view

import android.annotation.SuppressLint
import android.app.Application
import android.text.TextUtils
import android.widget.Toast

/**
 *
 * @Package: com.evc.foundation.view
 * @Description: 避免多次显示同一个 toast
 * @Author: EvanChan
 * @CreateDate: 8/10/21 11:20 AM
 * @m-mail: dadaintheair@gmail.com
 */
class GlobalNativeToast {
    companion object{
        private var oldMsg: String? = null
        private var time: Long = 0
        @JvmStatic
        @SuppressLint("ShowToast")
        fun show(
            application: Application?,
            text: CharSequence?,
            toastDuration: Int
        ) {
            /**
             *
             */
            if (TextUtils.isEmpty(text) || text == "未知异常" || text.toString().startsWith("HTTP 504")) {
                return
            }
            if (text != oldMsg) { // 当显示的内容不一样时，即断定为不是同一个Toast
                Toast.makeText(application, text, toastDuration).show()
                time = System.currentTimeMillis()
            } else {
                // 显示内容一样时，只有间隔时间大于2秒时才显示
                if (System.currentTimeMillis() - time > 2000) {
                    Toast.makeText(application, text, toastDuration).show()
                    time = System.currentTimeMillis()
                }
            }
            oldMsg = text.toString()
        }

        @JvmStatic
        fun show(application: Application?, text: CharSequence) {
            show(application, text, Toast.LENGTH_SHORT)
        }
    }

}
