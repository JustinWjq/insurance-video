package com.txt.video.ui.video.barrage.view;


import com.txt.video.ui.video.barrage.model.TUIBarrageModel;

public interface ITUIBarrageSendView {
    /**
     * 发送弹幕
     *
     * @param model 弹幕信息
     */
    void sendBarrage(TUIBarrageModel model);
}
