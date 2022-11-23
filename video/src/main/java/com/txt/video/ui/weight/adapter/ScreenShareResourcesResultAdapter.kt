package com.txt.video.ui.weight.adapter

import android.widget.ImageView
import android.widget.TextView
import com.txt.video.R
import com.txt.video.common.adapter.base.TxBaseQuickAdapter
import com.txt.video.common.adapter.base.TxBaseViewHolder
import com.txt.video.common.glide.TxGlide
import com.txt.video.net.bean.ResourceTypeBean
import com.txt.video.ui.weight.RoundTransform

/**
 * Created by JustinWjq
 * @date 2021/2/4.
 * description： 共享资源展示
 */
public class ScreenShareResourcesResultAdapter  :
    TxBaseQuickAdapter<ResourceTypeBean, TxBaseViewHolder>(R.layout.tx_adapter_resources_list_item_result){
    override fun convert(helper: TxBaseViewHolder, item: ResourceTypeBean) {
        helper.getView<TextView>(R.id.tx_tv_name).setText(item.name)

        TxGlide.with( helper.itemView.context).load(item.picUrl) //图片地址
            .transform(
                RoundTransform(
                    helper.itemView.context,
                    10
                )
            )
            .placeholder(R.drawable.tx_icon_resouce_placem)
            .into(helper.getView<ImageView>(R.id.iv))
    }
}