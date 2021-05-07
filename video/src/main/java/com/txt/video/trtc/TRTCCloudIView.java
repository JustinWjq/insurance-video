package com.txt.video.trtc;

import android.graphics.Bitmap;

/**
 * Created by JustinWjq
 *
 * @date 2020/8/24.
 * description： 可能会与界面相关的回调
 */
public interface TRTCCloudIView {


    /**
     * 是否开启音量提示条，界面可以显示/隐藏音量提示条
     *
     * @param enable
     */
    void onAudioVolumeEvaluationChange(boolean enable);

    /**
     * 开始连麦回调，界面可以展示loading状态
     */
    void onStartLinkMic();

    /**
     * 屏蔽本地视频回调，界面可以更新对应按钮状态
     *
     * @param isMute
     */
    void onMuteLocalVideo(boolean isMute);

    /**
     * 屏蔽本地音频回调，界面可以更新对应按钮状态
     *
     * @param isMute
     */
    void onMuteLocalAudio(boolean isMute);

    /**
     * 视频截图回调
     *
     * @param bmp
     */
    void onSnapshotLocalView(Bitmap bmp);
}
