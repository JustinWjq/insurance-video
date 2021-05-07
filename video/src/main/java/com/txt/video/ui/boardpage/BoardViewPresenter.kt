package com.txt.video.ui.boardpage

import android.content.Context
import com.txt.video.base.BasePresenter
import com.txt.video.net.http.HttpRequestClient
import com.txt.video.net.http.SystemHttpRequest
import com.txt.video.ui.video.VideoActivity
import com.txt.video.ui.video.VideoContract

/**
 * author ：Justin
 * time ：2021/3/17.
 * des ：
 */
public class BoardViewPresenter(val context: Context, val activity: BoardViewActivity) :
    BasePresenter<BoardViewContract.ICollectView>(), BoardViewContract.ICollectPresenter {

    private var mServiceId :String ?= null

    override fun setServiceId(serviceId: String) {
        this.mServiceId = serviceId
    }

    override fun extendTime() {
        SystemHttpRequest.getInstance()
            .extendTime(
                mServiceId,
                object : HttpRequestClient.RequestHttpCallBack {
                    override fun onSuccess(json: String?) {
                       view.showMessage("延长成功！")
                    }

                    override fun onFail(err: String?, code: Int) {
                        view.showMessage("延长失败！")
                    }

                })
    }

}