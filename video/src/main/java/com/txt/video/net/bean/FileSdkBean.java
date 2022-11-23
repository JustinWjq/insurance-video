package com.txt.video.net.bean;

import java.util.List;

/**
 * author ：Justin
 * time ：2022/11/21.
 * des ： 用于 接受甲方传入的视频数据
 */
public class FileSdkBean {

    //图片，视频，链接
    public FileType type;

    //图片数组
    private List<String> pics;
    //提词器内容
    private List<String> picsWord;

    public List<String> getPicsWord() {
        return picsWord;
    }

    public void setPicsWord(List<String> picsWord) {
        this.picsWord = picsWord;
    }

    //视频url
    private String videoUrl;

    //链接url
    private String h5Url;

    private String h5Name;

    private String cookie;

    public String getH5Name() {
        return h5Name;
    }

    public void setH5Name(String h5Name) {
        this.h5Name = h5Name;
    }

    public FileSdkBean(FileType type, List<String> pics) {
        this.type = type;
        this.pics = pics;
    }

    public FileSdkBean(FileType type, String videoUrl) {
        this.type = type;
        this.videoUrl = videoUrl;
    }

    public FileSdkBean(FileType type, String h5Url, String cookie) {
        this.type = type;
        this.h5Url = h5Url;
        this.cookie = cookie;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getH5Url() {
        return h5Url;
    }

    public void setH5Url(String h5Url) {
        this.h5Url = h5Url;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    @Override
    public String toString() {
        return "FileSdkBean{" +
                "type=" + type +
                ", pics=" + pics +
                ", picsWord=" + picsWord +
                ", videourl='" + videoUrl + '\'' +
                ", h5url='" + h5Url + '\'' +
                ", cookie=" + cookie +
                '}';
    }
}
