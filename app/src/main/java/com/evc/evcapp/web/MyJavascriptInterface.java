package com.evc.evcapp.web;

import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSON;
import com.evc.evcapp.bean.GetLoactionBean;
import com.evc.evcapp.bean.UserBean;

/**
 * Created by jingbin on 2016/11/17.
 * js通信接口
 */
public class MyJavascriptInterface extends Object {

    /**
     * 网页使用的js，方法无参数
     * H5原生交互获取经纬度
     */
    @JavascriptInterface
    public String getLocation() {
        GetLoactionBean bean = new GetLoactionBean();
//        113.351317,23.098207
        bean.setState(true);
        bean.setLatitude("113.351317");
        bean.setLongitude("23.098207");

        //转成json
        String s = JSON.toJSONString(bean);
        return s;
    }


    @JavascriptInterface
    public String getUserData() {
        UserBean bean = new UserBean();

        bean.setUserName("周杰伦");
        bean.setPoliceNum("654321");
        bean.setIdCard("4401023198001234567");
        bean.setPoliceNum("13412345678");
        bean.setDeptCode("123123123");
        bean.setDeptName("广东省广州市交通局公共交通分局侦查十大队");
        bean.setPoliceType("mj");
        bean.setJob("jobSample");

        //转成json
        String s = JSON.toJSONString(bean);
        return s;
    }


}