package com.txt.video.ui.weight.adapter

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import android.widget.RelativeLayout
import android.widget.TextView
import com.txt.video.R
import com.txt.video.net.bean.ToolType
import com.txt.video.common.CircleImageView
import com.txt.video.common.adapter.base.TxBaseQuickAdapter
import com.txt.video.common.adapter.base.TxBaseViewHolder

/**
 * Created by JustinWjq
 * @date 2021/2/4.
 * description：显示颜色的
 */
public class PaintColorAdapter : TxBaseQuickAdapter<ToolType, TxBaseViewHolder>(R.layout.tx_layout_paint) {
    override fun convert(helper: TxBaseViewHolder, item: ToolType?) {
        val rl_colors = helper.getView<TextView>(R.id.rl_colors)

        val tv_gou = helper.getView<ImageView>(R.id.tv_gou)
        if (item?.isSelect!!) {
            tv_gou.visibility = View.VISIBLE
        }else{
            tv_gou.visibility = View.GONE
        }
        rl_colors.setBackgroundColor( Color.parseColor(item.color))
    }

}