package com.txt.video.net.bean;

/**
 * author ：Justin
 * time ：4/12/21.
 * des ：
 */
public class ResourceTypeBean {

    String name;
    String picUrl;
    String screenUrl;

    public ResourceTypeBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getScreenUrl() {
        return screenUrl;
    }

    public ResourceTypeBean(String name, String picUrl) {
        this.name = name;
        this.picUrl = picUrl;
    }

    public void setScreenUrl(String screenUrl) {
        this.screenUrl = screenUrl;
    }
}
