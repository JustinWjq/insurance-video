package com.txt.video.ui

import android.app.Activity
import android.os.Build
import android.os.Handler
import android.os.Looper
import com.txt.video.TXSdk
import com.txt.video.base.constants.IntentKey
import com.txt.video.net.bean.RoomParamsBean
import com.txt.video.net.http.HttpRequestClient.RequestHttpCallBack
import com.txt.video.net.http.SystemHttpRequest
import com.txt.video.net.utils.TxLogUtils.i
import com.txt.video.ui.video.VideoActivity
import com.txt.video.common.callback.StartVideoResultOnListener
import com.txt.video.common.callback.onCreateRoomListener
import com.txt.video.common.callback.onSDKListener
import com.txt.video.common.utils.PermissionConstants
import com.txt.video.common.utils.TxPermissionUtils
import com.txt.video.ui.video.RoomControlConfig
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by JustinWjq
 *
 * @date 2021/2/23.
 * description：sdk实现类
 */
class TXManagerImpl : TXManager {
    private val mHandler = Handler(Looper.getMainLooper())

    //快速会议检测
    override fun checkPermission(
        context: Activity?,
        agent: String?,
        orgAccount: String?,
        sign: String?,
        businessData: JSONObject?,
        listener: StartVideoResultOnListener,
        isAgent: Boolean
    ) {
        checkPermission(
            context,
            "",
            agent,
            "",
            orgAccount,
            sign,
            businessData,
            listener,
            isAgent
        )
    }

    //检测进入房间需要的权限
    override fun checkPermission(
        context: Activity?,
        roomId: String?,
        account: String?,
        userName: String?,
        orgAccount: String?,
        sign: String?,
        businessData: JSONObject?,
        listener: StartVideoResultOnListener,
        isAgent: Boolean
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            TxPermissionUtils.permission(
                PermissionConstants.CAMERA,
                PermissionConstants.MICROPHONE,
                PermissionConstants.PHONE
            ).callback(object : TxPermissionUtils.FullCallback {
                override fun onGranted(permissionsGranted: List<String>) {
                    i("txsdk---joinRoom---- ")
                    i("txsdk---onGranted---- $permissionsGranted")
                    if (permissionsGranted.contains("android.permission.CAMERA") && permissionsGranted.contains(
                            "android.permission.RECORD_AUDIO"
                        )
                    ) {
                        if (isAgent) {
                            enterRoom(context, account, orgAccount, sign, businessData, listener)
                        } else {
                            joinRoom(
                                context,
                                roomId,
                                account,
                                userName,
                                orgAccount,
                                sign,
                                businessData,
                                listener
                            )
                        }

                    } else {
                        listener.onResultFail(10000, "视频权限或音频权限未申请！")
                    }
                }

                override fun onDenied(
                    permissionsDeniedForever: List<String>,
                    permissionsDenied: List<String>
                ) {
                    i("txsdk---permissionsDeniedForever ---- $permissionsDeniedForever")
                    i("txsdk---permissionsDenied ---- $permissionsDenied")
                    if (permissionsDenied.contains("android.permission.CAMERA") || permissionsDenied.contains(
                            "android.permission.RECORD_AUDIO"
                        )
                    ) {
                        listener.onResultFail(10000, "视频权限或音频权限未申请！")
                    } else {
                    }
                }
            }
            ).request()
        } else {
            i("txsdk---joinRoom----23以下 ")
            if (isAgent) {
                enterRoom(context, userName, orgAccount, sign, businessData, listener)
            } else {
                joinRoom(
                    context,
                    roomId,
                    account,
                    userName,
                    orgAccount,
                    sign,
                    businessData,
                    listener
                )
            }
        }
    }

    private var mCacheJson: RoomParamsBean? = null

    //直接进入会议模式
    override fun enterRoom(
        context: Activity?,
        agent: String?,
        orgAccount: String?,
        sign: String?,
        businessData: JSONObject?,
        listener: StartVideoResultOnListener
    ) {
        if (TXSdk.getInstance().share) {
            mHandler.post {
                listener.onResultSuccess()
                VideoActivity.startAc(context!!, mCacheJson!!).takeIf { null != mCacheJson }

            }
        } else {
            SystemHttpRequest.getInstance()
                .startAgent(agent, orgAccount, sign, businessData, object : RequestHttpCallBack {
                    override fun onSuccess(json: String) {

                        mHandler.post {
                            listener.onResultSuccess()
                            val roomParamsBean = RoomParamsBean()
                            mCacheJson = roomParamsBean
                            try {
                                val jsonObject = JSONObject(json)
                                roomParamsBean.apply {
                                    sdkAppId = jsonObject.getInt(IntentKey.SDKAPPID)
                                    serviceId = jsonObject.getString(IntentKey.SERVICEID)
                                    roomId = jsonObject.getInt(IntentKey.ROOMID)
                                    groupId = jsonObject.getString(IntentKey.GROUPID)
                                    userSig = jsonObject.getString(IntentKey.AGENTSIG)
                                    userId = jsonObject.getString(IntentKey.AGENTID)
                                    agentName = jsonObject.getString(IntentKey.AGENTNAME)
                                    userRole = "owner"
                                    maxRoomTime =
                                        jsonObject.getInt(IntentKey.MAXROOMTIME)
                                    maxRoomUser =
                                        jsonObject.getInt(IntentKey.MAXROOMUSER)
                                    inviteNumber =
                                        jsonObject.getString(IntentKey.INVITENUMBER)
                                }
                                TXSdk.getInstance().agent =
                                    jsonObject.getString(IntentKey.AGENTNAME)
                                VideoActivity.startAc(
                                    context!!,
                                    roomParamsBean
                                )
                            } catch (e: JSONException) {
                                e.printStackTrace()
                                listener.onResultFail(123, "解析失败")
                            }
                        }
                    }

                    override fun onFail(err: String, code: Int) {
                        mHandler.post { listener.onResultFail(code, err) }
                    }
                })
        }
    }

    //通过预约房间，加入房间模式
    override fun joinRoom(
        context: Activity?,
        roomId: String?,
        account: String?,
        userName: String?,
        orgAccount: String?,
        sign: String?,
        businessData: JSONObject?,
        listener: StartVideoResultOnListener
    ) {
        if (TXSdk.getInstance().share) {
            mHandler.post {
                listener.onResultSuccess()
                VideoActivity.startAc(context!!, mCacheJson!!).takeIf { null != mCacheJson }

            }
        } else {
            SystemHttpRequest.getInstance()
                .startUser(
                    roomId,
                    account,
                    userName,
                    orgAccount,
                    sign,
                    businessData,
                    object : RequestHttpCallBack {
                        override fun onSuccess(json: String) {
                            mHandler.post {
                                listener.onResultSuccess()
                                val roomParamsBean = RoomParamsBean()
                                mCacheJson = roomParamsBean
                                try {
                                    val jsonObject = JSONObject(json)
                                    roomParamsBean.apply {
                                        sdkAppId = jsonObject.getInt(IntentKey.SDKAPPID)
                                        serviceId = jsonObject.getString(IntentKey.SERVICEID)
                                        this.roomId = jsonObject.getInt(IntentKey.ROOMID)
                                        groupId = jsonObject.getString(IntentKey.GROUPID)
                                        userSig = jsonObject.getString(IntentKey.USERSIG)
                                        userId = jsonObject.getString(IntentKey.USERID)
                                        agentName = userName
                                        userRole = jsonObject.getString(IntentKey.USERROLE)
                                        inviteNumber =
                                            jsonObject.getString(IntentKey.INVITENUMBER)
                                        maxRoomUser =
                                            jsonObject.getInt(IntentKey.MAXROOMUSER)
                                        TXSdk.getInstance().agent =
                                            jsonObject.getString(IntentKey.AGENTNAME)
                                    }
                                    VideoActivity.startAc(
                                        context!!,
                                        roomParamsBean
                                    )
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                    listener.onResultFail(123, "解析失败")
                                }
                            }
                        }

                        override fun onFail(err: String, code: Int) {
                            mHandler.post { listener.onResultFail(code, err) }
                        }
                    }
                )
        }
    }

    override fun createRoom(
        account: String?,
        orgAccount: String?,
        sign: String?,
        roomInfo: JSONObject?,
        businessData: JSONObject?,
        listener: onCreateRoomListener
    ) {
        SystemHttpRequest.getInstance().createRoom(
            account,
            orgAccount,
            sign,
            roomInfo,
            businessData,
            object : RequestHttpCallBack {
                override fun onSuccess(json: String) {
                    mHandler.post {
                        try {
                            val jsonObject = JSONObject(json)
                            val inviteNumber =
                                jsonObject.optString(IntentKey.INVITENUMBER)
                            val serviceId = jsonObject.optString(IntentKey.SERVICEID)
                            if (inviteNumber.isEmpty()) {
                                listener.onResultFail(0, "")
                            } else {
                                listener.onResultSuccess(inviteNumber,serviceId,account!!)
                                listener.onResultSuccess(inviteNumber)
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            listener.onResultFail(0, "")
                        }
                    }
                }

                override fun onFail(err: String, code: Int) {
                    mHandler.post { listener.onResultFail(0, "") }
                }
            })
    }

    override fun setAgentInRoomStatus(
        account: String,
        userName: String,
        serviceId: String,
        inviteAccount: String,
        action: String,
        orgAccount: String,
        sign: String,
        onSDKListener: onSDKListener
    ) {
        SystemHttpRequest.getInstance().setRoomStatus(
            account,
            userName,
            serviceId,
            inviteAccount,
            action,
            orgAccount,
            sign,
            object : RequestHttpCallBack {
                override fun onSuccess(json: String?) {
                    mHandler.post {
                        onSDKListener.onResultSuccess(json!!)
                    }
                }

                override fun onFail(err: String?, code: Int) {
                    mHandler.post {
                        onSDKListener.onResultFail(code, err!!)
                    }
                }

            })
    }

    override fun getAgentAndRoomStatus(
        account: String,
        serviceId: String,
        orgAccount: String,
        sign: String,
        onSDKListener: onSDKListener
    ) {
        SystemHttpRequest.getInstance().getRoomStatus(
            account,
            serviceId,
            orgAccount,
            sign,
            object : RequestHttpCallBack {
                override fun onSuccess(json: String?) {
                    mHandler.post {
                        onSDKListener.onResultSuccess(json!!)
                    }
                }

                override fun onFail(err: String?, code: Int) {
                    mHandler.post {
                        onSDKListener.onResultFail(code, err!!)
                    }
                }

            })
    }

    override fun getRoomControlConfig(): RoomControlConfig {
        return TXSdk.getInstance().roomControlConfig
    }

    companion object {
        @Volatile
        private var singleton: TXManagerImpl? = null

        @JvmStatic
        val instance: TXManagerImpl?
            get() {
                if (singleton == null) {
                    synchronized(TXManagerImpl::class.java) {
                        if (singleton == null) {
                            singleton =
                                TXManagerImpl()
                        }
                    }
                }
                return singleton
            }
    }
}