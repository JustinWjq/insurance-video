package com.txt.video.common.callback

/**
 * Created by JustinWjq
 * @date 2020/8/28.
 * description：
 */
open interface onDialogListenerCallBack {
    fun onConfirm()
    fun onFile()
    fun onItemClick(id: String?, url: String){}
    fun onItemClick(id: String?, url: String,name: String){}
    fun onItemClick(url: String?, images: MutableList<String>){}
    fun onItemLongClick(id: String?)
}