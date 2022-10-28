package com.txt.video.ui.weight.adapter

import android.widget.ImageView
import android.widget.TextView
import com.txt.video.R
import com.txt.video.common.adapter.base.TxBaseQuickAdapter
import com.txt.video.common.adapter.base.TxBaseViewHolder
import com.txt.video.net.bean.*

/**
 * Created by JustinWjq
 * @date 2021/2/4.
 * description： 远程会议筛选选择
 */
public class ScreenShareResourcesAdapter  :
    TxBaseQuickAdapter<ResourcegsConditionsBean, TxBaseViewHolder>(R.layout.tx_adapter_resources_list_item_check){
    override fun convert(helper: TxBaseViewHolder, item: ResourcegsConditionsBean?) {
        if (item!!.isCheck) {
            helper.setVisible(R.id.iv_check,true)
            helper.setBackgroundRes(R.id.layout_res,R.drawable.tx_button_border_selector_red)
        }else{
            helper.setVisible(R.id.iv_check,false)
            helper.setBackgroundRes(R.id.layout_res,R.drawable.tx_button_border_selector_black)
        }


        helper.getView<TextView>(R.id.tx_tv_name).setText(item?.name)
    }
}