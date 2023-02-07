package com.txt.video.common.adapter.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;
import android.widget.Space;

import androidx.recyclerview.widget.RecyclerView;

import com.txt.video.R;

/**
 * 添加recyclerview的边距
 */

public class CustomDividerItemDecoration extends RecyclerView.ItemDecoration {

    private  int space;
    private  boolean isRight;
    public CustomDividerItemDecoration(boolean isRight,int space) {
        this.space = space;
        this.isRight = isRight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (isRight) {
            outRect.right =space;
        }else{
            outRect.bottom =0;
        }

    }

}
