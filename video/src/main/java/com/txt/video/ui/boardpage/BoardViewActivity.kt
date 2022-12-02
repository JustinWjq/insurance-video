package com.txt.video.ui.boardpage

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.google.gson.Gson
import com.tencent.teduboard.TEduBoardController
import com.txt.video.R
import com.txt.video.base.BaseActivity
import com.txt.video.base.constants.IMkey
import com.txt.video.base.constants.IntentKey
import com.txt.video.base.constants.VideoCode
import com.txt.video.common.callback.onCheckDialogListenerCallBack
import com.txt.video.common.callback.onExitDialogListener
import com.txt.video.common.dialog.common.TxBaseDialog
import com.txt.video.common.utils.AppUtils
import com.txt.video.common.utils.ToastUtils
import com.txt.video.net.bean.ThickType
import com.txt.video.net.bean.ToolType
import com.txt.video.net.utils.TxLogUtils
import com.txt.video.trtc.TICManager
import com.txt.video.trtc.ticimpl.utils.MyBoardCallback
import com.txt.video.trtc.videolayout.Utils
import com.txt.video.ui.video.VideoActivity
import com.txt.video.ui.video.barrage.model.TUIBarrageModel
import com.txt.video.ui.video.barrage.presenter.TUIBarragePresenter
import com.txt.video.ui.video.barrage.view.ITUIBarrageListener
import com.txt.video.ui.video.barrage.view.TUIBarrageButton
import com.txt.video.ui.video.barrage.view.TUIBarrageDisplayView
import com.txt.video.ui.video.word.view.TxWordDisplayView
import com.txt.video.ui.weight.PicQuickAdapter
import com.txt.video.ui.weight.dialog.CommonDialog
import com.txt.video.ui.weight.dialog.PaintThickPopup
import com.txt.video.ui.weight.dialog.TxMessageDialog
import com.txt.video.ui.weight.easyfloat.utils.DisplayUtils
import kotlinx.android.synthetic.main.tx_activity_board_view.*

/**
 * author ：Justin
 * time ：2021/3/17.
 * des ：白板业务
 */
class BoardViewActivity : BaseActivity<BoardViewContract.ICollectView, BoardViewPresenter>(),
    BoardViewContract.ICollectView, onCheckDialogListenerCallBack,
    TEduBoardController.TEduBoardCallback {

    override fun getContentViewId(): Int {
        return R.layout.tx_activity_board_view
    }

    override fun init(savedInstanceState: Bundle?) {
        hideStatusBar()
        initView()

    }

    private fun initBoardTools() {
        val extras = intent.extras
        val position = extras?.getInt(IntentKey.CHECKTOOLSPOSTIONS, -1)
        if (-1 != position) {
            val isShowPop = extras?.getBoolean(IntentKey.ISSHOWPOP)
            restoreBoardTool(position!!, isShowPop!!)
        }

    }

    override fun createPresenter(): BoardViewPresenter? {
        return BoardViewPresenter(this, this)
    }

    fun initBoardView() {
        initBoard()
        try {
            showAudioStatus()
            videoBoradBusiness = arrayListOf(
                tx_pen,
                tx_arrow,
                tx_eraser
            )

            initBoardTools()

        } catch (e: Exception) {

        }

    }

    var boardController: TEduBoardController? = null
    var videoBoradBusiness: ArrayList<ImageButton>? = null
    fun initView() {
        initBoardView()
        val extras = intent.extras

        extras?.getString(IntentKey.SERVICEID)?.let { mPresenter?.setServiceId(it) }
        extras?.getString(IntentKey.AGENTID)?.let { mPresenter?.setAgentId(it) }
        extras?.getString(IntentKey.GROUPID)?.let {
            initBarrage(
                it,
                extras?.getString(IntentKey.SERVICEID)!!
            )
        }
        iv_endshare.setOnClickListener {
            //结束共享
            endShare()

        }
    }

    fun endShare() {
        TxMessageDialog.Builder(this)
            .setTitle("确定结束")
            .setMessage("您确定要结束共享吗?")
            .setConfirm("结束")
            .setCancel("取消")
            .setListener(object : TxMessageDialog.OnListener {
                override fun onConfirm(dialog: TxBaseDialog?) {
                    mPresenter?.sendGroupMessage(
                        mPresenter?.setIMTextData(IMkey.ENDWHITEBOARD)
                            ?.put(IMkey.SHAREUSERID, mPresenter?.getAgentId()).toString(),
                        "1"
                    )
                    mPresenter?.setShareStatus(false, "", null)
//            mPresenter?.setRoomShareStatus(false)
                }

                override fun onCancel(dialog: TxBaseDialog?) {

                }

            }).show()
    }

    fun showAudioStatus() {
//        val audioConfig = ConfigHelper.getInstance().audioConfig
//        tx_ib_audiomute.isSelected = !audioConfig.isEnableAudio
    }

    override fun showMessage(message: String) {
        runOnUiThread {
            ToastUtils.showShort(message)
        }
    }

    var picQuickAdapter: PicQuickAdapter? = null
    private var boardIdList = ArrayList<String>()

    //初始化缩略图list
    private fun initPicAdapter() {
        TxLogUtils.i("initPicAdapter","onTEBInit-----initPicAdapter")
        val extras = intent.extras

        val stringArrayList = extras?.getStringArrayList(IntentKey.BOARDLISTS)

        if (null !=  extras?.getString(IntentKey.VIDEOURL)) {
            extras?.getString(IntentKey.VIDEOURL)?.let {
                TxLogUtils.i(" IntentKey.VIDEOURL" + it)
                boardController?.addVideoFile(it)
            }
        }else{

            if (null != stringArrayList && stringArrayList.size > 1) {
                ll_borads.visibility = View.VISIBLE
//            val boardIdLists = intent.extras?.getStringArrayList(IntentKey.BOARDIDLISTS)
                val picsWordLists = intent.extras?.getStringArrayList(IntentKey.PICSWORDLISTS)
                val addImagesFile = boardController?.addImagesFile(stringArrayList)

                picQuickAdapter =
                    PicQuickAdapter()
                tx_rv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                    this
                ).apply {
                    orientation = androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
                }
                tx_rv.adapter = picQuickAdapter
                initWord()

                txWordDisplayView?.setContent(picsWordLists?.get(0))
                if( picsWordLists?.get(0)?.isEmpty() == true){
                    checkLand(isLand,false)
                }else{
                    checkLand(isLand,true)
                }
                picQuickAdapter?.setOnItemClickListener { adapter, view, position ->
                    if (0!= boardIdList.size) {
                        boardController?.gotoBoard(boardIdList?.get(position))
                        //点击展示对应的提词器
                        txWordDisplayView?.setContent(picsWordLists?.get(position))
                        if (picsWordLists?.get(position)?.isEmpty() == true){
                            checkLand(isLand,false)
                        }else{
                            checkLand(isLand,true)
                        }
                    }

                }
                picQuickAdapter?.setNewData(stringArrayList)
                //展示提词器
            } else {
                ll_borads.visibility = View.GONE
            }
        }

    }

    var txWordDisplayView: TxWordDisplayView? = null;
    private fun initWord() {

        txWordDisplayView = TxWordDisplayView(this, "")
        setWordShow(txWordDisplayView as View)
        rl_word_show.visibility = View.VISIBLE
        txWordDisplayView?.setOnPenListener(object : TxWordDisplayView.onOpenListener{
            override fun open(isOpen: Boolean) {
                checkLand(isLand,false)
            }

        })
    }

    private fun setWordShow(view: View) {
        val params = RelativeLayout.LayoutParams(
            DisplayUtils.getScreenHeight(this) / 2,
            DisplayUtils.getScreenHeight(this) / 2 + 50
        )

        rl_word_show.addView(view, params)
    }

    override fun finishPage() {
        boardController?.reset()
        board_view_container.removeView(boardController?.boardRenderView)
        setResult(VideoCode.FINISHPAGE_CODE)
        finish()
    }

    override fun sendIMSuccess() {
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

        if (id == R.id.tx_boardtools) {
            //点击画笔
            tx_board_view_business.visibility = if (isShowPaint) {
                //隐藏
                boardController?.isDrawEnable = true
                tx_boardtools.setImageResource(R.drawable.tx_paint_default)
                boardController!!.toolType =
                    TEduBoardController.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_ZOOM_DRAG
                View.GONE
            } else {
                //展示
                boardController?.isDrawEnable = true
                tx_boardtools.setImageResource(R.drawable.tx_paint_default)
                val color = PaintThickPopup.mColorMap[VideoActivity.paintColorPostion].color
                boardController!!.brushColor = TEduBoardController.TEduBoardColor(color!!)
                boardController!!.toolType =
                    TEduBoardController.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_PEN
                selectIb(tx_pen)
                View.VISIBLE
            }


            isShowPaint = !isShowPaint
        } else if (id == R.id.tx_pen) {
            //画笔
            val color = PaintThickPopup.mColorMap[VideoActivity.paintColorPostion].color
            val size = PaintThickPopup.mColorMap1[VideoActivity.paintSizeIntPostion].size
            boardController!!.brushColor = TEduBoardController.TEduBoardColor(color!!)
            boardController!!.brushThin = size
//            boardController!!.setGraphStyle(TEduBoardController.TEduBoardGraphStyle.solidLineEndArrow())
            boardController!!.setToolType(TEduBoardController.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_PEN)
//            boardController!!.setToolType(TEduBoardController.TEduBoardArrowType.TEDU_BOARD_ARROW_TYPE_SOLID)
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
            //橡皮擦
            boardController!!.toolType =
                TEduBoardController.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_ERASER
            //setPiecewiseErasureEnable

            selectIb(tx_eraser)
        } else if (id == R.id.tx_arrow) {

            val color = PaintThickPopup.mColorMap[VideoActivity.paintColorPostion].color
            val size = PaintThickPopup.mColorMap1[VideoActivity.paintSizeIntPostion].size
            boardController!!.brushColor = TEduBoardController.TEduBoardColor(color!!)
            boardController!!.brushThin = size
            //移动
            boardController!!.toolType =
                TEduBoardController.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_OVAL

            if (tx_arrow.isSelected) {
                showPopupWindow(
                    "2",
                    VideoActivity.paintColorPostion,
                    VideoActivity.paintSizeIntPostion
                )
            } else {
                tx_arrow.isSelected = true
            }

            selectIb(tx_arrow)
        } else if (id == R.id.tx_textstyle) {
            //字体大小
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
                //显示
                View.VISIBLE
            } else {
                View.GONE
            }
            tx_rlshow.isSelected = !selected
        } else if (id == R.id.iv_switchscreen) {
            switchScreen()
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
    var isLand = true
    //横竖屏切换逻辑
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        TxLogUtils.i("onConfigurationChanged")
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            TxLogUtils.i("Configuration.ORIENTATION_LANDSCAPE")
            val layoutParams1 = boardController?.boardRenderView?.layoutParams
            layoutParams1?.height = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams1?.width = Utils.getWindowHeight(this) / 9 * 16

            val bvlayoutParams = board_view_container.layoutParams
            bvlayoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
            bvlayoutParams?.width = Utils.getWindowHeight(this) / 9 * 16

            iv_switchscreen.isSelected = true
            isLand = true
            txWordDisplayView?.setLand(true);
            //
        } else {
            TxLogUtils.i("Configuration.ORIENTATION_PORTRAIT")
            val layoutParams1 = boardController?.boardRenderView?.layoutParams
            layoutParams1?.width = ViewGroup.LayoutParams.MATCH_PARENT
            layoutParams1?.height = Utils.getWindowWidth(this) / 16 * 9

            val bvlayoutParams = board_view_container.layoutParams
            bvlayoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT
            bvlayoutParams?.height = Utils.getWindowWidth(this) / 16 * 9

            iv_switchscreen.isSelected = false
            isLand = false
            txWordDisplayView?.setLand(true);
        }
        checkLand(isLand,false)
    }

    var paintThickPopup: PaintThickPopup? = null


    private fun showPopupWindow(type: String, paintColorPostion: Int, paintSizeIntPostion: Int) {
        paintThickPopup = PaintThickPopup(
            rl_board_business,
            R.layout.tx_layout_paintstyle
        )
        val xDp = if (type == "1") {
            10
        } else {
            70
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

    override fun onBackPressed() {
        endShare()
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
//        tx_board_view_business.visibility = View.VISIBLE
//        boardController?.isDrawEnable = true
//        tx_boardtools.setImageResource(R.drawable.tx_paint_default)
//        isShowPaint = true
//        val selectIB = videoBoradBusiness?.get(index)
//        selectIb(selectIB!!)

        if (isShowToolPop) {


        }
    }

    override fun onDestroy() {
        if (boardController != null) {
            boardController!!.reset()
            val boardview = boardController!!.boardRenderView
            if (board_view_container != null && boardview != null) {
                board_view_container!!.removeView(boardview)
            }
        }
        super.onDestroy()
    }

    override fun startShareSuccess(
        shareStatus: Boolean,
        url: String?,
        images: MutableList<String>?
    ) {
        //跳出白板页面
        finishPage()
    }

    override fun startShareFail(shareStatus: Boolean) {

    }

    override fun initBoard() {

        val instance = TICManager.getInstance()
        boardController = instance.boardController
        boardController?.addCallback(this)

        val windowWidth = Utils.getWindowHeight(this) / 9 * 16
        val layoutParams =
            FrameLayout.LayoutParams(
                windowWidth,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        if ( boardController?.boardRenderView?.getParent() != null) {
            ( boardController?.boardRenderView?.getParent() as ViewGroup).removeView(boardController?.boardRenderView)
        }

        board_view_container.addView(boardController?.boardRenderView, layoutParams)
        val layoutParams1 = board_view_container.layoutParams
        layoutParams1.width =windowWidth
        layoutParams1.height =  ViewGroup.LayoutParams.MATCH_PARENT
        boardController?.boardContentFitMode =
            TEduBoardController.TEduBoardContentFitMode.TEDU_BOARD_CONTENT_FIT_MODE_NONE
        TxLogUtils.i("txsdk---addBoardView")
        boardController?.isDrawEnable = false

        Handler().postDelayed({ initPicAdapter()},1000)

    }

    var mBoardCallback: MyBoardCallback? = null
    override fun joinClassroom() {
//       var groupId = intent.extras?.getString(IntentKey.GROUPID)
//        mBoardCallback =
//            MyBoardCallback(this)
//        //2、如果用户希望白板显示出来时，不使用系统默认的参数，就需要设置特性缺省参数，如是使用默认参数，则填null。
//        val initParam = TEduBoardController.TEduBoardInitParam()
//        initParam.brushColor = TEduBoardController.TEduBoardColor(255, 0, 0, 255)
//        initParam.smoothLevel = 0f //用于指定笔迹平滑级别，默认值0.1，取值[0, 1]
//        val classroomOption = TICClassroomOption()
//        classroomOption.apply {
//            classId = groupId?.toInt()!!
//            boardCallback = mBoardCallback
//            boardInitPara = initParam
//        }
//
//        TICManager.getInstance()?.joinClassroom(classroomOption, object : TICCallback<String> {
//            override fun onSuccess(data: String?) {
//                TxLogUtils.i("txsdk---joinClassroom:onSuccess---$data")
////                joinClass()
//            }
//
//            override fun onError(module: String?, errCode: Int, errMsg: String?) {
//                TxLogUtils.i("txsdk---joinClassroom:onError---$errCode----$errMsg")
//            }
//
//        })
    }

    fun addBoardView() {


    }

    override fun onTEBError(p0: Int, p1: String?) {
    }

    override fun onTEBWarning(p0: Int, p1: String?) {
    }

    override fun onTEBInit() {
        TxLogUtils.i("initPicAdapter","onTEBInit-----1")

    }

    override fun onTEBHistroyDataSyncCompleted() {
    }

    override fun onTEBSyncData(p0: String?) {
    }

    override fun onTEBUndoStatusChanged(p0: Boolean) {
    }

    override fun onTEBRedoStatusChanged(p0: Boolean) {
    }

    override fun onTEBImageStatusChanged(p0: String?, p1: String?, p2: Int) {
    }

    override fun onTEBSetBackgroundImage(p0: String?) {
    }

    override fun onTEBAddImageElement(p0: String?) {
    }

    override fun onTEBBackgroundH5StatusChanged(p0: String?, p1: String?, p2: Int) {
    }

    override fun onTEBAddBoard(p0: MutableList<String>?, p1: String?) {
        boardIdList?.clear()
        boardIdList?.addAll(p0!!)
//        boardController?.gotoBoard()
    }


    override fun onTEBDeleteBoard(p0: MutableList<String>?, p1: String?) {
    }

    override fun onTEBGotoBoard(p0: String?, p1: String?) {
    }

    override fun onTEBGotoStep(p0: Int, p1: Int) {
    }

    override fun onTEBRectSelected() {
    }

    override fun onTEBRefresh() {
    }

    override fun onTEBAddTranscodeFile(p0: String?) {
    }

    override fun onTEBDeleteFile(p0: String?) {
    }

    override fun onTEBSwitchFile(p0: String?) {
    }

    override fun onTEBFileUploadProgress(p0: String?, p1: Int, p2: Int, p3: Int, p4: Float) {
    }

    override fun onTEBFileUploadStatus(p0: String?, p1: Int, p2: Int, p3: String?) {
    }

    override fun onTEBFileTranscodeProgress(
        p0: String?,
        p1: String?,
        p2: String?,
        p3: TEduBoardController.TEduBoardTranscodeFileResult?
    ) {
    }

    override fun onTEBH5FileStatusChanged(p0: String?, p1: Int) {
    }

    override fun onTEBAddImagesFile(p0: String?) {
    }

    override fun onTEBVideoStatusChanged(p0: String?, p1: Int, p2: Float, p3: Float) {
    }

    override fun onTEBSnapshot(p0: String?, p1: Int, p2: String?) {
    }

    override fun onTEBH5PPTStatusChanged(p0: Int, p1: String?, p2: String?) {
    }


    var displayView: TUIBarrageDisplayView? = null
    var tuiBarbt: TUIBarrageButton? = null
    var groupId = ""
    private fun initBarrage(groupId: String, serviceId: String) {
        this.groupId = groupId
        //弹幕发送View
        tuiBarbt = TUIBarrageButton(this, groupId, serviceId)
        //弹幕显示View
        displayView = TUIBarrageDisplayView(this, groupId)
        TUIBarragePresenter.sharedInstance().initDisplayView(displayView)
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
            }

            override fun onFailed(code: Int, msg: String?) {

            }

        })

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

    fun setConstrainSet(){
        //iv_switchscreen
        //rl_word_show
        val constraintSet = ConstraintSet()
        constraintSet.clone(layout_root)
        constraintSet.clear(R.id.iv_switchscreen,ConstraintSet.RIGHT)
        constraintSet.clear(R.id.iv_switchscreen,ConstraintSet.BOTTOM)
        constraintSet.clear(R.id.iv_switchscreen,ConstraintSet.TOP)
        constraintSet.clear(R.id.iv_switchscreen,ConstraintSet.LEFT)
        constraintSet.connect(R.id.iv_switchscreen,ConstraintSet.RIGHT,R.id.layout_root,ConstraintSet.RIGHT)
        constraintSet.connect(R.id.iv_switchscreen,ConstraintSet.BOTTOM,R.id.ll_borads,ConstraintSet.TOP)

        constraintSet.clear(R.id.rl_word_show,ConstraintSet.RIGHT)
        constraintSet.clear(R.id.rl_word_show,ConstraintSet.BOTTOM)
        constraintSet.clear(R.id.rl_word_show,ConstraintSet.TOP)
        constraintSet.clear(R.id.rl_word_show,ConstraintSet.LEFT)
        constraintSet.connect(R.id.rl_word_show,ConstraintSet.TOP,R.id.iv_endshare,ConstraintSet.BOTTOM)
        constraintSet.connect(R.id.rl_word_show,ConstraintSet.RIGHT,R.id.layout_root,ConstraintSet.RIGHT)

        constraintSet.applyTo(layout_root)
    }

    fun setConstrainSetPro(){
        //iv_switchscreen
        //rl_word_show
        val constraintSet = ConstraintSet()
        constraintSet.clone(layout_root)
        constraintSet.clear(R.id.iv_switchscreen,ConstraintSet.RIGHT)
        constraintSet.clear(R.id.iv_switchscreen,ConstraintSet.BOTTOM)
        constraintSet.clear(R.id.iv_switchscreen,ConstraintSet.TOP)
        constraintSet.clear(R.id.iv_switchscreen,ConstraintSet.LEFT)
        constraintSet.connect(R.id.iv_switchscreen,ConstraintSet.RIGHT,R.id.layout_root,ConstraintSet.RIGHT)
        constraintSet.connect(R.id.iv_switchscreen,ConstraintSet.BOTTOM,R.id.ll_borads,ConstraintSet.TOP)
        constraintSet.connect(R.id.iv_switchscreen,ConstraintSet.BOTTOM,R.id.rl_barrage_audience,ConstraintSet.BOTTOM)

        constraintSet.clear(R.id.rl_word_show,ConstraintSet.RIGHT)
        constraintSet.clear(R.id.rl_word_show,ConstraintSet.BOTTOM)
        constraintSet.clear(R.id.rl_word_show,ConstraintSet.TOP)
        constraintSet.clear(R.id.rl_word_show,ConstraintSet.LEFT)
        constraintSet.connect(R.id.rl_word_show,ConstraintSet.TOP,R.id.iv_endshare,ConstraintSet.BOTTOM)
        constraintSet.connect(R.id.rl_word_show,ConstraintSet.RIGHT,R.id.layout_root,ConstraintSet.RIGHT)

        constraintSet.applyTo(layout_root)
    }


    fun setConstrainSetHasWord(){
        //iv_switchscreen
        //rl_word_show
        val constraintSet = ConstraintSet()
        constraintSet.clone(layout_root)
        constraintSet.clear(R.id.iv_switchscreen,ConstraintSet.RIGHT)
        constraintSet.clear(R.id.iv_switchscreen,ConstraintSet.BOTTOM)
        constraintSet.clear(R.id.iv_switchscreen,ConstraintSet.TOP)
        constraintSet.clear(R.id.iv_switchscreen,ConstraintSet.LEFT)
        constraintSet.connect(R.id.iv_switchscreen,ConstraintSet.BOTTOM,R.id.ll_borads,ConstraintSet.TOP)
        constraintSet.connect(R.id.iv_switchscreen,ConstraintSet.RIGHT,R.id.rl_word_show,ConstraintSet.LEFT)

        constraintSet.clear(R.id.rl_word_show,ConstraintSet.RIGHT)
        constraintSet.clear(R.id.rl_word_show,ConstraintSet.BOTTOM)
        constraintSet.clear(R.id.rl_word_show,ConstraintSet.TOP)
        constraintSet.clear(R.id.rl_word_show,ConstraintSet.LEFT)
        constraintSet.connect(R.id.rl_word_show,ConstraintSet.RIGHT,R.id.layout_root,ConstraintSet.RIGHT)
        constraintSet.connect(R.id.rl_word_show,ConstraintSet.BOTTOM,R.id.ll_borads,ConstraintSet.TOP)
        constraintSet.connect(R.id.rl_word_show,ConstraintSet.TOP,R.id.iv_endshare,ConstraintSet.BOTTOM)
        constraintSet.connect(R.id.rl_word_show,ConstraintSet.BOTTOM,R.id.iv_switchscreen,ConstraintSet.BOTTOM)

        constraintSet.applyTo(layout_root)
    }

    fun setConstrainSetHasWordPro(){
        //iv_switchscreen
        //rl_word_show
        val constraintSet = ConstraintSet()
        constraintSet.clone(layout_root)
        constraintSet.clear(R.id.iv_switchscreen,ConstraintSet.RIGHT)
        constraintSet.clear(R.id.iv_switchscreen,ConstraintSet.BOTTOM)
        constraintSet.clear(R.id.iv_switchscreen,ConstraintSet.TOP)
        constraintSet.clear(R.id.iv_switchscreen,ConstraintSet.LEFT)
        constraintSet.connect(R.id.iv_switchscreen,ConstraintSet.RIGHT,R.id.layout_root,ConstraintSet.RIGHT)
        constraintSet.connect(R.id.iv_switchscreen,ConstraintSet.BOTTOM,R.id.ll_borads,ConstraintSet.TOP)

        constraintSet.clear(R.id.rl_word_show,ConstraintSet.RIGHT)
        constraintSet.clear(R.id.rl_word_show,ConstraintSet.BOTTOM)
        constraintSet.clear(R.id.rl_word_show,ConstraintSet.TOP)
        constraintSet.clear(R.id.rl_word_show,ConstraintSet.LEFT)
        constraintSet.connect(R.id.rl_word_show,ConstraintSet.RIGHT,R.id.layout_root,ConstraintSet.RIGHT)
        constraintSet.connect(R.id.rl_word_show,ConstraintSet.TOP,R.id.iv_endshare,ConstraintSet.BOTTOM)

        constraintSet.applyTo(layout_root)
    }

    fun checkLand(isLand :Boolean,hasWord1:Boolean){
        try {
            var hasWord = txWordDisplayView?.getContent()!!

            TxLogUtils.i("checkLand","isLand:"+isLand +"-----hasWord"+hasWord)
            if (isLand) {
                if (!hasWord.isEmpty() && txWordDisplayView?.isSelect!!) {

                    setConstrainSetHasWord()
                    //改变布局
                    txWordDisplayView?.changView()
                }else{
                    setConstrainSet()
                }

            }else{
                if (!hasWord.isEmpty()&& txWordDisplayView?.isSelect!!) {
                    setConstrainSetHasWordPro()
                    //改变布局
                    txWordDisplayView?.changView()
                }else{
                    setConstrainSetPro()
                }
            }
        }catch (e:Exception){

        }

    }

}