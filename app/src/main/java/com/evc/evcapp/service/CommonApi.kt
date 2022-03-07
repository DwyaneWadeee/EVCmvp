package com.evc.evcapp.service
import com.evc.evcapp.bean.TokenBean
import com.evc.foundation.net.HttpResult
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * Created by Android Studio.
 * User: evan
 * Date: 2020/11/13
 * Time: 5:17 PM
 */
interface CommonApi{
    /**
     * 登录
     */
    @POST("auth/auth/v1/login/id_card/rsa")
    fun getToken(@Body body:RequestBody): Observable<HttpResult<TokenBean?>>

}