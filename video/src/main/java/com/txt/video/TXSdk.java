package com.txt.video;

import android.app.Activity;
import android.app.Application;


import com.txt.video.net.bean.FileSdkBean;
import com.txt.video.net.bean.TxConfig;
import com.txt.video.net.http.SystemHttpRequest;
import com.txt.video.net.utils.TxLogUtils;
import com.txt.video.trtc.TICManager;
import com.txt.video.common.callback.StartVideoResultOnListener;
import com.txt.video.common.callback.onCreateRoomListener;
import com.txt.video.common.callback.onFileClickListener;
import com.txt.video.common.utils.AppUtils;
import com.txt.video.ui.TXManagerImpl;
import com.txt.video.ui.video.FileClickObservable;
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

    private FileClickObservable mFriendBtObservable;

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
        mFriendBtObservable = new FileClickObservable();
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
    public void startVideo(Activity context, String agent,String userName, String orgAccount, String sign, StartVideoResultOnListener listener) {
        TXManagerImpl.getInstance().checkPermission(context, agent,userName, orgAccount, sign, null, listener, true);
    }



    RoomControlConfig mRoomControlConfig;


    public RoomControlConfig getRoomControlConfig() {
        if (null != mRoomControlConfig) {
            return mRoomControlConfig;
        } else {
            RoomControlConfig builder = new RoomControlConfig.Builder().enableVideo(false).build();
            return builder;
        }


    }

    private onFileClickListener onFileClickListener;

    @Override
    public void addOnFileClickListener(onFileClickListener onFileClickListener) {
        //mFriendBtObservable.addObserver(onFriendBtListener);
        this.onFileClickListener = onFileClickListener;
    }

    @Override
    public void removeOnFileClickListener(onFileClickListener onFileClickListener) {
        this.onFileClickListener = null;
    }

    @Override
    public void addFileToSdk(FileSdkBean mFileSdkBean) {
        TXManagerImpl.getInstance().addFileToSdk(mFileSdkBean);
    }

    public onFileClickListener getOnFriendBtListener() {
        return this.onFileClickListener;
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
