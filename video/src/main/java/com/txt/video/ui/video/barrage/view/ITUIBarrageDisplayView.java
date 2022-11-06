package com.txt.video.ui.video.barrage.view;


import com.txt.video.ui.video.barrage.model.TUIBarrageModel;

public interface ITUIBarrageDisplayView {

    /**
     * 接收弹幕
     *
     * @param model 弹幕信息
     */
    void receiveBarrage(TUIBarrageModel model);
}
