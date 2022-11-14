package com.txt.video.ui.video.barrage;

import android.view.View;

import com.txt.video.trtc.TICManager;
import com.txt.video.trtc.ticimpl.TICManagerImpl;
import com.txt.video.ui.video.barrage.view.TUIBarrageButton;
import com.txt.video.ui.video.barrage.view.TUIBarrageDisplayView;

/**
 * author ：Justin
 * time ：2022/11/14.
 * des ：
 */
public class BarrageManager {
    private final static byte[] SYNC = new byte[1];
    private static volatile BarrageManager instance;
    public static BarrageManager getInstance() {
        if (instance == null) {
            synchronized (SYNC) {
                if (instance == null) {
                    instance = new BarrageManager();
                }
            }
        }
        return instance;
    }

    //    tuiBarbt = TUIBarrageButton(this, groupId, serviceId)
    //        setBarrage(tuiBarbt as View)
    //        //弹幕显示View
    //        displayView = TUIBarrageDisplayView(this, groupId)
    private TUIBarrageButton mTUIBarrageButton;

    public TUIBarrageButton getTUIBarrageButton() {
        return mTUIBarrageButton;
    }

    public void setTUIBarrageButton(TUIBarrageButton tUIBarrageButton) {
        this.mTUIBarrageButton = tUIBarrageButton;
    }

    private TUIBarrageDisplayView mTUIBarrageDisplayView;

    public TUIBarrageDisplayView getTUIBarrageDisplayView() {
        return mTUIBarrageDisplayView;
    }

    public void setTUIBarrageDisplayView(TUIBarrageDisplayView tUIBarrageDisplayView) {
        this.mTUIBarrageDisplayView = tUIBarrageDisplayView;
    }

    public void destroy() {
        if (null != mTUIBarrageButton) {
            mTUIBarrageButton = null;
        }
        if (null != mTUIBarrageDisplayView) {
            mTUIBarrageDisplayView = null;
        }
    }

}
