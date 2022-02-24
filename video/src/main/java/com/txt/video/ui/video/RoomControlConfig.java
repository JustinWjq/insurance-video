package com.txt.video.ui.video;

/**
 * author ：Justin
 * time ：2021/12/27.
 * des ：
 */
public class RoomControlConfig {
    //水平滚动模式 1
    //九宫格模式
    private int videoMode;

    private boolean enableVideo;


    public boolean isEnableVideo() {
        return this.enableVideo;
    }

    public int getVideoMode() {
        return this.videoMode;
    }

    private RoomControlConfig(boolean enableVideo) {
        this.enableVideo = enableVideo;
    }

    private RoomControlConfig(int videoMode) {
        this.videoMode = videoMode;
    }

    private RoomControlConfig(boolean enableVideo, int videoMode) {
        this.enableVideo = enableVideo;
        this.videoMode = videoMode;
    }

    public static class Builder {
        boolean enableVideo = true;
        int videoMode = VideoMode.getVIDEOMODE_HORIZONTAL();


        public Builder() {
        }

        public RoomControlConfig.Builder enableVideo(boolean enableVideo) {
            this.enableVideo = enableVideo;
            return this;
        }

        public RoomControlConfig.Builder setVideoMode(int videoMode) {
            this.videoMode = videoMode;
            return this;
        }

        public RoomControlConfig build() {
            return new RoomControlConfig(this.enableVideo, this.videoMode);
        }
    }
}
