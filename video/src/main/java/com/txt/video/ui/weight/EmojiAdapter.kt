package com.txt.video.ui.weight

import android.widget.ImageView
import android.widget.TextView
import com.txt.video.R
import com.txt.video.net.bean.ThickType
import com.txt.video.common.adapter.base.TxBaseQuickAdapter
import com.txt.video.common.adapter.base.TxBaseViewHolder
import com.txt.video.net.bean.EmojiBean

/**
 * Created by JustinWjq
 * @date 2021/2/4.
 * description：白板工具选择的adapter
 */
public class EmojiAdapter :
    TxBaseQuickAdapter<EmojiBean, TxBaseViewHolder>(R.layout.tx_layout_item_emoji) {
    override fun convert(helper: TxBaseViewHolder, item: EmojiBean?) {
        val tv_emoji = helper.getView<TextView>(R.id.tv_emoji)

        //将当前 code 转换为 16 进制数
//        var hex = Integer.parseInt(item!!.code.replace("U+",""), 16)
//        //将当前 16 进制数转换成字符数组
//        var chars = Character.toChars(hex);
//        //将当前字符数组转换成 TextView 可加载的 String 字符串
//        var mEmojiString = String(chars)
        tv_emoji.text = item!!.code
    }
}