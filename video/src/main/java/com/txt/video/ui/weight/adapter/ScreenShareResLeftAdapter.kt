package com.txt.video.ui.weight.adapter

import com.txt.video.R
import com.txt.video.common.adapter.base.TxBaseQuickAdapter
import com.txt.video.common.adapter.base.TxBaseViewHolder
import com.txt.video.net.bean.ResourcegsConditionsBean

/**
 * Created by JustinWjq
 * @date 2021/2/4.
 * description： 共享资源展示
 */
public class ScreenShareResLeftAdapter  :
    TxBaseQuickAdapter<ResourcegsConditionsBean, TxBaseViewHolder>(R.layout.tx_adapter_resources_list_item_left){
    override fun convert(helper: TxBaseViewHolder, item: ResourcegsConditionsBean?) {
        helper.setText(R.id.tv_name,item?.name)
        helper.setVisible(R.id.tv_div,item!!.isCheck)
        if (item!!.isCheck) {
            helper.setTextColor(R.id.tv_name,helper.itemView.resources.getColor(R.color.tx_color_e6b980));
        }else{
            helper.setTextColor(R.id.tv_name,helper.itemView.resources.getColor(R.color.tx_white));
        }

    }
}