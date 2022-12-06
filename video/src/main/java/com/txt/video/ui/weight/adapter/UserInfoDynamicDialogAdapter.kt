package com.txt.video.ui.weight.adapter

import com.txt.video.R
import com.txt.video.common.adapter.base.TxBaseQuickAdapter
import com.txt.video.common.adapter.base.TxBaseViewHolder
import com.txt.video.net.bean.UserInfoBean

/**
 * author ：Justin
 * time ：2022/11/28.
 * des ：用户信息展示
 */
public class UserInfoDynamicDialogAdapter : TxBaseQuickAdapter<UserInfoBean, TxBaseViewHolder>(R.layout.tx_adapter_userinfo_item){
    override fun convert(helper: TxBaseViewHolder, item: UserInfoBean?) {
        helper.setText(R.id.tv_name,item?.title)
        helper.setText(R.id.tv_content,item?.content)
    }
}
