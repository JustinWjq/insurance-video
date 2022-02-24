package com.txt.video.trtc.videolayout.list;

import android.text.TextUtils;

import com.txt.video.TXSdk;

public class MemberEntity {
    public static final int QUALITY_GOOD   = 3;
    public static final int QUALITY_NORMAL = 2;
    public static final int QUALITY_BAD    = 1;

    private String userId;
    private String userName;
    private String userAvatar;
    private String userRole = "";
    private String userRoleIconPath = "";

    private int videoMode = TXSdk.getInstance().getRoomControlConfig().getVideoMode();

    public int getVideoMode() {
        return videoMode;
    }

    public void setVideoMode(int videoMode) {
        this.videoMode = videoMode;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserRoleIconPath() {
        return userRoleIconPath;
    }

    public void setUserRoleIconPath(String userRoleIconPath) {
        this.userRoleIconPath = userRoleIconPath;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean host) {
        isHost = host;
    }

    //是否主持人
    private boolean isHost;
    private int              quality;
    private int              audioVolume;
    private boolean          isShowAudioEvaluation = false;
    private boolean          isShowOutSide = false;
    // 用户是否打开了视频
    private boolean          isVideoAvailable;
    // 用户是否打开音频
    private boolean          isAudioAvailable;
    // 是否对用户静画
    private boolean          isMuteVideo;
    // 是否对用户静音
    private boolean          isMuteAudio;
    // 用户投屏状态
    private boolean          isScreen;

    public boolean isScreen() {
        return isScreen;
    }

    public void setScreen(boolean screen) {
        isScreen = screen;
    }

    private MeetingVideoView mMeetingVideoView;
    private boolean          needFresh     = false;

    public boolean isNeedFresh() {
        return needFresh;
    }

    public void setNeedFresh(boolean needFresh) {
        this.needFresh = needFresh;
    }


    public int getAudioVolume() {
        return audioVolume;
    }

    public void setAudioVolume(int audioVolume) {
        this.audioVolume = audioVolume;
    }

    public boolean isShowOutSide() {
        return isShowOutSide;
    }

    public void setShowOutSide(boolean showOutSide) {
        isShowOutSide = showOutSide;
    }

    public boolean isShowAudioEvaluation() {
        return isShowAudioEvaluation;
    }

    public void setShowAudioEvaluation(boolean showAudioEvaluation) {
        isShowAudioEvaluation = showAudioEvaluation;
    }

    public MeetingVideoView getMeetingVideoView() {
        return mMeetingVideoView;
    }

    public void setMeetingVideoView(MeetingVideoView meetingVideoView) {
        mMeetingVideoView = meetingVideoView;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return TextUtils.isEmpty(userName) ? "" : userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public boolean isVideoAvailable() {
        return isVideoAvailable;
    }

    public void setVideoAvailable(boolean videoAvailable) {
        isVideoAvailable = videoAvailable;
    }

    public boolean isAudioAvailable() {
        return isAudioAvailable;
    }

    public void setAudioAvailable(boolean audioAvailable) {
        isAudioAvailable = audioAvailable;
    }

    public boolean isMuteVideo() {
        return isMuteVideo;
    }

    public void setMuteVideo(boolean muteVideo) {
        isMuteVideo = muteVideo;
    }

    public boolean isMuteAudio() {
        return isMuteAudio;
    }

    public void setMuteAudio(boolean muteAudio) {
        isMuteAudio = muteAudio;
    }
}
