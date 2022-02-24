package com.txt.video.common.callback;

/**
 * author ：Justin
 * time ：2022/2/11.
 * des ：
 */
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
