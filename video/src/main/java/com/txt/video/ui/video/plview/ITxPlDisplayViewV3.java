package com.txt.video.ui.video.plview;


import androidx.annotation.Nullable;

public interface ITxPlDisplayViewV3 {

    /**
     * 横竖屏切换
     * @param content
     */
    public void switchScreen(boolean content);


    public void notifyItemChangedPld(int position, @Nullable Object payload);

    public void notifyItemInsertedPld(int position);

    public void notifyItemRemovedPld(int position);

    public void notifyItemChangedPld(int position);

}
