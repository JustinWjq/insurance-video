package com.txt.video.ui.video.barrage.view.adapter;

import android.content.Context;

import androidx.recyclerview.widget.LinearSmoothScroller;

/**
 * author ：Justin
 * time ：2022/12/8.
 * des ：
 */
public class TopSmoothScroller extends LinearSmoothScroller {
    public TopSmoothScroller(Context context) {
        super(context);
    }

    @Override
    protected int getHorizontalSnapPreference() {
        return SNAP_TO_START;
    }

    @Override
    protected int getVerticalSnapPreference() {
        return SNAP_TO_START;
    }
}
