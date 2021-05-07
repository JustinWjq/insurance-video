package com.txt.video.ui.weight

import android.widget.ImageView
import com.txt.video.R
import com.txt.video.TXSdk
import com.txt.video.common.adapter.base.TxBaseQuickAdapter
import com.txt.video.common.adapter.base.TxBaseViewHolder
import com.txt.video.common.glide.TxGlide

/**
 * Created by JustinWjq
 * @date 2020/9/3.
 * description：显示白板展示多页图片的adapter
 */
public class PicQuickAdapter :
    TxBaseQuickAdapter<String, TxBaseViewHolder>(R.layout.tx_adapter_pic_list_item, null){
    override fun convert(holder: TxBaseViewHolder, item: String) {
        val itemPosition = this.data.indexOf(item)
        val view = holder.getView<ImageView>(R.id.tx_imageView)
        holder.setText(R.id.tx_tv_count, "${itemPosition + 1}")

        TxGlide.with(TXSdk.getInstance().application).load(item).into(view)
    }

}