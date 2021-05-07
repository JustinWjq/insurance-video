package com.txt.video.common.callback

/**
 * Created by JustinWjq
 * @date 2020/8/28.
 * description：
 */
open interface onCreateRoomListener {
    fun onResultSuccess(roomId:String){}
    fun onResultSuccess(roomId:String,serviceId:String,inviteAccount:String){}
    fun onResultFail(errCode:Int,errMsg:String)
}