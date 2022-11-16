package com.txt.video.ui.video.barrage.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tencent.imsdk.TIMMessage;
import com.txt.video.base.constants.IMkey;
import com.txt.video.net.utils.TxLogUtils;
import com.txt.video.trtc.TICManager;
import com.txt.video.trtc.ticimpl.TICCallback;
import com.txt.video.trtc.ticimpl.TICMessageListener;
import com.txt.video.ui.video.barrage.presenter.ITUIBarragePresenter;
import com.txt.video.ui.video.barrage.presenter.TUIBarrageCallBack;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * IM服务类,调用IM SDK接口进行弹幕消息的发送和接收
 * sendBarrage              发送弹幕信息
 * onRecvGroupCustomMessage 接收弹幕信息
 */
public class TUIBarrageIMService {
    private static final String TAG = "TUIBarrageIMService";

    private SimpleListener       mSimpleListener;
    private ITUIBarragePresenter mPresenter;
    private String               mGroupId;
    private String               mAppId;

    public TUIBarrageIMService(ITUIBarragePresenter presenter, String appId) {
        initIMListener();
        this.mAppId = appId;
        mPresenter = presenter;
    }

    public void setGroupId(String groupId) {
        mGroupId = groupId;
    }

    //初始化IM监听
    private void initIMListener() {

//        V2TIMMessageManager messageManager = V2TIMManager.getMessageManager();
        if (mSimpleListener == null) {
            mSimpleListener = new SimpleListener();
        }
        TICManager.getInstance().addIMMessageListener(mSimpleListener);
    }

    public void unInitImListener() {
//        TICManager.getInstance()
//        V2TIMManager.getInstance().setGroupListener(null);
        TICManager.getInstance().removeIMMessageListener(mSimpleListener);
    }

    public void sendBarrage(TUIBarrageModel model, final TUIBarrageCallBack.ActionCallback callback) {
        if (TextUtils.isEmpty(model.content)) {
            TxLogUtils.i(TAG, "sendBarrage data is empty");
            return;
        }
        Gson gson = new Gson();
        TxLogUtils.i(TAG,"sendBarrage: data = " +gson.toJson(model) + " , mGroupId = " + mGroupId);
        TICManager.getInstance().sendGroupTextMessage(
                gson.toJson(model),
                new TICCallback() {
                    @Override
                    public void onSuccess(Object data) {
                        TxLogUtils.i("onSuccess----onSuccess");
                        if (callback != null) {
                            callback.onCallback(0, "send group message success.");
                            TxLogUtils.i("sendGroupCustomMessage success");
                        }
                    }

                    @Override
                    public void onError(String module, int errCode, String errMsg) {
                        TxLogUtils.i("sendGroupCustomMessage error " +errCode + " errorMessage:" + errMsg);
                        if (callback != null) {
                            callback.onCallback(errCode, errMsg);
                        }
                    }
                }
        );
//        V2TIMManager.getInstance().sendGroupTextMessage(model.message, mGroupId, V2TIMMessage.V2TIM_PRIORITY_HIGH,
//                new V2TIMValueCallback<V2TIMMessage>() {
//                    @Override
//                    public void onSuccess(V2TIMMessage v2TIMMessage) {
//                        if (callback != null) {
//                            callback.onCallback(0, "send group message success.");
//                            Log.e(TAG, "sendGroupCustomMessage success");
//                        }
//                    }
//
//                    @Override
//                    public void onError(int i, String s) {
//                        Log.e(TAG, "sendGroupCustomMessage error " + i + " errorMessage:" + s);
//                        if (callback != null) {
//                            callback.onCallback(i, s);
//                        }
//                    }
//                });


    }

//    private class SimpleListener extends V2TIMSimpleMsgListener {
//        @Override
//        public void onRecvGroupTextMessage(String msgID, String groupID, V2TIMGroupMemberInfo sender, String text) {
//            Log.d(TAG, "onRecvGroupCustomMessage: msgID = " + msgID + " , groupID = " + groupID
//                    + " , mGroupId = " + mGroupId + " , sender = " + sender + " , text = " + text);
//            if (groupID == null || !groupID.equals(mGroupId)) {
//                return;
//            }
//            if (TextUtils.isEmpty(text)) {
//                Log.d(TAG, "onRecvGroupCustomMessage customData is empty");
//                return;
//            }
//            HashMap<String, String> userMap = new HashMap<>();
//            userMap.put(TUIBarrageConstants.KEY_USER_ID, sender.getUserID());
//            userMap.put(TUIBarrageConstants.KEY_USER_NAME, sender.getNickName());
//            userMap.put(TUIBarrageConstants.KEY_USER_AVATAR, sender.getFaceUrl());
//            TUIBarrageModel model = new TUIBarrageModel();
//            model.message = text;
//            model.extInfo = userMap;
//
//            if (mPresenter != null) {
//                mPresenter.receiveBarrage(model);
//            }
//        }
//    }


    private class SimpleListener implements TICMessageListener {

        @Override
        public void onTICRecvTextMessage(String fromUserId, String text) {

        }

        @Override
        public void onTICRecvCustomMessage(String fromUserId, byte[] data) {

        }

        @Override
        public void onTICRecvGroupTextMessage(String fromUserId, String text) {
            if (mPresenter != null) {
                try {
                    TxLogUtils.i(TAG,"onTICRecvGroupTextMessage"+text);
                    JSONObject jsonObject = new JSONObject(text);
                    String type = jsonObject.optString("type");
                    if (IMkey.wxIM.equals(type)) {
                        String content = jsonObject.optString("content");
                        String userName = jsonObject.optString("userName");
                        String userId = jsonObject.optString("userId");
                        TUIBarrageModel tuiBarrageModel = new TUIBarrageModel();
                        tuiBarrageModel.content = content ;
                        tuiBarrageModel.userId = userId;
                        tuiBarrageModel.userName =userName ;
                        mPresenter.receiveBarrage(tuiBarrageModel);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

        @Override
        public void onTICRecvGroupCustomMessage(String fromUserId, byte[] data) {

        }
//onTICRecvGroupTextMessage
        @Override
        public void onTICRecvMessage(TIMMessage message) {

        }
    }

    //自定义弹幕发送数据
    public static String getTextMsgJsonStr(TUIBarrageModel model) {
        if (model == null) {
            return null;
        }
        TUIBarrageJson sendJson = new TUIBarrageJson();
        sendJson.setBusinessID(TUIBarrageConstants.VALUE_BUSINESS_ID);
        sendJson.setPlatform(TUIBarrageConstants.VALUE_PLATFORM);
        sendJson.setVersion(TUIBarrageConstants.VALUE_VERSION);

        TUIBarrageJson.Data data = new TUIBarrageJson.Data();
        data.setMessage(model.content);

        //扩展信息
        TUIBarrageJson.Data.ExtInfo extInfo = new TUIBarrageJson.Data.ExtInfo();
//        extInfo.setUserID(model.userId);
//        extInfo.setNickName(model.extInfo.get(TUIBarrageConstants.KEY_USER_NAME));
//        extInfo.setAvatarUrl(model.extInfo.get(TUIBarrageConstants.KEY_USER_AVATAR));

        data.setExtInfo(extInfo);
        sendJson.setData(data);

        Gson gson = new Gson();
        return gson.toJson(sendJson);
    }
}
