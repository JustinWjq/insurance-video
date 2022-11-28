package com.txt.video.ui.video

import android.app.Activity
import android.app.PendingIntent
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothHeadset
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
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
import com.txt.video.base.IBaseView
import com.txt.video.base.constants.IMkey
import com.txt.video.base.constants.IntentKey
import com.txt.video.base.constants.VideoCode
import com.txt.video.common.adapter.base.entity.MultiItemEntity
import com.txt.video.common.callback.*
import com.txt.video.common.dialog.common.TxBaseDialog
import com.txt.video.common.floatview.FloatingView
import com.txt.video.common.utils.CheckHeadSetSUtils
import com.txt.video.common.utils.ToastUtils
import com.txt.video.net.bean.*
import com.txt.video.net.utils.TxLogUtils
import com.txt.video.trtc.*
import com.txt.video.trtc.remoteuser.RemoteUserConfigHelper
import com.txt.video.trtc.remoteuser.TRTCRemoteUserIView
import com.txt.video.trtc.ticimpl.utils.MyBoardCallback
import com.txt.video.trtc.videolayout.Utils
import com.txt.video.trtc.videolayout.list.*
import com.txt.video.trtc.videolayout.list.MemberListAdapter.*
import com.txt.video.ui.TXManagerImpl
import com.txt.video.ui.boardpage.BoardViewActivity
import com.txt.video.ui.video.barrage.model.TUIBarrageModel
import com.txt.video.ui.video.barrage.presenter.TUIBarragePresenter
import com.txt.video.ui.video.barrage.view.ITUIBarrageListener
import com.txt.video.ui.video.barrage.view.TUIBarrageButton
import com.txt.video.ui.video.barrage.view.TUIBarrageDisplayView
import com.txt.video.ui.video.plview.V2VideoLayout
import com.txt.video.ui.video.remoteuser.RemoteUserListView
import com.txt.video.ui.weight.dialog.*
import com.txt.video.ui.weight.easyfloat.EasyFloat
import com.txt.video.ui.weight.easyfloat.utils.DisplayUtils
import com.txt.video.ui.weight.view.IOSLoadingView
import com.txt.video.ui.weight.view.ScreenView
import kotlinx.android.synthetic.main.tx_activity_video.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.math.absoluteValue

/**
 * Created by JustinWjq
 * @date 2020-12-23.
 * description：ui
 */
class VideoActivity : BaseActivity<VideoContract.ICollectView, VideoPresenter>(), TRTCCloudIView,
    TRTCCloudManagerListener,
    TRTCRemoteUserIView, VideoContract.ICollectView,
    onShareWhiteBroadDialogListener, onMuteDialogListener,
    onCheckDialogListenerCallBack,
    ScreenView.BigScreenViewCallback {

    var mBoard: TEduBoardController? = null
    var mBoardCallback: MyBoardCallback? = null

    private var bigMeetingEntity: MemberEntity? = null

    var mViewVideo: MeetingVideoView? = null


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

    //横竖屏切换逻辑
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        TxLogUtils.i("onConfigurationChanged")
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            TxLogUtils.i("Configuration.ORIENTATION_LANDSCAPE")
            if (null != mTxBaseDialog) {
                if (mTxBaseDialog?.isShowing!!) {
                    changeDialogLandscape(true)
                }
            }

            iv_switchscreen.isSelected = true
        } else {
            TxLogUtils.i("Configuration.ORIENTATION_PORTRAIT")
            if (null != mTxBaseDialog) {
                if (mTxBaseDialog?.isShowing!!) {
                    changeDialogLandscape(false)
                }
            }

            iv_switchscreen.isSelected = false
        }
        trtc_video_view_layout.switchScreen(false)
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
    }

    private fun changeDialogLandscape(isShow: Boolean) {

        if (mTxBaseDialog != null) {
            var window = mTxBaseDialog?.getWindow();
            var lp = window?.attributes
            if (isShow) {
                lp!!.gravity = Gravity.RIGHT
            } else {
                lp!!.gravity = Gravity.CENTER
            }

            window?.attributes = lp
        }
    }


    override fun initViews() {
        TXManagerImpl.instance?.setAc(this)
        iv_switchscreen.isSelected = true
        regeistHeadsetReceiver()
        showInviteBt(isShow = true, noRemoterUser = true)
        mViewVideo = mPresenter!!.getMemberEntityList()[0].meetingVideoView
        trtc_video_view_layout.setOnClickListener(object : V2VideoLayout.onVideoLayout {
            override fun onClick() {
                hideBar()
            }

        })
        val statusBarHeight = DisplayUtils.getStatusBarHeight(this)
        ll_title.setPadding(0, statusBarHeight, 0, 0)
        startHideBartimer()
        trtc_video_view_layout.initAdapter(mPresenter?.getMemberEntityList())
        trtc_video_view_layout.setTRTCRemoteUserManager(mPresenter?.getTRTCRemoteUserManager())
        trtc_video_view_layout.setData(mPresenter?.getStringMemberEntityMap())
        trtc_video_view_layout.setPageListener(object : V2VideoLayout.IBusListener {
            override fun onItemVisible(fromItem: Int, toItem: Int) {
                TxLogUtils.i("onItemVisible:fromItem" + fromItem + "------toItem" + toItem)
                if (fromItem == 0) {
                    processSelfVideoPlay()
                    mPresenter?.processVideoPlay(0, toItem)
                } else {
                    mPresenter?.processVideoPlay(fromItem, toItem)
                }
            }

            override fun onRvItemClick(position: Int) {
                if (position != 0) {
//                            checkItemToBig(position)
                }
                hideBar()
            }

        })

        regToWx()
        modifyExitBt()
        mPresenter?.muteLocalAudio(!mPresenter!!.getRoomSoundStatus())
        val isEnableVideo = TXManagerImpl.instance?.getRoomControlConfig()?.isEnableVideo!!
        TxLogUtils.i("isEnableVideo---$isEnableVideo")
        mPresenter?.muteLocalVideo(!isEnableVideo)
        mPresenter?.isCloseVideo = !isEnableVideo
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
                entity?.isMuteAudio = isMute
                trtc_video_view_layout!!.notifyItemChangedPld(
                    indexOf, VOLUME_SHOW
                )

            }

        } else {
            trtc_video_view_layout!!.bigMeetingEntity.isMuteAudio = isMute
            trtc_video_view_layout!!.bigMeetingEntity.isShowAudioEvaluation = !isMute
            trtc_video_view_layout!!.notifyItemChangedPld(
                mPresenter?.getTRTCParams()!!.userId,
                0, VOLUME_SHOW
            )
        }
        webDialog?.checkAudio(isMute)
        mTxRemoteUserDialogBuilder?.notifyDataSetChanged()
    }

    override fun onMuteLocalVideo(isMute: Boolean) {
        TxLogUtils.i("onMuteLocalVideo$isMute")
        tx_business_video.isSelected = isMute
        tx_business_switch.visibility = if (isMute) {
            View.GONE
        } else {
            View.VISIBLE
        }
        val entity =
            mPresenter!!.getStringMemberEntityMap()[mPresenter?.getTRTCParams()!!.userId]
        if (null != entity) {
            entity?.isMuteVideo = isMute
            trtc_video_view_layout!!.notifyItemChangedPld(
                entity?.userId!!,
                mPresenter?.getMemberEntityList()!!.indexOf(
                    entity
                ), VIDEO_CLOSE
            )
        } else {
            trtc_video_view_layout!!.bigMeetingEntity.isMuteVideo = isMute
            trtc_video_view_layout!!.notifyItemChangedPld(
                mPresenter?.getTRTCParams()!!.userId,
                0, VIDEO_CLOSE
            )
        }


        mTxRemoteUserDialogBuilder?.notifyDataSetChanged()
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

    override fun onScreenCaptureStarted() {}

    override fun onScreenCaptureStopped(reason: Int) {}

    override fun onRemoteUserEnterRoom(remoteUserId: String?) {
        TxLogUtils.i("txsdk---onRemoteUserEnterRoom-----$remoteUserId")
        TxLogUtils.i("txsdk---onRemoteUserEnterRoom-----${mPresenter?.isBroad}")

        //协配员进入房间，大屏显示业务员
        //业务员的话，流程不变
        //进入获取房间信息
        val contains = remoteUserId?.contains("tic_record")
        if (!contains!!) {
//            val mMeetingVideoView = V2TRTCVideoLayout(this@VideoActivity)
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
            mPresenter?.addMemberEntity(entity)
            trtc_video_view_layout!!.notifyItemInsertedPld(insertIndex)
            mPresenter?.getRoomInfo(
                remoteUserId,
                TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG,
                false,
                entity,
                false
            )
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
        mTxRemoteUserDialogBuilder?.notifyDataSetChanged()

        if (index!! >= 0) {
            //todo
            trtc_video_view_layout!!.notifyItemRemovedPld(index)
        } else {
//            if (bigMeetingEntity != null && userId == bigMeetingEntity?.userId) {
//                bigscreen?.visibility = View.GONE
//                trtc_fl_no_video.visibility = View.GONE
//                rl_bigscreen_name.visibility = View.GONE
//                bigMeetingEntity = null
//                if (!mPresenter?.isBroad!! && mPresenter?.getAllMemberEntityList()!!.size == 1) {
//                    checkSmallVideoToBigVideo(true)
//                }
//
//            }
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
        val entity1 = mPresenter!!.getAllMemberEntityMap()[userId]
        if (entity1 != null) {
            entity1.isMuteVideo = !available
        }

        mTxRemoteUserDialogBuilder?.notifyDataSetChanged()

        val entity = mPresenter!!.getStringMemberEntityMap()[userId]
        if (entity != null) {
            entity.isNeedFresh = true
            entity.isVideoAvailable = available
            entity.meetingVideoView.isNeedAttach = available
        }
        if (entity != null) {
            trtc_video_view_layout!!.notifyItemChangedPld(
                entity?.userId!!,
                mPresenter?.getMemberEntityList()!!.indexOf(
                    entity
                ), VIDEO_CLOSE
            )
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
//        if (changeUserStateDialog?.isShowing!!) {
//            if (changeUserStateDialog?.memberEntity?.userId == entity1?.userId) {
//                changeUserStateDialog?.changeLay(entity1)
//            }
//        }
        mTxRemoteUserDialogBuilder?.notifyDataSetChanged()


        val entity = mPresenter!!.getStringMemberEntityMap()[userId]
        if (entity != null) {
            entity.isAudioAvailable = available
            entity.isShowAudioEvaluation = available
            //todo
            trtc_video_view_layout!!.notifyItemChangedPld(
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
        if (null != entity) {
            trtc_video_view_layout!!.notifyItemChangedPld(
                mPresenter?.getMemberEntityList()!!.indexOf(entity),
                MemberListAdapter.VOLUME
            )
        } else {
            trtc_video_view_layout!!.changeBigScreenViewVoice(volume)
        }


    }

    override fun onUserVoiceVolume(
        userVolumes: ArrayList<TRTCCloudDef.TRTCVolumeInfo>?,
        totalVolume: Int
    ) {
        userVolumes?.forEach {
            changeUserVoiceVolume(it.userId, it.volume)

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
    var mTxBaseDialog: TxBaseDialog? = null
    private fun showExitInfoDialog() {

        if (mPresenter?.isOwner()!!) {

        } else {
            //判断投屏按钮或者白板按钮状态
            if (tx_business_share.isSelected) {
                showMessage("当前正在共享，请结束后再试")
                return
            }
        }


        TxMessageDialog.Builder(this)
            .setTitle("确定结束")
            .setMessage("您确定要结束会议?")
            .setConfirm("结束")
            .setCancel("取消")
            .setListener(object : TxMessageDialog.OnListener {
                override fun onConfirm(dialog: TxBaseDialog?) {
                    if (mPresenter?.isOwner()!!) {
                        TXSdk.getInstance().share = false
                    } else {
                    }
                    mPresenter?.destroyRoom()

                }

                override fun onCancel(dialog: TxBaseDialog?) {
//                    if (mPresenter?.isOwner()!!) {
//                        TXSdk.getInstance().share = true
//                        //暂时离开
//                        skipCaller()
//                    } else {
//
//                    }

                }

            }).show()
    }


    override fun skipCaller() {
        finish()
//        startActivity(
//            Intent(
//                Intent.ACTION_VIEW,
//                Uri.parse(
//                    if (TXSdk.getInstance().isDemo) {
//                        "txt://txtvideo:8888/videopage"
//                    } else {
//                        "txt://txtvideo:9999/videopage"
//                    }
//                )
//
//
//            )
//        )
    }

    fun showDeleteFileDialog(id: String?, isDeleteFile: Boolean = true) {
        TxMessageDialog.Builder(this)
            .setTitle(resources.getString(R.string.tx_dialog_exit_title))
            .setMessage("")
            .setConfirm(resources.getString(R.string.tx_dialog_exit_confirm))
            .setCancel(resources.getString(R.string.tx_dialog_exit_cancel))
            .setListener(object : TxMessageDialog.OnListener {
                override fun onConfirm(dialog: TxBaseDialog?) {

                    TxLogUtils.i("txsdk---deleteFile-----$id")
                    if (isDeleteFile) {
                        mPresenter?.deleteFile(id)
                    } else {
                        mPresenter?.deleteScreenFile(id)
                    }

                }

                override fun onCancel(dialog: TxBaseDialog?) {

                }

            }).create().show()

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
//        if (isShow) {
//            onEnd()
//        } else {
//            if (shareWhiteBroadDialog == null) {
//                shareWhiteBroadDialog =
//                    ShareWhiteBroadDialog(this)
//            }
//
//            shareWhiteBroadDialog?.setOnShareWhiteBroadDialogListener(this)
//            shareWhiteBroadDialog?.show()
//        }
        var paintThickPopup = ShareScreenPopup(
            tx_business_share,
            R.layout.tx_layout_popup_sharescreen
        )
        paintThickPopup.setOnCheckDialogListener(object : onShareWhiteBroadDialogListener {
            override fun onCheckFileWhiteBroad() {
                if (null == TXSdk.getInstance().onFriendBtListener) {
                    showMessage( IBaseView.MessageType.MESSAGETYPE_FAIL,"该方法暂未注册")
                } else {
                    TXSdk.getInstance().onFriendBtListener.onSuccess()
                }
            }

            override fun onCheckBroad() {

                mPresenter?.setShareStatus(true, null, null)
            }

        })
        paintThickPopup?.setBgColor(
            ContextCompat.getColor(
                this@VideoActivity,
                R.color.tx_color_80_black
            )
        );
        paintThickPopup?.setArrowOffsetXDp(36)
        paintThickPopup?.show()
    }

    override fun showShareDialog() {
        TxShareDialog.Builder(this)
            .setListener(object : onShareDialogListener {
                override fun onConfirmWx() {
                    mPresenter?.requestWX()
                }

                override fun onConfirmFd() {
                    //
                    if (null == TXSdk.getInstance().onFriendBtListener) {
                        showMessage("该方法暂未注册")
//                        TXSdk.getInstance().onFriendBtListener.onFail(60001,"该方法暂未注册")
                    } else {
                        TXSdk.getInstance().onFriendBtListener.onSuccess()
                        TXSdk.getInstance().share = true
                        //暂时离开
//                        skipCaller()
                    }

                }

                override fun onConfirmMSG() {
                    mPresenter?.sendMsg()
                }

            })
            .showLand(requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            .show()

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
            showWhiteBroad(true)
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
            mPresenter?.getTRTCRemoteUserManager()
                ?.addRemoteUser(userId, entity?.userName, streamType)
            mTxRemoteUserDialogBuilder?.notifyDataSetChanged()
            if (bigScreen) {
//                changeBigScreenViewName(
//                    entity!!.userName,
//                    entity.userRole,
//                    entity.userRoleIconPath
//                )
            } else {
                //todo
                trtc_video_view_layout!!.notifyItemChangedPld(
                    mPresenter?.getMemberEntityList()!!.indexOf(
                        entity
                    ), NAME_CHANGE
                )
            }
            trtc_video_view_layout!!.notifyItemChangedPld(
                mPresenter?.getMemberEntityList()!!.indexOf(
                    entity
                ), NAME_CHANGE
            )
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

    var mTxRemoteUserDialogBuilder: TxRemoteUserDialog.Builder? = null
    var mTxRemoteUserDialog: TxBaseDialog? = null

    override fun handleMemberListView() {
        mTxRemoteUserDialogBuilder = TxRemoteUserDialog.Builder(this)

        mTxRemoteUserDialog = mTxRemoteUserDialogBuilder!!
            .setRemoteUser(mPresenter?.getAllMemberEntityList())
            .showLand(
                requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            )
            .setListener(object : RemoteUserListView.RemoteUserListCallback {
                override fun onFinishClick() {
//                    hideRemoteUserListView(true)
                }

                override fun onMuteAllAudioClick() {
                    //
                    TxMessageDialog1.Builder(this@VideoActivity)
                        .setMessage("所有参会人员将被静音")
                        .setConfirm("确定")
                        .setCancel("取消")
                        .setListener {
                            //
                            mPresenter?.sendGroupMessage(
                                mPresenter?.setIMTextData(IMkey.MUTEAUDIO)!!.put(
                                    IMkey.USERS,
                                    mPresenter?.setAllVideoStatusMemberToJSON(false, isMute = true)
                                )
                                    .toString()
                            )
                            mPresenter?.setSoundStatus(false)
                            //点击全体静音
                            selectAudioBtn(true)
                        }
                        .show()
                    //showTimerDialog("4", 1, 1, 1, 1)
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
                    selectAudioBtn(false)
                }

                override fun onMuteAllVideoClick() {
                    showShareDialog()
                }

                override fun onMuteAudioClick(memberEntity: MemberEntity) {
                    if (!memberEntity.isHost) {
                        showChangeUserStateDialog(memberEntity)
                    }
                }

                override fun onMuteVideoClick(memberEntity: MemberEntity) {
                    if (!memberEntity.isHost) {
                        showChangeUserStateDialog(memberEntity)
                    }

                }

            }).create()
        //判断当前是横屏，
        //成员管理在最右边


        mTxRemoteUserDialog?.show()

    }

    override fun selectAudioBtn(isSelect: Boolean) {
        mTxRemoteUserDialogBuilder?.selectAudioBtn(isSelect)
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

    }

    override fun showPersonWhiteBroad(isShow: Boolean) {
        if (isShow) {
            mPresenter?.setRoomShareStatus(true)
//            if (board_view_container?.visibility == View.GONE) {
//                mPresenter?.isBroad = true
//                ll_board_business?.visibility = View.VISIBLE
//                board_view_container?.visibility = View.VISIBLE
//                checkBigVideoToFirstSmallVideo(true)
//
//            } else {
//                mBoard?.reset()
//            }

        } else {
            mPresenter?.isBroad = false
            mPresenter?.setRoomShareStatus(false)
//            if (board_view_container?.visibility == View.VISIBLE) {
//                ll_board_business?.visibility = View.GONE
//                board_view_container?.visibility = View.GONE
//                checkSmallVideoToBigVideo(true)
//
//            } else {
//
//            }

        }
    }


    fun showWBroad(isShow: Boolean) {
//        if (isShow) {
//            if (board_view_container?.visibility == View.GONE) {
//                mPresenter?.isBroad = true
//                ll_board_business?.visibility = View.VISIBLE
//                board_view_container?.visibility = View.VISIBLE
//                checkBigVideoToFirstSmallVideo(true)
//            } else {
//                mBoard?.reset()
//            }
//
//        } else {
//            mPresenter?.isBroad = false
//            if (board_view_container?.visibility == View.VISIBLE) {
//                ll_board_business?.visibility = View.GONE
//                board_view_container?.visibility = View.GONE
//                checkSmallVideoToBigVideo(true)
//            } else {
//
//            }
//
//        }

    }

    override fun showWhiteBroad(isShow: Boolean) {

        if (isShow) {
            mPresenter?.isBroad = true
            mPresenter?.sendGroupMessage(
                mPresenter?.setIMTextData(IMkey.SHAREWHITEBOARD)
                    ?.put(IMkey.SHAREUSERID, mPresenter?.getSelfUserId()).toString()
            )
            mPresenter?.setRoomShareStatus(true)
            skipToBoardPage("0")
        } else {
            mPresenter?.isBroad = false
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
        TICManager.getInstance().unInit()
        TRTCCloudManager.sharedInstance().destroy()
        TUIBarragePresenter.sharedInstance().destroyPresenter()
        TUIBarragePresenter.sharedInstance().clearList()
        EasyFloat.hide(this)
        paintColorPostion = 0
        paintSizeIntPostion = 1
        textColorIntPostion = 0
        textSizeIntPostion = 1
        unregisterReceiver(broadcastReceiver)
        unregisterReceiver(mHeadSetReceiver)
        super.onDestroy()
    }

    fun hideBoardTools() {

    }

    fun showBoardTools() {

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
        showMessage(IBaseView.MessageType.MESSAGETYPE_NOTIP, message)
    }

    override fun showMessage(type: IBaseView.MessageType, message: String) {
        val view = layoutInflater.inflate(R.layout.tx_layout_toast, null)
        val tv_message1 = view.findViewById<TextView>(R.id.tv_message)
        val iv_status1 = view.findViewById<ImageView>(R.id.iv_status)
        tv_message1.text = message
        when (type) {
            IBaseView.MessageType.MESSAGETYPE_NOTIP -> {
                iv_status1.visibility = View.GONE
            }
            IBaseView.MessageType.MESSAGETYPE_SUCCESS -> {
                iv_status1.visibility = View.VISIBLE
                iv_status1.setImageResource(R.drawable.tx_icon_green_gou)
            }
            IBaseView.MessageType.MESSAGETYPE_FAIL -> {
                iv_status1.visibility = View.VISIBLE
                iv_status1.setImageResource(R.drawable.tx_icon_message_fail)
            }
        }



        ToastUtils.setGravity(Gravity.CENTER, 0, 0)
        ToastUtils.showCustomShort(view)
    }


    override fun modifyExitBt() {
        trtc_ib_back.text = "退出"
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

    //实时监听耳机状态


    inner class HeadsetDetectReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (Intent.ACTION_HEADSET_PLUG == action) {
                if (intent.hasExtra("state")) {
                    val state = intent.getIntExtra("state", 0)

                    if (state == 1) {
                        TxLogUtils.i("onReceive: 插入耳机")
                        autoCheckAudioHand()
                    } else if (state == 0) {
                        TxLogUtils.i("onReceive: 拔出耳机")
                        autoCheckAudioHand()
                    }

                }
            } else if (BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {
                TxLogUtils.i(action)
                var adapter = BluetoothAdapter.getDefaultAdapter()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    var state = adapter.getProfileConnectionState(BluetoothProfile.HEADSET);
                    if (BluetoothProfile.STATE_CONNECTED == state) {
                        TxLogUtils.i("onReceive: 插入蓝牙耳机")
                        autoCheckAudioHand()
                    }
                    if (BluetoothProfile.STATE_DISCONNECTED == state) {
                        TxLogUtils.i("onReceive: 拔出蓝牙耳机")
                        autoCheckAudioHand()
                    }
                }
            }

        }
    }

    private var mHeadSetReceiver: HeadsetDetectReceiver? = null

    //注册监听耳机插入和拔出的广播
    private fun regeistHeadsetReceiver() {
        mHeadSetReceiver = HeadsetDetectReceiver()
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_HEADSET_PLUG)
        filter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED)
        registerReceiver(mHeadSetReceiver, filter)
    }

    var currentHeadsetType = CheckHeadSetSUtils.HeadType.NONE

    override fun autoCheckAudioHand() {


        //进入房间后，判断需要切换成外放还是耳机类型，判断当前是外放还是耳机模式
        //在有线耳机先连接的情况下，蓝牙连接了，自动切到蓝牙耳机
        //在蓝牙耳机先连接的情况下，有线耳机连接了，会自动切到有线耳机
        //有线耳机不能切换
        //蓝牙耳机可以切换
        //会议时，检测耳机的状态，逻辑跟上面一样
        val checkHeadSetSUtils = CheckHeadSetSUtils();
        val headSetStatus = checkHeadSetSUtils.getHeadSetStatus(this)
        when (headSetStatus) {
            CheckHeadSetSUtils.HeadType.Only_WiredHeadset -> {
                //有线耳机连接
                switchAudioHand(false)
                currentHeadsetType = CheckHeadSetSUtils.HeadType.Only_WiredHeadset
                TxLogUtils.i("autoCheckAudioHand---Only_WiredHeadset")
            }
            CheckHeadSetSUtils.HeadType.Only_bluetooth -> {
                //蓝牙耳机连接
                switchAudioHand(false)
                currentHeadsetType = CheckHeadSetSUtils.HeadType.Only_bluetooth
                TxLogUtils.i("autoCheckAudioHand---Only_bluetooth")
            }
            CheckHeadSetSUtils.HeadType.WiredHeadsetAndBluetooth -> {
                switchAudioHand(false)
                currentHeadsetType = CheckHeadSetSUtils.HeadType.WiredHeadsetAndBluetooth
                TxLogUtils.i("autoCheckAudioHand---WiredHeadsetAndBluetooth")
            }

            else -> {
                switchAudioHand(true)
                currentHeadsetType = CheckHeadSetSUtils.HeadType.NONE
                TxLogUtils.i("autoCheckAudioHand---NONE")
            }
        }

    }

    fun switchAudioHand(mIsAudioEarpieceMode: Boolean) {
        //有蓝牙设备在的时候，不让切换
        if (mIsAudioEarpieceMode) {
            //切换扬声器
            TRTCCloudManager.sharedInstance().enableAudioHandFree(true)
            tx_business_switchmic.isSelected = false
        } else {
            //切换听筒模式
            TRTCCloudManager.sharedInstance().enableAudioHandFree(false)
            tx_business_switchmic.isSelected = true
        }


    }

    //状态栏覆盖在视频上面，展开缩放不影响视频的宽高
    //3秒无操作
    //点击画面或者音频块区域
    var isHide = false

    /**
     * 隐藏上下栏
     */
    fun hideBar() {
        if (!isHide) {
            val y = ll_title.y.absoluteValue + ll_title.height
            var moveY = -(y)
            var height = DisplayUtils.rejectedNavHeight(this)
            val ll_bottomY = if (ll_bottom.y <= height) {
                height - ll_bottom.y
            } else {
                ll_bottom.y - height
            }
            //todo 悬浮窗的bottom问题
            ll_title.animate().translationYBy(moveY).setDuration(500).start()
            ll_bottom.animate().translationYBy(ll_bottomY).setDuration(500).start()
            iv_switchscreen.animate().translationYBy(100f).setDuration(500).start()
            hideStatusBar()
            rl_barrage_audience.visibility = View.GONE

        } else {
            val y = ll_title.y.absoluteValue
            var moveY = y
            var height = DisplayUtils.rejectedNavHeight(this)
            var ll_bottomY = if (ll_bottom.y >= height) {
                ll_bottom.y -height + ll_bottom.height
            } else {
                val fl = height - ll_bottom.y
                val fl1 = ll_bottom.height - fl
                fl1
            }

            ll_title.animate().translationYBy(moveY).setDuration(500).start()
            ll_bottom.animate().translationYBy((-ll_bottomY).toFloat()).setDuration(500).start()
            iv_switchscreen.animate().translationYBy(-100f).setDuration(500).start()
            showStatusBar()
            startHideBartimer()
            rl_barrage_audience.visibility = View.VISIBLE
        }
        isHide = !isHide
    }

    var showHideBartimer: CountDownTimer? = null
    private fun startHideBartimer() {
        stopHideBartimer()
        showHideBartimer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                TxLogUtils.i("onFinish")
                if (!isHide) {
                    hideBar()
                }

            }

        }
        showHideBartimer?.start()
    }

    private fun stopHideBartimer() {
        showHideBartimer?.cancel()
        showHideBartimer = null
    }

    private var txUserChatDialogBuilder: TxUserChatDialog.Builder? = null

    fun onTxClick(v: View?) {
        val id = v?.id
        if (id == R.id.trtc_ib_back) {
            showExitInfoDialog()
        } else if (id == R.id.iv_switchscreen) {
            //切换屏幕方向
            switchScreen()
        } else if (id == R.id.tx_business_switchmic) {
            //true 扬声器
            TxLogUtils.i("TRTCCloudManager.sharedInstance().mIsAudioEarpieceMode")
            if (CheckHeadSetSUtils.HeadType.Only_bluetooth == currentHeadsetType || CheckHeadSetSUtils.HeadType.Only_WiredHeadset == currentHeadsetType) {
                showMessage(IBaseView.MessageType.MESSAGETYPE_FAIL, "无法切换外放设备")
            } else {
                switchAudioHand(TRTCCloudManager.sharedInstance().mIsAudioEarpieceMode)
            }

        } else if (id == R.id.tv_invite) {
            //邀请好友

            handleMemberListView()

        } else if (id == R.id.tx_business_video) {

            //关闭视频
            if (mPresenter?.isShare!! && mPresenter?.isOwner()!!) {
                showMessage(getString(R.string.tx_str_share))
                return
            }
            //获取当前可用视频
            val videoConfig = ConfigHelper.getInstance().videoConfig
            mPresenter?.muteLocalVideo(videoConfig.isEnableVideo)
            mPresenter?.isCloseVideo = !videoConfig.isEnableVideo
        } else if (id == R.id.tx_business_audio) {
            //开启静音
            val audioConfig = ConfigHelper.getInstance().audioConfig
            mPresenter?.muteLocalAudio(audioConfig.isEnableAudio)
            //获取当前可用音频
        } else if (id == R.id.tx_business_switch) {
            //反转镜头
            switchCamera()
//            startActivity(Intent(this, CheckResourcesActivity::class.java))
//            startActivity(Intent(this,MainActivity::class.java))
        } else if (id == R.id.tx_business_share) {
            //文档共享
            if (tx_business_share.isSelected) {
                //如果选择了，证明是可以点击的
                showChangeBroadModeDialog(mPresenter?.isBroad!!)
            } else {
                //判断共享
                showChangeBroadModeDialog(mPresenter?.isBroad!!)
            }


        } else if (id == R.id.bt_startrecord) {
            //开始录制
            //通过im发送给全体人员授权消息
            //---1.如果有一方拒绝弹出提示,同意之后才可修改录制状态
            //---2.全部都同意了，修改录制状态
            //再次点击重新发送
//            bt_startrecord.isSelected = true
            TxMessageDialog.Builder(this)
                .setMessage(
                    "本次录制需获得全部参会人员授权确\n" +
                            "认后可进行录制，请您确认"
                )
                .setCancel("取消")
                .setConfirm("确认")
                .setListener {
                    //发送消息
                    mPresenter?.setCurrentArrowCount(0)
                    mPresenter?.sendGroupMessage(
                        mPresenter?.setIMTextData(IMkey.startRecordFromHost)!!
                            .put(
                                IMkey.USERID,
                                mPresenter?.getAgentId()
                            ).toString()
                    )
                }
                .show()
        } else if (id == R.id.rl_chat_message) {
            //群聊
            val getmMsgList = displayView?.getmMsgList()
            //获取聊天信息
            txUserChatDialogBuilder =
                TxUserChatDialog.Builder(this)
                    .showLand(requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                    ?.setRemoteUser(
                        getmMsgList as List<MultiItemEntity>?
                    )
                    ?.setListener(object : TxUserChatDialog.TxUserChatDialogif {
                        override fun onFinishClick() {
                        }

                        override fun onSendMsg(msg: String?) {

                            tuiBarbt?.sendView?.sendBarrage(
                                tuiBarbt?.sendView?.createBarrageModel(
                                    msg
                                )
                            )
                        }

                    })
            txUserChatDialogBuilder?.show()

        } else if (id == R.id.bt_more) {
            //点击更多显示聊天按钮
            rl_chat_message.visibility = if (rl_chat_message.visibility == View.VISIBLE) {
                bt_more.isSelected = false
                bt_more.setTextColor(ContextCompat.getColor(this, R.color.tx_color_dadada))
                View.GONE
            } else {
                bt_more.isSelected = true
                bt_more.setTextColor(ContextCompat.getColor(this, R.color.tx_color_e6b980))
                View.VISIBLE
            }

        }
    }

    /**
     * 切换屏幕。对应的视图也要切换
     */
    private fun switchScreen() {
        //判断当前是横屏还是竖屏
        if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            TxLogUtils.i("switchScreen----SCREEN_ORIENTATION_LANDSCAPE")
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        } else if (requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            TxLogUtils.i("switchScreen----SCREEN_ORIENTATION_PORTRAIT")
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        }

    }

    override fun  skipToBoardPage(type:String){
        skipToBoardPage(type,null)
    }
    // 0 纯白板 1 图片 2 视频

    override fun skipToBoardPage(type:String,mFileSdkBean: FileSdkBean?) {

        val intent = Intent(this, BoardViewActivity::class.java)
        intent.putExtra(IntentKey.SERVICEID, mPresenter?.getServiceId())
        intent.putExtra(IntentKey.AGENTID, mPresenter!!.getSelfUserId())
        intent.putExtra(IntentKey.GROUPID, "" + mPresenter?.getTRTCParams()?.roomId)


        if (null !=  mFileSdkBean) {
            when (type) {
                "1" -> {
                    //图片
                    if (null != mFileSdkBean.pics ) {
                        intent.putStringArrayListExtra(
                            IntentKey.BOARDLISTS,
                            (mFileSdkBean.pics as java.util.ArrayList<String>?)!!
                        )
                        intent.putStringArrayListExtra(
                            IntentKey.PICSWORDLISTS,
                            (mFileSdkBean.picsWord as java.util.ArrayList<String>?)!!
                        )
                    }
                }
                else -> {
                    //视频
                    intent.putExtra(
                        IntentKey.VIDEOURL,
                        mFileSdkBean.videoUrl
                    )
                }
            }

        }

        startActivityForResult(intent, VideoCode.SKIPBOARDPAGE_CODE)
    }


    fun restoreBoardTool(index: Int, isShowToolPop: Boolean) {

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
        mPresenter?.joinClassroom(mBoardCallback)
    }

    private var mFileSdkBean : FileSdkBean ?=null
    //展示文件共享逻辑
    override fun  showSharePage(mFileSdkBean: FileSdkBean){
        //传入共享文件需要的数据，前提是
        //如果是图片或者视频链接，进入白板页面
        //如果是同屏链接，这个时候弹出选择人，然后选择同屏文件进行共享
        TxLogUtils.i("mFileSdkBean-------"+mFileSdkBean.toString())
        if (null == mFileSdkBean) {
            showMessage(IBaseView.MessageType.MESSAGETYPE_FAIL,"共享文件数据为空")
        }else{

            when (mFileSdkBean.type) {
                FileType.video -> {
                    this.mFileSdkBean = mFileSdkBean
                    mPresenter?.isBroad = true
                    mPresenter?.sendGroupMessage(
                        mPresenter?.setIMTextData(IMkey.SHAREWHITEBOARD)
                            ?.put(IMkey.SHAREUSERID, mPresenter?.getSelfUserId()).toString()
                    )
                    mPresenter?.setRoomShareStatus(true)
                    skipToBoardPage("2",mFileSdkBean)
                }
                FileType.h5 -> {
                    mPresenter?.addShareUrl(TXSdk.getInstance().agent,
                        mFileSdkBean.h5Name,
                        mFileSdkBean.h5Url,
                        mFileSdkBean.cookie
                    )
                }
                else -> {
                    //图片
                    this.mFileSdkBean = mFileSdkBean
                    mPresenter?.isBroad = true
                    mPresenter?.sendGroupMessage(
                        mPresenter?.setIMTextData(IMkey.SHAREWHITEBOARD)
                            ?.put(IMkey.SHAREUSERID, mPresenter?.getSelfUserId()).toString()
                    )
                    mPresenter?.setRoomShareStatus(true)
                    skipToBoardPage("1",mFileSdkBean)
                }
            }
        }
    }

    override fun resetBoardLayout() {
        removeBoardView()
    }


    fun restoreBoardView() {
        val boardview = mBoard!!.boardRenderView

        val windowHeight = windowWidth!! / 16 * 9
        val layoutParams =
            FrameLayout.LayoutParams(
                windowWidth!!,
                windowHeight
            )

        mBoard?.boardContentFitMode =
            TEduBoardController.TEduBoardContentFitMode.TEDU_BOARD_CONTENT_FIT_MODE_NONE
    }

    override fun startTimer() {
        tx_time.text = "远程会议"
    }

    override fun stopTimer() {

    }


    //显示白板页面
    override fun addBoardView() {
        TxLogUtils.i("txsdk---addBoardView")
        mBoard?.isDrawEnable = false
        mBoard?.boardContentFitMode =
            TEduBoardController.TEduBoardContentFitMode.TEDU_BOARD_CONTENT_FIT_MODE_NONE
        if (mPresenter!!.getRoomShareStatus()) {
            skipToBoardPage("0")
        }
    }


    override fun removeBoardView() {
        if (mBoard != null) {
            val boardview = mBoard!!.boardRenderView
//            if (board_view_container != null && boardview != null) {
//                board_view_container!!.removeView(boardview)
//            }
        }
    }


    override fun showiOSLoading(show: Boolean) {
        val iOSLoadingView: IOSLoadingView =
            findViewById<View>(R.id.iOSLoadingView) as IOSLoadingView
        val ll_loading: LinearLayout =
            findViewById<View>(R.id.ll_loading) as LinearLayout
        if (null != iOSLoadingView) {
            if (show) {
                ll_loading.setVisibility(View.VISIBLE)
                iOSLoadingView.setVisibility(View.VISIBLE)
            } else {
                ll_loading.setVisibility(View.GONE)
                iOSLoadingView.setVisibility(View.GONE)
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
        initBarrage(
            "" + mPresenter?.getTRTCParams()?.roomId,
            mPresenter?.getServiceId()!!
        )
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
        if (!memberEntity.isMuteVideo) {
            showMessage(IBaseView.MessageType.MESSAGETYPE_SUCCESS, "已将该成员摄像头关闭")
        } else {
//            showMessage(IBaseView.MessageType.MESSAGETYPE_SUCCESS, "已将该成员摄像头打开")
        }


        mPresenter?.sendGroupMessage(
            mPresenter?.setIMTextData(IMkey.MUTEVIDEO)!!
                .put(
                    IMkey.USERS,
                    mPresenter?.setMuteVideoMemberToJSON(true, memberEntity.userId)
                ).toString()
        )
    }

    override fun onMuteAudio(memberEntity: MemberEntity) {
        if (!memberEntity.isMuteAudio) {
            showMessage(IBaseView.MessageType.MESSAGETYPE_SUCCESS, "已将该成员静音")
        } else {
//            showMessage(IBaseView.MessageType.MESSAGETYPE_SUCCESS, "已将该成员解除静音")
        }
        mPresenter?.sendGroupMessage(
            mPresenter?.setIMTextData(IMkey.MUTEAUDIO)!!
                .put(
                    IMkey.USERS,
                    mPresenter?.setMuteVideoMemberToJSON(false, memberEntity.userId)
                )
                .toString()
        )
    }

    override fun onMoveOutRomm(memberEntity: MemberEntity) {
        showMessage(IBaseView.MessageType.MESSAGETYPE_SUCCESS, "已将该成员移除会议室")
        mPresenter?.sendGroupMessage(
            mPresenter?.setIMTextData(IMkey.moveOutRoom)
                ?.put(IMkey.USERID, memberEntity.userId).toString()
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
//        val memberEntity = mPresenter!!.getMemberEntityList()[position]
//        val mCuccentmeetingVideoView = memberEntity.meetingVideoView
//        if (mCuccentmeetingVideoView.isSelfView || mPresenter?.isBroad!!) {
//            return
//        }
//        val mCuccentmemberEntity =
//            mPresenter!!.getStringMemberEntityMap()[mCuccentmeetingVideoView.meetingUserId!!]
//        mPresenter?.getStringMemberEntityMap()!!.remove(mCuccentmeetingVideoView.meetingUserId!!)
//
//        //第一个用户不给点击
//        val mCurrentmeetingVideoView = mCuccentmemberEntity?.meetingVideoView
//        val mCuccentmemberUserId = mCuccentmemberEntity?.userId
//        val mCuccentmemberUserName = mCuccentmemberEntity?.userName
//        val mCuccentmemberIsAudioAvailable = mCuccentmemberEntity?.isAudioAvailable
//        val mCuccentmemberIsVideoAvailable = mCuccentmemberEntity?.isVideoAvailable
//        val mCuccentmemberIsShowAudioEvaluation = mCuccentmemberEntity?.isShowAudioEvaluation
//        val mCuccentmemberIsHost = mCuccentmemberEntity?.isHost
//        val mCuccentmemberUserRole = mCuccentmemberEntity?.userRole
//        val mCuccentmemberUserRoleIconPath = mCuccentmemberEntity?.userRoleIconPath
//
//        //大屏幕把当前的video分开
//        if (bigMeetingEntity == null) {
//            bigMeetingEntity = MemberEntity()
//            //异常情况，当前大屏幕没有画面，点击小屏幕切换
//
//        } else {
//            val bigmeetingVideoView = bigMeetingEntity?.meetingVideoView
//            val mBigMeetingUserId = bigMeetingEntity?.userId
//            val mBigMeetingUserName = bigMeetingEntity?.userName
//            val mBigMeetingIsAudioAvailable = bigMeetingEntity?.isAudioAvailable
//            val mBigMeetingIsVideoAvailable = bigMeetingEntity?.isVideoAvailable
//            val mBigMeetingIsShowAudioEvaluation = bigMeetingEntity?.isShowAudioEvaluation
//            val mBigMeetingisHost = bigMeetingEntity?.isHost
//            val mBigMeetingUserRole = bigMeetingEntity?.userRole
//            val mBigMeetingUserRoleIconPath = bigMeetingEntity?.userRoleIconPath
//
//            bigscreen?.removeView(bigmeetingVideoView)
//
//            mCurrentmeetingVideoView?.detach()
//
//            mCurrentmeetingVideoView?.addViewToViewGroup(bigscreen)
//
//            bigmeetingVideoView?.waitBindGroup = mCuccentmeetingVideoView.waitBindGroup
//            bigmeetingVideoView?.detach()
//
//            bigMeetingEntity?.apply {
//                userId = mCuccentmemberUserId
//                userName = mCuccentmemberUserName
//                meetingVideoView = mCurrentmeetingVideoView
//                meetingVideoView.meetingUserId = mCuccentmemberUserId
//                meetingVideoView?.isNeedAttach = true
//                isNeedFresh = true
//                isShowOutSide = true
//                isVideoAvailable = mCuccentmemberIsVideoAvailable!!
//                isAudioAvailable = mCuccentmemberIsAudioAvailable!!
//                isShowAudioEvaluation = mCuccentmemberIsShowAudioEvaluation!!
//                isHost = mCuccentmemberIsHost!!
//                userRole = mCuccentmemberUserRole!!
//                userRoleIconPath = mCuccentmemberUserRoleIconPath!!
//            }
//            mCuccentmemberEntity?.apply {
//                userId = mBigMeetingUserId
//                userName = mBigMeetingUserName
//                meetingVideoView = bigmeetingVideoView
//                meetingVideoView.meetingUserId = mBigMeetingUserId
//                meetingVideoView?.isNeedAttach = true
//                isNeedFresh = true
//                isShowOutSide = false
//                isVideoAvailable = mBigMeetingIsVideoAvailable!!
//                isAudioAvailable = mBigMeetingIsAudioAvailable!!
//                isShowAudioEvaluation = mBigMeetingIsShowAudioEvaluation!!
//                isHost = mBigMeetingisHost!!
//                userRole = mBigMeetingUserRole!!
//                userRoleIconPath = mBigMeetingUserRoleIconPath!!
//            }
//            mCuccentmemberEntity?.meetingVideoView?.refreshParent()
//            TxLogUtils.i(
//                "onSingleClick",
//                "mCuccentmemberEntity?.isVideoAvailable " + mCuccentmemberEntity?.isVideoAvailable
//            )
//            mPresenter?.getTRTCRemoteUserManager()!!.setRemoteFillMode(
//                bigMeetingEntity?.userId,
//                TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG,
//                false
//            )
//
//            mPresenter?.getTRTCRemoteUserManager()!!.setRemoteFillMode(
//                mCuccentmemberEntity?.userId,
//                TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG,
//                true
//            )
//
//            mPresenter!!.getStringMemberEntityMap()
//                .put(mCuccentmemberEntity!!.userId, mCuccentmemberEntity!!)
//
//            changeBigVideo(bigMeetingEntity!!)
//            //todo
//            trtc_video_view_layout!!.notifyItemChanged(
//                mPresenter?.getMemberEntityList()!!.indexOf(
//                    mCuccentmemberEntity
//                ), VIDEOVIEW_CHANGE
//            )
//
//
//        }
//
//
    }


    override fun checkSmallVideoToBigVideo(isShowToBig: Boolean) {
    }

    override fun changeBigVideo(bigMeetingEntity: MemberEntity) {
    }

    override fun checkBigVideoToFirstSmallVideo(isShowToSmall: Boolean) {

    }

    override fun hideBigNoVideo(isHiden: Boolean) {

    }

    override fun showBroadFileRv(isShow: Boolean) {

    }

    override fun changeBigScreenViewName(text: String, userRole: String, userRoleIconPath: String) {

    }

    override fun changeBigScreenViewVoice(volume: Int) {
    }


    private fun showChangeUserStateDialog(memberEntity: MemberEntity) {
        TxRemoteUserControlDialog.Builder(this)
            .changeLay(memberEntity)
            .setListener(this)
            .showLand(requestedOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
            .show()

    }

    var paintThickPopup: PaintThickPopup? = null


    override fun checkOwenrToBigShareScreen(screenUserId: String) {

    }

    override fun detachBigShareScreen(screenUserId: String) {
        TxLogUtils.i("detachBigShareScreen------")


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
        fileName: String,
        cookie: String
    ) {
        val memberEntityList = RemoteUserConfigHelper.getInstance().getRemoteUserConfigList()
        val memberEntities = ArrayList<MemberEntity>()
        for (i in memberEntityList.indices) {
            val remoteUserConfig = memberEntityList[i]
            val memberEntity = MemberEntity()
            memberEntity.userId = remoteUserConfig.getmUserId()
            memberEntity.userName = remoteUserConfig.userName
            memberEntities.add(memberEntity)
        }
        TxLogUtils.i("memberEntityList" + memberEntities!!.size)
        if (memberEntities.size == 0) {
            showMessage("共享文件失败，会议内暂无其他人员")
            return
        }
        if (memberEntities!!.size == 1) {
            for (i in 0 until memberEntities.size) {
                try {
                    val memberEntity = memberEntities.get(i)
                    val userId = memberEntity.userId
                    TxLogUtils.i("memberEntityList" + userId)
                    if (isSameScreen) {
                        //  跳到
                        mPresenter!!.startShareWeb(
                            webId!!,
                            mPresenter!!.getServiceId(),
                            mPresenter!!.getSelfUserId(),
                            userId!!,
                            name,
                            cookie
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
                            name,
                            cookie
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
            selectPersonDialog?.invalidateAdapater(memberEntities!!)
        }

    }

    var personDialog: SelectChannelDialog? = null
    override fun showSelectChannelDialog(
        list: JSONArray,
        webId: String,
        url: String,
        name: String,
        fileName: String,
        cookie: String
    ) {
        showPersonDialog(webId!!, url, name, true, list, fileName,cookie)

    }

    override fun getPushWebUrlSuccess(
        webId: String,
        clientUrl: String,
        name: String,
        cookie: String
    ) {
        if (!clientUrl.isEmpty()) {
            showSelectChannelDialog(JSONArray(), webId!!, clientUrl, name, name,cookie)
        } else {
           showMessage("返回的clientUrl为空")
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
//                //todo
//                //判断房间的人数
//                mPresenter!!.setShareWebId(webId)
//                //需要从后台拿到分享url
//                mPresenter!!.getPushWebUrl(
//                    mPresenter!!.getSelfUserId(),
//                    webId!!,
//                    mPresenter!!.getServiceId(),
//                    url,
//                    name,
//                    cookie
//                )

            }

            override fun onConfirm() {
                InputDialog.Builder(this@VideoActivity)
                    .setTitle("上传产品")
                    .setConfirm("确认")
                    .setCancel("取消")
                    .setListener { dialog, name, url ->
                        if (name.isNotEmpty() && url.isNotEmpty()) {
//                            mPresenter?.addShareUrl(TXSdk.getInstance().agent, name!!, url!!)
                        } else {
                            if (name.isEmpty()) {
                                showMessage("产品名称不能为空")
                            } else {
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
        fromUserId: String,
        cookie: String
    ) {
        // fromAgent 为true 显示结束共享按钮
        if (webDialog == null) {
            webDialog =
                WebDialog(this,
                        url,
                    cookie
                )
        }
        webDialog?.setOnShareWhiteBroadDialogListener(object : onShareWhiteBroadDialogListener {
            override fun onCheckFileWhiteBroad() {
                //结束共享
                TxMessageDialog.Builder(this@VideoActivity)
                    .setMessage(
                       "请问是否结束共享？"
                    )
                    .setCancel("取消")
                    .setConfirm("确认")
                    .setListener {
                        //发送消息
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
                    .show()
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

    override fun uploadWebUrlSuccess(webId: String, url: String, name: String, cookie: String) {
        //上传完，准备发送
        //todo
        //判断房间的人数
        mPresenter!!.setShareWebId(webId)
        //需要从后台拿到分享url
        mPresenter!!.getPushWebUrl(
            mPresenter!!.getSelfUserId(),
            webId!!,
            mPresenter!!.getServiceId(),
            url,
            name,
            cookie
        )
    }

    var displayView: TUIBarrageDisplayView? = null
    var tuiBarbt: TUIBarrageButton? = null
    private fun initBarrage(groupId: String, serviceId: String) {
        //弹幕发送View
        tuiBarbt = TUIBarrageButton(this, groupId, serviceId)
        //弹幕显示View
        displayView = TUIBarrageDisplayView(this, groupId)

        setBarrage(tuiBarbt as View)
        setBarrageShow(displayView as View)
        tuiBarbt?.sendView?.setBarrageListener(object : ITUIBarrageListener {
            override fun onSuccess(code: Int, msg: String, model: TUIBarrageModel) {
                if (model == null || TextUtils.isEmpty(model.content)) {
                    TxLogUtils.i(
                        "message is null"
                    )
                    return
                }
                //todo 这里是所有弹幕的消息 展示在群聊里面
                TxLogUtils.i("model" + Gson().toJson(model))
                displayView?.receiveBarrage(model)

            }

            override fun onFailed(code: Int, msg: String) {}
        })
        displayView?.setReceiveBarrageListener(object : ITUIBarrageListener {
            override fun onSuccess(code: Int, msg: String?, model: TUIBarrageModel?) {
                //收到消息

                rl_barrage_show_audience.visibility = View.VISIBLE
                startShowBarrageTimer()

                if (null != txUserChatDialogBuilder) {
                    txUserChatDialogBuilder?.notifyDataSetChanged()
                }
            }

            override fun onFailed(code: Int, msg: String?) {

            }

        })

    }

    var showBartimer: CountDownTimer? = null
    private fun startShowBarrageTimer() {
        stopShowBarrageTimer()
        showBartimer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                TxLogUtils.i("onFinish")
                rl_barrage_show_audience.visibility = View.GONE
//                stopShowBarrageTimer()
            }

        }
        showBartimer?.start()
    }

    private fun stopShowBarrageTimer() {
        showBartimer?.cancel()
        showBartimer = null
    }

    /**
     * 布局发送弹幕按钮
     */
    private fun setBarrage(view: View) {
        val params: RelativeLayout.LayoutParams =
            RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        rl_barrage_audience.addView(view, params)
    }

    /**
     * 布局弹幕展示布局
     */
    private fun setBarrageShow(view: View) {
        val params = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        rl_barrage_show_audience.addView(view, params)
    }

    /**
     * 所有成员点击同意
     */
    override fun allowstartrecord() {
        //todo
        bt_startrecord.isSelected = true
    }

    override fun refuseStartRecord() {
        TxMessageDialog.Builder(this)
            .setMessage(
                "有人拒绝了您的录制,\n" +
                        "是否继续会议?"
            )
            .setCancel("取消")
            .setConfirm("确定")
            .setListener(object : TxMessageDialog.OnListener {

                override fun onCancel(dialog: TxBaseDialog?) {
                    //todo结束会议

                }

                override fun onConfirm(dialog: TxBaseDialog?) {
                }

            })
            .show()
    }

    override fun showBg(bgUrl: String) {
        trtc_video_view_layout.showBg(bgUrl)
//        if (mPresenter!!.getRoomShareStatus()) {
//            skipToBoardPage("0")
//        }
    }
}