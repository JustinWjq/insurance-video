package com.txt.video.common.callback

/**
 * Created by JustinWjq
 * @date 2020/8/28.
 * description：
 */
open interface onSDKListener {
    fun onResultSuccess(result:String)
    fun onResultFail(errCode:Int,errMsg:String)
}