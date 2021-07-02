package com.txt.video.ui.weight.adapter

import android.widget.ImageView
import com.txt.video.R
import com.txt.video.common.adapter.base.TxBaseQuickAdapter
import com.txt.video.common.adapter.base.TxBaseViewHolder
import com.txt.video.net.bean.PersonBean
import com.txt.video.net.bean.ThickType

/**
 * Created by JustinWjq
 * @date 2021/2/4.
 * description：
 */
public class PersonNameAdapter  :
    TxBaseQuickAdapter<PersonBean, TxBaseViewHolder>(R.layout.tx_adapter_person_list_item){
    override fun convert(helper: TxBaseViewHolder, item: PersonBean?) {
        helper.setText(R.id.tx_tv_name,item?.name)
    }
}