package com.txt.video.ui.weight.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * author ：Justin
 * time ：2022/11/16.
 * des ：
 */
public class IOSLoadingView extends View {
    private static final String TAG = IOSLoadingView.class.getSimpleName();
    private int width;
    private int height;
    private int widthRect;
    private int heigheRect;
    private Paint rectPaint;
    private int pos;
    private Rect rect;
    private String[] color;

    public IOSLoadingView(Context context) {
        this(context, (AttributeSet)null);
    }

    public IOSLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IOSLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.pos = 0;
        this.color = new String[]{"#bbbbbb", "#aaaaaa", "#999999", "#888888", "#777777", "#666666"};
        this.init();
    }

    private void init() {
        this.rectPaint = new Paint(1);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode != -2147483648 && heightMode != -2147483648) {
            this.width = MeasureSpec.getSize(widthMeasureSpec);
            this.height = MeasureSpec.getSize(heightMeasureSpec);
            this.width = Math.min(this.width, this.height);
        } else {
            this.width = 200;
        }

        this.widthRect = this.width / 12;
        this.heigheRect = 4 * this.widthRect;
        this.setMeasuredDimension(this.width, this.width);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.rect == null) {
            this.rect = new Rect((this.width - this.widthRect) / 2, 0, (this.width + this.widthRect) / 2, this.heigheRect);
        }

        for(int i = 0; i < 12; ++i) {
            if (i - this.pos >= 5) {
                this.rectPaint.setColor(Color.parseColor(this.color[5]));
            } else if (i - this.pos >= 0 && i - this.pos < 5) {
                this.rectPaint.setColor(Color.parseColor(this.color[i - this.pos]));
            } else if (i - this.pos >= -7 && i - this.pos < 0) {
                this.rectPaint.setColor(Color.parseColor(this.color[5]));
            } else if (i - this.pos >= -11 && i - this.pos < -7) {
                this.rectPaint.setColor(Color.parseColor(this.color[12 + i - this.pos]));
            }

            canvas.drawRect(this.rect, this.rectPaint);
            canvas.rotate(30.0F, (float)(this.width / 2), (float)(this.width / 2));
        }

        ++this.pos;
        if (this.pos > 11) {
            this.pos = 0;
        }

        this.postInvalidateDelayed(100L);
    }
}