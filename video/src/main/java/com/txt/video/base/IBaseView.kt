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
}