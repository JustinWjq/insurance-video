package com.txt.video.ui.weight.dialog

import android.graphics.Color
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
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
import com.txt.video.net.bean.EmojiBean
import com.txt.video.ui.weight.EmojiAdapter

/**
 * Created by JustinWjq
 *
 * @date 2021/2/2
 * description：empji表情展示
 */
open class EmojiPopup : TransformersTip {
    companion object {
        val mColorMap = arrayListOf<EmojiBean>(
            EmojiBean("\uD83D\uDE04"),
            EmojiBean("\uD83E\uDD23"),
            EmojiBean("\uD83D\uDE02"),
            EmojiBean("\uD83D\uDE06"),
            EmojiBean("\uD83E\uDD14"),
            EmojiBean("\uD83D\uDE31"),
            EmojiBean("\uD83D\uDE25"),
            EmojiBean("\uD83D\uDE2D"),
            EmojiBean("\uD83D\uDC4F"),
            EmojiBean("\uD83D\uDC4B"),
            EmojiBean("\uD83D\uDC4D"),
            EmojiBean("\uD83E\uDD1D"),
            EmojiBean("✌️"),
            EmojiBean("\uD83D\uDC4C"),
            EmojiBean("\uD83D\uDCAA"),
            EmojiBean("\uD83D\uDE4F"),
            EmojiBean("✊"),
            EmojiBean("\uD83E\uDD19")
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


    private var mListener: onEmojiListener? = null
    open fun setOnCheckDialogListener(listener: onEmojiListener) {
        mListener = listener
    }

    var emojiAdapter : EmojiAdapter?=null
    override fun initView(contentView: View) {
        super.initView(contentView)
        val recyclerview1 = contentView.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.rl_emoji)
        recyclerview1.layoutManager = GridLayoutManager(contentView.context,6)
        emojiAdapter = EmojiAdapter()
        recyclerview1.adapter = emojiAdapter
        emojiAdapter?.setNewData(mColorMap)
        emojiAdapter?.setOnItemClickListener { adapter, view, position ->
            var bean = mColorMap.get(position)
            mListener?.onEmojiClick(bean)
        }
    }



    override fun customAttributes() {
        setArrowGravity(ArrowGravity.TO_BOTTOM_ALIGN_START) // 设置箭头相对于浮窗的位置
        setBgColor(Color.parseColor("#ff222222")) // 设置背景色
        setShadowColor(Color.parseColor("#33000000")) // 设置阴影色
        setArrowHeightDp(0) // 设置箭头高度
        setRadiusDp(4) // 设置浮窗圆角半径
        setArrowOffsetYDp(0) // 设置箭头在 y 轴的偏移量
        setShadowSizeDp(6) // 设置阴影宽度
        setTipGravity(TipGravity.TO_TOP_CENTER) // 设置浮窗相对于锚点控件展示的位置
        setTipOffsetXDp(0) // 设置浮窗在 x 轴的偏移量
        setTipOffsetYDp(-10) // 设置浮窗在 y 轴的偏移量
        setBackgroundDimEnabled(false) // 设置是否允许浮窗的背景变暗
        setDismissOnTouchOutside(true) // 设置点击浮窗外部时是否自动关闭浮窗
    }

    interface onEmojiListener {
        fun onEmojiClick( emojiBean :EmojiBean)

    }
}