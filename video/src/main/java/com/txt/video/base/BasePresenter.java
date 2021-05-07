package com.txt.video.base;

import java.lang.ref.WeakReference;


/**
 * Created by JustinWjq
 *
 * @date 2019-12-23.
 * description：
 */
public class BasePresenter<V> {
    WeakReference<V> mWeakRef;


    /**
     * 创建弱引用View
     *
     * @param view
     */
    public void attachView(V view) {
        mWeakRef = new WeakReference<V>(view);
    }

    /**
     * 将View取出
     *
     * @return
     */
    protected V getView() {
        return null == mWeakRef ? null : mWeakRef.get();
    }

    /**
     * 判断是否使用弱引用创建View
     *
     * @return
     */
    protected boolean isViewAttached() {
        return null != mWeakRef && null != mWeakRef.get();
    }

    /**
     * 释放View 并取消订阅
     */
    public void detachView() {
        if (null != mWeakRef) {
            mWeakRef.clear();
            mWeakRef = null;
        }
    }
}
