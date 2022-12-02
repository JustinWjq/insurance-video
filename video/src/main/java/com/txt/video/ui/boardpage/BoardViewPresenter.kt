package com.txt.video.ui.boardpage

import android.content.Context
import com.tencent.imsdk.TIMMessage
import com.txt.video.TXSdk
import com.txt.video.base.BasePresenter
import com.txt.video.base.constants.IMkey
import com.txt.video.base.constants.IntentKey
import com.txt.video.net.http.HttpRequestClient
import com.txt.video.net.http.SystemHttpRequest
import com.txt.video.net.utils.TxLogUtils
import com.txt.video.trtc.TICManager
import com.txt.video.trtc.ticimpl.TICCallback
import com.txt.video.trtc.ticimpl.TICMessageListener
import com.txt.video.ui.video.VideoActivity
import com.txt.video.ui.video.VideoContract
import org.json.JSONObject

/**
 * author ：Justin
 * time ：2021/3/17.
 * des ：
 */
public class BoardViewPresenter(val context: Context, val activity: BoardViewActivity) :
    BasePresenter<BoardViewContract.ICollectView>(), BoardViewContract.ICollectPresenter,
    TICMessageListener {

    private var mServiceId: String? = null

    override fun setServiceId(serviceId: String) {
        this.mServiceId = serviceId
    }

    private var mAgentId: String? = null

    override fun setAgentId(mAgentId: String) {
        this.mAgentId = mAgentId
    }

    override fun getAgentId():String  = mAgentId!!

    var mTICManager: TICManager? = null

    init {
        mTICManager = TICManager.getInstance()
        mTICManager?.addIMMessageListener(this)
    }

    override fun extendTime() {
        SystemHttpRequest.getInstance()
            .extendTime(
                mServiceId,
                object : HttpRequestClient.RequestHttpCallBack {
                    override fun onSuccess(json: String?) {
                        view.showMessage("延长成功！")
                    }

                    override fun onFail(err: String?, code: Int) {
                        view.showMessage("延长失败！")
                    }

                })
    }

    override fun onTICRecvTextMessage(fromUserId: String?, text: String?) {
    }

    override fun onTICRecvMessage(message: TIMMessage?) {
    }

    override fun onTICRecvGroupTextMessage(fromUserId: String?, text: String?) {
        TxLogUtils.i("onTICRecvGroupTextMessage" + text)
        val jsonObject = JSONObject(text)
        val hasType = jsonObject.has("type")
        if (hasType) {
            val type = jsonObject.getString("type")
            val hasData = jsonObject.has("data")

            when (type) {
//                "notifyExtend" -> {
//                    if (hasData) {
//                        val dataJO = jsonObject.getJSONObject("data")
//                        val extendRoomTime = dataJO.getInt("extendRoomTime")
//                        val notifyExtendTime = dataJO.getInt("notifyExtendTime")
//                        view.showTimerDialog("2", 0, extendRoomTime, notifyExtendTime, 0)
//                    }
//
//                }
//                "notifyEnd" -> {
//                    if (hasData) {
//                        val dataJO = jsonObject.getJSONObject("data")
//                        val extendRoomTime = dataJO.getInt("extendRoomTime")
//                        val notifyEndTime = dataJO.getInt("notifyEndTime")
//                        view.showTimerDialog("3", 0, extendRoomTime, 0, notifyEndTime)
//                    }
//                }
                //去除白板
                IMkey.ENDWHITEBOARD -> {
//                    intent.putExtra(IntentKey.ENDWHITEBROAD, true)
                    view.finishPage()
                }
                else -> {
                }
            }
        }
    }

    override fun onTICRecvCustomMessage(fromUserId: String?, data: ByteArray?) {
    }

    override fun onTICRecvGroupCustomMessage(fromUserId: String?, data: ByteArray?) {
    }

    override fun sendGroupMessage(msg: String, type: String) {
        mTICManager!!.sendGroupTextMessage(msg, object : TICCallback<Any> {
            override fun onSuccess(data: Any) {
                TxLogUtils.i("txsdk---sendGroupTextMessage:onSuccess------$data")
                if (type == "1") {
//                    view.sendIMSuccess()
                }
            }

            override fun onError(module: String?, errCode: Int, errMsg: String?) {
                TxLogUtils.i("txsdk---sendGroupTextMessage:onError------$errCode---$errMsg")
                if (type == "1") {
//                    view.sendIMSuccess()
                }
            }

        })
    }

    override fun setIMTextData(type: String): JSONObject = JSONObject().apply {
        put("serviceId", mServiceId)
        put("type", type)
        put("agentId", mAgentId)
    }

    override fun setShareStatus(
        isShareStatus: Boolean,
        url: String?,
        images: MutableList<String>?
    ) {
        SystemHttpRequest.getInstance()
            .shareStatus(
                mServiceId,
                mAgentId,
                isShareStatus,
                object : HttpRequestClient.RequestHttpCallBack {
                    override fun onSuccess(json: String?) {
                        activity?.runOnUiThread {
                            view.startShareSuccess(isShareStatus, url, images)
                        }
                    }

                    override fun onFail(err: String?, code: Int) {
                        activity?.runOnUiThread {
                            view.startShareFail(isShareStatus)
                        }
                    }

                })
    }


    override fun detachView() {
        super.detachView()
//        TICManager.getInstance().removeIMMessageListener(this)
    }
}