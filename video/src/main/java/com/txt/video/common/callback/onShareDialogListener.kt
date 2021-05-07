package com.txt.video.common.callback

/**
 * Created by JustinWjq
 * @date 2020/8/28.
 * description：
 */
open interface onShareDialogListener {
    fun onConfirmWx()
    fun onConfirmFd()
    fun onConfirmMSG()
}