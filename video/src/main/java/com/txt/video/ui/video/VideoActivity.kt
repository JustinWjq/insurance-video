package com.txt.video.ui.video

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import com.google.gson.Gson
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory.createWXAPI
import com.tencent.teduboard.TEduBoardController
import com.tencent.teduboard.TEduBoardController.TEduBoardColor
import com.tencent.trtc.TRTCCloudDef
import com.tencent.trtc.TRTCStatistics
import com.txt.video.R
import com.txt.video.TXSdk
import com.txt.video.base.BaseActivity
import com.txt.video.base.constants.IMkey
import com.txt.video.base.constants.IntentKey
import com.txt.video.base.constants.VideoCode
import com.txt.video.common.callback.*
import com.txt.video.common.dialog.CommonDialog
import com.txt.video.common.floatview.FloatingView
import com.txt.video.common.glide.TxGlide
import com.txt.video.common.utils.CheckDoubleClickListener
import com.txt.video.common.utils.DatetimeUtil
import com.txt.video.common.utils.ToastUtils
import com.txt.video.net.bean.*
import com.txt.video.net.utils.TxLogUtils
import com.txt.video.trtc.ConfigHelper
import com.txt.video.trtc.TRTCCloudIView
import com.txt.video.trtc.TRTCCloudManagerListener
import com.txt.video.trtc.remoteuser.TRTCRemoteUserIView
import com.txt.video.trtc.ticimpl.utils.MyBoardCallback
import com.txt.video.trtc.videolayout.Utils
import com.txt.video.trtc.videolayout.list.*
import com.txt.video.trtc.videolayout.list.MemberListAdapter.*
import com.txt.video.ui.TXManagerImpl
import com.txt.video.ui.boardpage.BoardViewActivity
import com.txt.video.ui.video.remote.RemoteUserListView
import com.txt.video.ui.weight.PicQuickAdapter
import com.txt.video.ui.weight.dialog.*
import com.txt.video.ui.weight.easyfloat.EasyFloat
import com.txt.video.ui.weight.easyfloat.interfaces.OnInvokeView
import com.txt.video.ui.weight.view.ScreenView
import kotlinx.android.synthetic.main.tx_activity_video.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by JustinWjq
 * @date 2020-12-23.
 * description：ui
 */
class VideoActivity : BaseActivity<VideoContract.ICollectView, VideoPresenter>(), TRTCCloudIView,
    TRTCCloudManagerListener,
    TRTCRemoteUserIView, VideoContract.ICollectView,
    onShareWhiteBroadDialogListener, onMuteDialogListener,
    RemoteUserListView.RemoteUserListCallback, onCheckDialogListenerCallBack,
    ScreenView.BigScreenViewCallback {

    private var mMemberListAdapter: MemberListAdapter? = null

    var mBoard: TEduBoardController? = null
    var mBoardCallback: MyBoardCallback? = null

    private var bigMeetingEntity: MemberEntity? = null
    private var screenMeetingEntity: MemberEntity? = null
    private var mStubRemoteUserView: ViewStub? = null

    private var mListRv: androidx.recyclerview.widget.RecyclerView? = null
    var mViewVideo: MeetingVideoView? = null

    var videoBoradBusiness: ArrayList<ImageButton>? = null

    companion object {

        var paintColorPostion = 0
        var paintSizeIntPostion = 1
        var textColorIntPostion = 0
        var textSizeIntPostion = 1

        @JvmStatic
        public fun startAc(context: Activity, tx_data: String) {
            context.startActivity(Intent(context, VideoActivity::class.java).apply {
                putExtra(IntentKey.KEY_DATA, tx_data)
            })
        }

        @JvmStatic
        public fun startAc(context: Activity, tx_data: RoomParamsBean) {
            context.startActivity(Intent(context, VideoActivity::class.java).apply {
                putExtra(IntentKey.KEY_DATA, tx_data)
            })
        }

    }


    override fun initViews() {

        showInviteBt(isShow = true, noRemoterUser = true)
        mViewVideo = mPresenter!!.getMemberEntityList()[0].meetingVideoView
        val pageLayoutManager = MeetingPageLM(this)
        pageLayoutManager.orientation = androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
        pageLayoutManager.setPageListener(object : MeetingPageLayoutManager.PageListener {
            override fun onItemVisible(fromItem: Int, toItem: Int) {
                if (fromItem == 0) {
                    processSelfVideoPlay()
                    mPresenter?.processVideoPlay(1, toItem)
                } else {
                    mPresenter?.processVideoPlay(fromItem, toItem)
                }
            }

            override fun onPageSelect(pageIndex: Int) {
            }

            override fun onPageSizeChanged(pageSize: Int) {

            }

        })


        mListRv = trtc_video_view_layout

        mMemberListAdapter =
            MemberListAdapter(
                this,
                mPresenter?.getMemberEntityList(),
                object : MemberListAdapter.ListCallback {
                    override fun onItemClick(position: Int) {
                        TxLogUtils.i("onItemClick--${position}")
                        if (position != 0) {
                            checkItemToBig(position)
                        }
                    }

                    override fun onItemDoubleClick(position: Int) {

                    }

                })

        mListRv?.setHasFixedSize(true)

        mListRv?.layoutManager = pageLayoutManager
        mListRv?.adapter = mMemberListAdapter

        mStubRemoteUserView = findViewById<ViewStub>(R.id.view_stub_remote_user)
        initPicAdapter()
        regToWx()
        videoBoradBusiness = arrayListOf(
            tx_pen,
            tx_eraser,
            tx_textstyle,
            tx_zoom
        )

        modifyExitBt()
        mPresenter?.muteLocalAudio(!mPresenter!!.getRoomSoundStatus())
//        tx_business_video.isSelected = !TXManagerImpl.instance?.getRoomControlConfig()?.isEnableVideo!!
        val isEnableVideo = TXManagerImpl.instance?.getRoomControlConfig()?.isEnableVideo!!
        TxLogUtils.i("isEnableVideo---$isEnableVideo")
        mPresenter?.muteLocalVideo(!isEnableVideo)
        mPresenter?.isCloseVideo = !isEnableVideo
    }

    fun selectIb(imageButton: ImageButton) {
        videoBoradBusiness?.forEach {
            it.isSelected = it == imageButton
        }
    }


    override fun processSelfVideoPlay() {
        mViewVideo?.refreshParent()
    }


    override fun onSnapshotLocalView(bmp: Bitmap?) {
    }

    override fun onMuteLocalAudio(isMute: Boolean) {
        TxLogUtils.i("onMuteLocalAudio$isMute")
        tx_business_audio.isSelected = isMute
        val entity =
            mPresenter!!.getStringMemberEntityMap()[mPresenter?.getTRTCParams()!!.userId]
        if (null != entity) {
            val indexOf = mPresenter?.getMemberEntityList()!!.indexOf(
                entity
            )
            if (indexOf >= 0) {
                entity?.isShowAudioEvaluation = !isMute
                mMemberListAdapter!!.notifyItemChanged(
                    indexOf, VOLUME_SHOW
                )

            }

        }
        mScreenView?.muteAudio(isMute)
        webDialog?.checkAudio(isMute)
    }

    override fun onMuteLocalVideo(isMute: Boolean) {
        TxLogUtils.i("onMuteLocalVideo$isMute")
        tx_business_video.isSelected = isMute
        val entity =
            mPresenter!!.getStringMemberEntityMap()[mPresenter?.getTRTCParams()!!.userId]
        entity?.isMuteVideo = isMute
        mMemberListAdapter!!.notifyItemChanged(
            mPresenter?.getMemberEntityList()!!.indexOf(
                entity
            ), VIDEO_CLOSE
        )
        mScreenView?.muteVideo(isMute)
    }

    override fun onStartLinkMic() {
    }


    override fun onAudioVolumeEvaluationChange(enable: Boolean) {

    }

    override fun onFirstVideoFrame(userId: String?, streamType: Int, width: Int, height: Int) {
    }

    //用户流渲染
    override fun onUserVideoAvailable(userId: String?, available: Boolean) {
        TxLogUtils.i("txsdk---onUserVideoAvailable---$userId+available$available")
        onUserVideoChange(userId!!, TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG, available)

    }

    override fun onExitRoom(reason: Int) {
    }

    override fun onScreenCaptureStarted() {

        mPresenter?.sendGroupMessage(
            mPresenter?.setIMTextData(IMkey.STARTSHARE)
                ?.put(IMkey.SCREENUSERID, mPresenter?.getSelfUserId()).toString()
        )
        mPresenter?.setRoomScreenStatus(true)
        mPresenter?.isShare = true
        tx_business_screen.isSelected = true
        val entity =
            mPresenter!!.getStringMemberEntityMap()[mPresenter!!.getTRTCParams().userId]
        entity?.isScreen = true
        mMemberListAdapter!!.notifyItemChanged(
            mPresenter!!.getMemberEntityList().indexOf(
                entity
            ), VIDEO_SCREEN_CLOSE
        )

        if (mPresenter?.isCloseVideo!!) {
            mPresenter?.getTRTCCloudManager()!!.muteLocalVideo(false)
        }
        showFloatingWindow()
    }

    override fun onScreenCaptureStopped(reason: Int) {
        mPresenter?.sendGroupMessage(
            mPresenter?.setIMTextData(IMkey.ENDSHARE)
                ?.put(IMkey.SCREENUSERID, mPresenter?.getSelfUserId()).toString()
        )
        mPresenter?.setScreenStatus(false)
        mPresenter?.setRoomScreenStatus(false)
        mPresenter?.isShare = false
        tx_business_screen.isSelected = false
        hideFloatingWindow()
        val entity =
            mPresenter!!.getStringMemberEntityMap()[mPresenter!!.getTRTCParams().userId]
        if (mPresenter?.isCloseVideo!!) {
            mPresenter?.getTRTCCloudManager()!!.muteLocalVideo(true)

        } else {
            entity?.isScreen = false
            mMemberListAdapter!!.notifyItemChanged(
                mPresenter?.getMemberEntityList()!!.indexOf(
                    entity
                ), VIDEO_SCREEN_CLOSE
            )
        }
    }

    override fun onRemoteUserEnterRoom(remoteUserId: String?) {
        TxLogUtils.i("txsdk---onRemoteUserEnterRoom-----$remoteUserId")
        TxLogUtils.i("txsdk---onRemoteUserEnterRoom-----${mPresenter?.isBroad}")

        //协配员进入房间，大屏显示业务员
        //业务员的话，流程不变
        //进入获取房间信息
        val contains = remoteUserId?.contains("tic_record")
        if (!contains!!) {
            val mMeetingVideoView = MeetingVideoView(this@VideoActivity).apply {
                meetingUserId = remoteUserId
                isNeedAttach = false
                isPlaying = true
            }

            val insertIndex = mPresenter?.getMemberEntityList()!!.size
            val entity = MemberEntity().apply {
                userId = remoteUserId
                userName = ""
                meetingVideoView = mMeetingVideoView
                isMuteAudio = true
                isMuteVideo = true
                isVideoAvailable = false
                isAudioAvailable = false
                isNeedFresh = true
                isShowAudioEvaluation = false
            }

            mPresenter?.addToAllMemberEntity(entity)

            if (mPresenter?.isBroad!!) {

                mPresenter?.addMemberEntity(entity)
                mMemberListAdapter!!.notifyItemInserted(insertIndex)
                mPresenter?.getRoomInfo(
                    remoteUserId,
                    TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG,
                    false,
                    entity,
                    false
                )
            } else {
                if (mPresenter?.getAllMemberEntityList()!!.size == 1) {
                    //显示大画面
                    bigMeetingEntity = entity
                    mPresenter?.getRoomInfo(
                        remoteUserId,
                        TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG,
                        false,
                        bigMeetingEntity,
                        true
                    )
                } else {
                    mPresenter?.addMemberEntity(entity)
                    mMemberListAdapter!!.notifyItemInserted(insertIndex)
                    mPresenter?.getRoomInfo(
                        remoteUserId,
                        TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG,
                        false,
                        entity,
                        false
                    )
                }


            }


        }

    }

    override fun onNetworkQuality(
        localQuality: TRTCCloudDef.TRTCQuality?,
        remoteQuality: ArrayList<TRTCCloudDef.TRTCQuality>?
    ) {

    }

    override fun onUserSubStreamAvailable(userId: String?, available: Boolean) {
    }


    override fun onRemoteUserLeaveRoom(userId: String?, reason: Int) {
        TxLogUtils.i("txsdk---onRemoteUserLeaveRoom--$userId----$reason")

        // 回收分配的渲染的View
        mPresenter?.getTRTCRemoteUserManager()!!.removeRemoteUser(userId)
        val index = mPresenter?.removeMemberEntity(userId!!)

        mPresenter?.removeForAllMemberEntity(userId!!)
        mRemoteUserListView?.notifyDataSetChanged()

        if (userId == changeUserStateDialog?.userId) {
            if (changeUserStateDialog?.isShowing!!) {
                changeUserStateDialog?.dismiss()
            }
        }

        if (index!! >= 0) {
            mMemberListAdapter!!.notifyItemRemoved(index)
        } else {
            if (bigMeetingEntity != null && userId == bigMeetingEntity?.userId) {
                bigscreen?.visibility = View.GONE
                trtc_fl_no_video.visibility = View.GONE
                rl_bigscreen_name.visibility = View.GONE
                bigMeetingEntity = null
                if (!mPresenter?.isBroad!! && mPresenter?.getAllMemberEntityList()!!.size == 1) {
                    checkSmallVideoToBigVideo(true)
                }

            }
        }

        if (mPresenter?.getAllMemberEntityList()!!.size == 0) {
//            如果当前没有客户 隐藏
            tx_icon_invite.visibility = View.GONE
            showInviteBt(isShow = true, noRemoterUser = true)
            if (changeUserStateDialog?.isShowing!!) {
                changeUserStateDialog?.dismiss()
            }
        }

    }

    override fun onAudioEffectFinished(effectId: Int, code: Int) {
    }

    override fun onRecvCustomCmdMsg(userId: String?, cmdID: Int, seq: Int, message: ByteArray?) {
    }


    override fun onEnterRoom(elapsed: Long) {
        if (elapsed >= 0) {
            TxLogUtils.i("txsdk---onEnterRoom-----耗时$elapsed 毫秒")
            mPresenter?.startRecord()
            // 发起云端混流
            mPresenter?.getTRTCRemoteUserManager()!!.updateCloudMixtureParams()
            showActivityFloat()
        } else {
            TxLogUtils.i("txsdk---onEnterRoom-----耗时$elapsed 毫秒")
            ToastUtils.showShort(R.string.tx_joinroom_error)
            mPresenter?.exitRoom()
        }
    }

    private fun onUserVideoChange(
        userId: String,
        streamType: Int,
        available: Boolean
    ) {

        if (null != screenMeetingEntity && userId == screenMeetingEntity?.userId!!) {

            mPresenter?.getTRTCRemoteUserManager()?.remoteUserVideoAvailable(
                bigMeetingEntity?.userId,
                TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG,
                bigMeetingEntity?.meetingVideoView?.playVideoView, false
            )
            return
        }

        val entity1 = mPresenter!!.getAllMemberEntityMap()[userId]
        if (entity1 != null) {
            entity1.isMuteVideo = !available
        }
        if (changeUserStateDialog?.isShowing!!) {
            if (changeUserStateDialog?.memberEntity?.userId == entity1?.userId) {
                changeUserStateDialog?.changeLay(entity1)
            }
        }


        mRemoteUserListView?.notifyDataSetChanged()

        val entity = mPresenter!!.getStringMemberEntityMap()[userId]
        if (entity != null) {
            entity.isNeedFresh = true
            entity.isVideoAvailable = available
            entity.meetingVideoView.isNeedAttach = available
        } else {
            if (bigMeetingEntity != null && bigMeetingEntity?.userId!! == userId && !mPresenter?.isBroad!!) {
                bigMeetingEntity?.isNeedFresh = true
                bigMeetingEntity?.isVideoAvailable = available
                bigMeetingEntity?.meetingVideoView?.isNeedAttach = available
            }

        }
        if (entity != null) {
            mMemberListAdapter!!.notifyItemChanged(
                mPresenter?.getMemberEntityList()!!.indexOf(
                    entity
                )
            )
        }
        if (available) {
            if (mPresenter?.isBroad!!) {

            } else {

                if (bigMeetingEntity != null && bigMeetingEntity?.userId!! == userId) {
                    TxLogUtils.i("bigMeetingEntity")
                    bigscreen?.visibility = View.VISIBLE
                    trtc_fl_no_video.visibility = View.GONE
                    val meetingVideoView = bigMeetingEntity?.meetingVideoView
                    meetingVideoView?.detach()
                    meetingVideoView?.addViewToViewGroup(bigscreen)
                    mPresenter?.getTRTCRemoteUserManager()
                        ?.remoteUserVideoAvailable(
                            userId,
                            streamType,
                            meetingVideoView?.playVideoView,
                            false
                        )
                    changeBigScreenViewName(
                        bigMeetingEntity?.userName!!,
                        bigMeetingEntity!!.userRole,
                        bigMeetingEntity!!.userRoleIconPath
                    )
                }
            }

            if (!mPresenter?.getTRTCParams()!!.userId?.equals(userId)!!) { //开启定时器

                tx_icon_invite.visibility = View.GONE
                showInviteBt(isShow = true, noRemoterUser = false)
            }


        } else {
            if (entity != null) {
                mMemberListAdapter!!.notifyItemChanged(
                    mPresenter?.getMemberEntityList()!!.indexOf(
                        entity
                    )
                )
            } else {
                if (bigMeetingEntity != null && bigMeetingEntity?.userId!! == userId && !mPresenter?.isBroad!!) {
                    trtc_fl_no_video.visibility = View.VISIBLE
                    val meetingVideoView = bigMeetingEntity?.meetingVideoView
                    meetingVideoView?.detach()
                    mPresenter?.getTRTCRemoteUserManager()
                        ?.remoteUserVideoUnavailable(
                            userId,
                            streamType
                        )
                }
            }
        }

    }

    override fun onDisConnectOtherRoom(err: Int, errMsg: String?) {
    }

    override fun onUserAudioAvailable(userId: String?, available: Boolean) {
        onUserAudioChange(userId, available)
    }

    private fun onUserAudioChange(userId: String?, available: Boolean) {
        TxLogUtils.i("txsdk---onUserAudioAvailable----$userId----available-$available")
        TxLogUtils.i("txsdk---onUserAudioAvailablebigMeetingEntity----$userId----available-${bigMeetingEntity?.userId}")

        val entity1 = mPresenter!!.getAllMemberEntityMap()[userId]
        if (entity1 != null) {
            entity1.isMuteAudio = !available
        }
        if (changeUserStateDialog?.isShowing!!) {
            if (changeUserStateDialog?.memberEntity?.userId == entity1?.userId) {
                changeUserStateDialog?.changeLay(entity1)
            }
        }
        mRemoteUserListView?.notifyDataSetChanged()


        val entity = mPresenter!!.getStringMemberEntityMap()[userId]
        if (entity != null) {
            entity.isAudioAvailable = available
            entity.isShowAudioEvaluation = available
            mMemberListAdapter!!.notifyItemChanged(
                mPresenter?.getMemberEntityList()!!.indexOf(entity),
                MemberListAdapter.VOLUME_SHOW
            )
            //界面暂时没有变更
        } else {
            if (bigMeetingEntity != null && userId == bigMeetingEntity?.userId) {
                bigMeetingEntity?.apply {
                    isAudioAvailable = available
                    isShowAudioEvaluation = available
                }
                changeBigScreenViewVoice(50)
            }

        }

    }

    override fun onStatistics(statics: TRTCStatistics?) {
    }

    override fun onConnectOtherRoom(userID: String?, err: Int, errMsg: String?) {
    }

    //改变用户的音量大小显示
    private fun changeUserVoiceVolume(userId: String, volume: Int) {
        val entity = mPresenter!!.getStringMemberEntityMap()[userId]
        entity?.audioVolume = volume
        mMemberListAdapter!!.notifyItemChanged(
            mPresenter?.getMemberEntityList()!!.indexOf(entity),
            MemberListAdapter.VOLUME
        )
    }

    override fun onUserVoiceVolume(
        userVolumes: ArrayList<TRTCCloudDef.TRTCVolumeInfo>?,
        totalVolume: Int
    ) {
        userVolumes?.forEach {
//            TxLogUtils.d("volume------${it.userId}")
            if (mPresenter?.isBroad!!) {
                changeUserVoiceVolume(it.userId, it.volume)
            } else {
                if (bigMeetingEntity != null && bigMeetingEntity?.userId == it.userId) {
                    changeBigScreenViewVoice(it.volume)
                } else {
                    changeUserVoiceVolume(it.userId, it.volume)
                }
            }


        }
    }

    override fun onError(errCode: Int, errMsg: String?, extraInfo: Bundle?) {
        //-1308 分享屏幕失败
        TxLogUtils.i("txsdk---onError----：$errCode----：$errMsg")
        if (errCode == -1308) {
            mPresenter?.isShare = false
            ToastUtils.showShort(R.string.tx_showscreen_error)
            mPresenter?.stopScreenCapture()
            mPresenter?.setScreenStatus(false)
        }

    }

    override fun onRecvSEIMsg(userId: String?, data: ByteArray?) {
    }


    /**
     * 显示确认消息
     *
     * @param msg     消息内容
     * @param isError true错误消息（必须退出） false提示消息（可选择是否退出）
     */
    var dialog: ExitDialog? = null
    private fun showExitInfoDialog() {

        if (mPresenter?.isOwner()!!) {

        } else {
            //判断投屏按钮或者白板按钮状态
            if (tx_business_share.isSelected) {
                showMessage("当前正在共享，请结束后再试")
                return
            }
            if (tx_business_screen.isSelected) {
                showMessage("当前正在投屏，请结束后再试")
                return
            }
        }

        if (dialog == null) {
            dialog = if (mPresenter?.isOwner()!!) {
                ExitDialog(
                    this,
                    "暂时离开",
                    "确定离开",
                    "结束会话",
                    "请选择暂时离开还是结束会话？"
                )
            } else {

                ExitDialog(
                    this,
                    "取消",
                    "确定离开",
                    "离开",
                    "请确认是否离开会议？"
                )
            }

            dialog?.setOnConfirmlickListener(object :
                onExitDialogListener {
                override fun onConfirm() {
                    if (mPresenter?.isOwner()!!) {
                        TXSdk.getInstance().share = false
                    } else {
                    }
                    mPresenter?.destroyRoom()

                }

                override fun onTemporarilyLeave() {
                    if (mPresenter?.isOwner()!!) {
                        TXSdk.getInstance().share = true
                        //暂时离开
                        skipCaller()
                    } else {

                    }


                }

            })
        } else {

        }

        dialog?.show()

    }


    override fun skipCaller() {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    if (TXSdk.getInstance().isDemo) {
                        "txt://txtvideo:8888/videopage"
                    } else {
                        "txt://txtvideo:9999/videopage"
                    }
                )


            )
        )
    }

    var dialog1: ExitDialog? = null
    fun showDeleteFileDialog(id: String?, isDeleteFile: Boolean = true) {
        if (dialog1 == null) {
            dialog1 = ExitDialog(
                this,
                resources.getString(R.string.tx_dialog_exit_cancel),
                resources.getString(R.string.tx_dialog_exit_title),
                resources.getString(R.string.tx_dialog_exit_confirm),
                ""
            )
        }

        dialog1?.setOnConfirmlickListener(object :
            onExitDialogListener {
            override fun onConfirm() {
                TxLogUtils.i("txsdk---deleteFile-----$id")
                if (isDeleteFile) {
                    mPresenter?.deleteFile(id)
                } else {
                    mPresenter?.deleteScreenFile(id)
                }

            }

            override fun onTemporarilyLeave() {

            }

        })
        dialog1?.show()

    }


    var mTimerdialog: CommonDialog? = null
    override fun showTimerDialog(
        type: String,
        maxRoomTime: Int,
        extendRoomTime: Int,
        notifyExtendTime: Int,
        notifyEndTime: Int
    ) {

        if (mTimerdialog == null) {
            mTimerdialog = CommonDialog(
                this
            )
        }
        mTimerdialog?.setOnConfirmlickListener(object :
            onExitDialogListener {
            override fun onConfirm() {
                when (type) {
                    "2", "3" -> {
                        mPresenter?.extendTime()
                    }
                    "4" -> {
                        //todo 写到presenter
                        mPresenter?.sendGroupMessage(
                            mPresenter?.setIMTextData(IMkey.MUTEAUDIO)!!.put(
                                IMkey.USERS,
                                mPresenter?.setAllVideoStatusMemberToJSON(false, isMute = true)
                            )
                                .toString()
                        )
                        mPresenter?.setSoundStatus(false)
                        //点击全体静音
                        mRemoteUserListView?.selectAudioBtn(true)
                    }
                    else -> {
                    }
                }
            }

            override fun onTemporarilyLeave() {
                when (type) {
                    "2" -> {
                    }
                    "3" -> {
                        mPresenter?.destroyRoom()
                    }
                    "4" -> {
                    }
                    else -> {
                    }
                }
            }

            override fun end() {
                mPresenter?.destroyRoom()
            }

        })
        mTimerdialog?.show()
        when (type) {
            "1" -> {
                //显示单个
                mTimerdialog?.apply {
                    setTv_content("本次会议有效时间为${maxRoomTime / 60}分钟")
                    setOnlyConfirm("知道了")
                }

            }
            "2" -> {
                //通知延长
                mTimerdialog?.apply {
                    setTv_content("距离会议结束还有${notifyExtendTime / 60}分钟，\n 确认延长有效时间${extendRoomTime / 60}分钟？")
                    setTv_confirm("延长会话")
                    setTv_cancel("取消延长")
                }
            }
            "3" -> {
                //通知结束

                mTimerdialog?.apply {
                    setTv_content("会议有效时间已用尽，\n 确认延长有效时间${extendRoomTime / 60}分钟？")
                    setTv_confirm("延长会话")
                    startCommonTimer(notifyEndTime, "(若${notifyEndTime}s内无操作将自动结束会话)")
                }
            }
            "4" -> {
                mTimerdialog?.apply {
                    setTv_content("确认将会议内和新加入的成员都静音？")
                    setTv_confirm("确认")
                    setTv_cancel("取消")
                }
            }
            else -> {
            }
        }
    }


    override fun showBroad() {
        ll_board_business?.visibility = View.GONE

        if (mPresenter?.getAllMemberEntityList()!!.size == 0) {
            //如果没有客户
            tx_icon_invite.visibility = View.GONE
        } else {

        }


    }

    var changeFileDialog: SelectFileDialog? = null
    fun showChangeFileDialog() {

        if (changeFileDialog == null) {
            changeFileDialog =
                SelectFileDialog(this)
        } else {
            changeFileDialog?.request()
        }
        changeFileDialog?.setRequestID(TXSdk.getInstance().agent)
        changeFileDialog?.setOnConfirmlickListener(object :
            onDialogListenerCallBack {
            override fun onItemLongClick(id: String?) {

                showDeleteFileDialog(id)
            }

            override fun onFile() {
                //上传文件
                mPresenter?.chooseFile(true)
            }

            override fun onItemClick(url: String?, images: MutableList<String>) {
                //点击显示白板
                mPresenter?.setShareStatus(true, url, images)

            }

            override fun onConfirm() {
                //上传图片
                mPresenter?.chooseFile(false)
            }


        })

        changeFileDialog?.show()

    }

    var shareWhiteBroadDialog: ShareWhiteBroadDialog? = null
    override fun showChangeBroadModeDialog(isShow: Boolean) {
        if (isShow) {
            onEnd()
        } else {
            if (shareWhiteBroadDialog == null) {
                shareWhiteBroadDialog =
                    ShareWhiteBroadDialog(this)
            }

            shareWhiteBroadDialog?.setOnShareWhiteBroadDialogListener(this)
            shareWhiteBroadDialog?.show()
        }


    }

    var shareDialog: ShareDialog? = null
    override fun showShareDialog() {
        if (shareDialog == null) {
            shareDialog =
                ShareDialog(this)
            shareDialog?.setOnConfirmlickListener(object :
                onShareDialogListener {
                override fun onConfirmWx() {
                    mPresenter?.requestWX()
                }

                override fun onConfirmFd() {
                    //
                    if (null == TXSdk.getInstance().onFriendBtListener) {
                        showMessage("该方法暂未注册")
//                        TXSdk.getInstance().onFriendBtListener.onFail(60001,"该方法暂未注册")
                    } else {
                        TXSdk.getInstance().onFriendBtListener.onSuccess(
                            "" + mPresenter?.getRoomId(),
                            mPresenter?.getServiceId()!!,
                            mPresenter?.getSelfUserId()!!
                        )
                        TXSdk.getInstance().share = true
                        //暂时离开
//                        skipCaller()
                    }

                }

                override fun onConfirmMSG() {
                    mPresenter?.sendMsg()
                }

            })
        }

        shareDialog?.show()
        shareDialog?.setShareContent((mPresenter?.getMaxRoomUser()!! - 1).toString())
    }

    override fun updateAdapter(json: String) {
        this@VideoActivity.runOnUiThread {
            TxLogUtils.i("txsdk---getAgent:onSuccess---$json")
            val allFileBean =
                Gson().fromJson<FileBean>(json, FileBean::class.java)
            changeFileDialog?.invalidateAdapater(allFileBean.list)
        }
    }

    override fun uploadFileSuccess() {
        TxLogUtils.i("txsdk---uploadFile:onSuccess")
        //上传成功
        mPresenter?.update()
    }

    override fun uploadFileFail(msg: String) {
        TxLogUtils.i("txsdk---uploadFile:onFail")
        runOnUiThread { ToastUtils.showShort(msg) }
    }

    var api: IWXAPI? = null

    override fun sendReq(req: BaseReq) {

        api?.sendReq(req)
    }

    override fun startSoundSuccess() {
    }

    override fun startShareSuccess(
        shareStatus: Boolean,
        url: String?,
        images: MutableList<String>?
    ) {
        if (shareStatus) {
            if (null != images) {
                //展示文件白板
                val arrayListOf = arrayListOf<String>()
                rl_rv.visibility = if (images.size > 1) {
                    hideBoardTools()
                    showWhiteBroad(true)
                    mBoard?.addImagesFile(images)
                    picQuickAdapter?.setNewData(images)
                    tx_business_share.isSelected = true
                    changeFileDialog?.dismiss()
                    View.VISIBLE
                } else {
                    if (images.isEmpty()) {
                        showMessage(getString(R.string.tx_toast_transcoding_retry))
                    } else {
                        hideBoardTools()
                        showWhiteBroad(true)
                        arrayListOf.add(images[0])
                        tx_business_share.isSelected = true
                        mBoard?.addImagesFile(arrayListOf)
                        changeFileDialog?.dismiss()
                    }

                    View.GONE
                }
            } else {
                //展示纯白板
                boardIdList.clear()
                hideBoardTools()
                showWhiteBroad(true)
                showBroadFileRv(false)
                tx_business_share.isSelected = true
            }

        } else {

        }

    }

    override fun startShareFail(shareStatus: Boolean) {
        if (shareStatus) {
            showMessage("他人正在操作")
        } else {

        }
    }

    override fun startSoundFail() {
    }

    override fun getRoomInfoSuccess(
        json: String?,
        userId: String,
        streamType: Int,
        entity: MemberEntity?,
        available: Boolean,
        bigScreen: Boolean
    ) {
        runOnUiThread {
            mRemoteUserListView?.notifyDataSetChanged()
            if (bigScreen) {
                changeBigScreenViewName(
                    entity!!.userName,
                    entity.userRole,
                    entity.userRoleIconPath
                )
            } else {
                mMemberListAdapter!!.notifyItemChanged(
                    mPresenter?.getMemberEntityList()!!.indexOf(
                        entity
                    ), NAME_CHANGE
                )
            }

        }
    }

    override fun showSuccess() {
        runOnUiThread {
            showMessage(getString(R.string.tx_toast_success))
        }
    }

    override fun showFail() {
        runOnUiThread {
            showMessage(getString(R.string.tx_toast_error))
        }
    }

    override fun sendIMSuccess() {
        mBoard?.reset()
    }

    var mRemoteUserListView: RemoteUserListView? = null

    override fun handleMemberListView() {
        if (mRemoteUserListView == null) {
            mStubRemoteUserView?.inflate()
            mRemoteUserListView = findViewById<RemoteUserListView>(R.id.view_remote_user)
            mRemoteUserListView?.setRemoteUserListCallback(
                this
            )
            mRemoteUserListView?.setRemoteUser(mPresenter?.getAllMemberEntityList())

        } else {
            mRemoteUserListView?.visibility = if (mRemoteUserListView?.isShown!!) {
                View.GONE
            } else {
                View.VISIBLE
            }
            mRemoteUserListView?.post {
                mRemoteUserListView?.notifyDataSetChanged()
            }
        }
    }


    override fun onMuteAllVideoClick() {
        showShareDialog()
    }

    override fun onFinishClick() {
        hideRemoteUserListView(true)
    }

    override fun onSwitch() {
        switchCamera()
    }

    override fun onMuteAudioClick() {
        //开启静音
        val audioConfig = ConfigHelper.getInstance().audioConfig
        mPresenter?.muteLocalAudio(audioConfig.isEnableAudio)
    }

    override fun onMuteVideoClick() {
        val videoConfig = ConfigHelper.getInstance().videoConfig
        mPresenter?.muteLocalVideo(videoConfig.isEnableVideo)
    }

    override fun hideRemoteUserListView(isHiden: Boolean) {
        mRemoteUserListView?.visibility = View.GONE
    }

    override fun onMuteAudioClick(position: Int) {
        val memberEntity = mPresenter!!.getAllMemberEntityList()[position]
        showChangeUserStateDialog(memberEntity)

    }

    override fun onMuteVideoClick(position: Int) {
        val memberEntity = mPresenter!!.getAllMemberEntityList()[position]
        showChangeUserStateDialog(memberEntity)
    }

    override fun onMuteAllAudioClick() {
        showTimerDialog("4", 1, 1, 1, 1)
    }

    override fun onMuteAllAudioOffClick() {
        showMessage(getString(R.string.tx_toast_muteallmember))
        mPresenter?.sendGroupMessage(
            mPresenter?.setIMTextData(IMkey.MUTEAUDIO)!!
                .put(
                    IMkey.USERS,
                    mPresenter?.setAllVideoStatusMemberToJSON(false, isMute = false)
                ).toString()
        )
        mPresenter?.setSoundStatus(true)
        mRemoteUserListView?.selectAudioBtn(false)
    }

    override fun showPersonWhiteBroad(isShow: Boolean) {
        if (isShow) {
            if (board_view_container?.visibility == View.GONE) {
                mPresenter?.isBroad = true
                ll_board_business?.visibility = View.VISIBLE
                board_view_container?.visibility = View.VISIBLE
                checkBigVideoToFirstSmallVideo(true)
                mPresenter?.setRoomShareStatus(true)
            } else {
                mBoard?.reset()
            }

        } else {
            mPresenter?.isBroad = false
            if (board_view_container?.visibility == View.VISIBLE) {
                ll_board_business?.visibility = View.GONE
                board_view_container?.visibility = View.GONE
                checkSmallVideoToBigVideo(true)
                mPresenter?.setRoomShareStatus(false)
            } else {

            }

        }
    }


    fun showWBroad(isShow: Boolean) {
        if (isShow) {
            if (board_view_container?.visibility == View.GONE) {
                mPresenter?.isBroad = true
                ll_board_business?.visibility = View.VISIBLE
                board_view_container?.visibility = View.VISIBLE
                checkBigVideoToFirstSmallVideo(true)
            } else {
                mBoard?.reset()
            }

        } else {
            mPresenter?.isBroad = false
            if (board_view_container?.visibility == View.VISIBLE) {
                ll_board_business?.visibility = View.GONE
                board_view_container?.visibility = View.GONE
                checkSmallVideoToBigVideo(true)
            } else {

            }

        }

    }

    override fun showWhiteBroad(isShow: Boolean) {

        if (isShow) {
            if (board_view_container?.visibility == View.GONE) {
                mPresenter?.isBroad = true
                ll_board_business?.visibility = View.VISIBLE
                board_view_container?.visibility = View.VISIBLE
                checkBigVideoToFirstSmallVideo(true)
                mPresenter?.sendGroupMessage(
                    mPresenter?.setIMTextData(IMkey.SHAREWHITEBOARD)
                        ?.put(IMkey.SHAREUSERID, mPresenter?.getSelfUserId()).toString()
                )

                mPresenter?.setRoomShareStatus(true)
            } else {
                mBoard?.reset()
            }

        } else {
            mPresenter?.isBroad = false
            if (board_view_container?.visibility == View.VISIBLE) {
                ll_board_business?.visibility = View.GONE
                board_view_container?.visibility = View.GONE
                checkSmallVideoToBigVideo(true)
                mPresenter?.sendGroupMessage(
                    mPresenter?.setIMTextData(IMkey.ENDWHITEBOARD)
                        ?.put(IMkey.SHAREUSERID, mPresenter?.getSelfUserId()).toString(),
                    "1"
                )
                mPresenter?.setShareStatus(false, "", null)
                mPresenter?.setRoomShareStatus(false)
            } else {

            }

        }


    }

    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            api?.registerApp(TXSdk.getInstance().txConfig.wxKey)
        }

    }

    private fun regToWx() {

        api = createWXAPI(this, TXSdk.getInstance().txConfig.wxKey, TXSdk.getInstance().isDebug)
        api?.registerApp(TXSdk.getInstance().txConfig.wxKey)
        registerReceiver(
            broadcastReceiver, IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP)
        )

    }

    override fun onDestroy() {
        EasyFloat.hide(this)
        paintColorPostion = 0
        paintSizeIntPostion = 1
        textColorIntPostion = 0
        textSizeIntPostion = 1
        unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

    fun hideBoardTools() {
        mBoard?.isDrawEnable = true
        tx_boardtools.isSelected = false
        mBoard!!.toolType =
            TEduBoardController.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_ZOOM_DRAG

        tx_board_view_business.visibility = View.GONE
    }

    fun showBoardTools() {
        mBoard?.isDrawEnable = true
        tx_boardtools.isSelected = true
        val color = PaintThickPopup.mColorMap[paintColorPostion].color
        mBoard!!.apply {
            brushColor = TEduBoardController.TEduBoardColor(color!!)
            toolType = TEduBoardController.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_PEN
        }
        selectIb(tx_pen)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        TxLogUtils.i("txsdk---requestCode--$requestCode -----resultCode--$resultCode")
        TxLogUtils.i("txsdk---photo_code---${data?.data}")
        when (resultCode) {
            VideoCode.FINISHPAGE_CODE -> {
                if (requestCode == VideoCode.SKIPBOARDPAGE_CODE) {
                    mPresenter?.isSkipBoradPage = false
                    restoreBoardView()
                    val extras = data?.extras
                    val position = extras?.getInt(IntentKey.CHECKTOOLSPOSTIONS, -1)
                    val endWhiteBroad = extras?.getBoolean(IntentKey.ENDWHITEBROAD, false)
                    TxLogUtils.i("endWhiteBroad ---$endWhiteBroad")
                    if (endWhiteBroad!!) {
                        mPresenter?.setRoomShareStatus(false)
                        showPersonWhiteBroad(false)
                    } else {
                        if (-1 != position) {
                            val isShowPop = extras.getBoolean(IntentKey.ISSHOWPOP)
                            restoreBoardTool(position!!, isShowPop)
                        } else {
                            hideBoardTools()
                        }
                    }


                }
            }
            Activity.RESULT_OK -> {

                if (data?.data != null) {
                    val data1 = data?.data
                    when (requestCode) {
                        VideoCode.PHOTO_CODE, VideoCode.FILE_CODE -> {
                            //获取本地图片
                            TxLogUtils.i("txsdk---onActivityResult:data---code---$data1----")
                            mPresenter?.uploadFile(data1)
                        }

                        100 -> {
                            //录取权限开启
                        }
                        else -> {
                        }
                    }
                }

            }
            else -> {
            }
        }


    }

    override fun showMessage(message: String) {
        ToastUtils.showShort(message)
    }


    override fun modifyExitBt() {
        trtc_ib_back.text = if (
            mPresenter?.isOwner()!!
        ) {
            "结束会议"
        } else {
            "离开会议"
        }
    }

    override fun setScreenStatusFail(screenStatus: Boolean) {
        if (screenStatus) {
            showMessage("他人正在操作")
        } else {

        }
    }

    override fun setScreenStatusSuccess(screenStatus: Boolean) {
        if (screenStatus) {
            mPresenter?.requestPermission()
        } else {

        }

    }

    override fun onPermissionDenied() {
        mPresenter?.setScreenStatus(screenStatus = false)
    }

    fun onTxClick(v: View?) {
        val id = v?.id
        if (id == R.id.trtc_ib_back) {
            showExitInfoDialog()

        } else if (id == R.id.tv_invite || id == R.id.tx_icon_invite) {
            //邀请好友
            if (tv_invite.text == getString(R.string.tx_str_invite)) {
                showShareDialog()
            } else {
                handleMemberListView()
            }

        } else if (id == R.id.tx_business_video) {
            //关闭视频
            if (mPresenter?.isShare!! && mPresenter?.isOwner()!!) {
                ToastUtils.showShort(getString(R.string.tx_str_share))
                return
            }
            val videoConfig = ConfigHelper.getInstance().videoConfig
            mPresenter?.muteLocalVideo(videoConfig.isEnableVideo)
            mPresenter?.isCloseVideo = !videoConfig.isEnableVideo
        } else if (id == R.id.tx_business_audio) {
            //开启静音
            val audioConfig = ConfigHelper.getInstance().audioConfig
            mPresenter?.muteLocalAudio(audioConfig.isEnableAudio)
        } else if (id == R.id.tx_business_switch) {
            //反转镜头
            switchCamera()
        } else if (id == R.id.tx_business_share) {
            if (tx_business_screen.isSelected) {
                showMessage("您当前正在投屏，此时无法发起共享")
                return
            }
            //文档共享
            if (tx_business_share.isSelected) {
                //如果选择了，证明是可以点击的
                showChangeBroadModeDialog(mPresenter?.isBroad!!)
            } else {
//                if (mPresenter?.getShareUserId()?.isNotEmpty()!!) {
//                    //当前共享白板人 离开房间的话，需要释放这个权限
//
//                    val entity = mPresenter!!.getAllMemberEntityMap()[ mPresenter?.getShareUserId()!!]
//                    if (null==entity){
//                        //证明离开了
//                        mPresenter?.setShareStatus(false,"",null)
//
//                    }else{
//
//                    }
//
//                }
                //判断共享
                if (mPresenter?.getRoomShareStatus()!!) {
                    showMessage("他人正在共享，此时无法发起共享")
                    return
                }
                if (mPresenter?.getRoomScreenStatus()!!) {
                    showMessage("他人正在投屏，此时无法发起共享")
                    return
                }
                showChangeBroadModeDialog(mPresenter?.isBroad!!)
            }


        } else if (id == R.id.tx_business_screen) {
            if (tx_business_share.isSelected) {
                showMessage("您当前正在共享，此时无法发起投屏")
                return
            }
            //投屏 如果关闭摄像头，投屏中自动打开
            if (tx_business_screen.isSelected) {
                if (mPresenter?.isShare!!) {
                    //关闭
                    mPresenter?.stopScreenCapture()
                } else {
                    mPresenter?.setScreenStatus(screenStatus = true)

                }
            } else {
                if (mPresenter?.getRoomShareStatus()!!) {
                    showMessage("他人正在共享，此时无法发起投屏")
                    return
                }
//                if (mPresenter?.getRoomScreenStatus()!!) {
//                    showMessage("他人正在投屏，此时无法发起投屏")
//                    return
//                }

                if (mPresenter?.isShare!!) {
                    //关闭
                    mPresenter?.stopScreenCapture()
                } else {
                    mPresenter?.setScreenStatus(screenStatus = true)
                }
            }


        } else if (id == R.id.tx_boardtools) {
            //点击画笔
            tx_board_view_business.visibility = if (tx_boardtools.isSelected) {
                //隐藏
                hideBoardTools()
                View.GONE
            } else {
                //展示
                showBoardTools()
                View.VISIBLE
            }


        } else if (id == R.id.tx_pen) {
            //画笔
            val color = PaintThickPopup.mColorMap[paintColorPostion].color
            val size = PaintThickPopup.mColorMap1[paintSizeIntPostion].size
            mBoard!!.apply {
                brushColor = TEduBoardController.TEduBoardColor(color!!)
                brushThin = size
                toolType = TEduBoardController.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_PEN
            }

            if (tx_pen.isSelected) {
                showPopupWindow("1", paintColorPostion, paintSizeIntPostion)
            } else {
                tx_pen.isSelected = true
            }
            selectIb(tx_pen)

        } else if (id == R.id.tx_eraser) {
            //画圆
            mBoard!!.toolType = TEduBoardController.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_ERASER
            selectIb(tx_eraser)

        } else if (id == R.id.tx_zoom) {
            //移动
            mBoard!!.toolType = TEduBoardController.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_ZOOM_DRAG
            selectIb(tx_zoom)
        } else if (id == R.id.tx_textstyle) {
            //字体大小
            val color = PaintThickPopup.mColorMap[textColorIntPostion].color
            val size = PaintThickPopup.mTextMap[textSizeIntPostion].size
            mBoard!!.apply {
                textColor = TEduBoardColor(color)
                textSize = size
                toolType = TEduBoardController.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_TEXT
            }

            if (tx_textstyle.isSelected) {
                showPopupWindow("2", textColorIntPostion, textSizeIntPostion)
            } else {
                tx_textstyle.isSelected = true
            }
            selectIb(tx_textstyle)
        } else if (id == R.id.tx_ib_checkscreen) {
            mPresenter?.isSkipBoradPage = true
            removeBoardView()
            skipToBoardPage()

        }
    }

    private fun skipToBoardPage() {
        val intent = Intent(this, BoardViewActivity::class.java)
        val data = picQuickAdapter?.data
        intent.putExtra(IntentKey.SERVICEID, mPresenter?.getServiceId())
        if (null != data && boardIdList.size > 1) {
            intent.putStringArrayListExtra(
                IntentKey.BOARDLISTS,
                (data as java.util.ArrayList<String>?)!!
            )
            intent.putStringArrayListExtra(
                IntentKey.BOARDIDLISTS,
                (boardIdList as java.util.ArrayList<String>?)!!
            )
            intent.putExtra(IntentKey.CHECKPOSTIONS, mCheckPostion)
        }
        val selectToolsPosition = getSelectToolsPosition()
        intent.putExtra(IntentKey.CHECKTOOLSPOSTIONS, selectToolsPosition)

        //判断pop 是否显示
        val showing = paintThickPopup?.isShowing

        intent.putExtra(IntentKey.ISSHOWPOP, showing)


        startActivityForResult(intent, VideoCode.SKIPBOARDPAGE_CODE)
    }

    fun getSelectToolsPosition(): Int {
        val isSelectedList = videoBoradBusiness?.filter { it.isSelected }
        return if (isSelectedList?.size == 0 || !tx_boardtools.isSelected) {
            -1
        } else {
            videoBoradBusiness?.indexOf(isSelectedList?.get(0))!!
        }
    }

    fun restoreBoardTool(index: Int, isShowToolPop: Boolean) {
        tx_board_view_business.visibility = View.VISIBLE
        mBoard?.isDrawEnable = true
        tx_boardtools.isSelected = true
        val selectIB = videoBoradBusiness?.get(index)
        selectIb(selectIB!!)
    }

    var picQuickAdapter: PicQuickAdapter? = null
    var changeUserStateDialog: ChangeUserStateDialog? = null
    var mCheckPostion = 0


    //初始化缩略图list
    private fun initPicAdapter() {

        val mLllayoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            this
        ).apply {
            orientation = androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
        }

        picQuickAdapter =
            PicQuickAdapter()
        tx_rv.layoutManager = mLllayoutManager
        tx_rv.adapter = picQuickAdapter

        picQuickAdapter?.setOnItemClickListener { adapter, view, position ->
            TxLogUtils.i("txsdk---onItemClick---${boardIdList?.get(position)}")
            mCheckPostion = position
            mBoard?.gotoBoard(boardIdList[position])
        }

        if (changeUserStateDialog == null) {
            changeUserStateDialog =
                ChangeUserStateDialog(this@VideoActivity)
        }
        changeUserStateDialog?.setOnMuteDialogListener(this)
    }


    override fun onBackPressed() {
        showExitInfoDialog()
    }


    private var mFloatingWindow: FloatingView? = null
    override fun showFloatingWindow() {
        if (mFloatingWindow == null) {
            mFloatingWindow =
                FloatingView(
                    TXSdk.getInstance().application,
                    R.layout.tx_meeting_screen_capture_floating_window
                )
        }


        mFloatingWindow?.setTouchButtonClickListener {
            val activity = PendingIntent.getActivity(
                TXSdk.getInstance().application,
                130,
                Intent(this, VideoActivity::class.java),
                0
            )
            activity.send()
        }
        mFloatingWindow?.show()
    }

    override fun hideFloatingWindow() {
        mFloatingWindow?.hideView()
        mFloatingWindow = null
    }

    override fun getRemoteUserViewById(userId: String?, steamType: Int): MeetingVideoView? {
//        var view = mTRTCVideoLayout!!.findCloudViewView(userId, steamType)
//        if (view == null) {
//            view = mTRTCVideoLayout!!.allocCloudVideoView(userId, steamType, "")
//        }
        return mViewVideo
    }

    override fun onSnapshotRemoteView(bm: Bitmap?) {
    }

    override fun onRemoteViewStatusUpdate(userId: String?, enable: Boolean) {
    }

    var windowWidth: Int? = null
    override fun initBoard() {
        windowWidth = Utils.getWindowWidth(this)
        mBoard = mPresenter?.getTicManager()!!.boardController
        mBoard?.isDrawEnable = false
    }

    override fun joinClassroom() {
        //1、设置白板的回调
        mBoardCallback =
            MyBoardCallback(this)
        mPresenter?.joinClassroom(mBoardCallback!!)
    }


    override fun resetBoardLayout() {
//        if (mPresenter?.isOwner()!!){
//            mBoard?.reset()
//        }
        mBoard?.isDrawEnable = false
        tx_board_view_business.visibility = View.GONE
        tx_boardtools.visibility = View.GONE
        board_view_container?.visibility = View.GONE
        removeBoardView()
    }

    private var boardIdList = ArrayList<String>()

    fun onTEBAddBoard(boardId: List<String>, fileId: String) {
        TxLogUtils.i("txsdk---boardId ${boardId.size}---onTEBAddBoard:$fileId")
        mBoard!!.toolType = TEduBoardController.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_ZOOM_DRAG
        boardIdList?.clear()
        boardIdList?.addAll(boardId)
    }

    fun restoreBoardView() {
        val boardview = mBoard!!.boardRenderView

        val windowHeight = windowWidth!! / 16 * 9
        val layoutParams =
            FrameLayout.LayoutParams(
                windowWidth!!,
                windowHeight
            )

        board_view_container?.removeAllViews()
        board_view_container!!.addView(boardview, layoutParams)
        mBoard?.boardContentFitMode =
            TEduBoardController.TEduBoardContentFitMode.TEDU_BOARD_CONTENT_FIT_MODE_NONE
    }


    //展示录像计时器
    var timer: CountDownTimer? = null
    var mCurrentTimer = 0L
    override fun startTimer() {
        if (timer == null) {
            timer = object : CountDownTimer(60000, 1000) {
                @SuppressLint("SetTextI18n")
                override fun onTick(millisUntilFinished: Long) {
                    mCurrentTimer += 1000
                    tx_time.text = DatetimeUtil.getFormatHMS(mCurrentTimer)
                }

                override fun onFinish() {
                    timer!!.start()

                }
            }

            timer!!.start()
        }


    }

    override fun stopTimer() {
        timer?.cancel()
        timer = null
    }

    override fun addBoardView() {
        TxLogUtils.i("txsdk---addBoardView")
        mBoard?.isDrawEnable = false
        restoreBoardView()
        mBoard?.boardContentFitMode =
            TEduBoardController.TEduBoardContentFitMode.TEDU_BOARD_CONTENT_FIT_MODE_NONE
        tx_boardtools.visibility = View.VISIBLE
        tx_board_view_business.visibility = View.GONE
        if (mPresenter!!.getRoomShareStatus()) {
            // 需要显示白板出来
            // boardIdList.clear()
            hideBoardTools()
//            showWhiteBroad(true)
            showWBroad(true)
            showBroadFileRv(false)
            // tx_business_share.isSelected = true
        }


        if (mPresenter!!.getRoomScreenStatus() && !mPresenter!!.isOwner()) {
            checkOwenrToBigShareScreen(mPresenter!!.getScreenUserId())
        }

    }


    override fun removeBoardView() {
        if (mBoard != null) {
            val boardview = mBoard!!.boardRenderView
            if (board_view_container != null && boardview != null) {
                board_view_container!!.removeView(boardview)
            }
        }
    }


    override fun sendSystemMSG() {
        startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:")).apply {
            putExtra(
                "sms_body",
                "请打开微信-搜索【云助理智慧展业】小程序，输入邀请码 ${mPresenter?.getInviteNumber()} 进入会议"
            )
        })
    }

    override fun showInviteBt(isShow: Boolean, noRemoterUser: Boolean) {
        if (mPresenter!!.isOwner()) {
            tv_invite.visibility = if (isShow) {
                tv_invite.text = if (noRemoterUser) {
                    getString(R.string.tx_str_invite)
                } else {
                    getString(R.string.tx_str_invite1)
                }
                View.VISIBLE
            } else {
                View.GONE
            }

        } else {
            tv_invite.visibility = if (isShow) {
                tv_invite.text = getString(R.string.tx_str_invite)
                View.VISIBLE
            } else {
                View.GONE
            }

        }

    }


    override fun getContentViewId(): Int {

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        return R.layout.tx_activity_video
    }


    override fun createPresenter(): VideoPresenter = VideoPresenter(this, this)


    override fun init(savedInstanceState: Bundle?) {
        openImmersionBar()
        val parcelableExtra = intent.getParcelableExtra<RoomParamsBean>(IntentKey.KEY_DATA)
        mPresenter?.setTRTCParams(parcelableExtra!!)
    }

    override fun onCheckFileWhiteBroad() {
        showChangeFileDialog()
    }

    override fun onCheckBroad() {
        mPresenter?.setShareStatus(true, "", null)

    }

    override fun onEnd() {
        hideBoardTools()
        showWhiteBroad(false)
        showBroadFileRv(false)
        tx_business_share.isSelected = false
        mBoard?.reset()
    }


    override fun onMuteVideo(memberEntity: MemberEntity) {
        mPresenter?.sendGroupMessage(
            mPresenter?.setIMTextData(IMkey.MUTEVIDEO)!!
                .put(
                    IMkey.USERS,
                    mPresenter?.setMuteVideoMemberToJSON(true, memberEntity.userId)
                ).toString()
        )
    }

    override fun onMuteAudio(memberEntity: MemberEntity) {
        mPresenter?.sendGroupMessage(
            mPresenter?.setIMTextData(IMkey.MUTEAUDIO)!!
                .put(
                    IMkey.USERS,
                    mPresenter?.setMuteVideoMemberToJSON(false, memberEntity.userId)
                )
                .toString()
        )
    }


    override fun onCheckColor(postion: Int, toolType: ToolType, type: String) {
        val eduBoardColor = TEduBoardColor(toolType.color)
        if (type == "1") {
            paintColorPostion = postion
            mBoard!!.brushColor = eduBoardColor
        } else {
            textColorIntPostion = postion
            mBoard!!.textColor = eduBoardColor
        }
    }

    override fun onCheckThick(postion: Int, thickType: ThickType, type: String) {
        if (type == "1") {
            paintSizeIntPostion = postion
            mBoard!!.brushThin = thickType.size
        } else {
            textSizeIntPostion = postion
            mBoard!!.textSize = thickType.size
        }

    }


    override fun checkItemToBig(position: Int) {
        val memberEntity = mPresenter!!.getMemberEntityList()[position]
        val mCuccentmeetingVideoView = memberEntity.meetingVideoView
        if (mCuccentmeetingVideoView.isSelfView || mPresenter?.isBroad!!) {
            return
        }
        val mCuccentmemberEntity =
            mPresenter!!.getStringMemberEntityMap()[mCuccentmeetingVideoView.meetingUserId!!]
        mPresenter?.getStringMemberEntityMap()!!.remove(mCuccentmeetingVideoView.meetingUserId!!)

        //第一个用户不给点击
        val mCurrentmeetingVideoView = mCuccentmemberEntity?.meetingVideoView
        val mCuccentmemberUserId = mCuccentmemberEntity?.userId
        val mCuccentmemberUserName = mCuccentmemberEntity?.userName
        val mCuccentmemberIsAudioAvailable = mCuccentmemberEntity?.isAudioAvailable
        val mCuccentmemberIsVideoAvailable = mCuccentmemberEntity?.isVideoAvailable
        val mCuccentmemberIsShowAudioEvaluation = mCuccentmemberEntity?.isShowAudioEvaluation
        val mCuccentmemberIsHost = mCuccentmemberEntity?.isHost
        val mCuccentmemberUserRole = mCuccentmemberEntity?.userRole
        val mCuccentmemberUserRoleIconPath = mCuccentmemberEntity?.userRoleIconPath

        //大屏幕把当前的video分开
        if (bigMeetingEntity == null) {
            bigMeetingEntity = MemberEntity()
            //异常情况，当前大屏幕没有画面，点击小屏幕切换

        } else {
            val bigmeetingVideoView = bigMeetingEntity?.meetingVideoView
            val mBigMeetingUserId = bigMeetingEntity?.userId
            val mBigMeetingUserName = bigMeetingEntity?.userName
            val mBigMeetingIsAudioAvailable = bigMeetingEntity?.isAudioAvailable
            val mBigMeetingIsVideoAvailable = bigMeetingEntity?.isVideoAvailable
            val mBigMeetingIsShowAudioEvaluation = bigMeetingEntity?.isShowAudioEvaluation
            val mBigMeetingisHost = bigMeetingEntity?.isHost
            val mBigMeetingUserRole = bigMeetingEntity?.userRole
            val mBigMeetingUserRoleIconPath = bigMeetingEntity?.userRoleIconPath

            bigscreen?.removeView(bigmeetingVideoView)

            mCurrentmeetingVideoView?.detach()

            mCurrentmeetingVideoView?.addViewToViewGroup(bigscreen)

            bigmeetingVideoView?.waitBindGroup = mCuccentmeetingVideoView.waitBindGroup
            bigmeetingVideoView?.detach()

            bigMeetingEntity?.apply {
                userId = mCuccentmemberUserId
                userName = mCuccentmemberUserName
                meetingVideoView = mCurrentmeetingVideoView
                meetingVideoView.meetingUserId = mCuccentmemberUserId
                meetingVideoView?.isNeedAttach = true
                isNeedFresh = true
                isShowOutSide = true
                isVideoAvailable = mCuccentmemberIsVideoAvailable!!
                isAudioAvailable = mCuccentmemberIsAudioAvailable!!
                isShowAudioEvaluation = mCuccentmemberIsShowAudioEvaluation!!
                isHost = mCuccentmemberIsHost!!
                userRole = mCuccentmemberUserRole!!
                userRoleIconPath = mCuccentmemberUserRoleIconPath!!
            }
            mCuccentmemberEntity?.apply {
                userId = mBigMeetingUserId
                userName = mBigMeetingUserName
                meetingVideoView = bigmeetingVideoView
                meetingVideoView.meetingUserId = mBigMeetingUserId
                meetingVideoView?.isNeedAttach = true
                isNeedFresh = true
                isShowOutSide = false
                isVideoAvailable = mBigMeetingIsVideoAvailable!!
                isAudioAvailable = mBigMeetingIsAudioAvailable!!
                isShowAudioEvaluation = mBigMeetingIsShowAudioEvaluation!!
                isHost = mBigMeetingisHost!!
                userRole = mBigMeetingUserRole!!
                userRoleIconPath = mBigMeetingUserRoleIconPath!!
            }
            mCuccentmemberEntity?.meetingVideoView?.refreshParent()
            TxLogUtils.i(
                "onSingleClick",
                "mCuccentmemberEntity?.isVideoAvailable " + mCuccentmemberEntity?.isVideoAvailable
            )
            mPresenter?.getTRTCRemoteUserManager()!!.setRemoteFillMode(
                bigMeetingEntity?.userId,
                TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG,
                false
            )

            mPresenter?.getTRTCRemoteUserManager()!!.setRemoteFillMode(
                mCuccentmemberEntity?.userId,
                TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG,
                true
            )

            mPresenter!!.getStringMemberEntityMap()
                .put(mCuccentmemberEntity!!.userId, mCuccentmemberEntity!!)

            changeBigVideo(bigMeetingEntity!!)
            mMemberListAdapter!!.notifyItemChanged(
                mPresenter?.getMemberEntityList()!!.indexOf(
                    mCuccentmemberEntity
                ), VIDEOVIEW_CHANGE
            )


        }


    }


    override fun checkSmallVideoToBigVideo(isShowToBig: Boolean) {
        //用户第一个视频切换成大屏幕
        if (mPresenter!!.getMemberEntityList().size == 1) return
        val memberEntity = mPresenter!!.getMemberEntityList()[1]
        if (memberEntity != null && isShowToBig) {
            val meetingVideoView = memberEntity.meetingVideoView
            val userId = memberEntity.userId
            val userName = memberEntity.userName
            val isVideoAvailable = memberEntity.isVideoAvailable

            val isShowAudioEvaluation = memberEntity.isShowAudioEvaluation
            val memberIsHost = memberEntity.isHost
            val mUserRole = memberEntity.userRole
            val mUserRoleIconPath = memberEntity.userRoleIconPath
            if (bigMeetingEntity == null) {
                bigMeetingEntity = MemberEntity()
            }
            bigMeetingEntity?.userId = userId
            bigMeetingEntity?.userName = userName
            bigMeetingEntity?.meetingVideoView = meetingVideoView
            bigMeetingEntity?.isVideoAvailable = isVideoAvailable
            bigMeetingEntity?.isShowAudioEvaluation = isShowAudioEvaluation
            bigMeetingEntity?.isHost = memberIsHost
            bigMeetingEntity?.userRole = mUserRole
            bigMeetingEntity?.userRoleIconPath = mUserRoleIconPath
            changeBigVideo(bigMeetingEntity!!)
            meetingVideoView.detach()
            meetingVideoView.addViewToViewGroup(bigscreen)
            mPresenter?.getTRTCRemoteUserManager()!!.setRemoteFillMode(
                bigMeetingEntity?.userId,
                TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG,
                false
            )
            val index = mPresenter!!.removeMemberEntity(userId!!)
            if (index >= 0) {
                mMemberListAdapter!!.notifyItemRemoved(index!!)
            }
            TxLogUtils.i("onSingleClick", "checkSmallVideoToBigVideo${isVideoAvailable}")
            rl_bigscreen_name.visibility = View.VISIBLE
        }

    }

    override fun changeBigVideo(bigMeetingEntity: MemberEntity) {
        if (bigMeetingEntity.isVideoAvailable) {
            trtc_fl_no_video.visibility = View.GONE
            bigscreen?.visibility = View.VISIBLE
        } else {
            trtc_fl_no_video.visibility = View.VISIBLE
            bigscreen?.visibility = View.GONE
        }
        changeBigScreenViewName(bigMeetingEntity.userName, bigMeetingEntity.userRole,bigMeetingEntity.userRoleIconPath)
        changeBigScreenViewVoice(50)
    }

    override fun checkBigVideoToFirstSmallVideo(isShowToSmall: Boolean) {
        if (bigMeetingEntity != null && isShowToSmall) {
            hideBigNoVideo(false)
            bigMeetingEntity?.apply {
                isNeedFresh = true
                meetingVideoView.isNeedAttach = true
                isShowOutSide = false
            }
            val bigmeetingVideoView = bigMeetingEntity?.meetingVideoView
            bigscreen?.removeView(bigmeetingVideoView)

            mPresenter?.getTRTCRemoteUserManager()!!.setRemoteFillMode(
                bigMeetingEntity?.userId,
                TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG,
                true
            )

            mPresenter?.addMemberEntity(1, bigMeetingEntity!!)
            mMemberListAdapter!!.notifyItemInserted(1)

        }
    }

    override fun hideBigNoVideo(isHiden: Boolean) {
        rl_bigscreen_name.visibility = View.GONE
        trtc_fl_no_video.visibility = View.GONE
    }

    override fun showBroadFileRv(isShow: Boolean) {
        rl_rv.visibility = if (isShow) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun changeBigScreenViewName(text: String, userRole: String, userRoleIconPath: String) {
        rl_bigscreen_name.visibility = View.VISIBLE
        bigscreen_trtc_tv_content.text = text
        trtc_icon_host.visibility = if (userRole=="owner" || userRole=="assistant") {
            TxGlide.with(TXSdk.getInstance().application).load(userRoleIconPath).into(trtc_icon_host)
            View.VISIBLE
        }else{
            View.GONE
        }

        changeBigScreenViewVoice(10)
    }

    override fun changeBigScreenViewVoice(volume: Int) {
        if (!bigMeetingEntity?.isShowAudioEvaluation!!) {
            bigscreen_trtc_pb_audio.setImageResource(R.drawable.tx_icon_volume_mute)
            return
        }
        bigscreen_trtc_pb_audio.setImageResource(
            when (volume) {
                -1 -> {
                    R.drawable.tx_icon_volume_mute
                }
                0 -> {
                    R.drawable.tx_icon_volume_0
                }
                in 1..19 -> {
                    R.drawable.tx_icon_volume_1
                }
                in 20..39 -> {
                    R.drawable.tx_icon_volume_2
                }
                in 40..59 -> {
                    R.drawable.tx_icon_volume_3
                }
                in 60..79 -> {
                    R.drawable.tx_icon_volume_4
                }
                in 80..100 -> {
                    R.drawable.tx_icon_volume_5
                }
                else -> R.drawable.tx_icon_volume_4
            }
        )
    }


    private fun showChangeUserStateDialog(memberEntity: MemberEntity) {
        changeUserStateDialog?.show()
        changeUserStateDialog?.changeLay(memberEntity)
    }

    var paintThickPopup: PaintThickPopup? = null


    private fun showPopupWindow(type: String, paintColorPostion: Int, paintSizeIntPostion: Int) {
        paintThickPopup = PaintThickPopup(
            tx_boardtools,
            R.layout.tx_layout_paintstyle
        )
        val xDp = if (type == "1") {
            80
        } else {
            190
        }

        paintThickPopup?.setArrowOffsetXDp(xDp)
        paintThickPopup?.setOnCheckDialogListener(this)
        paintThickPopup?.refreshUI(type, paintColorPostion, paintSizeIntPostion)
        paintThickPopup?.show()

    }

    var mScreenView: ScreenView? = null

    override fun checkOwenrToBigShareScreen(screenUserId: String) {
        if (mScreenView == null) {
            bigsharescreen.inflate()
            mScreenView = findViewById<ScreenView>(R.id.view_bigscreen)
        }
        mScreenView?.setBigScreenCallBack(this)
        val audioConfig = ConfigHelper.getInstance().audioConfig
        val videoConfig = ConfigHelper.getInstance().videoConfig
        mScreenView?.muteAudio(!audioConfig.isEnableAudio)
        mScreenView?.muteVideo(!videoConfig.isEnableVideo)


        TxLogUtils.i("checkOwenrToBigShareScreen-----checkItemToBig")
        val ownerUserId = screenUserId
        if (!ownerUserId?.isEmpty()!!) {
            var entity = mPresenter!!.getStringMemberEntityMap()[ownerUserId]

            if (null == entity) {
                entity = bigMeetingEntity
            } else {

            }
            val meetingVideoView = entity?.meetingVideoView
            if (screenMeetingEntity == null) {
                screenMeetingEntity = MemberEntity()
            }
            screenMeetingEntity?.meetingVideoView = meetingVideoView
            screenMeetingEntity?.userId = entity?.userId
            //没有Parent
            meetingVideoView?.detach()
            meetingVideoView?.addViewToViewGroup(mScreenView?.bigScreenView)
//            entity?.isVideoAvailable = true
//            entity?.isMuteVideo = false
            mScreenView?.visibility = View.VISIBLE
            mPresenter?.getTRTCRemoteUserManager()?.setRemoteFillMode(
                entity?.userId,
                TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG,
                false
            )
            entity?.isShowOutSide = true
        }

    }

    override fun detachBigShareScreen(screenUserId: String) {
        TxLogUtils.i("detachBigShareScreen------")
        if (screenUserId.isNotEmpty() && null != screenMeetingEntity) {
            mScreenView?.bigScreenView?.removeAllViews()
            var entity = mPresenter!!.getStringMemberEntityMap()[screenUserId]

            if (null == entity) {
                entity = bigMeetingEntity
            } else {

            }
            val mMeetingVideoView = screenMeetingEntity?.meetingVideoView
            entity?.apply {
                meetingVideoView = mMeetingVideoView
                isShowOutSide = false
                isNeedFresh = true
            }

            mScreenView?.visibility = View.GONE
            mPresenter!!.getTRTCRemoteUserManager().setRemoteFillMode(
                entity?.userId,
                TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG,
                true
            )

            val indexOf = mPresenter!!.getMemberEntityList().indexOf(
                entity
            )
            if (indexOf < 0) {
                mMeetingVideoView?.detach()
                mMeetingVideoView?.addViewToViewGroup(bigscreen)
                changeBigVideo(entity!!)
            } else {
                mMemberListAdapter?.notifyItemChanged(
                    mPresenter!!.getMemberEntityList().indexOf(
                        entity
                    )
                )
            }


            screenMeetingEntity = null
        }


    }

    override fun switchCamera() {
        //反转镜头
        mPresenter?.getTRTCCloudManager()?.switchCamera()
        tx_business_switch.isSelected = !mPresenter?.getTRTCCloudManager()?.isFontCamera!!
    }

    override fun onScreenFinishClick() {
        showExitInfoDialog()
    }

    var selectPersonDialog: SelectPersonDialog? = null
    override fun showPersonDialog(
        webId: String?,
        url: String,
        name: String,
        isSameScreen: Boolean,
        list: JSONArray,
        fileName: String
    ) {
        val memberEntityList = mPresenter?.getAllMemberEntityList()
        TxLogUtils.i("memberEntityList" + memberEntityList!!.size)
        if (memberEntityList.size == 0) {
            showMessage("共享产品失败，会议内暂无其他人员")
            return
        }
        if (memberEntityList!!.size == 1) {
            for (i in 0 until memberEntityList.size) {
                try {
                    val memberEntity = memberEntityList.get(i)
                    val userId = memberEntity.userId
                    TxLogUtils.i("memberEntityList" + userId)
                    if (userId ==  mPresenter!!.getSelfUserId()) {
                        //本人不添加
                    } else {
                        if (isSameScreen) {
                            //  跳到
                            mPresenter!!.startShareWeb(
                                webId!!,
                                mPresenter!!.getServiceId(),
                                mPresenter!!.getSelfUserId(),
                                userId!!,
                                name
                            )

                        } else {
                            //推送链接给小程序
                            mPresenter?.sendGroupMessage(JSONObject().apply {
                                put("serviceId", mPresenter!!.getServiceId())
                                put("type", IMkey.WXPUSHWEBFILE)
                                put("userId", userId)
                                put("webId", webId)
                                put("webUrl", url)
                                put("fromId", mPresenter!!.getSelfUserId())
                                put(IMkey.FILENAME, name)
                            }.toString(), "3")
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        } else {
            if (selectPersonDialog == null) {
                selectPersonDialog =
                    SelectPersonDialog(this)

            } else {

            }
            selectPersonDialog?.setRequestID(TXSdk.getInstance().agent)
            selectPersonDialog?.setOnConfirmlickListener(object :
                onDialogListenerCallBack {
                override fun onItemLongClick(id: String?) {

                }

                override fun onFile() {

                }

                override fun onItemClick(userId: String?, nameStr: String) {
                    if (isSameScreen) {
                        //  跳到
                        mPresenter!!.startShareWeb(
                            webId!!,
                            mPresenter!!.getServiceId(),
                            mPresenter!!.getSelfUserId(),
                            userId!!,
                            name
                        )

                    } else {
                        //推送链接给小程序
                        mPresenter?.sendGroupMessage(JSONObject().apply {
                            put("serviceId", mPresenter!!.getServiceId())
                            put("type", IMkey.WXPUSHWEBFILE)
                            put("userId", userId)
                            put("webId", webId)
                            put("webUrl", url)
                            put("fromId", mPresenter!!.getSelfUserId())
                            put(IMkey.FILENAME, name)
                        }.toString(), "3")
                    }

                }

                override fun onConfirm() {

                }


            })

            selectPersonDialog?.show()
            selectPersonDialog?.invalidateAdapater(memberEntityList!!)
        }

    }

    var personDialog: SelectChannelDialog? = null
    override fun showSelectChannelDialog(
        list: JSONArray,
        webId: String,
        url: String,
        name: String,
        fileName: String
    ) {

        if (personDialog == null) {
            personDialog =
                SelectChannelDialog(this)
        }
        personDialog?.setOnShareWhiteBroadDialogListener(object : onShareWhiteBroadDialogListener {
            override fun onCheckFileWhiteBroad() {

                showPersonDialog(webId!!, url, name, true, list, fileName)
            }

            override fun onCheckBroad() {
                showPersonDialog(webId!!, url, name, false, list, fileName)

            }

            override fun onShareWhiteBroadEnd() {

            }

        })

        personDialog?.show()
        personDialog?.setText(name)
    }

    override fun getPushWebUrlSuccess(webId: String, clientUrl: String, name: String) {
        if (!clientUrl.isEmpty()) {
            mPresenter!!.getRoomInfo(webId, clientUrl, name, name)
        }else{
            ToastUtils.showShort("返回的clientUrl为空")
        }

    }

    var selectWebUrlDialog: SelectWebUrlDialog? = null
    fun showSelectWebUrlDialog() {

        if (selectWebUrlDialog == null) {
            selectWebUrlDialog =
                SelectWebUrlDialog(this)

        } else {
            selectWebUrlDialog?.request()
        }
        selectWebUrlDialog?.setRequestID(TXSdk.getInstance().agent)
        selectWebUrlDialog?.setOnConfirmlickListener(object :
            onDialogListenerCallBack {
            override fun onItemLongClick(id: String?) {

                showDeleteFileDialog(id, false)
            }

            override fun onFile() {

            }

            override fun onItemClick(webId: String?, url: String, name: String) {
                //判断房间的人数
                mPresenter!!.setShareWebId(webId)
                //需要从后台拿到分享url
                mPresenter!!.getPushWebUrl(
                    mPresenter!!.getSelfUserId(),
                    webId!!,
                    mPresenter!!.getServiceId(),
                    url,
                    name
                   )

            }

            override fun onConfirm() {
                InputDialog.Builder(this@VideoActivity)
                    .setTitle("上传产品")
                    .setConfirm("确认")
                    .setCancel("取消")
                    .setListener { dialog, name, url ->
                        if (name.isNotEmpty() && url.isNotEmpty()) {
                            mPresenter?.addShareUrl(TXSdk.getInstance().agent, name!!, url!!)
                        } else {
                            if (name.isEmpty()) {
                                showMessage("产品名称不能为空")
                            }else{
                                showMessage("产品链接不能为空")
                            }
                        }
                    }
                    .show()
            }


        })

        selectWebUrlDialog?.show()

    }


    var webDialog: WebDialog? = null
    override fun showWebDialog(
        url: String,
        userId: String,
        webId: String,
        fromAgent: Boolean,
        fileName: String,
        toUserId: String,
        fromUserId: String
    ) {
        // fromAgent 为true 显示结束共享按钮

        if (webDialog == null) {
            webDialog =
                WebDialog(this)
        }
        webDialog?.setOnShareWhiteBroadDialogListener(object : onShareWhiteBroadDialogListener {
            override fun onCheckFileWhiteBroad() {
                //结束共享
                var dialog1 = ExitDialog(
                    this@VideoActivity,
                    "取消",
                    "",
                    "确认",
                    "请问是否结束共享"
                )
                dialog1?.setOnConfirmlickListener(object :
                    onExitDialogListener {
                    override fun onConfirm() {
                        if (toUserId.isEmpty()) {
                            //推送类型
                        } else {
                            mPresenter?.sendGroupMessage(JSONObject().apply {
                                put("serviceId", mPresenter!!.getServiceId())
                                put("type", "wxShareWebFileEnd")
                                put("userId", userId)
                                put("fromUserId", fromUserId)
                                put("toUserId", toUserId)
                            }.toString(), "3")
                            mPresenter?.stopShareWeb(
                                mPresenter!!.getSelfUserId(),
                                webId,
                                mPresenter!!.getServiceId()
                            )
                        }


                        hideWebDialog()
                    }

                    override fun onTemporarilyLeave() {


                    }

                })

                dialog1?.show()

            }

            override fun onEnd() {
                //加载完成
                //推送链接给小程序
                if (toUserId.isEmpty()) {
                    mPresenter?.sendGroupMessage(JSONObject().apply {
                        put("serviceId", mPresenter!!.getServiceId())
                        put("type", IMkey.WXPUSHWEBFILESUCCESS)
                        put("userId", fromUserId)
                    }.toString(), "3")
                }

            }

            override fun onCheckBroad() {
                //静音
                val audioConfig = ConfigHelper.getInstance().audioConfig
                mPresenter?.muteLocalAudio(audioConfig.isEnableAudio)

            }

            override fun onShareWhiteBroadEnd() {


                //结束共享
                var dialog1 = ExitDialog(
                    this@VideoActivity,
                    "取消",
                    "",
                    "确认",
                    "请问是否结束共享"
                )
                dialog1?.setOnConfirmlickListener(object :
                    onExitDialogListener {
                    override fun onConfirm() {
                        if (toUserId.isEmpty()) {
                            //推送类型
                            showMessage("您已结束产品共享")
                        } else {
                            //点击返回按钮
                            mPresenter?.sendGroupMessage(JSONObject().apply {
                                put("serviceId", mPresenter!!.getServiceId())
                                put("type", "wxShareWebFileEnd")
                                put("userId", userId)
                                put("fromUserId", fromUserId)
                                put("toUserId", toUserId)
                            }.toString())
                        }

                        hideWebDialog()
                    }

                    override fun onTemporarilyLeave() {


                    }

                })

                dialog1?.show()
            }

        })
        webDialog?.show()
        webDialog?.request(url, fromAgent, fileName)

    }

    override fun hideWebDialog() {
        webDialog?.dismiss()
    }

    override fun onShareWhiteBroadEnd() {
        showSelectWebUrlDialog()
    }

    override fun updateWebUrlAdapter(json: String) {
        TxLogUtils.i("txsdk---updateWebUrlAdapter---$json")
        val allFileBean =
            Gson().fromJson<FileBean>(json, FileBean::class.java)
        selectWebUrlDialog!!.invalidateAdapater(allFileBean.list)
    }

    override fun uploadWebUrlFail(msg: String) {

    }

    override fun uploadWebUrlSuccess() {
        selectWebUrlDialog?.request()
    }

    @SuppressLint("SetTextI18n")
    private fun showActivityFloat() {
        Handler().postDelayed({
            EasyFloat.with(this)
                .setGravity(Gravity.BOTTOM or Gravity.RIGHT, -30, -300)
                .setLayout(R.layout.tx_float_custom, OnInvokeView {
                    it.findViewById<TextView>(R.id.tx_float).setOnClickListener(CheckDoubleClickListener(){
                        showSmartDialog()
                    })

                })
                .registerCallback {

                }
                .show()
        },200)

    }

    var mSmartWebDialog: SmartWebDialog? = null
    private fun showSmartDialog(){
        if (mSmartWebDialog == null) {
            mSmartWebDialog =
                SmartWebDialog(this)
        }
        mSmartWebDialog?.show()
        mSmartWebDialog?.request("https://ics.webank.com/s/serviceHall/?app_id=W4054396&user_id=12345675&nonce=5wpnjcQ5UUedWDacb1aKcBhQIl9jFsd4&sign=93fb1153abafba8f847259b92211eb09fdb23e93&version=1.0.0#/index",
            "智慧锦囊")
    }

    private fun hideSmartWebDialog(){
        mSmartWebDialog?.dismiss()
    }
}