package com.txt.video.ui.checkres

import android.content.Context
import com.txt.video.base.BasePresenter
import com.txt.video.net.http.HttpRequestClient
import com.txt.video.net.http.SystemHttpRequest

/**
 * author ：Justin
 * time ：2021/3/17.
 * des ：
 */
public class CheckResourcesPresenter(val context: Context, val activity: CheckResourcesActivity) :
    BasePresenter<CheckResourcesContract.ICollectView>(), CheckResourcesContract.ICollectPresenter {

    override fun searchWord(word: String) {

    }


}