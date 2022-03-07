package com.evc.evcapp.service

import com.evc.toolkit.http.HttpServiceGenerator


/**
 * Created by Android Studio.
 * User: evan
 * Date: 2020/11/13
 * Time: 5:16 PM
 */
object CommomService{
    public val api: CommonApi by lazy {
        HttpServiceGenerator.create(CommonApi::class.java)
    }

}