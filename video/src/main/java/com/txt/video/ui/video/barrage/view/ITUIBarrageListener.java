package com.txt.video.ui.video.barrage.view;

import com.txt.video.ui.video.barrage.model.TUIBarrageModel;

/**
 * 上层发送事件的监听,便于用户在发送完后更新自己的界面,或者进行其他操作
 */
public interface ITUIBarrageListener {

    void onSuccess(int code, String msg, TUIBarrageModel model);

    void onFailed(int code, String msg);
}
