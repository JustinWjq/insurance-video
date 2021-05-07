package com.txt.video.trtc.videolayout.list;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;


/**
 * Created by JustinWjq
 *
 * @date 2020/9/11.
 * description：
 */
public class MeetingPageLM extends LinearLayoutManager {
    public MeetingPageLM(Context context) {
        super(context);
    }

    public MeetingPageLM(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public MeetingPageLM(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

    private MeetingPageLayoutManager.PageListener mPageListener = null;

    public void setPageListener(MeetingPageLayoutManager.PageListener pageListener) {
        mPageListener = pageListener;
    }

    public interface PageListener {
        /**
         * 页面总数量变化
         *
         * @param pageSize 页面总数
         */
        void onPageSizeChanged(int pageSize);

        /**
         * 页面被选中
         *
         * @param pageIndex 选中的页面
         */
        void onPageSelect(int pageIndex);

        void onItemVisible(int fromItem, int toItem);
    }

}
