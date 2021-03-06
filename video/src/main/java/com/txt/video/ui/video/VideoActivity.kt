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
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.txt.video.common.utils.CheckDoubleClickListener
import com.txt.video.common.utils.DatetimeUtil
import com.txt.video.net.bean.*
import com.txt.video.net.utils.TxLogUtils
import com.txt.video.net.utils.TxLogUtils.i
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
import com.txt.video.ui.video.VideoMode.VIDEOMODE_HORIZONTAL
import com.txt.video.ui.video.VideoMode.VIDEOMODE_VERTICAL
import com.txt.video.ui.video.remote.RemoteUserListView
import com.txt.video.ui.weight.PicQuickAdapter
import com.txt.video.ui.weight.dialog.*
import com.txt.video.ui.weight.easyfloat.EasyFloat
import com.txt.video.ui.weight.easyfloat.interfaces.OnInvokeView
import com.txt.video.ui.weight.view.BigScreenView
import com.txt.video.ui.weight.view.ScreenView
import kotlinx.android.synthetic.main.tx_activity_video.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by JustinWjq
 * @date 2020-12-23.
 * description???????????????
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

    private var mListRv: RecyclerView? = null
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
        //???view??????????????????????????????
        // ?????????layoutmannger??????????????????????????????
        showInviteBt(isShow = true, noRemoterUser = true)
        mViewVideo = mPresenter!!.getMemberEntityList()[0].meetingVideoView
        var pageLayoutManager: RecyclerView.LayoutManager? = null
        if (VIDEOMODE_HORIZONTAL == TXSdk.getInstance().roomControlConfig.videoMode) {
            pageLayoutManager = MeetingPageLinearLayoutManager(this)
            pageLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        } else {
            pageLayoutManager = MeetingPageMultiChatLayoutManager(
                2,
                2,
                MeetingPageMultiChatLayoutManager.VERTICAL
            )
        }
        if (pageLayoutManager is MeetingPageMultiChatLayoutManager) {
            pageLayoutManager.setPageListener(object : PageListener {
                override fun onItemVisible(fromItem: Int, toItem: Int) {
                    TxLogUtils.i("onItemVisible : fromItem--${fromItem}--toItem${toItem}")
                    if (fromItem == 0) {
                        processSelfVideoPlay()
                        mPresenter?.processVideoPlay(1, toItem)
                        if (0 != toItem) {
                            TxLogUtils.i(
                                "MeetingVideoView",
                                "onItemVisible : fromItem--${fromItem}--toItem${toItem}"
                            )
                            //todo layoutmannger ???????????????????????????width height ??????0


                        }

                    } else {
                        mPresenter?.processVideoPlay(fromItem, toItem)
                    }
                }

                override fun onLayoutCompleted(count: Int) {
                    if (count >= 2 ) {
                        mPresenter?.getAllMemberEntityList()?.forEach {
                            TxLogUtils.i(
                                "onLayoutCompleted--${count}--${it?.meetingVideoView?.playVideoView?.width}"

                            )
                            if (0 == it?.meetingVideoView?.playVideoView?.width){
                                Handler().postDelayed({ mMemberListAdapter?.notifyItemChanged(0, NAME_CHANGE)},2000)
                            }
                        }

                    }
                }

                override fun onPageSelect(pageIndex: Int) {
                }

                override fun onPageSizeChanged(pageSize: Int) {

                }

            })
        } else if (pageLayoutManager is MeetingPageLinearLayoutManager) {

            pageLayoutManager.setPageListener(object : PageListener {
                override fun onItemVisible(fromItem: Int, toItem: Int) {
                    TxLogUtils.i("onItemVisible : fromItem--${fromItem}--toItem${toItem}")
                    if (fromItem == 0) {
                        processSelfVideoPlay()
                        mPresenter?.processVideoPlay(1, toItem)
                    } else {
                        mPresenter?.processVideoPlay(fromItem, toItem)
                    }
                }

                override fun onLayoutCompleted(count: Int) {

                }

                override fun onPageSelect(pageIndex: Int) {
                }

                override fun onPageSizeChanged(pageSize: Int) {

                }

            })
        }



        mListRv = trtc_video_view_layout

        mMemberListAdapter =
            MemberListAdapter(
                this,
                mPresenter?.getMemberEntityList(),
                object : MemberListAdapter.ListCallback {
                    override fun onItemClick(position: Int) {
                        TxLogUtils.i("onItemClick--${position}")
                        if (VIDEOMODE_HORIZONTAL == TXSdk.getInstance().roomControlConfig.videoMode) {
                            if (position != 0) {
                                checkItemToBig(position)
                            }
                        } else {
                            if (position != 0) {

                            }
                            val memberEntity = mPresenter!!.getMemberEntityList()[position]
                            checkOwenrToBigScreen(memberEntity.userId)
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
        if (null != mPresenter?.getMemberEntityList()) {
            if (0 != mPresenter?.getMemberEntityList()!!.size) {
                if (null != mPresenter!!.getMemberEntityList().get(0)) {
                    if (!mPresenter!!.getMemberEntityList()[0].isShowOutSide) {
                        val meetingVideoView: MeetingVideoView =
                            mPresenter!!.getMemberEntityList()[0].meetingVideoView
                        meetingVideoView.refreshParent()
                    }

                }
            }
        }

    }


    override fun onSnapshotLocalView(bmp: Bitmap?) {
    }

    override fun onMuteLocalAudio(isMute: Boolean) {
        TxLogUtils.i("onMuteLocalAudio$isMute")
        tx_business_audio.isSelected = isMute

        if (null != bigMeetingEntity&& bigMeetingEntity?.userId.equals(mPresenter?.getTRTCParams()!!.userId)) {
            bigMeetingEntity?.isShowAudioEvaluation = !isMute
            if (TXSdk.getInstance().roomControlConfig.videoMode==VideoMode.VIDEOMODE_VERTICAL){
                bigScreenVerticalView?.changeBigScreenViewVoice(!isMute,0)
            }else{
                bigscreen?.changeBigScreenViewVoice(!isMute,0)
            }

        } else{
            val entity =
                mPresenter!!.getStringMemberEntityMap()[mPresenter?.getTRTCParams()!!.userId]
            if (null != entity) {
                val indexOf = mPresenter?.getMemberEntityList()!!.indexOf(
                    entity
                )
                if (indexOf >= 0) {
                    entity?.isShowAudioEvaluation = !isMute


                }
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

        if (null != bigMeetingEntity&& bigMeetingEntity?.userId.equals(mPresenter?.getTRTCParams()!!.userId)) {
            bigMeetingEntity?.isMuteVideo = isMute
            if (TXSdk.getInstance().roomControlConfig.videoMode==VideoMode.VIDEOMODE_VERTICAL){
                //?????????view???????????????
                bigScreenVerticalView?.closeVideo(isMute,bigMeetingEntity?.userHeadPath)
            }else{
                bigscreen?.closeVideo(isMute,bigMeetingEntity?.userHeadPath)
            }
        }else{
            val entity =
                mPresenter!!.getStringMemberEntityMap()[mPresenter?.getTRTCParams()!!.userId]
            entity?.isMuteVideo = isMute
            mMemberListAdapter!!.notifyItemChanged(
                mPresenter?.getMemberEntityList()!!.indexOf(
                    entity
                ), VIDEO_CLOSE
            )
        }

        mScreenView?.muteVideo(isMute)
    }

    override fun onStartLinkMic() {
    }


    override fun onAudioVolumeEvaluationChange(enable: Boolean) {

    }

    override fun onFirstVideoFrame(userId: String?, streamType: Int, width: Int, height: Int) {
    }

    //???????????????
    override fun onUserVideoAvailable(userId: String?, available: Boolean) {
        TxLogUtils.i("txsdk---onUserVideoAvailable---$userId+available$available")
        onUserVideoChange(userId!!, TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG, available)

    }

    override fun onExitRoom(reason: Int) {
    }

    override fun onScreenCaptureStarted() {
        TxLogUtils.i("onScreenCaptureStarted--")
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
        if (null != bigMeetingEntity&& bigMeetingEntity?.userId.equals(mPresenter?.getTRTCParams()!!.userId)) {
            TxLogUtils.i("onScreenCaptureStarted--bigscreenview")
            if (TXSdk.getInstance().roomControlConfig.videoMode==VideoMode.VIDEOMODE_VERTICAL){
                bigScreenVerticalView.showScreenIcon(true)
            }else{
                bigscreen.showScreenIcon(true)
            }

            bigMeetingEntity?.isScreen = true
        }else{

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
        if (null != bigMeetingEntity&& bigMeetingEntity?.userId.equals(mPresenter?.getTRTCParams()!!.userId)) {
            if (TXSdk.getInstance().roomControlConfig.videoMode==VideoMode.VIDEOMODE_VERTICAL){
                bigScreenVerticalView.showScreenIcon(false)
            }else{
                bigscreen.showScreenIcon(false)
            }
            bigMeetingEntity?.isScreen = false
        }else{

        }
        if (mPresenter?.isCloseVideo!!) {
            mPresenter?.getTRTCCloudManager()!!.muteLocalVideo(true)
            entity?.isScreen = false
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

        //?????????????????????????????????????????????
        //??????????????????????????????
        //????????????????????????
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

            if (mPresenter?.isBroad!! || mPresenter?.getAllMemberEntityList()!!.size > 1 || VIDEOMODE_VERTICAL == TXSdk.getInstance().roomControlConfig.videoMode) {

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
                //???????????????
                bigMeetingEntity = entity
                mPresenter?.getRoomInfo(
                    remoteUserId,
                    TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG,
                    false,
                    bigMeetingEntity,
                    true
                )
                if (TXSdk.getInstance().roomControlConfig.videoMode==VideoMode.VIDEOMODE_VERTICAL){
                    bigScreenVerticalView?.visibility = View.VISIBLE
//                    bigScreenVerticalView.closeVideo(true)
                }else{
                    bigscreen?.visibility = View.VISIBLE
//                    bigscreen.closeVideo(true)

                }



            }
            if (!mPresenter?.getTRTCParams()!!.userId?.equals(remoteUserId)!!) { //???????????????

                tx_icon_invite.visibility = View.GONE
                showInviteBt(isShow = true, noRemoterUser = false)
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

        // ????????????????????????View
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
            if (VIDEOMODE_VERTICAL == TXSdk.getInstance().roomControlConfig.videoMode) {
                if (bigMeetingEntity != null && userId == bigMeetingEntity?.userId) {
                    bigScreenVerticalView?.visibility = View.GONE
                    bigScreenVerticalView.closeVideo(false)
                    bigMeetingEntity = null
                }
            }else{
                if (bigMeetingEntity != null && userId == bigMeetingEntity?.userId) {
                    bigscreen?.visibility = View.GONE
                    bigscreen.closeVideo(false)
                    bigMeetingEntity = null
                }
            }

        } else {

            if (VIDEOMODE_VERTICAL == TXSdk.getInstance().roomControlConfig.videoMode) {
                if (bigMeetingEntity != null && userId == bigMeetingEntity?.userId) {
                    bigScreenVerticalView?.visibility = View.GONE
                    bigScreenVerticalView.closeVideo(false)
                    bigMeetingEntity = null
                    if (!mPresenter?.isBroad!! && mPresenter?.getAllMemberEntityList()!!.size == 1) {
                        checkSmallVideoToBigVideo(true)
                    }

                }
            } else {
                if (bigMeetingEntity != null && userId == bigMeetingEntity?.userId) {
                    bigscreen?.visibility = View.GONE
                    bigscreen.closeVideo(false)
                    bigMeetingEntity = null
                }
            }

        }

        if (mPresenter?.getAllMemberEntityList()!!.size == 0) {
//            ???????????????????????? ??????
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
            TxLogUtils.i("txsdk---onEnterRoom-----??????$elapsed ??????")
            mPresenter?.startRecord()
            // ??????????????????
            mPresenter?.getTRTCRemoteUserManager()!!.updateCloudMixtureParams()
            showActivityFloat()
        } else {
            TxLogUtils.i("txsdk---onEnterRoom-----??????$elapsed ??????")
            showMessage(getString(R.string.tx_joinroom_error))
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
                screenMeetingEntity?.userId,
                TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG,
                screenMeetingEntity?.meetingVideoView?.playVideoView, false
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
                    val meetingVideoView = bigMeetingEntity?.meetingVideoView
                    meetingVideoView?.detach()
                    if (TXSdk.getInstance().roomControlConfig.videoMode == VIDEOMODE_VERTICAL) {
                        bigScreenVerticalView?.visibility = View.VISIBLE
                        bigScreenVerticalView.closeVideo(false)

                        meetingVideoView?.addViewToViewGroup(bigScreenVerticalView.bigScreenView)
                    } else {
                        bigscreen?.visibility = View.VISIBLE
                        bigscreen.closeVideo(false)

                        meetingVideoView?.addViewToViewGroup(bigscreen.bigScreenView)
                    }

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




        } else {
            if (TXSdk.getInstance().roomControlConfig.videoMode == VideoMode.VIDEOMODE_VERTICAL) {
                if (bigMeetingEntity != null && bigMeetingEntity?.userId!! == userId && !mPresenter?.isBroad!!) {
                    bigScreenVerticalView?.visibility = View.VISIBLE
                    bigScreenVerticalView.closeVideo(true,bigMeetingEntity?.userHeadPath)

                    val meetingVideoView = bigMeetingEntity?.meetingVideoView
                    meetingVideoView?.detach()
                    mPresenter?.getTRTCRemoteUserManager()
                        ?.remoteUserVideoUnavailable(
                            userId,
                            streamType
                        )
                } else {
                    if (null != entity) {
                        mMemberListAdapter!!.notifyItemChanged(
                            mPresenter?.getMemberEntityList()!!.indexOf(
                                entity
                            )
                        )
                    }
                }

            } else {
                if (null != entity) {
                    mMemberListAdapter!!.notifyItemChanged(
                        mPresenter?.getMemberEntityList()!!.indexOf(
                            entity
                        )
                    )
                } else {
                    if (bigMeetingEntity != null && bigMeetingEntity?.userId!! == userId && !mPresenter?.isBroad!!) {
                        bigscreen?.visibility = View.VISIBLE
                        bigscreen.closeVideo(true,bigMeetingEntity?.userHeadPath)
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



        if (TXSdk.getInstance().roomControlConfig.videoMode == VIDEOMODE_VERTICAL) {
            if (bigMeetingEntity != null && userId == bigMeetingEntity?.userId) {
                bigMeetingEntity?.apply {
                    isAudioAvailable = available
                    isShowAudioEvaluation = available
                }
                var volume = if (!available) {
                    -1
                } else {
                    50
                }
                changeBigScreenViewVoice(volume)
            } else {
                val entity = mPresenter!!.getStringMemberEntityMap()[userId]
                if (entity != null) {
                    entity.isAudioAvailable = available
                    entity.isShowAudioEvaluation = available
                    mMemberListAdapter!!.notifyItemChanged(
                        mPresenter?.getMemberEntityList()!!.indexOf(entity),
                        MemberListAdapter.VOLUME
                    )
                    //????????????????????????
                }
            }
        } else {
            val entity = mPresenter!!.getStringMemberEntityMap()[userId]
            if (entity != null) {
                entity.isAudioAvailable = available
                entity.isShowAudioEvaluation = available
                mMemberListAdapter!!.notifyItemChanged(
                    mPresenter?.getMemberEntityList()!!.indexOf(entity),
                    MemberListAdapter.VOLUME_SHOW
                )
                //????????????????????????
            } else {
                if (bigMeetingEntity != null && userId == bigMeetingEntity?.userId) {
                    bigMeetingEntity?.apply {
                        isAudioAvailable = available
                        isShowAudioEvaluation = available
                    }
                    var volume = if (!available) {
                        -1
                    } else {
                        50
                    }
                    changeBigScreenViewVoice(volume)
                }

            }
        }


    }

    override fun onStatistics(statics: TRTCStatistics?) {
    }

    override fun onConnectOtherRoom(userID: String?, err: Int, errMsg: String?) {
    }

    //?????????????????????????????????
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
        //-1308 ??????????????????
        TxLogUtils.i("txsdk---onError----???$errCode----???$errMsg")
        if (errCode == -1308) {
            mPresenter?.isShare = false
            showMessage(getString(R.string.tx_showscreen_error))
            mPresenter?.stopScreenCapture()
            mPresenter?.setScreenStatus(false)
        }

    }

    override fun onRecvSEIMsg(userId: String?, data: ByteArray?) {
    }


    /**
     * ??????????????????
     *
     * @param msg     ????????????
     * @param isError true?????????????????????????????? false???????????????????????????????????????
     */
    var dialog: ExitDialog? = null
    private fun showExitInfoDialog() {

        if (mPresenter?.isOwner()!!) {

        } else {
            //??????????????????????????????????????????
            if (tx_business_share.isSelected) {
                showMessage("???????????????????????????????????????")
                return
            }
            if (tx_business_screen.isSelected) {
                showMessage("???????????????????????????????????????")
                return
            }
        }

        if (dialog == null) {
            var cancelStr = ""
            var confirmStr = ""
            var contentStr = ""
            if (mPresenter?.isOwner()!!) {
                 cancelStr = "????????????"
                 confirmStr = "????????????"
                 contentStr = "????????????????????????????????????????"

            } else {
                 cancelStr = "??????"
                 confirmStr = "??????"
                 contentStr = "????????????????????????????"

            }
            dialog =   ExitDialog(
                this,
                cancelStr,
                "????????????",
                confirmStr,
                contentStr
            )

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
                        mPresenter?.unitConfig(needEndUser = true)
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
                        //todo ??????presenter
                        mPresenter?.sendGroupMessage(
                            mPresenter?.setIMTextData(IMkey.MUTEAUDIO)!!.put(
                                IMkey.USERS,
                                mPresenter?.setAllVideoStatusMemberToJSON(false, isMute = true)
                            )
                                .toString()
                        )
                        mPresenter?.setSoundStatus(false)
                        //??????????????????
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
                //????????????
                mTimerdialog?.apply {
                    setTv_content("???????????????????????????${maxRoomTime / 60}??????")
                    setOnlyConfirm("?????????")
                }

            }
            "2" -> {
                //????????????
                mTimerdialog?.apply {
                    setTv_content("????????????????????????${notifyExtendTime / 60}?????????\n ????????????????????????${extendRoomTime / 60}?????????")
                    setTv_confirm("????????????")
                    setTv_cancel("????????????")
                }
            }
            "3" -> {
                //????????????

                mTimerdialog?.apply {
                    setTv_content("??????????????????????????????\n ????????????????????????${extendRoomTime / 60}?????????")
                    setTv_confirm("????????????")
                    startCommonTimer(notifyEndTime, "(???${notifyEndTime}s?????????????????????????????????)")
                }
            }
            "4" -> {
                mTimerdialog?.apply {
                    setTv_content("???????????????????????????????????????????????????")
                    setTv_confirm("??????")
                    setTv_cancel("??????")
                }
            }
            else -> {
            }
        }
    }


    override fun showBroad() {
        ll_board_business?.visibility = View.GONE

        if (mPresenter?.getAllMemberEntityList()!!.size == 0) {
            //??????????????????
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
                //????????????
                mPresenter?.chooseFile(true)
            }

            override fun onItemClick(url: String?, images: MutableList<String>) {
                //??????????????????
                mPresenter?.setShareStatus(true, url, images)

            }

            override fun onConfirm() {
                //????????????
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
                        showMessage("?????????????????????")
//                        TXSdk.getInstance().onFriendBtListener.onFail(60001,"?????????????????????")
                    } else {
                        TXSdk.getInstance().onFriendBtListener.onSuccess(
                            "" + mPresenter?.getRoomId(),
                            mPresenter?.getServiceId()!!,
                            mPresenter?.getSelfUserId()!!
                        )
//                        TXSdk.getInstance().share = true
                        //????????????
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
        //????????????
        mPresenter?.update()
    }

    override fun uploadFileFail(msg: String) {
        TxLogUtils.i("txsdk---uploadFile:onFail")
        runOnUiThread { showMessage(msg) }
    }

    var api: IWXAPI? = null

    override fun sendReq(req: BaseReq) {

        api?.sendReq(req)
    }

    override fun startSoundSuccess() {
    }

    //??????????????????
    override fun startShareSuccess(
        shareStatus: Boolean,
        url: String?,
        images: MutableList<String>?
    ) {
        if (shareStatus) {
            if (null != images) {
                //??????????????????
                if (VIDEOMODE_HORIZONTAL == TXSdk.getInstance().roomControlConfig.videoMode) {
                    if (images.isEmpty()) {
                        showMessage(getString(R.string.tx_toast_transcoding_retry))
                    } else {
                        hideBoardTools()
                        showWhiteBroad(true)
                        tx_business_share.isSelected = true
                        changeFileDialog?.dismiss()
                        mBoard?.addImagesFile(images)
                        rl_rv.visibility = if (images.size > 1) {
                            picQuickAdapter?.setNewData(images)
                            View.VISIBLE
                        } else {
                            View.GONE
                        }
                    }

                } else {
                    if (images.isEmpty()) {
                        showMessage(getString(R.string.tx_toast_transcoding_retry))
                    } else {
                        hideBoardTools()
                        tx_business_share.isSelected = true
                        changeFileDialog?.dismiss()
                        picQuickAdapter?.setNewData(images)
                        mBoard?.addImagesFile(images)
                        mPresenter?.sendGroupMessage(
                            mPresenter?.setIMTextData(IMkey.SHAREWHITEBOARD)
                                ?.put(IMkey.SHAREUSERID, mPresenter?.getSelfUserId())
                                ?.put(IMkey.SHAREUSERNAME, mPresenter?.getSelfName())
                                .toString()
                        )
                        mPresenter?.setShareUserId(mPresenter?.getSelfUserId()!!)
                        mPresenter?.setRoomShareStatus(true)
                        //????????????id????????????
                        Handler().postDelayed({
                            removeBoardView()
                            skipToBoardPage()
                        }, 1000)

                    }


                }

            } else {
                //???????????????
                //?????????????????????????????????????????????
                tx_business_share.isSelected = true
                if (VIDEOMODE_HORIZONTAL == TXSdk.getInstance().roomControlConfig.videoMode) {
                    boardIdList.clear()
                    hideBoardTools()
                    showWhiteBroad(true)
                    showBroadFileRv(false)
                } else {
                    mPresenter?.sendGroupMessage(
                        mPresenter?.setIMTextData(IMkey.SHAREWHITEBOARD)
                            ?.put(IMkey.SHAREUSERID, mPresenter?.getSelfUserId())
                            ?.put(IMkey.SHAREUSERNAME, mPresenter?.getSelfName())
                            .toString()
                    )
                    mPresenter?.setShareUserId(mPresenter?.getSelfUserId()!!)
                    mPresenter?.setRoomShareStatus(true)
                    removeBoardView()
                    skipToBoardPage()
                }
            }

        } else {

        }

    }

    override fun startShareFail(shareStatus: Boolean) {
        if (shareStatus) {
            showMessage("??????????????????")
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
                TxLogUtils.i("getRoomInfoSuccess",
                    bigMeetingEntity?.userName+""+bigMeetingEntity?.isMuteVideo!!+ bigMeetingEntity?.userHeadPath)
                if (TXSdk.getInstance().roomControlConfig.videoMode == VideoMode.VIDEOMODE_VERTICAL) {
                    bigScreenVerticalView.closeVideo(bigMeetingEntity?.isMuteVideo!!,bigMeetingEntity?.userHeadPath)
                } else {
                    bigscreen.closeVideo(bigMeetingEntity?.isMuteVideo!!,bigMeetingEntity?.userHeadPath)
                }

            } else {
                TxLogUtils.i("getRoomInfoSuccess",entity?.userName+""+entity?.isMuteVideo!!)
                TxLogUtils.i("getRoomInfoSuccess",""+entity?.userHeadPath!!)
                mMemberListAdapter!!.notifyItemChanged(
                    mPresenter?.getMemberEntityList()!!.indexOf(
                        entity
                    ), NAMEANDUSERHEAD_CHANGE
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
        //????????????
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

        if (VIDEOMODE_HORIZONTAL == TXSdk.getInstance().roomControlConfig.videoMode) {
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
        } else {
            TxLogUtils.i("??????????????????")
            mPresenter?.isBroad = false
            if (isShow) {
                showSharePersonTipLayout()
                removeBoardView()
                skipToBoardPage()
            } else {
                showChangeBroadModeDialog(true)
                hideSharePersonTipLayout()
            }


        }


    }

    private fun hideSharePersonTipLayout() {
        rl_screen.visibility = View.GONE
    }

    override fun showSharePersonTipLayout() {
        if (VIDEOMODE_HORIZONTAL != TXSdk.getInstance().roomControlConfig.videoMode) {
            rl_screen.visibility = View.VISIBLE
            tv_shareingname.text = "${mPresenter?.getShareUserName()}???????????????????????????"
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
                        ?.put(IMkey.SHAREUSERID, mPresenter?.getSelfUserId())
                        ?.put(IMkey.SHAREUSERNAME, mPresenter?.getSelfName())
                        .toString()
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
        mBoard!!.uninit()
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
                    if (VIDEOMODE_HORIZONTAL == TXSdk.getInstance().roomControlConfig.videoMode) {
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
                    } else {
                        //??????????????????????????????
                        if (mPresenter?.getRoomShareStatus()!!) {
                            showSharePersonTipLayout()
                        } else {
                            showChangeBroadModeDialog(true)
                            hideSharePersonTipLayout()
                        }


                    }


                }
            }
            Activity.RESULT_OK -> {

                if (data?.data != null) {
                    val data1 = data?.data
                    when (requestCode) {
                        VideoCode.PHOTO_CODE, VideoCode.FILE_CODE -> {
                            //??????????????????
                            TxLogUtils.i("txsdk---onActivityResult:data---code---$data1----")
                            mPresenter?.uploadFile(data1)
                        }

                        100 -> {
                            //??????????????????
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


    override fun modifyExitBt() {
        trtc_ib_back.text = if (
            mPresenter?.isOwner()!!
        ) {
            "????????????"
        } else {
            "????????????"
        }
    }

    override fun setScreenStatusFail(screenStatus: Boolean) {
        if (screenStatus) {
            showMessage("??????????????????")
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
            //????????????
            if (tv_invite.text == getString(R.string.tx_str_invite)) {
                showShareDialog()
            } else {
                handleMemberListView()
            }

        } else if (id == R.id.tx_business_video) {
            //????????????
            TxLogUtils.i("????????????")
            if (mPresenter?.isShare!! && mPresenter?.isOwner()!!) {
                showMessage(getString(R.string.tx_str_share))
                return
            }
            val videoConfig = ConfigHelper.getInstance().videoConfig
            mPresenter?.muteLocalVideo(videoConfig.isEnableVideo)
            mPresenter?.isCloseVideo = !videoConfig.isEnableVideo
        } else if (id == R.id.tx_business_audio) {
            //????????????
            val audioConfig = ConfigHelper.getInstance().audioConfig
            mPresenter?.muteLocalAudio(audioConfig.isEnableAudio)
        } else if (id == R.id.tx_business_switch) {
            //????????????
//            test()
            switchCamera()
        } else if (id == R.id.tx_business_share) {
            if (tx_business_screen.isSelected) {
                showMessage("????????????????????????????????????????????????")
                return
            }
            //????????????
            if (tx_business_share.isSelected) {
                //??????????????????????????????????????????
                showChangeBroadModeDialog(mPresenter?.isBroad!!)
            } else {
                //????????????
                if (mPresenter?.getRoomShareStatus()!!) {
                    showMessage("?????????????????????????????????????????????")
                    return
                }
                if (mPresenter?.getRoomScreenStatus()!!) {
                    showMessage("?????????????????????????????????????????????")
                    return
                }
                showChangeBroadModeDialog(mPresenter?.isBroad!!)
            }


        } else if (id == R.id.tx_business_screen) {
            if (tx_business_share.isSelected) {
                showMessage("????????????????????????????????????????????????")
                return
            }
            //?????? ?????????????????????????????????????????????
            if (tx_business_screen.isSelected) {
                if (mPresenter?.isShare!!) {
                    //??????
                    mPresenter?.stopScreenCapture()
                } else {
                    mPresenter?.setScreenStatus(screenStatus = true)

                }
            } else {
                if (mPresenter?.getRoomShareStatus()!!) {
                    showMessage("?????????????????????????????????????????????")
                    return
                }

                if (mPresenter?.isShare!!) {
                    //??????
                    mPresenter?.stopScreenCapture()
                } else {
                    mPresenter?.setScreenStatus(screenStatus = true)
                }
            }


        } else if (id == R.id.tx_boardtools) {
            //????????????
            tx_board_view_business.visibility = if (tx_boardtools.isSelected) {
                //??????
                hideBoardTools()
                View.GONE
            } else {
                //??????
                showBoardTools()
                View.VISIBLE
            }


        } else if (id == R.id.tx_pen) {
            //??????
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
            //??????
            mBoard!!.toolType = TEduBoardController.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_ERASER
            selectIb(tx_eraser)

        } else if (id == R.id.tx_zoom) {
            //??????
            mBoard!!.toolType = TEduBoardController.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_ZOOM_DRAG
            selectIb(tx_zoom)
        } else if (id == R.id.tx_textstyle) {
            //????????????
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
        } else if (id == R.id.tx_ib_checkscreen || id == R.id.rl_screen) {
            removeBoardView()
            skipToBoardPage()

        }
    }

    private fun skipToBoardPage() {
        mPresenter?.isSkipBoradPage = true
        val intent = Intent(this, BoardViewActivity::class.java)
        val data = picQuickAdapter?.data
        intent.putExtra(IntentKey.SERVICEID, mPresenter?.getServiceId())
        TxLogUtils.i("boardIdList:${boardIdList.size}")
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


        //??????pop ????????????
        val showing = paintThickPopup?.isShowing

        intent.putExtra(IntentKey.ISSHOWPOP, showing)
        //??????????????????????????????
        intent.putExtra(
            IntentKey.ISSHAREPERSON,
            mPresenter!!.getShareUserId() == mPresenter!!.getSelfUserId()
        )

        if (VIDEOMODE_VERTICAL == TXSdk.getInstance().roomControlConfig.videoMode) {
        }
        VideoTransitData.instance!!.setPresenter(mPresenter!!)


        startActivityForResult(intent, VideoCode.SKIPBOARDPAGE_CODE)
    }

    private fun getSelectToolsPosition(): Int {
        val isSelectedList = videoBoradBusiness?.filter { it.isSelected }
        return if (isSelectedList?.size == 0 || !tx_boardtools.isSelected) {
            -1
        } else {
            videoBoradBusiness?.indexOf(isSelectedList?.get(0))!!
        }
    }

    private fun restoreBoardTool(index: Int, isShowToolPop: Boolean) {
        tx_board_view_business.visibility = View.VISIBLE
        mBoard?.isDrawEnable = true
        tx_boardtools.isSelected = true
        val selectIB = videoBoradBusiness?.get(index)
        selectIb(selectIB!!)
    }

    var picQuickAdapter: PicQuickAdapter? = null
    var changeUserStateDialog: ChangeUserStateDialog? = null
    var mCheckPostion = 0


    //??????????????????list
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
        //1????????????????????????
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

    //?????????????????????
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


    //?????????????????????
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

        // ????????????????????????
        if (mPresenter!!.getRoomShareStatus()) {
            if (VIDEOMODE_HORIZONTAL == TXSdk.getInstance().roomControlConfig.videoMode) {
                hideBoardTools()
                showWBroad(true)
                showBroadFileRv(false)
            } else {
                //????????????????????????????????????
//                showSharePersonTipLayout()

            }


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
                "???????????????-???????????????????????????????????????????????????????????? ${mPresenter?.getInviteNumber()} ????????????"
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

    override fun onCheckFileOnWhiteBroad() {
        showChangeFileDialog()
    }

    override fun onCheckWhiteBroad() {
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

        //???????????????????????????
        val mCurrentmeetingVideoView = mCuccentmemberEntity?.meetingVideoView
        val mCuccentmemberUserId = mCuccentmemberEntity?.userId
        val mCuccentmemberUserName = mCuccentmemberEntity?.userName
        val mCuccentmemberIsAudioAvailable = mCuccentmemberEntity?.isAudioAvailable
        val mCuccentmemberIsVideoAvailable = mCuccentmemberEntity?.isVideoAvailable
        val mCuccentmemberIsShowAudioEvaluation = mCuccentmemberEntity?.isShowAudioEvaluation
        val mCuccentmemberIsHost = mCuccentmemberEntity?.isHost
        val mCuccentmemberUserRole = mCuccentmemberEntity?.userRole
        val mCuccentmemberUserRoleIconPath = mCuccentmemberEntity?.userRoleIconPath
        val mCuccentmemberUserHeadPath = mCuccentmemberEntity?.userHeadPath
        //?????????????????????video??????
        if (bigMeetingEntity == null) {
            bigMeetingEntity = MemberEntity()
            //??????????????????????????????????????????????????????????????????

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
            val mBigMeetingUserHeadPath = bigMeetingEntity?.userHeadPath

            bigscreen.bigScreenView?.removeView(bigmeetingVideoView)

            mCurrentmeetingVideoView?.detach()

            mCurrentmeetingVideoView?.addViewToViewGroup(bigscreen.bigScreenView)

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
                userHeadPath = mCuccentmemberUserHeadPath!!
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
                userHeadPath = mBigMeetingUserHeadPath!!
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
        //???????????????????????????????????????
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
            val mUserHeadPath = memberEntity.userHeadPath
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
            bigMeetingEntity?.userHeadPath = mUserHeadPath
            meetingVideoView.detach()
            meetingVideoView.addViewToViewGroup(bigscreen.bigScreenView)
            mPresenter?.getTRTCRemoteUserManager()!!.setRemoteFillMode(
                bigMeetingEntity?.userId,
                TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG,
                false
            )
            val index = mPresenter!!.removeMemberEntity(userId!!)
            changeBigVideo(bigMeetingEntity!!)
//            bigscreen.changeBigScreenViewName(
//                bigMeetingEntity?.userName,
//                bigMeetingEntity?.userRole,
//                bigMeetingEntity?.userRoleIconPath
//            )
            if (index >= 0) {
                mMemberListAdapter!!.notifyItemRemoved(index!!)
            }
            TxLogUtils.i("onSingleClick", "checkSmallVideoToBigVideo${isVideoAvailable}")
        }

    }

    override fun changeBigVideo(bigMeetingEntity: MemberEntity) {
        if (TXSdk.getInstance().roomControlConfig.videoMode == VideoMode.VIDEOMODE_VERTICAL) {
            if (bigMeetingEntity.isVideoAvailable) {
                bigScreenVerticalView.closeVideo(false,bigMeetingEntity.userHeadPath)
                bigScreenVerticalView?.visibility = View.VISIBLE
            } else {
                bigScreenVerticalView.closeVideo(true,bigMeetingEntity.userHeadPath)
                bigScreenVerticalView?.visibility = View.GONE
            }
        }else{
            if (bigMeetingEntity.isVideoAvailable) {
                bigscreen.closeVideo(false,bigMeetingEntity.userHeadPath)

            } else {
                bigscreen.closeVideo(true,bigMeetingEntity.userHeadPath)
            }
            bigscreen?.visibility = View.VISIBLE
        }

        changeBigScreenViewName(
            bigMeetingEntity.userName,
            bigMeetingEntity.userRole,
            bigMeetingEntity.userRoleIconPath
        )
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
            bigmeetingVideoView?.detach()

            bigscreen.bigScreenView?.removeView(bigmeetingVideoView)
            mPresenter?.addMemberEntity(1, bigMeetingEntity!!)
//            bigMeetingEntity?.meetingVideoView?.refreshParent()
            mMemberListAdapter!!.notifyItemInserted(1)

        }
    }

    override fun hideBigNoVideo(isHiden: Boolean) {
        if (TXSdk.getInstance().roomControlConfig.videoMode == VideoMode.VIDEOMODE_VERTICAL) {
            bigScreenVerticalView.visibility = View.GONE
        }else{
            bigscreen.visibility = View.GONE
        }
    }

    override fun showBroadFileRv(isShow: Boolean) {
        rl_rv.visibility = if (isShow) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun changeBigScreenViewName(text: String, userRole: String, userRoleIconPath: String) {


        if (TXSdk.getInstance().roomControlConfig.videoMode == VideoMode.VIDEOMODE_VERTICAL) {
            bigScreenVerticalView.changeBigScreenViewName(text, userRole, userRoleIconPath)
            bigScreenVerticalView.changeBigScreenViewVoice(bigMeetingEntity!!.isShowAudioEvaluation, 10)
        } else {
            bigscreen.changeBigScreenViewName(text, userRole, userRoleIconPath)
            bigscreen.changeBigScreenViewVoice(bigMeetingEntity!!.isShowAudioEvaluation, 10)
        }
    }

    override fun changeBigScreenViewVoice(volume: Int) {

        if (TXSdk.getInstance().roomControlConfig.videoMode == VideoMode.VIDEOMODE_VERTICAL) {
            bigScreenVerticalView.changeBigScreenViewVoice(bigMeetingEntity!!.isShowAudioEvaluation, volume)
        } else {
            bigscreen.changeBigScreenViewVoice(bigMeetingEntity!!.isShowAudioEvaluation, volume)
        }

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
            //??????Parent
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


    fun checkOwenrToBigScreen(screenUserId: String) {
        TxLogUtils.i("checkOwenrToBigShareScreen-----checkItemToBig---$screenUserId")
        val ownerUserId = screenUserId
        if (!ownerUserId?.isEmpty()!!) {
            var entity = mPresenter!!.getStringMemberEntityMap()[ownerUserId]
            if (null == bigMeetingEntity) {
                bigMeetingEntity = MemberEntity()
            }
            entity?.isShowOutSide = true
            val meetingVideoView = entity?.meetingVideoView
            bigMeetingEntity?.meetingVideoView = meetingVideoView
            bigMeetingEntity?.userId = entity?.userId
            bigMeetingEntity?.userName = entity?.userName
            bigMeetingEntity?.userRoleIconPath = entity?.userRoleIconPath
            bigMeetingEntity?.isAudioAvailable = entity?.isAudioAvailable!!
            bigMeetingEntity?.isVideoAvailable = entity?.isVideoAvailable!!
            bigMeetingEntity?.isMuteVideo = entity?.isMuteVideo!!
            bigMeetingEntity?.isShowAudioEvaluation = entity?.isShowAudioEvaluation!!
            bigMeetingEntity?.userRole = entity?.userRole!!
            bigMeetingEntity?.userHeadPath = entity?.userHeadPath!!
            bigMeetingEntity?.isScreen = entity?.isScreen!!
            //??????Parent
            bigScreenVerticalView.visibility = View.VISIBLE
            meetingVideoView?.detach()
            meetingVideoView?.addViewToViewGroup(bigScreenVerticalView.bigScreenView)
//            entity?.isVideoAvailable = true
            TxLogUtils.i("checkOwenrToBigScreen", bigMeetingEntity?.userRole)
            TxLogUtils.i("checkOwenrToBigScreen", bigMeetingEntity?.userRoleIconPath)
            TxLogUtils.i("checkOwenrToBigScreen", ""+bigMeetingEntity?.isVideoAvailable!!)
            TxLogUtils.i("checkOwenrToBigScreen", ""+bigMeetingEntity?.isScreen!!)
            bigScreenVerticalView.changeBigScreenViewName(
                bigMeetingEntity?.userName,
                bigMeetingEntity?.userRole,
                bigMeetingEntity?.userRoleIconPath
            )
            bigScreenVerticalView.changeBigScreenViewVoice(bigMeetingEntity!!.isShowAudioEvaluation, 40)
            if(bigMeetingEntity?.isVideoAvailable!!){

            }else{

            }
            mPresenter?.getTRTCRemoteUserManager()?.setRemoteFillMode(
                bigMeetingEntity?.userId,
                TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG,
                true
            )
            if (bigMeetingEntity?.isScreen!!) {
                bigScreenVerticalView.showScreenIcon(bigMeetingEntity?.isScreen!!)
            }else{
                bigScreenVerticalView.closeVideo(bigMeetingEntity?.isMuteVideo!!,bigMeetingEntity?.userHeadPath)
            }


            bigScreenVerticalView.setBigScreenCallBack(object : BigScreenView.BigScreenViewCallback {
                override fun onScreenFinishClick() {
                    detachBigScreen(screenUserId)
                }

                override fun onSwitch() {
                }

                override fun onMuteAudioClick() {
                }

                override fun onMuteVideoClick() {
                }

            })
        }

    }

    fun detachBigScreen(screenUserId: String) {
        TxLogUtils.i("detachBigShareScreen------$screenUserId")
        if (screenUserId.isNotEmpty() && null != bigMeetingEntity) {
            bigScreenVerticalView?.bigScreenView?.removeAllViews()
            var entity = mPresenter!!.getStringMemberEntityMap()[screenUserId]

            val mMeetingVideoView = bigMeetingEntity?.meetingVideoView
            mMeetingVideoView?.detach()
            entity?.apply {
                meetingVideoView = mMeetingVideoView
                isShowOutSide = false
                isNeedFresh = true
                userId = bigMeetingEntity?.userId
                userName = bigMeetingEntity?.userName
                userRoleIconPath = bigMeetingEntity?.userRoleIconPath
                isAudioAvailable = bigMeetingEntity?.isAudioAvailable!!
                isVideoAvailable = bigMeetingEntity?.isVideoAvailable!!
                isShowAudioEvaluation = bigMeetingEntity?.isShowAudioEvaluation!!
                userRole = bigMeetingEntity?.userRole!!
                userHeadPath = bigMeetingEntity?.userHeadPath!!
                isScreen = bigMeetingEntity?.isScreen!!
                isMuteAudio = bigMeetingEntity?.isMuteAudio!!
                isMuteVideo = bigMeetingEntity?.isMuteVideo!!
            }

            bigScreenVerticalView?.visibility = View.GONE
            entity?.meetingVideoView?.refreshParent()
            val indexOf = mPresenter!!.getMemberEntityList().indexOf(
                entity
            )
            if (indexOf < 0) {

            } else {
                //todo ??????????????????
                mMemberListAdapter?.notifyItemChanged(
                    mPresenter!!.getMemberEntityList().indexOf(
                        entity
                    )
                )
            }


            bigMeetingEntity = null
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

            entity?.meetingVideoView?.refreshParent()
            val indexOf = mPresenter!!.getMemberEntityList().indexOf(
                entity
            )
            if (indexOf < 0) {
                mMeetingVideoView?.detach()
                if (TXSdk.getInstance().roomControlConfig.videoMode == VideoMode.VIDEOMODE_VERTICAL) {
                    mMeetingVideoView?.addViewToViewGroup(bigScreenVerticalView.bigScreenView)
                }else{
                    mMeetingVideoView?.addViewToViewGroup(bigscreen.bigScreenView)
                }

                changeBigVideo(bigMeetingEntity!!)
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
        //????????????
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
            showMessage("????????????????????????????????????????????????")
            return
        }
        if (memberEntityList!!.size == 1) {
            for (i in 0 until memberEntityList.size) {
                try {
                    val memberEntity = memberEntityList.get(i)
                    val userId = memberEntity.userId
                    TxLogUtils.i("memberEntityList" + userId)
                    if (userId == mPresenter!!.getSelfUserId()) {
                        //???????????????
                    } else {
                        if (isSameScreen) {
                            //  ??????
                            mPresenter!!.startShareWeb(
                                webId!!,
                                mPresenter!!.getServiceId(),
                                mPresenter!!.getSelfUserId(),
                                userId!!,
                                name
                            )

                        } else {
                            //????????????????????????
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
                        //  ??????
                        mPresenter!!.startShareWeb(
                            webId!!,
                            mPresenter!!.getServiceId(),
                            mPresenter!!.getSelfUserId(),
                            userId!!,
                            name
                        )

                    } else {
                        //????????????????????????
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
            override fun onCheckFileOnWhiteBroad() {

                showPersonDialog(webId!!, url, name, true, list, fileName)
            }

            override fun onCheckWhiteBroad() {
                showPersonDialog(webId!!, url, name, false, list, fileName)

            }

            override fun onShareScreenUrl() {

            }

        })

        personDialog?.show()
        personDialog?.setText(name)
    }

    override fun getPushWebUrlSuccess(webId: String, clientUrl: String, name: String) {
        if (!clientUrl.isEmpty()) {
            mPresenter!!.getRoomInfo(webId, clientUrl, name, name)
        } else {
            showMessage("?????????clientUrl??????")
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
                //?????????????????????
                mPresenter!!.setShareWebId(webId)
                //???????????????????????????url
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
                    .setTitle("????????????")
                    .setConfirm("??????")
                    .setCancel("??????")
                    .setListener { dialog, name, url ->
                        if (name.isNotEmpty() && url.isNotEmpty()) {
                            mPresenter?.addShareUrl(TXSdk.getInstance().agent, name!!, url!!)
                        } else {
                            if (name.isEmpty()) {
                                showMessage("????????????????????????")
                            } else {
                                showMessage("????????????????????????")
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
        // fromAgent ???true ????????????????????????

        if (webDialog == null) {
            webDialog =
                WebDialog(this)
        }
        webDialog?.setOnShareWhiteBroadDialogListener(object : onShareWhiteBroadDialogListener {
            override fun onCheckFileOnWhiteBroad() {
                //????????????
                var dialog1 = ExitDialog(
                    this@VideoActivity,
                    "??????",
                    "",
                    "??????",
                    "????????????????????????"
                )
                dialog1?.setOnConfirmlickListener(object :
                    onExitDialogListener {
                    override fun onConfirm() {
                        if (toUserId.isEmpty()) {
                            //????????????
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
                //????????????
                //????????????????????????
                if (toUserId.isEmpty()) {
                    mPresenter?.sendGroupMessage(JSONObject().apply {
                        put("serviceId", mPresenter!!.getServiceId())
                        put("type", IMkey.WXPUSHWEBFILESUCCESS)
                        put("userId", fromUserId)
                    }.toString(), "3")
                }

            }

            override fun onCheckWhiteBroad() {
                //??????
                val audioConfig = ConfigHelper.getInstance().audioConfig
                mPresenter?.muteLocalAudio(audioConfig.isEnableAudio)

            }

            override fun onShareScreenUrl() {


                //????????????
                var dialog1 = ExitDialog(
                    this@VideoActivity,
                    "??????",
                    "",
                    "??????",
                    "????????????????????????"
                )
                dialog1?.setOnConfirmlickListener(object :
                    onExitDialogListener {
                    override fun onConfirm() {
                        if (toUserId.isEmpty()) {
                            //????????????
                            showMessage("????????????????????????")
                        } else {
                            //??????????????????
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

    override fun onShareScreenUrl() {
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
                    it.findViewById<TextView>(R.id.tx_float)
                        .setOnClickListener(CheckDoubleClickListener() {
                            showSmartDialog()
                        })

                })
                .registerCallback {

                }
                .show()
        }, 200)

    }

    var mSmartWebDialog: SmartWebDialog? = null
    private fun showSmartDialog() {
        if (mSmartWebDialog == null) {
            mSmartWebDialog =
                SmartWebDialog(this)
        }
        mSmartWebDialog?.show()
        mSmartWebDialog?.request(
            "https://ics.webank.com/s/serviceHall/?app_id=W4054396&user_id=12345675&nonce=5wpnjcQ5UUedWDacb1aKcBhQIl9jFsd4&sign=93fb1153abafba8f847259b92211eb09fdb23e93&version=1.0.0#/index",
            "????????????"
        )
    }

    private fun hideSmartWebDialog() {
        mSmartWebDialog?.dismiss()
    }
}