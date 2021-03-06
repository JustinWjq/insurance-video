package com.txt.video.ui.boardpage

import android.graphics.Bitmap
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import com.tencent.imsdk.TIMMessage
import com.tencent.teduboard.TEduBoardController
import com.txt.video.R
import com.txt.video.TXSdk
import com.txt.video.base.BaseActivity
import com.txt.video.base.constants.IMkey
import com.txt.video.base.constants.IntentKey
import com.txt.video.base.constants.VideoCode
import com.txt.video.net.bean.ThickType
import com.txt.video.net.bean.ToolType
import com.txt.video.net.utils.TxLogUtils
import com.txt.video.trtc.ConfigHelper
import com.txt.video.trtc.TICManager
import com.txt.video.trtc.TRTCCloudManager
import com.txt.video.trtc.ticimpl.TICMessageListener
import com.txt.video.trtc.videolayout.Utils
import com.txt.video.ui.video.VideoActivity
import com.txt.video.ui.weight.PicQuickAdapter
import com.txt.video.common.callback.onCheckDialogListenerCallBack
import com.txt.video.common.callback.onExitDialogListener
import com.txt.video.common.dialog.CommonDialog
import com.txt.video.common.toast.ToastUtils
import com.txt.video.ui.weight.dialog.PaintThickPopup
import com.txt.video.common.utils.AppUtils
import com.txt.video.trtc.TRTCCloudIView
import com.txt.video.ui.video.VideoMode
import com.txt.video.ui.video.VideoPresenter
import com.txt.video.ui.video.VideoTransitData
import com.txt.video.ui.weight.dialog.ExitDialog
import kotlinx.android.synthetic.main.tx_activity_board_view.*
import kotlinx.android.synthetic.main.tx_activity_board_view.board_view_container
import kotlinx.android.synthetic.main.tx_activity_board_view.tx_board_view_business
import kotlinx.android.synthetic.main.tx_activity_board_view.tx_eraser
import kotlinx.android.synthetic.main.tx_activity_board_view.tx_boardtools
import kotlinx.android.synthetic.main.tx_activity_board_view.tx_pen
import kotlinx.android.synthetic.main.tx_activity_board_view.tx_rv
import kotlinx.android.synthetic.main.tx_activity_board_view.tx_textstyle
import kotlinx.android.synthetic.main.tx_activity_board_view.tx_zoom
import org.json.JSONObject

/**
 * ????????????????????????????????????????????????
 *
 * */
class BoardViewActivity : BaseActivity<BoardViewContract.ICollectView, BoardViewPresenter>(),
    BoardViewContract.ICollectView, onCheckDialogListenerCallBack, TICMessageListener {

    override fun getContentViewId(): Int {
        return R.layout.tx_activity_board_view
    }

    override fun init(savedInstanceState: Bundle?) {
        openImmersionBar()
        initView()

    }

    var isSharePerson = false
    var mVideoPresenter: VideoPresenter? = null
    private fun initBoardTools() {

        val extras = intent.extras
        val position = extras?.getInt(IntentKey.CHECKTOOLSPOSTIONS, -1)
        isSharePerson = extras!!.getBoolean(IntentKey.ISSHAREPERSON, true)
        if (-1 != position) {
            val isShowPop = extras?.getBoolean(IntentKey.ISSHOWPOP)
            restoreBoardTool(position!!, isShowPop!!)
        }

    }

    override fun createPresenter(): BoardViewPresenter? {
        return BoardViewPresenter(this, this)
    }

    var boardController: TEduBoardController? = null
    var videoBoradBusiness: ArrayList<ImageButton>? = null
    fun initView() {
        val instance = TICManager.getInstance()
        boardController = instance.boardController

        val windowWidth = Utils.getWindowHeight(this) / 9 * 16
        val layoutParams =
            FrameLayout.LayoutParams(
                windowWidth,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        layoutParams.topMargin = 30
        if (null != boardController?.boardRenderView){
            board_view_container.addView(boardController?.boardRenderView, layoutParams)
        }else{
            finish()
            TxLogUtils.i("boardController?.boardRenderView??????")
        }

        instance.addIMMessageListener(this)
        showAudioStatus()
        videoBoradBusiness = arrayListOf(
            tx_pen,
            tx_eraser,
            tx_textstyle,
            tx_zoom
        )
        boardController?.boardContentFitMode =
            TEduBoardController.TEduBoardContentFitMode.TEDU_BOARD_CONTENT_FIT_MODE_NONE
        initBoardTools()
        initPicAdapter()
        //???????????????????????????????????????
        tx_ib_checkscreen1.visibility =
            if (VideoMode.VIDEOMODE_HORIZONTAL == TXSdk.getInstance().roomControlConfig.videoMode) {
                View.VISIBLE
            } else {
                mVideoPresenter = VideoTransitData.instance!!.getPresenter()
                //???????????????
                //???????????????????????????
                if (isSharePerson) {
                    tv_endshare.visibility = View.VISIBLE
                    tv_back.visibility = View.GONE
                } else {
                    tv_endshare.visibility = View.GONE
                    tv_back.visibility = View.VISIBLE
                }
                View.INVISIBLE
            }
//        TRTCCloudManager.sharedInstance().setViewListener(this)
    }

    fun showAudioStatus() {
        val audioConfig = ConfigHelper.getInstance().audioConfig
        tx_ib_audiomute.isSelected = !audioConfig.isEnableAudio
    }


    var picQuickAdapter: PicQuickAdapter? = null

    //??????????????????list
    private fun initPicAdapter() {

        val extras = intent.extras
        val stringArrayList = extras?.getStringArrayList(IntentKey.BOARDLISTS)
        extras?.getString(IntentKey.SERVICEID)?.let { mPresenter?.setServiceId(it) }
        if (null != stringArrayList && stringArrayList.size > 1) {
            ll_borads.visibility = View.VISIBLE
            val boardIdLists = intent.extras?.getStringArrayList(IntentKey.BOARDIDLISTS)

            picQuickAdapter =
                PicQuickAdapter()
            tx_rv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                this
            ).apply {
                orientation = androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
            }
            tx_rv.adapter = picQuickAdapter
            picQuickAdapter?.setOnItemClickListener { adapter, view, position ->
                boardController?.gotoBoard(boardIdLists?.get(position))
            }
            picQuickAdapter?.setNewData(stringArrayList)

        } else {
            ll_borads.visibility = View.GONE
        }


    }


    private fun finishPage() {
        board_view_container.removeView(boardController?.boardRenderView)
        val selectToolsPosition = getSelectToolsPosition()
        intent.putExtra(IntentKey.CHECKTOOLSPOSTIONS, selectToolsPosition)

        //??????pop ????????????
        val showing = paintThickPopup?.isShowing

        intent.putExtra(IntentKey.ISSHOWPOP, showing)

        setResult(VideoCode.FINISHPAGE_CODE, intent)
        finish()
    }

    fun getSelectToolsPosition(): Int {
        val isSelectedList = videoBoradBusiness?.filter { it.isSelected }
        return if (isSelectedList?.size == 0 || !isShowPaint) {
            -1
        } else {
            videoBoradBusiness?.indexOf(isSelectedList?.get(0))!!
        }
    }

    private var isShowPaint = false

    fun onTxClick(v: View?) {
        val id = v?.id

        if (id == R.id.tx_ib_checkscreen1) {
            finishPage()
        } else if (id == R.id.tx_boardtools) {
            //????????????
            tx_board_view_business.visibility = if (isShowPaint) {
                //??????
                boardController?.isDrawEnable = true
                tx_boardtools.setImageResource(R.drawable.tx_paint_default)
                boardController!!.toolType =
                    TEduBoardController.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_ZOOM_DRAG
                View.GONE
            } else {
                //??????
                boardController?.isDrawEnable = true
                tx_boardtools.setImageResource(R.drawable.tx_paint_check)
                val color = PaintThickPopup.mColorMap[VideoActivity.paintColorPostion].color
                boardController!!.brushColor = TEduBoardController.TEduBoardColor(color!!)
                boardController!!.toolType =
                    TEduBoardController.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_PEN
                selectIb(tx_pen)
                View.VISIBLE
            }


            isShowPaint = !isShowPaint
        } else if (id == R.id.tx_pen) {
            //??????
            val color = PaintThickPopup.mColorMap[VideoActivity.paintColorPostion].color
            val size = PaintThickPopup.mColorMap1[VideoActivity.paintSizeIntPostion].size
            boardController!!.apply {
                brushColor = TEduBoardController.TEduBoardColor(color!!)
                brushThin = size
                toolType =
                    TEduBoardController.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_PEN

            }

            if (tx_pen.isSelected) {
                showPopupWindow(
                    "1",
                    VideoActivity.paintColorPostion,
                    VideoActivity.paintSizeIntPostion
                )
            } else {
                tx_pen.isSelected = true
            }

            selectIb(tx_pen)
        } else if (id == R.id.tx_eraser) {
            //??????
            boardController!!.toolType =
                TEduBoardController.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_ERASER
            selectIb(tx_eraser)
        } else if (id == R.id.tx_zoom) {
            //??????
            boardController!!.toolType =
                TEduBoardController.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_ZOOM_DRAG

            selectIb(tx_zoom)
        } else if (id == R.id.tx_textstyle) {
            //????????????
            val color = PaintThickPopup.mColorMap[VideoActivity.textColorIntPostion].color
            val size = PaintThickPopup.mTextMap[VideoActivity.textSizeIntPostion].size
            boardController!!.apply {
                textColor = TEduBoardController.TEduBoardColor(color)
                textSize = size
                toolType =
                    TEduBoardController.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_TEXT
            }

            if (tx_textstyle.isSelected) {
                showPopupWindow(
                    "2",
                    VideoActivity.textColorIntPostion,
                    VideoActivity.textSizeIntPostion
                )
            } else {
                tx_textstyle.isSelected = true
            }

            selectIb(tx_textstyle)
        } else if (id == R.id.tx_rlshow) {
            val selected = tx_rlshow.isSelected
            rl_rv.visibility = if (selected) {
                //??????
                View.VISIBLE
            } else {
                View.GONE
            }
            tx_rlshow.isSelected = !selected
        } else if (id == R.id.tx_ib_audiomute) {
            val audioConfig = ConfigHelper.getInstance().audioConfig
            audioConfig.isEnableAudio = !audioConfig.isEnableAudio
            TRTCCloudManager.sharedInstance().muteLocalAudio(!audioConfig.isEnableAudio)
            tx_ib_audiomute.isSelected = !audioConfig.isEnableAudio
        } else if (id == R.id.tv_endshare) { //?????????????????????????????????
            //????????????
            var dialog1 = ExitDialog(
                this,
                "??????",
                "",
                "??????",
                "????????????????????????"
            )
            dialog1?.setOnConfirmlickListener(object :
                onExitDialogListener {
                override fun onConfirm() {
                    //??????????????????
                    mVideoPresenter?.sendGroupMessage(
                        mVideoPresenter?.setIMTextData(IMkey.ENDWHITEBOARD)
                            ?.put(IMkey.SHAREUSERID, mVideoPresenter?.getSelfUserId()).toString(),
                        "1"
                    )
                    mVideoPresenter?.setShareStatus(false, "", null)
                    mVideoPresenter?.setRoomShareStatus(false)
                    //??????????????????
                    finishPage()
                }

                override fun onTemporarilyLeave() {


                }

            })

            dialog1?.show()

        } else if (id == R.id.tv_back) {
            //????????????
            var dialog1 = ExitDialog(
                this,
                "??????",
                "?????????",
                "??????",
                ""
            )
            dialog1?.setOnConfirmlickListener(object :
                onExitDialogListener {
                override fun onConfirm() {
                    //??????????????????
                    finishPage()
                }

                override fun onTemporarilyLeave() {


                }

            })

            dialog1?.show()
        }

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
            180
        }

        paintThickPopup?.setArrowOffsetXDp(xDp)
        paintThickPopup?.setOnCheckDialogListener(this)
        paintThickPopup?.refreshUI(type, paintColorPostion, paintSizeIntPostion)
        paintThickPopup?.show()

    }


    override fun onCheckColor(postion: Int, toolType: ToolType, type: String) {
        val eduBoardColor = TEduBoardController.TEduBoardColor(toolType.color)
        if (type == "1") {
            VideoActivity.paintColorPostion = postion
            boardController!!.brushColor = eduBoardColor
        } else {
            VideoActivity.textColorIntPostion = postion
            boardController!!.textColor = eduBoardColor
        }
    }

    override fun onCheckThick(postion: Int, thickType: ThickType, type: String) {
        if (type == "1") {
            VideoActivity.paintSizeIntPostion = postion
            boardController!!.brushThin = thickType.size
        } else {
            VideoActivity.textSizeIntPostion = postion
            boardController!!.textSize = thickType.size
        }
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
                IMkey.NOTIFYEXTEND -> {
                    if (hasData && VideoTransitData.instance?.getPresenter()?.isOwner()!!) {

                        val dataJO = jsonObject.getJSONObject("data")
                        val extendRoomTime = dataJO.optInt("extendRoomTime", 0)
                        val notifyExtendTime = dataJO.getInt("notifyExtendTime")
                        if (0 != extendRoomTime) {
                            showTimerDialog("2", 0, extendRoomTime, notifyExtendTime, 0)
                        }

                    }

                }
                IMkey.NOTIFYEND -> {
                    if (hasData && VideoTransitData.instance?.getPresenter()?.isOwner()!!) {
                        val dataJO = jsonObject.getJSONObject("data")
                        val extendRoomTime = dataJO.optInt("extendRoomTime", 0)
                        val notifyEndTime = dataJO.getInt("notifyEndTime")
                        if (0 != extendRoomTime) {
                            showTimerDialog("3", 0, extendRoomTime, 0, notifyEndTime)
                        }
                    }
                }
                //????????????
                IMkey.ENDWHITEBOARD -> {
                    mVideoPresenter?.setRoomShareStatus(false)
                    intent.putExtra(IntentKey.ENDWHITEBROAD, true)
                    finishPage()
                }
                IMkey.MUTEAUDIO -> {
                    //???????????????????????????
                    VideoTransitData.instance?.getPresenter()
                        ?.judgeMuteVideoOrAudioForSomeone(false, jsonObject)

                }
                IMkey.MUTEVIDEO -> {
                    VideoTransitData.instance?.getPresenter()
                        ?.judgeMuteVideoOrAudioForSomeone(true, jsonObject)
                }
                IMkey.END->{
                    finish()
                    VideoTransitData.instance?.getPresenter()?.destroyRoom()
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


    var mTimerdialog: CommonDialog? = null
    fun showTimerDialog(
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

                    else -> {
                    }
                }
            }

            override fun onTemporarilyLeave() {
                when (type) {
                    "2" -> {
                    }
                    "3" -> {
                        finishAllPage()
                    }

                    else -> {
                    }
                }
            }

            override fun end() {
                finishAllPage()
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

    override fun onBackPressed() {
        finishPage()
    }


    fun finishAllPage() {
        AppUtils.getActivityList().forEach {
            if (it is VideoActivity) {
                it.mPresenter?.destroyRoom()
            } else {
                it.finish()
            }
        }
    }

    fun selectIb(imageButton: ImageButton) {
        videoBoradBusiness?.forEach {
            it.isSelected = it == imageButton
        }
    }

    fun restoreBoardTool(index: Int, isShowToolPop: Boolean) {
        tx_board_view_business.visibility = View.VISIBLE
        boardController?.isDrawEnable = true
        tx_boardtools.setImageResource(R.drawable.tx_paint_check)
        isShowPaint = true
        val selectIB = videoBoradBusiness?.get(index)
        selectIb(selectIB!!)

        if (isShowToolPop) {


        }
    }

    override fun onDestroy() {
        super.onDestroy()
        TICManager.getInstance().removeIMMessageListener(this)
    }


}