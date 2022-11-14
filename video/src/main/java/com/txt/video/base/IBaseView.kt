package com.txt.video.base

/**
 * Created by JustinWjq
 * @date 2019-12-23.
 * description：
 */
interface IBaseView {
    /**
     * 加载中
     */
    fun onLoading(){}

    /**
     * 加载错误回调
     */
    fun onLoadFailed(){}

    /**
     * 加载完成
     */
    fun onLoadSuccess(){}

    fun showMessage(message:String){}

    enum class MessageType{
        MESSAGETYPE_NOTIP,//没有icon的消息
        MESSAGETYPE_SUCCESS,//带成功icon的消息
        MESSAGETYPE_FAIL//带失败icon的消息

    }

    fun showMessage(type: MessageType,message:String){}

}