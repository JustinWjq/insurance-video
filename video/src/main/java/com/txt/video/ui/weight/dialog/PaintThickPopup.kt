package com.txt.video.ui.weight.dialog

import android.graphics.Color
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.txt.video.R
import com.txt.video.net.bean.ThickType
import com.txt.video.net.bean.ToolType
import com.txt.video.ui.weight.adapter.PaintColorAdapter
import com.txt.video.ui.weight.PaintThickAdapter
import com.txt.video.common.callback.onCheckDialogListenerCallBack
import com.txt.video.common.dialog.config.TransformersTip
import com.txt.video.common.dialog.config.gravity.ArrowGravity
import com.txt.video.common.dialog.config.gravity.TipGravity

/**
 * Created by JustinWjq
 *
 * @date 2021/2/2
 * description：
 */
open class PaintThickPopup : TransformersTip {
    companion object {
        val mColorMap = arrayListOf<ToolType>(
            ToolType("", "#333333", true),
            ToolType("", "#FF4848", false),
            ToolType("", "#1AD27C", false),
            ToolType("", "#FDC126", false),
            ToolType("", "#9F2DFF", false),
            ToolType("", "#4085FF", false)
        )


        val mColorMap1 = arrayListOf<ThickType>(
            ThickType(
                "白",
                R.drawable.tx_icon_thick_default_1,
                R.drawable.tx_icon_thick_check_1,
                50,
                false
            ),
            ThickType(
                "白",
                R.drawable.tx_icon_thick_default_2,
                R.drawable.tx_icon_thick_check_2,
                100,
                true
            ),
            ThickType(
                "白",
                R.drawable.tx_icon_thick_default_3,
                R.drawable.tx_icon_thick_check_3,
                130,
                false
            )
        )


        val mTextMap = arrayListOf<ThickType>(
            ThickType(
                "白",
                R.drawable.tx_icon_text_default_1,
                R.drawable.tx_icon_text_check_1,
                500,
                false
            ),
            ThickType(
                "白",
                R.drawable.tx_icon_text_default_2,
                R.drawable.tx_icon_text_check_2,
                600,
                true
            ),
            ThickType(
                "白",
                R.drawable.tx_icon_text_default_3,
                R.drawable.tx_icon_text_check_3,
                700,
                false
            ),
            ThickType(
                "白",
                R.drawable.tx_icon_text_default_4,
                R.drawable.tx_icon_text_check_4,
                800,
                false
            ),
            ThickType(
                "白",
                R.drawable.tx_icon_text_default_5,
                R.drawable.tx_icon_text_check_5,
                1000,
                false
            )
        )

    }


    constructor(anchorView: View?, contentView: View?) : super(
        anchorView,
        contentView
    ) {
    }

    constructor(anchorView: View?, layoutResId: Int) : super(
        anchorView,
        layoutResId
    ) {
    }

    var mPaintColorAdapter: PaintColorAdapter? = null
    var mPaintThickAdapter: PaintThickAdapter? = null

    private var mListener: onCheckDialogListenerCallBack? = null
    open fun setOnCheckDialogListener(listener: onCheckDialogListenerCallBack) {
        mListener = listener
    }


    override fun initView(contentView: View) {
        super.initView(contentView)
        // 点击浮窗中自定按钮关闭浮窗

        mPaintColorAdapter = PaintColorAdapter()
        mPaintThickAdapter = PaintThickAdapter()

        val recyclerview = contentView.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rl_thick)
        recyclerview.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(
                contentView.context,
                androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
                false
            )
        recyclerview.adapter = mPaintThickAdapter
//        mPaintThickAdapter?.bindToRecyclerView(recyclerview)




        val recyclerview1 = contentView.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rl_paintcolors)
        recyclerview1.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(
                contentView.context,
                androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,
                false
            )
        recyclerview1.adapter =mPaintColorAdapter
//        mPaintColorAdapter?.bindToRecyclerView(recyclerview1)

        mPaintThickAdapter?.setOnItemClickListener { adapter, view, position ->

            val toolType = if (true) {
                mColorMap1[position]
            } else {
                mTextMap[position]
            }
            checkThick(position)
            mListener?.onCheckThick(position,toolType,type!!)
        }

        mPaintColorAdapter?.setOnItemClickListener { adapter, view, position ->
            val toolType = mColorMap[position]
            checkColor(position)
            mListener?.onCheckColor(position,toolType,type!!)
        }

        refreshUI("1",0,1)
    }

    private var type: String? = null

    public fun refreshUI(type: String,paintColorPostion:Int,paintSizeIntPostion:Int) {
        //1,2
        this.type = type

        checkColor(paintColorPostion)
        checkThick(paintSizeIntPostion)
        //画笔
        mPaintColorAdapter?.setNewData(mColorMap)
        mPaintThickAdapter?.setNewData(mColorMap1)
//        if (type == "1") {
//
//        } else {
//            //字体
//            mPaintColorAdapter?.setNewData(mColorMap)
//            mPaintThickAdapter?.setNewData(mTextMap)
//        }

    }

    public fun checkColor(paintColorPostion:Int) {
        mColorMap.forEach {
            it.isSelect = false
        }
        mColorMap[paintColorPostion].isSelect = true

        mPaintColorAdapter?.notifyDataSetChanged()
    }

    public fun checkThick(postion:Int) {
        if (true) {
            mColorMap1.forEach {
                it.isSelect = false
            }
            mColorMap1[postion].isSelect = true
        } else {
            mTextMap.forEach {
                it.isSelect = false
            }
            mTextMap[postion].isSelect = true
        }
        mPaintThickAdapter?.notifyDataSetChanged()
    }


    override fun customAttributes() {
        setArrowGravity(ArrowGravity.TO_BOTTOM_ALIGN_START) // 设置箭头相对于浮窗的位置
        setBgColor(Color.WHITE) // 设置背景色
        setShadowColor(Color.parseColor("#33000000")) // 设置阴影色
        setArrowHeightDp(6) // 设置箭头高度
        setRadiusDp(4) // 设置浮窗圆角半径
        width = 600
        setArrowOffsetYDp(0) // 设置箭头在 y 轴的偏移量
        setShadowSizeDp(6) // 设置阴影宽度
        setTipGravity(TipGravity.TO_TOP_TO_START) // 设置浮窗相对于锚点控件展示的位置
        setTipOffsetXDp(0) // 设置浮窗在 x 轴的偏移量
        setTipOffsetYDp(-10) // 设置浮窗在 y 轴的偏移量
        setBackgroundDimEnabled(false) // 设置是否允许浮窗的背景变暗
        setDismissOnTouchOutside(true) // 设置点击浮窗外部时是否自动关闭浮窗
    }
}