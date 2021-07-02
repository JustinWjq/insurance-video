package com.txt.video.ui.video

import android.net.Uri
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.trtc.TRTCCloudDef
import com.txt.video.base.IBaseView
import com.txt.video.net.bean.RoomParamsBean
import com.txt.video.trtc.TICManager
import com.txt.video.trtc.TRTCCloudManager
import com.txt.video.trtc.remoteuser.TRTCRemoteUserManager
import com.txt.video.trtc.ticimpl.utils.MyBoardCallback
import com.txt.video.trtc.videolayout.list.MeetingVideoView
import com.txt.video.trtc.videolayout.list.MemberEntity
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by JustinWjq
 * @date 2019-12-23.
 * description：
 */
public class VideoContract {
    interface ICollectModel {
    }

    interface ICollectView : IBaseView {
        fun hideFloatingWindow()
        fun showFloatingWindow()

        fun processSelfVideoPlay()

        fun joinClassroom()

        fun initBoard()

        fun updateAdapter(json :String)

        fun uploadFileSuccess()

        fun uploadFileFail(msg : String)

        fun sendReq(req : BaseReq)

        fun  startSoundSuccess()

        fun  startShareSuccess(
            shareStatus: Boolean,
            url: String?,
            images: MutableList<String>?
        )

        fun  startShareFail(shareStatus: Boolean)

        fun  startSoundFail()

        fun getRoomInfoSuccess(
            json: String?,
            userId: String,
            streamType: Int,
            entity: MemberEntity?,
            available: Boolean,
            bigScreen: Boolean
        )

        fun showSuccess()

        fun showFail()

        fun showWhiteBroad(isShow:Boolean)

        fun showPersonWhiteBroad(isShow:Boolean)

        fun showBroadFileRv(isShow:Boolean)

        fun changeBigScreenViewName(text: String, isOwer: Boolean)

        fun changeBigScreenViewVoice(volume :Int)

        fun checkBigVideoToFirstSmallVideo(isShowToSmall: Boolean)

        fun checkSmallVideoToBigVideo(isShowToBig: Boolean)

        fun changeBigVideo(bigMeetingEntity: MemberEntity)

        fun hideBigNoVideo(isHiden: Boolean)

        fun checkItemToBig(position: Int)

        fun sendIMSuccess()

        fun handleMemberListView()

        fun sendSystemMSG()

        fun showInviteBt(isShow: Boolean,noRemoterUser:Boolean)

        fun hideRemoteUserListView(isHiden: Boolean)

        fun showChangeBroadModeDialog(isShow: Boolean)


        fun showShareDialog()

        fun showTimerDialog(
            type: String,
            maxRoomTime: Int,
            extendRoomTime: Int,
            notifyExtendTime: Int,
            notifyEndTime: Int
        )

        fun resetBoardLayout()

        fun stopTimer()

        fun skipCaller()

        fun initViews()

        fun showBroad()

        fun startTimer()

        fun addBoardView()

        fun removeBoardView()

        fun modifyExitBt()

        fun checkOwenrToBigShareScreen(screenUserId:String)

        fun detachBigShareScreen(screenUserId: String)

        fun switchCamera()

        fun setScreenStatusSuccess(screenStatus: Boolean)

        fun setScreenStatusFail(screenStatus: Boolean)

        fun onPermissionDenied()

        fun showPersonDialog(
            webId: String?,
            url: String,
            name: String,
            isSameScreen: Boolean,
            list: JSONArray,
            fileName: String
        )

        fun showSelectChannelDialog(
            list: JSONArray,
            webId: String,
            url: String,
            name: String,
            fileName:String
        )

        fun showWebDialog(
            url: String,
            userId: String,
            webId: String,
            fromAgent: Boolean = true,
            fileName: String,
            toUserId: String,
            fromUserId: String
        )

        fun updateWebUrlAdapter(json :String)

        fun uploadWebUrlSuccess()

        fun uploadWebUrlFail(msg : String)

        fun hideWebDialog()
    }


    interface ICollectPresenter {
        fun initMemberData()

        fun addMemberEntity(entity: MemberEntity)

        fun addToAllMemberEntity(entity: MemberEntity)

        fun addMemberEntity(positon:Int,entity: MemberEntity)

        fun removeMemberEntity(userId: String): Int

        fun removeForAllMemberEntity(userId: String): Int

        fun getTRTCParams(): TRTCCloudDef.TRTCParams

        fun setTRTCParams(stringExtra: RoomParamsBean)

        fun getMemberEntityList(): ArrayList<MemberEntity>

        fun getStringMemberEntityMap(): HashMap<String, MemberEntity>

        fun getAllMemberEntityList(): ArrayList<MemberEntity>

        fun getAllMemberEntityMap(): HashMap<String, MemberEntity>

        fun getRoomId(): Int

        fun getServiceId(): String

        fun getAgentId(): String

        fun getGroupId(): String

        fun getMaxRoomUser(): Int

        fun getMaxRoomTime(): Int

        fun getInviteNumber(): String

        fun startRecord()

        fun endRecord()

        fun initVideoSDK()

        fun enterRoom()

        fun loginImRoom()

        fun logoutClassRoom()

        fun startLocalPreview(viewVideo : MeetingVideoView)

        fun stopLocalPreview()

        fun startScreenCapture()

        fun stopScreenCapture()

        fun getTRTCCloudManager(): TRTCCloudManager

        fun getTRTCRemoteUserManager(): TRTCRemoteUserManager

        fun getTicManager(): TICManager

        fun exitRoom()

        fun initTICManager()

        fun joinClassroom(mBoardCallback: MyBoardCallback)

        fun sendGroupMessage(msg: String,type: String="")

        fun update()

        fun  uploadFile(data1: Uri?)

        fun  requestWX()

        fun startShare()

        fun setScreenStatus(screenStatus : Boolean)

        fun setShareStatus(
            screenStatus: Boolean,
            url: String?,
            images: MutableList<String>?
        )

        fun  deleteFile(id: String?)

        fun getRoomInfo(
            userId: String,
            streamType: Int,
            entity1: Boolean,
            entity: MemberEntity?,
            isBigScreen: Boolean
        )

        fun processVideoPlay(fromItem: Int, toItem: Int)

        fun setIMTextData(type :String) :JSONObject

        fun extendTime()

        fun sendMsg()

        fun clearMember()

        fun setSoundStatus(screenStatus: Boolean)

        fun requestPermission()

        fun muteLocalVideo(enableVideo: Boolean)

        fun muteLocalAudio(enableAudio: Boolean)

        fun chooseFile(isChooseFile:Boolean)

        fun setMuteVideoMemberToJSON(isVideoType:Boolean,usedId:String) : JSONArray

        fun setAllVideoStatusMemberToJSON(isVideoType:Boolean, isMute:Boolean) : JSONArray

        fun unitConfig()

        fun destroyRoom()

        fun isOwner(): Boolean

        fun getSelfUserId(): String

        fun getRoomScreenStatus(): Boolean

        fun setRoomScreenStatus(isScreenStatus :Boolean)

        fun getRoomSoundStatus(): Boolean

        fun setRoomSoundStatus(isSoundStatus :Boolean)

        fun getRoomShareStatus(): Boolean

        fun setRoomShareStatus(isShareStatus :Boolean)

        fun endUser()

        fun getOwnerUserId():String

        fun getScreenUserId():String

        fun getShareUserId():String

        fun startShareWeb(
            webId: String,
            serviceId: String,
            fromUserId: String,
            toUserId: String,
            fileName: String
        )

        fun stopShareWeb(userId:String,webId:String,serviceId:String,isSelf:Boolean=true)

        fun getRoomInfo(id: String?, url: String, name: String,fileName:String)

        fun addShareUrl(id: String?, name: String, url: String)

        fun deleteScreenFile(webId: String?)

        fun setShareWebId(webId: String?)

        fun getShareWebId() :String
    }
}