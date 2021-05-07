package com.txt.video.ui.weight

import android.widget.ImageView
import com.txt.video.R
import com.txt.video.net.bean.ThickType
import com.txt.video.common.adapter.base.TxBaseQuickAdapter
import com.txt.video.common.adapter.base.TxBaseViewHolder

/**
 * Created by JustinWjq
 * @date 2021/2/4.
 * description：白板工具选择的adapter
 */
public class PaintThickAdapter  :
    TxBaseQuickAdapter<ThickType, TxBaseViewHolder>(R.layout.tx_layout_paint){
    override fun convert(helper: TxBaseViewHolder, item: ThickType?) {
        val iv = helper.getView<ImageView>(R.id.rl_colors)
        iv.setImageResource(
            if (item?.isSelect!!) {
                item.selectSrc
            }else{
                item.defaultSrc
            }

        )
    }
}