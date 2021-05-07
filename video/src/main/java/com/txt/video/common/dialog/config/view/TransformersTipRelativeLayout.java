package com.txt.video.common.dialog.config.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.txt.video.common.dialog.config.ArrowDrawable;


/**
 * Created by justin on 2017/8/25.
 * 描述:继承自 RelativeLayout 来设置 ArrowDrawable 作为背景，便于在布局文件中预览
 */
public class TransformersTipRelativeLayout extends RelativeLayout {

    public TransformersTipRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TransformersTipRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ArrowDrawable arrowDrawable = new ArrowDrawable(context, attrs);
        arrowDrawable.expandShadowAndArrowPadding(this);
        setBackground(arrowDrawable);
    }
}
