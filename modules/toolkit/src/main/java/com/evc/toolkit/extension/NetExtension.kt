package com.evc.toolkit.extension

import com.jakewharton.rxbinding3.view.clicks
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

/**
 * Created by Android Studio.
 * User: evan
 * Date: 2020/11/16
 * Time: 5:24 PMs
 */
/**
 * 用于把String的json串转化为RequestBody，供请求使用
 */
fun String.convert2RequestBody(): RequestBody {
    return RequestBody.create("application/json;charset=UTF-8".toMediaTypeOrNull(), this)
}