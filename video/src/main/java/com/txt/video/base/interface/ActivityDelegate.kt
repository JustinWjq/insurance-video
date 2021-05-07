package com.txt.video.base.`interface`

import android.os.Bundle

/**
 * author ：Justin
 * time ：2021/3/12.
 * des ：
 */
interface ActivityDelegate {

    fun onCreate(saveInstanceState:Bundle) {

    }

    fun onStart()

    fun onResume()

    fun onPause()

    fun onStop()

    fun onSaveInstanceState(saveInstanceState: Bundle)

    fun onDestroy()

}