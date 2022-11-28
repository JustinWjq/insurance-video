package com.txt.video.net.bean;

/**
 * author ：Justin
 * time ：2022/11/28.
 * des ：
 */
public class UserInfoBean {
    private String title;
    private String content;

    public UserInfoBean(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
