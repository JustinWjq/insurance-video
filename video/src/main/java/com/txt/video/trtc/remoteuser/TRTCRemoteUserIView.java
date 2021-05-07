package com.txt.video.trtc.remoteuser;

import android.graphics.Bitmap;

import com.tencent.rtmp.ui.TXCloudVideoView;

/**
 * Created by JustinWjq
 *
 * @date 2020/8/25.
 * description：
 */
public interface TRTCRemoteUserIView {
    /**
     * 界面相关回调
     */

    TXCloudVideoView getRemoteUserViewById(String userId, int steamType);

    void onRemoteViewStatusUpdate(String userId, boolean enable);

    void onSnapshotRemoteView(Bitmap bm);


}
