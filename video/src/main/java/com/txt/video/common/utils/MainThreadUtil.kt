package com.txt.video.common.utils

import android.os.Handler
import android.os.Looper

/**
 * Created by JustinWjq
 * @date 2020/11/6.
 * description：
 */
public object MainThreadUtil {

    private val HANDLER = Handler(Looper.getMainLooper())

    public fun run (runnable : Runnable){
        if (isMainThread()){
            runnable.run()
        }else{
            HANDLER.post(runnable)
        }

    }

    public fun isMainThread(): Boolean = Looper.myLooper() == Looper.getMainLooper()

}