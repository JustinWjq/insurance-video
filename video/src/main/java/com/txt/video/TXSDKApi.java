package com.txt.video;

import android.app.Activity;
import android.app.Application;

import com.txt.video.common.callback.onSDKListener;
import com.txt.video.net.bean.TxConfig;
import com.txt.video.common.callback.StartVideoResultOnListener;
import com.txt.video.common.callback.onCreateRoomListener;
import com.txt.video.common.callback.onFriendBtListener;
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
     * @param businessData      额外字段
     * @param listener          操作回调
     * @param roomControlConfig 控制会议参数
     * @note
     */
    public abstract void startTXVideo(final Activity context, final String agent,
                                      String orgAccount, String sign, JSONObject businessData,
                                      RoomControlConfig roomControlConfig,
                                      final StartVideoResultOnListener listener);




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
    public abstract void startTXVideo(final Activity context, final String agent,
                                      String orgAccount, String sign,
                                      final StartVideoResultOnListener listener);

    /**
     * 预约会议
     *
     * @param agent
     * @param orgAccount
     * @param sign
     * @param listener   操作回调
     * @note
     */
    public abstract void createRoom(final String agent, String orgAccount, String sign, final onCreateRoomListener listener);

    /**
     * 加入会议
     *
     * @param context
     * @param roomId            邀请码
     * @param account           账号
     * @param userName          用户名字
     * @param orgAccount        机构
     * @param sign              aes 签名
     * @param businessData      额外字段
     * @param roomControlConfig 控制会议参数
     * @param listener          操作回调
     * @note
     */
    public abstract void joinRoom(final Activity context, String roomId, String account,
                                  String userName, String orgAccount, String sign, JSONObject businessData,
                                  RoomControlConfig roomControlConfig,
                                  final StartVideoResultOnListener listener);


    /**
     * 获取参会人信息
     *
     * @param agentId    业务员账号
     * @param serviceId  会议Id
     * @param orgAccount 机构code
     * @param sign       加密签名
     * @return 获取参会人信息
     * <p>
     * <p>
     * agentStatus : 业务员状态, on: 空闲, calling: 等待接听中, dealing: 会议中
     * roomStatus : 会议状态, Initiated: 已开始, ended: 已结束
     */
    public abstract void getAgentAndRoomStatus(String agentId, String serviceId, String orgAccount, String sign, onSDKListener onSDKListener);


    /*
     * 设置参会人信息
     *
     * @param agentId
     * 业务员账号
     *
     * @param action
     * 业务员操作, invited: 收到会议邀请, refused: 拒绝会议邀请
     *
     * @param orgAccount
     * 机构code
     *
     * @param sign
     * 加密签名
     */

    public abstract void setAgentInRoomStatus(String account, String userName, String serviceId, String inviteAccount, String action, String orgAccount, String sign, onSDKListener onSDKListener);

    /**
     * 监听云助理好友按钮监听
     *
     * @param onFriendBtListener
     * @return
     */
    public abstract void addOnFriendBtListener(onFriendBtListener onFriendBtListener);

    /**
     * 监听云助理好友按钮监听
     *
     * @param onFriendBtListener
     * @return
     */
    public abstract void removeOnFriendBtListener(onFriendBtListener onFriendBtListener);


}
