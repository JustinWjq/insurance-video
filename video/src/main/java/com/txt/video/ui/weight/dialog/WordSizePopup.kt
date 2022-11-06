package com.txt.video.ui.weight.dialog

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.txt.video.R
import com.txt.video.common.dialog.config.TransformersTip
import com.txt.video.common.dialog.config.gravity.ArrowGravity
import com.txt.video.common.dialog.config.gravity.TipGravity

/**
 * Created by JustinWjq
 *
 * @date 2021/2/2
 * description：
 */
open class WordSizePopup : TransformersTip {



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


    private var mListener: onWordSizePopupListener? = null
    open fun setOnCheckDialogListener(listener: onWordSizePopupListener) {
        mListener = listener
    }


    override fun initView(contentView: View) {
        super.initView(contentView)
        contentView.findViewById<TextView>(R.id.tv_size_big).setOnClickListener {
            if (null != mListener) {
                mListener?.onBigSize()
                dismissTip()
            }
        }
        contentView.findViewById<TextView>(R.id.tv_size_medium).setOnClickListener {
            if (null != mListener) {
                mListener?.onMediumSize()
                dismissTip()
            }
        }
        contentView.findViewById<TextView>(R.id.tv_size_small).setOnClickListener {
            if (null != mListener) {
                mListener?.onSmallSize()
                dismissTip()
            }
        }


    }



    override fun customAttributes() {
        setArrowGravity(ArrowGravity.ALIGN_BOTTOM_TO_END) // 设置箭头相对于浮窗的位置
        setShadowColor(Color.parseColor("#33000000")) // 设置阴影色
        setArrowHeightDp(6) // 设置箭头高度
        setRadiusDp(4) // 设置浮窗圆角半径
        setArrowOffsetYDp(0) // 设置箭头在 y 轴的偏移量
        setShadowSizeDp(6) // 设置阴影宽度
        setTipGravity(TipGravity.ALIGN_BOTTOM_TO_START) // 设置浮窗相对于锚点控件展示的位置
        setTipOffsetXDp(20) // 设置浮窗在 x 轴的偏移量
        setTipOffsetYDp(0) // 设置浮窗在 y 轴的偏移量
        setBackgroundDimEnabled(false) // 设置是否允许浮窗的背景变暗
        setDismissOnTouchOutside(true) // 设置点击浮窗外部时是否自动关闭浮窗
    }

    interface onWordSizePopupListener {
        fun onBigSize()
        fun onMediumSize()
        fun onSmallSize()

    }
}