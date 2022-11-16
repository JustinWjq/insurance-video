package com.txt.video.ui.weight.dialog

import android.graphics.Color
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.txt.video.R
import com.txt.video.net.bean.ThickType
import com.txt.video.net.bean.ToolType
import com.txt.video.ui.weight.adapter.PaintColorAdapter
import com.txt.video.ui.weight.PaintThickAdapter
import com.txt.video.common.callback.onCheckDialogListenerCallBack
import com.txt.video.common.callback.onShareWhiteBroadDialogListener
import com.txt.video.common.dialog.config.TransformersTip
import com.txt.video.common.dialog.config.gravity.ArrowGravity
import com.txt.video.common.dialog.config.gravity.TipGravity

/**
 * Created by JustinWjq
 *
 * @date 2021/2/2
 * description：
 */
open class ShareScreenPopup : TransformersTip {



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


    private var mListener: onShareWhiteBroadDialogListener? = null
    open fun setOnCheckDialogListener(listener: onShareWhiteBroadDialogListener) {
        mListener = listener
    }


    override fun initView(contentView: View) {
        super.initView(contentView)
        contentView.findViewById<TextView>(R.id.tv_file).setOnClickListener {
            //点击文件
            if (null != mListener) {
                mListener?.onCheckFileWhiteBroad()
                dismissTip()
            }
        }
        contentView.findViewById<TextView>(R.id.tv_whitebroad).setOnClickListener {
            //点击白板
            if (null != mListener) {
                mListener?.onCheckBroad()
                dismissTip()
            }
        }

    }



    override fun customAttributes() {
        setArrowGravity(ArrowGravity.TO_BOTTOM_ALIGN_START) // 设置箭头相对于浮窗的位置
        setBgColor(Color.WHITE) // 设置背景色
        setShadowColor(Color.parseColor("#33000000")) // 设置阴影色
        setArrowHeightDp(6) // 设置箭头高度
        setRadiusDp(4) // 设置浮窗圆角半径
        setArrowOffsetYDp(0) // 设置箭头在 y 轴的偏移量
        setShadowSizeDp(6) // 设置阴影宽度
        setTipGravity(TipGravity.TO_TOP_CENTER) // 设置浮窗相对于锚点控件展示的位置
        setTipOffsetXDp(0) // 设置浮窗在 x 轴的偏移量
        setTipOffsetYDp(-10) // 设置浮窗在 y 轴的偏移量
        setBackgroundDimEnabled(false) // 设置是否允许浮窗的背景变暗
        setDismissOnTouchOutside(true) // 设置点击浮窗外部时是否自动关闭浮窗
    }
}