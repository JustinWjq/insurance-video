package com.txt.video.ui.boardpage

import android.net.Uri
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.trtc.TRTCCloudDef
import com.txt.video.base.IBaseView
import com.txt.video.trtc.TICManager
import com.txt.video.trtc.TRTCCloudManager
import com.txt.video.trtc.remoteuser.TRTCRemoteUserManager
import com.txt.video.trtc.ticimpl.utils.MyBoardCallback
import com.txt.video.trtc.videolayout.list.MeetingVideoView
import com.txt.video.trtc.videolayout.list.MemberEntity
import org.json.JSONObject

/**
 * author ：Justin
 * time ：2021/3/17.
 * des ：
 */
public class BoardViewContract {
    interface ICollectModel {
    }

    interface ICollectView : IBaseView {
        fun showTimerDialog(
            type: String,
            maxRoomTime: Int,
            extendRoomTime: Int,
            notifyExtendTime: Int,
            notifyEndTime: Int
        )

        fun finishPage()

        fun sendIMSuccess()

        fun  startShareSuccess(
            shareStatus: Boolean,
            url: String?,
            images: MutableList<String>?
        )

        fun  startShareFail(shareStatus: Boolean)

        fun initBoard()

        fun joinClassroom()
    }


    interface ICollectPresenter {
        fun setServiceId(serviceId: String)

        fun extendTime()

        fun sendGroupMessage(msg: String, type: String)

        fun setIMTextData(type: String): JSONObject

        fun setShareStatus(
            isShareStatus: Boolean,
            url: String?,
            images: MutableList<String>?
        )

        fun setAgentId(mAgentId: String)

        fun getAgentId() :String
    }

}