package com.txt.video.ui.weight.adapter

import android.graphics.Color
import androidx.core.content.ContextCompat
import android.widget.RelativeLayout
import com.txt.video.R
import com.txt.video.net.bean.ToolType
import com.txt.video.common.CircleImageView
import com.txt.video.common.adapter.base.TxBaseQuickAdapter
import com.txt.video.common.adapter.base.TxBaseViewHolder

/**
 * Created by JustinWjq
 * @date 2021/2/4.
 * description：
 */
public class PaintColorAdapter : TxBaseQuickAdapter<ToolType, TxBaseViewHolder>(R.layout.tx_layout_paint) {
    override fun convert(helper: TxBaseViewHolder, item: ToolType?) {
        val rl_bg = helper.getView<RelativeLayout>(R.id.rl_bg)
        val rl_colors = helper.getView<CircleImageView>(R.id.rl_colors)

        rl_bg.background  = ContextCompat.getDrawable(helper.itemView.context,
                 if (item?.isSelect!!) {
                     R.drawable.tx_shape_border_3b71ee
                }else{
                     R.drawable.tx_shape_border_white
                }
        )

        rl_colors.circleBackgroundColor = Color.parseColor(item.color)
    }

}