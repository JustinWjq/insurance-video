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
public class BoardViewContract{
    interface ICollectModel {
    }

    interface ICollectView : IBaseView {

    }


    interface ICollectPresenter {
        fun setServiceId(serviceId :String)

       fun extendTime()
    }

}