package com.txt.video.trtc.ticimpl;

import com.tencent.imsdk.TIMMessage;

/**
 * Created by JustinWjq
 *
 * @date 2020/8/25.
 * description：
 */

//IM消息回调
public interface TICMessageListener {
    //点到点消息
    void onTICRecvTextMessage(String fromUserId, String text);
    void onTICRecvCustomMessage(String fromUserId, byte[] data);

    //群消息
    void onTICRecvGroupTextMessage(String fromUserId, String text);
    void onTICRecvGroupCustomMessage(String fromUserId, byte[] data);

    //所有消息
    void onTICRecvMessage(TIMMessage message);
}