package com.txt.video.ui.video

import com.txt.video.ui.TXManagerImpl
import java.lang.NullPointerException

/**
 * author ：Justin
 * time ：2022/2/14.
 * des ：
 */
public class VideoTransitData {
    companion object {
        @Volatile
        private var singleton: VideoTransitData? = null

        @JvmStatic
        val instance: VideoTransitData?
            get() {
                if (singleton == null) {
                    synchronized(VideoTransitData::class.java) {
                        if (singleton == null) {
                            singleton =
                                VideoTransitData()
                        }
                    }
                }
                return singleton
            }
    }
    private var mDataPresenter : VideoPresenter?=null
    public fun setPresenter(videoPresenter: VideoPresenter){
        mDataPresenter = videoPresenter
    }
    public fun getPresenter() : VideoPresenter{
        if (null!=  mDataPresenter) {
            return mDataPresenter!!
        }else{
            throw NullPointerException("presenter not be null !!")
        }

    }
}