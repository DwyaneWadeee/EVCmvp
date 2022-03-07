package com.evc.foundation.net;


/**
 * Created by Android Studio.
 * User: evan
 * Date: 2020/10/27
 * Time: 5:04 PM
 */
public class HttpResult<T> {
    /**
     * 1
     * 约定返回角色
     * code : 0
     * errCode : -1
     * message : SUCCESS
     * responseTime : 2020-11-24 19:06:35
     * data : {"user":null,"permissions":null,"roles":null,"accessToken":"bdaecdc6-33c4-4f3d-826a-7a9cbe318a04","tokenType":"bearer","refreshToken":"9b97f264-5a94-439e-bff9-87e6d48b6b62","expiresIn":"37000"}
     * extend : {}
     */
    private int code;
    private int errCode;
    private String message;
    private String responseTime;
    private T data;
    private ExtendBean extend;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ExtendBean getExtend() {
        return extend;
    }

    public void setExtend(ExtendBean extend) {
        this.extend = extend;
    }

    public static class ExtendBean {
    }

}
