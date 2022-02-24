package com.txt.video.trtc.videolayout.list;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

import com.txt.video.common.callback.PageListener;


/**
 * Created by JustinWjq
 *
 * @date 2020/9/11.
 * description：
 */
public class MeetingPageLinearLayoutManager extends LinearLayoutManager {
    public MeetingPageLinearLayoutManager(Context context) {
        super(context);
    }

    public MeetingPageLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public MeetingPageLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new  RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler,state);
            Log.d("onLayoutChildren", "childCount "+getChildCount()+"--" + "="+recycler.getScrapList().size());
            // 如果是 preLayout 则不重新布局
            if (state.isPreLayout() || getUsableWidth() == 0) {
                return;
            }
            if (getItemCount() == 0) {

                return;
            } else if (getItemCount() == 1) {

                if (mPageListener != null) {
                    mPageListener.onItemVisible(0, 0);
                }
                return;
            } else if (getItemCount() == 2) {

                if (mPageListener != null) {
                    mPageListener.onItemVisible(0, 1);
                }
                return;
            } else {
                if (mPageListener != null) {
                    mPageListener.onItemVisible(0, getItemCount()-1);
                }
                return;
            }

        }catch (IndexOutOfBoundsException e){

        }


    }

    /**
     * 获取可用的宽度
     *
     * @return 宽度 - padding
     */
    private int getUsableWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    //--- 对外接口 ----------------------------------------------------------------------------------

    private PageListener mPageListener = null;

    public void setPageListener(PageListener pageListener) {
        mPageListener = pageListener;
    }



}
