package com.txt.video.ui.video.barrage.presenter;

import com.txt.video.ui.video.barrage.model.TUIBarrageModel;
import com.txt.video.ui.video.barrage.view.ITUIBarrageDisplayView;
import com.txt.video.ui.video.barrage.view.ITUIBarrageListener;

public interface ITUIBarragePresenter {
    /**
     * 初始化弹幕显示视图
     *
     * @param view 弹幕显示视图
     */

    void initDisplayView(ITUIBarrageDisplayView view);

    /**
     * 销毁Present 释放对象
     */
    void destroyPresenter();

    /**
     * 发送弹幕
     *
     * @param model    弹幕内容
     * @param callback 发送成功或失败的回调
     */
    void sendBarrage(TUIBarrageModel model, TUIBarrageCallBack.BarrageSendCallBack callback);

    /**
     * 接收弹幕
     *
     * @param model 弹幕内容
     */
    void receiveBarrage(TUIBarrageModel model);

    void removeOb(ITUIBarrageListener mITUIBarrageListener);

}
