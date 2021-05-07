package com.txt.video.common.callback

/**
 * author ：Justin
 * time ：2021/3/29.
 * des ：云助理好友回调
 */
open interface  onFriendBtListener{
    fun onSuccess(roomId:String,serviceId:String,inviteAccount:String)
    fun onFail(errCode:Int,errMsg:String)
}