package com.txt.video.ui.video

import com.txt.video.common.callback.onFriendBtListener
import com.txt.video.trtc.ticimpl.observer.TICObservable
import java.lang.ref.WeakReference
import java.util.*

/**
 * author ：Justin
 * time ：4/7/21.
 * des ：
 */
class FriendBtObservable : TICObservable<onFriendBtListener>(),onFriendBtListener{

    override fun onSuccess(roomId: String, serviceId: String, inviteAccount: String) {
        val tmpList: LinkedList<WeakReference<onFriendBtListener>> =
            LinkedList<WeakReference<onFriendBtListener>>(listObservers)
        val it: Iterator<WeakReference<onFriendBtListener>> =
            tmpList.iterator()

        while (it.hasNext()) {
            val t: onFriendBtListener? = it.next().get()
            if (t != null) {
                t.onSuccess(roomId, serviceId,inviteAccount)
            }
        }
    }

    override fun onFail(errCode: Int, errMsg: String) {
        val tmpList: LinkedList<WeakReference<onFriendBtListener>> =
            LinkedList<WeakReference<onFriendBtListener>>(listObservers)
        val it: Iterator<WeakReference<onFriendBtListener>> =
            tmpList.iterator()

        while (it.hasNext()) {
            val t: onFriendBtListener? = it.next().get()
            if (t != null) {
                t.onFail(errCode, errMsg)
            }
        }
    }

}