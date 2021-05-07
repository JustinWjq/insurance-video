package com.txt.video.trtc.videolayout;

/**
 * Created by JustinWjq
 *
 * @date 2020/8/24.
 * description：
 */
public interface IVideoLayoutListener {
    void onClickFill(TRTCVideoLayout view, boolean enableFill);

    void onClickMuteAudio(TRTCVideoLayout view, boolean isMute);

    void onClickMuteVideo(TRTCVideoLayout view, boolean isMute);

    void onClickMuteInSpeakerAudio(TRTCVideoLayout view, boolean isMute);
}