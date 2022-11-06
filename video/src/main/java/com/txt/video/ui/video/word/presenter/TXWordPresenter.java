package com.txt.video.ui.video.word.presenter;

import android.content.Context;
import android.util.Log;

import com.txt.video.ui.video.barrage.model.TUIBarrageIMService;
import com.txt.video.ui.video.barrage.model.TUIBarrageModel;
import com.txt.video.ui.video.barrage.presenter.TUIBarrageCallBack;
import com.txt.video.ui.video.barrage.view.ITUIBarrageDisplayView;
import com.txt.video.ui.video.word.view.ITxWordDisplayView;

public class TXWordPresenter implements ITxWordPresenter {
    private static final String TAG = "TUIBarragePresenter";

    protected Context                mContext;
    public    String                 mGroupId;
    public    String                 mAppId;
    private ITxWordDisplayView mDisplayView;

    public TXWordPresenter(Context context, String appId, String groupId) {
        mContext = context;
        mGroupId = groupId;
        mAppId = appId;
        initIMService();
    }

    private void initIMService() {

    }

    @Override
    public void initDisplayView(ITxWordDisplayView view) {
        mDisplayView = view;
    }

    @Override
    public void destroyPresenter() {
        mDisplayView = null;
    }

    @Override
    public void sendBarrage(final TUIBarrageModel model, final TUIBarrageCallBack.BarrageSendCallBack callback) {

    }

    @Override
    public void receiveBarrage(TUIBarrageModel model) {
        if (model == null || model.content == null) {
            Log.d(TAG, "receiveBarrage groupId or barrage is empty");
            return;
        }
        if (mDisplayView != null) {

        }
    }
}
