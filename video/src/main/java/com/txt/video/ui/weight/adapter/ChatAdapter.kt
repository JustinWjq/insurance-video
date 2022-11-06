package com.txt.video.ui.weight.adapter

import com.txt.video.R
import com.txt.video.common.adapter.base.BaseMultiItemQuickAdapter
import com.txt.video.common.adapter.base.TxBaseViewHolder
import com.txt.video.common.adapter.base.entity.MultiItemEntity
import com.txt.video.ui.video.barrage.view.adapter.TUIBarrageMsgEntity

/**
 * Created by JustinWjq
 * @date 2020/8/31.
 * description： 聊天记录
 */
public class ChatAdapter(var data: ArrayList<MultiItemEntity>) :
    BaseMultiItemQuickAdapter<MultiItemEntity, TxBaseViewHolder>(
        data
    ) {
    val TYPE_LEVEL_0 = 0 //左边聊天
    val TYPE_LEVEL_1 = 1 //右边聊天 是自己

    init {
        addItemType(TYPE_LEVEL_0, R.layout.tx_adapter_item_chat_left)
        addItemType(TYPE_LEVEL_1, R.layout.tx_adapter_item_chat_right)

    }

    override fun convert(helper: TxBaseViewHolder, item: MultiItemEntity?) {
        when (helper.itemViewType) {
            TYPE_LEVEL_0 -> {
                val chatBean = item as TUIBarrageMsgEntity
                helper.setText(R.id.iv_ro_name,getUserName(chatBean.userName))
                helper.setText(R.id.iv_name,chatBean.userName)
                val content = chatBean.content
//                val contains: Boolean = content.contains("U+")
//                var result = ""
//                result = if (contains) {
//                    val hex: Int = chatBean.content.replace("U+", "").toInt(16)
//                    //将当前 16 进制数转换成字符数组
//                    val chars = Character.toChars(hex)
//                    //将当前字符数组转换成 TextView 可加载的 String 字符串
//                    val mEmojiString = String(chars)
//                     mEmojiString
//                } else {
//                    content
//                }

                helper.setText(R.id.tv_message,content)

            }
            else -> {
                val chatBean = item as TUIBarrageMsgEntity
                helper.setText(R.id.iv_ro_name,getUserName(chatBean.userName))
                helper.setText(R.id.iv_name,chatBean.userName)
                val content = chatBean.content
//                val contains: Boolean = content.contains("U+")
//                var result = ""
//                result = if (contains) {
//                    val hex: Int = chatBean.content.replace("U+", "").toInt(16)
//                    //将当前 16 进制数转换成字符数组
//                    val chars = Character.toChars(hex)
//                    //将当前字符数组转换成 TextView 可加载的 String 字符串
//                    val mEmojiString = String(chars)
//                    mEmojiString
//                } else {
//                    content
//                }

                helper.setText(R.id.tv_message,content)
            }

        }
    }

    private fun getUserName(userName:String) :String{
       var userNameSub = ""
        if (userName.length > 2) {
            userNameSub = userName.substring(userName.length - 2, userName.length)
        }
        return userNameSub
    }

}