package com.txt.video.ui.weight.adapter

import com.txt.video.R
import com.txt.video.common.adapter.base.TxBaseQuickAdapter
import com.txt.video.common.adapter.base.TxBaseViewHolder
import com.txt.video.net.bean.ResourcegsConditionsBean

/**
 * Created by JustinWjq
 * @date 2021/2/4.
 * description： 远程会议增员
 */
public class ScreenShareResourcesAdapter1  :
    TxBaseQuickAdapter<ResourcegsConditionsBean, TxBaseViewHolder>(R.layout.tx_adapter_resources_list_item_check1){
    override fun convert(helper: TxBaseViewHolder, item: ResourcegsConditionsBean?) {
        helper.setText(R.id.tx_tv_name,item?.name)
        if (item!!.isCheck) {
            helper.setBackgroundRes(R.id.tx_layout,R.drawable.tx_shape_border_e6b980_50)
        }else{
            helper.setBackgroundRes(R.id.tx_layout,R.drawable.tx_button_border_selector_black_50)
        }



    }
}