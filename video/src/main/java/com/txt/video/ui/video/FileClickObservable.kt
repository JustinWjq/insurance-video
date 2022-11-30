package com.txt.video.ui.video

import com.txt.video.common.callback.onFileClickListener
import com.txt.video.trtc.ticimpl.observer.TICObservable
import java.lang.ref.WeakReference
import java.util.*

/**
 * author ：Justin
 * time ：4/7/21.
 * des ：
 */
class FileClickObservable : TICObservable<onFileClickListener>(),onFileClickListener{

    override fun onSuccess(roomId : Int) {
        val tmpList: LinkedList<WeakReference<onFileClickListener>> =
            LinkedList<WeakReference<onFileClickListener>>(listObservers)
        val it: Iterator<WeakReference<onFileClickListener>> =
            tmpList.iterator()

        while (it.hasNext()) {
            val t: onFileClickListener? = it.next().get()
            if (t != null) {
                t.onSuccess(roomId)
            }
        }
    }

    override fun onFail(errCode: Int, errMsg: String) {
        val tmpList: LinkedList<WeakReference<onFileClickListener>> =
            LinkedList<WeakReference<onFileClickListener>>(listObservers)
        val it: Iterator<WeakReference<onFileClickListener>> =
            tmpList.iterator()

        while (it.hasNext()) {
            val t: onFileClickListener? = it.next().get()
            if (t != null) {
                t.onFail(errCode, errMsg)
            }
        }
    }

}