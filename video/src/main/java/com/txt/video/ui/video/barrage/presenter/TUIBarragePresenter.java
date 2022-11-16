package com.txt.video.ui.video.barrage.presenter;

import android.content.Context;
import android.util.Log;

import com.txt.video.TXSdk;
import com.txt.video.base.constants.IMkey;
import com.txt.video.trtc.TRTCCloudManager;
import com.txt.video.ui.video.barrage.model.TUIBarrageIMService;
import com.txt.video.ui.video.barrage.model.TUIBarrageModel;
import com.txt.video.ui.video.barrage.view.ITUIBarrageDisplayView;
import com.txt.video.ui.video.barrage.view.ITUIBarrageListener;
import com.txt.video.ui.video.barrage.view.adapter.TUIBarrageMsgEntity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class TUIBarragePresenter implements ITUIBarragePresenter {
    private static final String TAG = "TUIBarragePresenter";

    protected Context mContext;
    public String mGroupId;
    public String mAppId;
    private ITUIBarrageDisplayView mDisplayView;
    private TUIBarrageIMService mImService;
    private final static byte[] SYNC = new byte[1];
    private static volatile TUIBarragePresenter instance;

    public static TUIBarragePresenter sharedInstance() {
        if (instance == null) {
            synchronized (SYNC) {
                if (instance == null) {
                    instance = new TUIBarragePresenter();
                }
            }
        }
        return instance;
    }

    public  ArrayList<TUIBarrageMsgEntity> mMsgList  = new ArrayList<>();
    public ArrayList<TUIBarrageMsgEntity> getmMsgList() {
        return mMsgList;
    }

    public TUIBarragePresenter() {
    }

    public void init(Context context, String appId, String groupId) {
        mContext = context;
        mGroupId = groupId;
        mAppId = appId;
        initIMService();
    }

    private void initIMService() {
        if (mImService == null) {
            mImService = new TUIBarrageIMService(this, mAppId);
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
        if (null != mImService) {
            mImService.unInitImListener();
        }
    }
    public void clearList(){
      if (null!= mMsgList){
          mMsgList.clear();
        }
    }
    protected LinkedList<WeakReference<ITUIBarrageListener>> listObservers = new LinkedList<WeakReference<ITUIBarrageListener>>();
    public void addObserver(ITUIBarrageListener mITUIBarrageListener) {

        for (WeakReference<ITUIBarrageListener> listener : listObservers) {
            ITUIBarrageListener t = listener.get();
            if (t != null && t.equals(mITUIBarrageListener)) {
                return;
            }
        }

        WeakReference<ITUIBarrageListener> weaklistener = new WeakReference<ITUIBarrageListener>(mITUIBarrageListener);
        listObservers.add(weaklistener);
    }

    public void sedMsg(int code, String msg, TUIBarrageModel model) {
        LinkedList<WeakReference<ITUIBarrageListener>> tmpList = new LinkedList<WeakReference<ITUIBarrageListener>>(listObservers);
        Iterator<WeakReference<ITUIBarrageListener>> it = tmpList.iterator();

        while (it.hasNext()) {
            ITUIBarrageListener t = it.next().get();
            if (t != null) {
                t.onSuccess(code, msg, model);
            }
        }

    }


    public void sedMsgFail(int code, String msg) {
        LinkedList<WeakReference<ITUIBarrageListener>> tmpList = new LinkedList<WeakReference<ITUIBarrageListener>>(listObservers);
        Iterator<WeakReference<ITUIBarrageListener>> it = tmpList.iterator();

        while (it.hasNext()) {
            ITUIBarrageListener t = it.next().get();
            if (t != null) {
                t.onFailed(code, msg);
            }
        }

    }

    public void removeObserver() {
        Iterator<WeakReference<ITUIBarrageListener>> it = listObservers.iterator();
        while (it.hasNext()) {
            ITUIBarrageListener t = it.next().get();
            if (t != null) {
                it.remove();
                break;
            }
        }
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

    @Override
    public void removeOb(ITUIBarrageListener mITUIBarrageListener) {
        Iterator<WeakReference<ITUIBarrageListener>> it = listObservers.iterator();
        while (it.hasNext()) {
            ITUIBarrageListener t = it.next().get();
            if (t != null && t == mITUIBarrageListener) {
                it.remove();
                break;
            }
        }
    }
}
