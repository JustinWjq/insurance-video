package com.txt.video.ui.video.barrage.presenter;

import android.content.Context;
import android.util.Log;

import com.txt.video.TXSdk;
import com.txt.video.base.constants.IMkey;
import com.txt.video.ui.video.barrage.model.TUIBarrageIMService;
import com.txt.video.ui.video.barrage.model.TUIBarrageModel;
import com.txt.video.ui.video.barrage.view.ITUIBarrageDisplayView;

public class TUIBarragePresenter implements ITUIBarragePresenter {
    private static final String TAG = "TUIBarragePresenter";

    protected Context                mContext;
    public    String                 mGroupId;
    public    String                 mAppId;
    private ITUIBarrageDisplayView mDisplayView;
    private TUIBarrageIMService mImService;

    public TUIBarragePresenter(Context context,String appId, String groupId) {
        mContext = context;
        mGroupId = groupId;
        mAppId = appId;
        initIMService();
    }

    private void initIMService() {
        if (mImService == null) {
            mImService = new TUIBarrageIMService(this,mAppId);
        }
        mImService.setGroupId(mGroupId);
    }

    @Override
    public void initDisplayView(ITUIBarrageDisplayView view) {
        mDisplayView = view;
    }

    @Override
    public void destroyPresenter() {
        mDisplayView = null;
        mImService.unInitImListener();
    }

    @Override
    public void sendBarrage(final TUIBarrageModel model, final TUIBarrageCallBack.BarrageSendCallBack callback) {
        if (mImService == null) {
            initIMService();
        }

        mImService.sendBarrage(model, new TUIBarrageCallBack.ActionCallback() {
            @Override
            public void onCallback(int code, String msg) {
                if (code != 0) {
                    callback.onFailed(code, msg);
                    Log.d(TAG, "sendBarrage failed errorCode = " + code + " , errorMsg = " + msg);
                    return;
                }
                //发送成功,回调给自己进行显示
                callback.onSuccess(code, msg, model);
            }
        });
    }

    @Override
    public void receiveBarrage(TUIBarrageModel model) {
        if (model == null || model.content == null) {
            Log.d(TAG, "receiveBarrage groupId or barrage is empty");
            return;
        }
        if (mDisplayView != null) {
            mDisplayView.receiveBarrage(model);
        }
    }
}
