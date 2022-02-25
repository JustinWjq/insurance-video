package com.txt.myapplication.demo

import android.widget.ImageView
import com.txt.myapplication.R
import com.txt.video.TXSdk
import com.txt.video.common.adapter.base.TxBaseQuickAdapter
import com.txt.video.common.adapter.base.TxBaseViewHolder

/**
 * Created by JustinWjq
 * @date 2020/9/3.
 * description：显示白板展示多页图片的adapter
 */
public class PicQuickAdapter :
    TxBaseQuickAdapter<String, TxBaseViewHolder>(R.layout.layout_pic, null){
    override fun convert(holder: TxBaseViewHolder, item: String) {
//        val itemPosition = this.data.indexOf(item)
//        val view = holder.getView<ImageView>(R.id.tx_imageView)
//        holder.setText(R.id.tx_tv_count, "${itemPosition + 1}")

    }

}