package com.evc.evcapp.bean;

/**
 * @Package: com.evc.evcapp.web
 * @Description: java类作用描述
 * @Author: EvanChan
 * @CreateDate: 9/26/21 4:12 PM
 * @m-mail: dadaintheair@gmail.com
 */
public class GetLoactionBean {
    private Boolean state;
    private String longitude;

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    private String latitude;
}
