package com.txt.video.common.callback

/**
 * Created by JustinWjq
 * @date 2020/8/31.
 * description：
 */
interface StartVideoResultOnListener {
    fun onResultSuccess()
    fun onResultFail(errCode:Int,errMsg:String)
}