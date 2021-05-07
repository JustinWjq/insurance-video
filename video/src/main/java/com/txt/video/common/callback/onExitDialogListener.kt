package com.txt.video.common.callback

/**
 * Created by JustinWjq
 * @date 2020/8/28.
 * description：
 */
open interface onExitDialogListener {
    fun onConfirm()

    fun onTemporarilyLeave()

    fun end(){}
}