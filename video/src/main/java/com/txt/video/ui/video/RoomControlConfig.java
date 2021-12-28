package com.txt.video.ui.video;

/**
 * author ：Justin
 * time ：2021/12/27.
 * des ：
 */
public class RoomControlConfig {

    //encParam.videoResolution = TRTCCloudDef.TRTC_VIDEO_RESOLUTION_640_360
    //        encParam.videoFps = Constant.VIDEO_FPS
    //        encParam.videoBitrate = Constant.RTC_VIDEO_BITRATE
    //        encParam.videoResolutionMode = TRTCCloudDef.TRTC_VIDEO_RESOLUTION_MODE_LANDSCAPE

    private boolean enableVideo;


    public boolean isEnableVideo() {
        return this.enableVideo;
    }

    private RoomControlConfig(boolean enableVideo) {
        this.enableVideo = enableVideo;
    }

    public static class Builder {
        boolean enableVideo = true;


        public Builder() {
        }

        public RoomControlConfig.Builder enableVideo(boolean enableVideo) {
            this.enableVideo = enableVideo;
            return this;
        }

        public RoomControlConfig build() {
            return new RoomControlConfig(this.enableVideo);
        }
    }
}
