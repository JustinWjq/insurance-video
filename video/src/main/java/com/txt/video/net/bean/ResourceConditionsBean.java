package com.txt.video.net.bean;

/**
 * author ：Justin
 * time ：4/12/21.
 * des ：
 */
public class ResourceConditionsBean {

    /**
     * name : 123
     */

    private String name;
    /**
     * userId : 123123
     */

    private String userId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public ResourceConditionsBean(String name, String userId) {
        this.name = name;
        this.userId = userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
