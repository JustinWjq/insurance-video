package com.txt.video.common.callback

/**
 * author ：Justin
 * time ：2021/3/29.
 * des ：点击共享文件回调，跳转到甲方的页面
 */
open interface  onFileClickListener{
    fun onSuccess()
    fun onFail(errCode:Int,errMsg:String)
}