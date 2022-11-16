package com.txt.video.trtc.remoteuser;

import com.tencent.trtc.TRTCCloudDef;

import java.io.Serializable;

/**
 * 远程用户设置项
 *
 * @author guanyifeng
 */
public class RemoteUserConfig implements Serializable {
    // 远程的用户名
    private String mUserName;
    //userId
    private String mUserId;
    // 远程用户是主流还是辅流
    private int     mStreamType;
    // 是否打开远程用户的视频
    private boolean mEnableVideo;
    // 是否打开远程用户的音频
    private boolean mEnableAudio;
    // 界面的填充模式， true 填充（画面可能会被拉伸裁剪） false 适应（画面可能会有黑边）
    private boolean mFillMode;
    // 画面的旋转角度 0, 90, 270
    private int     mRotation;
    // 远程用户音量大小
    private int     mVolume;

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public RemoteUserConfig(String userName) {
        reset();
        mUserName = userName;
    }

    public RemoteUserConfig(String userName, int streamType) {
        reset();
        mUserName = userName;
        mStreamType = streamType;
    }

    public RemoteUserConfig(String mUserName, String mUserId, int mStreamType) {
        this.mUserName = mUserName;
        this.mUserId = mUserId;
        this.mStreamType = mStreamType;
    }

    public void reset() {
        mUserName = "";
        mStreamType = TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG;
        mEnableVideo = true;
        mEnableAudio = true;
        mFillMode = true;
        mRotation = 0;
        mVolume = 50;
    }

    public int getStreamType() {
        return mStreamType;
    }

    public void setStreamType(int streamType) {
        mStreamType = streamType;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public boolean isEnableVideo() {
        return mEnableVideo;
    }

    public void setEnableVideo(boolean enableVideo) {
        mEnableVideo = enableVideo;
    }

    public boolean isEnableAudio() {
        return mEnableAudio;
    }

    public void setEnableAudio(boolean enableAudio) {
        mEnableAudio = enableAudio;
    }

    public boolean isFillMode() {
        return mFillMode;
    }

    public void setFillMode(boolean fillMode) {
        mFillMode = fillMode;
    }

    public int getRotation() {
        return mRotation;
    }

    public void setRotation(int rotation) {
        mRotation = rotation;
    }

    public int getVolume() {
        return mVolume;
    }

    public void setVolume(int volume) {
        mVolume = volume;
    }
}
