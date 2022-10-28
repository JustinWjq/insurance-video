package com.txt.video.net.bean;

/**
 * author ：Justin
 * time ：4/12/21.
 * des ：
 */
public class ResourcegsConditionsBean {

    public ResourcegsConditionsBean(String name, String key, boolean isCheck) {
        this.name = name;
        this.key = key;
        this.isCheck = isCheck;
    }

    /**
     * name : 123
     */

    private String name;

    private String key;

    private boolean isCheck;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
