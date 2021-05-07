package com.txt.video.common.callback

import com.txt.video.net.bean.ThickType
import com.txt.video.net.bean.ToolType

/**
 * Created by JustinWjq
 * @date 2020/8/28.
 * description：
 */
open interface onCheckDialogListenerCallBack {
    fun onCheckColor(postion: Int, toolType: ToolType,type:String)
    fun onCheckThick(postion: Int, thickType: ThickType,type:String)
}