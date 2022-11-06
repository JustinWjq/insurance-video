package com.txt.video.ui.video.barrage.presenter;

import com.txt.video.ui.video.barrage.model.TUIBarrageModel;

public class TUIBarrageCallBack {

    /**
     * 通用回调
     */
    public interface ActionCallback {
        void onCallback(int code, String msg);
    }

    /**
     * 弹幕发送的回调
     */
    public interface BarrageSendCallBack {
        void onSuccess(int code, String msg, TUIBarrageModel model);

        void onFailed(int code, String msg);
    }
}
