package com.txt.video.trtc.videolayout;

/**
 * Created by JustinWjq
 *
 * @date 2020/8/24.
 * description：
 */
public interface IVideoLayoutManagerListener {

    void onClickItemFill(String userId, int streamType, boolean enableFill);

    void onClickItemMuteAudio(String userId, boolean isMute);

    void onClickItemMuteVideo(String userId, int streamType, boolean isMute);

    void onClickItemMuteInSpeakerAudio(String userId, boolean isMute);

}