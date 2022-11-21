package com.txt.video;

import android.app.Activity;
import android.app.Application;


import com.txt.video.common.callback.onSDKListener;
import com.txt.video.net.bean.TxConfig;
import com.txt.video.net.http.SystemHttpRequest;
import com.txt.video.net.utils.TxLogUtils;
import com.txt.video.trtc.TICManager;
import com.txt.video.common.callback.StartVideoResultOnListener;
import com.txt.video.common.callback.onCreateRoomListener;
import com.txt.video.common.callback.onFriendBtListener;
import com.txt.video.common.utils.AppUtils;
import com.txt.video.ui.TXManagerImpl;
import com.txt.video.ui.video.FriendBtObservable;
import com.txt.video.ui.video.RoomControlConfig;

import org.json.JSONObject;


/**
 * Created by JustinWjq
 *
 * @date 2020/8/19.
 * description：暴露功能类
 */
public class TXSdk extends TXSDKApi {
    private static volatile TXSdk singleton = null;

    private boolean isDebug = true;

    private Environment environment = Environment.TEST;

    private String wxKey = "";

    private TxConfig txConfig;

    private boolean isDemo = false;

    private String SDKVersion = "v1.0.0";

    private String terminal = "android";

    private String wxTransaction = "";

    private String agent;

    private String agentName;

    private boolean isShare;

    private FriendBtObservable mFriendBtObservable;

    @Override
    public boolean getShare() {
        return isShare;
    }

    @Override
    public void setShare(boolean share) {
        isShare = share;
    }

    @Override
    public String getAgent() {
        return agent;
    }

    //后台处理过的username
    @Override
    public void setAgent(String agent) {
        this.agent = agent;
    }

    @Override
    public String getAgentName() {
        return agentName;
    }

    @Override
    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    @Override
    public String getTerminal() {
        return terminal;
    }

    @Override
    public String getSDKVersion() {
        return SDKVersion;
    }

    @Override
    public boolean isDemo() {
        return isDemo;
    }

    @Override
    public void setDemo(boolean demo) {
        isDemo = demo;
    }

    @Override
    public String getWxTransaction() {
        return wxTransaction;
    }

    @Override
    public void setWxTransaction(String wxTransaction) {
        this.wxTransaction = wxTransaction;
    }

    @Override
    public TxConfig getTxConfig() {
        if (txConfig == null) {
            txConfig = new TxConfig();
        }
        return txConfig;
    }

    @Override
    public void setTxConfig(TxConfig txConfig) {
        this.txConfig = txConfig;
    }

    @Override
    public Environment getEnvironment() {
        return environment;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public boolean isDebug() {
        return isDebug;
    }

    @Override
    public void setDebug(boolean debug) {
        isDebug = debug;
    }


    private TXSdk() {

    }

    public Application application;

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public void init(Application application) {
        init(application, environment, isDebug);
    }

    public void init(Application application, Environment en, boolean isDebug) {
        TxLogUtils.i("SDKVersion:" + getSDKVersion());
        this.application = application;
        this.isDebug = isDebug;
        if (txConfig == null) {
            txConfig = new TxConfig();
        }
        checkoutNetEnv(en);
        AppUtils.init(application);
        mFriendBtObservable = new FriendBtObservable();
    }

    @Override
    public void init(Application application, Environment en, boolean isDebug, TxConfig txConfig) {
        this.txConfig = txConfig;
        init(application, en, isDebug);
    }

    @Override
    public void checkoutNetEnv(Environment en) {
        setEnvironment(en);
        SystemHttpRequest.getInstance().changeIP(en);
    }


    @Override
    public void startTXVideo(Activity context, String agent, String orgAccount, String sign, JSONObject businessData, RoomControlConfig roomControlConfig, StartVideoResultOnListener listener) {
        this.mRoomControlConfig = roomControlConfig;
        TXManagerImpl.getInstance().checkPermission(context, agent, orgAccount, sign, businessData, listener, true);
    }

    @Override
    public void startTXVideo(Activity context, String agent, String orgAccount, String sign, StartVideoResultOnListener listener) {
        TXManagerImpl.getInstance().checkPermission(context, agent, orgAccount, sign, null, listener, true);
    }

    @Override
    public void createRoom(final String agent, String orgAccount, String sign, final onCreateRoomListener listener) {
        TXManagerImpl.getInstance().createRoom(agent, orgAccount, sign, null, null, listener);
    }


    RoomControlConfig mRoomControlConfig;

    @Override
    public void joinRoom(Activity context, String roomId, String account, String userName, String orgAccount, String sign, JSONObject businessData, RoomControlConfig roomControlConfig, StartVideoResultOnListener listener) {
        this.mRoomControlConfig = roomControlConfig;
        TXManagerImpl.getInstance().checkPermission(context, roomId, account, userName, orgAccount, sign, businessData, listener, false);
    }

    @Override
    public void getAgentAndRoomStatus(String agentId, String serviceId, String orgAccount, String sign, onSDKListener onSDKListener) {
        TXManagerImpl.getInstance().getAgentAndRoomStatus(
                agentId, serviceId, orgAccount, sign, onSDKListener
        );
    }

    @Override
    public void setAgentInRoomStatus(String account, String userName, String serviceId, String inviteAccount, String action, String orgAccount, String sign, onSDKListener onSDKListener) {
        TXManagerImpl.getInstance().setAgentInRoomStatus(
                account, userName, serviceId, inviteAccount, action, orgAccount, sign, onSDKListener
        );
    }

    public RoomControlConfig getRoomControlConfig() {
        if (null != mRoomControlConfig) {
            return mRoomControlConfig;
        } else {
            RoomControlConfig builder = new RoomControlConfig.Builder().enableVideo(false).build();
            return builder;
        }


    }

    private onFriendBtListener onFriendBtListener;

    @Override
    public void addOnFriendBtListener(onFriendBtListener onFriendBtListener) {
        //mFriendBtObservable.addObserver(onFriendBtListener);
        this.onFriendBtListener = onFriendBtListener;
    }

    @Override
    public void removeOnFriendBtListener(onFriendBtListener onFriendBtListener) {
        this.onFriendBtListener = null;
    }

    public onFriendBtListener getOnFriendBtListener() {
        return this.onFriendBtListener;
    }


    public void unInit() {
        if (TICManager.getInstance() != null) {
            TICManager.getInstance().unInit();
        }
    }

    public enum Environment {
        DEV,
        TEST,
        RELEASE
    }

    public interface TXSDKErrorCode {
        int TXSDK_ERROR_INVITENUMBER_INVALID = 1;

    }

    public static TXSdk getInstance() {
        if (singleton == null) {
            synchronized (TXSdk.class) {
                if (singleton == null) {
                    singleton = new TXSdk();
                }
            }
        }
        return singleton;
    }

}
