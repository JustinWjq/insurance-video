package com.txt.video;

import android.app.Activity;
import android.app.Application;

import com.txt.video.net.bean.FileSdkBean;
import com.txt.video.net.bean.TxConfig;
import com.txt.video.common.callback.StartVideoResultOnListener;
import com.txt.video.common.callback.onCreateRoomListener;
import com.txt.video.common.callback.onFileClickListener;
import com.txt.video.ui.TXManagerImpl;
import com.txt.video.ui.video.RoomControlConfig;

import org.json.JSONObject;

/**
 * author ：Justin
 * time ：2021/2/25.
 * des ：SDK api说明
 */
public abstract class TXSDKApi {

    /**
     * @note
     */
    public abstract boolean getShare();

    /**
     * @note
     */
    public abstract void setShare(boolean share);


    /**
     * @note
     */
    public abstract String getAgent();

    /**
     * @note
     */
    public abstract void setAgent(String agent);

    /**
     * @note
     */
    public abstract String getAgentName();

    /**
     * @note
     */
    public abstract void setAgentName(String agentName);

    /**
     * @note
     */
    public abstract String getTerminal();

    /**
     * @note
     */
    public abstract String getSDKVersion();

    /**
     * @note
     */
    public abstract boolean isDemo();

    /**
     * @note
     */
    public abstract void setDemo(boolean demo);


    /**
     * @note
     */
    public abstract String getWxTransaction();

    /**
     * @param wxTransaction
     * @note
     */
    public abstract void setWxTransaction(String wxTransaction);


    /**
     * 获取可配置信息
     *
     * @note
     */
    public abstract TxConfig getTxConfig();


    /**
     * 设置可配置信息（详情见TxConfig）
     *
     * @param txConfig
     * @note
     */
    public abstract void setTxConfig(TxConfig txConfig);

    /**
     * 获取环境
     *
     * @return 环境
     */
    public abstract TXSdk.Environment getEnvironment();

    /**
     * 设置环境
     *
     * @param environment
     * @note
     */
    public abstract void setEnvironment(TXSdk.Environment environment);

    /**
     * 获取debug状态
     *
     * @note
     */
    public abstract boolean isDebug();

    /**
     * 设置debug状态
     *
     * @param debug
     * @note
     */
    public abstract void setDebug(boolean debug);


    /**
     * 初始化
     *
     * @param application
     * @param en
     * @param isDebug
     * @param txConfig
     * @note
     */
    public abstract void init(Application application, TXSdk.Environment en, boolean isDebug, TxConfig txConfig);


    /**
     * 切换环境
     *
     * @param en
     * @note
     */
    public abstract void checkoutNetEnv(TXSdk.Environment en);

    /**
     * 快速会议
     *
     * @param context
     * @param agent
     * @param orgAccount
     * @param sign
     * @param listener          操作回调
     * @note
     */
    public abstract void startVideo(final Activity context,
                                    String roomId,
                                    final String agent,
                                    final String  userName,
                                    String orgAccount, String sign,
                                    final StartVideoResultOnListener listener);


    public abstract void startVideo(Activity context,
                                    String agent,
                                    String userName,
                                    String orgAccount,
                                    String sign,
                                    StartVideoResultOnListener listener);


    /**
     * 监听文件按钮监听
     *
     * @param onFileClickListener
     * @return
     */
    public abstract void addOnFileClickListener(onFileClickListener onFileClickListener);

    /**
     * 移除文件按钮监听
     *
     * @param onFileClickListener
     * @return
     */
    public abstract void removeOnFileClickListener(onFileClickListener onFileClickListener);


    /**
     * 传入视频数据
     *
     * @param mFileSdkBean
     * @return
     */
    public abstract void addFileToSdk(FileSdkBean mFileSdkBean);

    /**
     * 传入自定义小程序path参数
     *
     * @param userNickname
     *
     */
    public abstract void setUserNickname(String userNickname);

    /**
     * 获取自定义小程序path参数
     */
    public abstract String  getUserNickname();


}
