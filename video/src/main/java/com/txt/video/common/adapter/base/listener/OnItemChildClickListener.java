package com.txt.video.common.adapter.base.listener;

import android.view.View;

import com.txt.video.common.adapter.base.TxBaseQuickAdapter;

/**
 * Created by AllenCoder on 2016/8/03.
 * A convenience class to extend when you only want to OnItemChildClickListener for a subset
 * of all the SimpleClickListener. This implements all methods in the
 * {@link com.chad.library.adapter.base.listener.SimpleClickListener}
 **/

public abstract class OnItemChildClickListener extends SimpleClickListener {
    @Override
    public void onItemClick(TxBaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemLongClick(TxBaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(TxBaseQuickAdapter adapter, View view, int position) {
        onSimpleItemChildClick(adapter, view, position);
    }

    @Override
    public void onItemChildLongClick(TxBaseQuickAdapter adapter, View view, int position) {

    }

    public abstract void onSimpleItemChildClick(TxBaseQuickAdapter adapter, View view, int position);
}
