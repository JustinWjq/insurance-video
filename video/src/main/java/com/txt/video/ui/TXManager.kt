package com.txt.video.ui

import android.app.Activity
import com.txt.video.common.callback.StartVideoResultOnListener
import com.txt.video.common.callback.onCreateRoomListener
import com.txt.video.common.callback.onSDKListener
import org.json.JSONObject

/**
 * author ：Justin
 * time ：2021/4/2.
 * des ：
 */

interface TXManager {
    fun checkPermission(
        context: Activity?,
        agent: String?,
        orgAccount: String?,
        sign: String?,
        businessData: JSONObject?,
        listener: StartVideoResultOnListener,
        isAgent: Boolean
    )

    fun checkPermission(
        context: Activity?,
        roomId: String?,
        account: String?,
        userName: String?,
        orgAccount: String?,
        sign: String?,
        businessData: JSONObject?,
        listener: StartVideoResultOnListener,
        isAgent: Boolean
    )


        //直接进入会议模式
    fun enterRoom(
        context: Activity?,
        agent: String?,
        orgAccount: String?,
        sign: String?,
        businessData: JSONObject?,
        listener: StartVideoResultOnListener
    )

    fun joinRoom(
        context: Activity?,
        roomId: String?,
        account: String?,
        userName: String?,
        orgAccount: String?,
        sign: String?,
        businessData: JSONObject?,
        listener: StartVideoResultOnListener
    )

    fun createRoom(
        account: String?,
        orgAccount: String?,
        sign: String?,
        roomInfo: JSONObject?,
        businessData: JSONObject?,
        listener: onCreateRoomListener
    )

    fun setAgentInRoomStatus( account:String,userName:String, serviceId:String,inviteAccount:String, action:String,  orgAccount:String,  sign:String, onSDKListener:onSDKListener)

    fun  getAgentAndRoomStatus( account:String,  serviceId:String,  orgAccount:String,  sign:String,  onSDKListener: onSDKListener)

}