package com.txt.video.net.bean;

import android.support.annotation.DrawableRes;

/**
 * Created by JustinWjq
 *
 * @date 2021/2/3.
 * description：
 */
public class ThickType {
    String name;
    int defaultSrc;
    int selectSrc;
    int size;
    boolean isSelect;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDefaultSrc() {
        return defaultSrc;
    }

    public void setDefaultSrc(int defaultSrc) {
        this.defaultSrc = defaultSrc;
    }

    public int getSelectSrc() {
        return selectSrc;
    }

    public void setSelectSrc(int selectSrc) {
        this.selectSrc = selectSrc;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ThickType(String name, @DrawableRes int defaultSrc, @DrawableRes int selectSrc, int size, boolean isSelect) {
        this.name = name;
        this.defaultSrc = defaultSrc;
        this.selectSrc = selectSrc;
        this.size = size;
        this.isSelect = isSelect;
    }
}
