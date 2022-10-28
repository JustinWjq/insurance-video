package com.txt.video.common.manager;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;


import com.txt.video.common.dialog.common.TxBaseDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2021/01/29
 *    desc   : Dialog 显示管理类
 */
public final class TxDialogManager implements LifecycleEventObserver, TxBaseDialog.OnDismissListener {

    private final static HashMap<LifecycleOwner, TxDialogManager> DIALOG_MANAGER = new HashMap<>();

    public static TxDialogManager getInstance(LifecycleOwner lifecycleOwner) {
        TxDialogManager manager = DIALOG_MANAGER.get(lifecycleOwner);
        if (manager == null) {
            manager = new TxDialogManager(lifecycleOwner);
            DIALOG_MANAGER.put(lifecycleOwner, manager);
        }
        return manager;
    }

    private final List<TxBaseDialog> mDialogs = new ArrayList<>();

    private TxDialogManager(LifecycleOwner lifecycleOwner) {
        lifecycleOwner.getLifecycle().addObserver(this);
    }

    /**
     * 排队显示 Dialog
     */
    public void addShow(TxBaseDialog dialog) {
        if (dialog == null || dialog.isShowing()) {
            throw new IllegalStateException("are you ok?");
        }
        mDialogs.add(dialog);
        TxBaseDialog firstDialog = mDialogs.get(0);
        if (!firstDialog.isShowing()) {
            firstDialog.addOnDismissListener(this);
            firstDialog.show();
        }
    }

    /**
     * 取消所有 Dialog 的显示
     */
    public void clearShow() {
        if (mDialogs.isEmpty()) {
            return;
        }
        TxBaseDialog firstDialog = mDialogs.get(0);
        if (firstDialog.isShowing()) {
            firstDialog.removeOnDismissListener(this);
            firstDialog.dismiss();
        }
        mDialogs.clear();
    }

    @Override
    public void onDismiss(TxBaseDialog dialog) {
        dialog.removeOnDismissListener(this);
        mDialogs.remove(dialog);
        for (TxBaseDialog nextDialog : mDialogs) {
            if (!nextDialog.isShowing()) {
                nextDialog.addOnDismissListener(this);
                nextDialog.show();
                break;
            }
        }
    }

    /**
     * {@link LifecycleEventObserver}
     */

    @Override
    public void onStateChanged(@NonNull LifecycleOwner lifecycleOwner, @NonNull Lifecycle.Event event) {
        if (event != Lifecycle.Event.ON_DESTROY) {
            return;
        }
        DIALOG_MANAGER.remove(lifecycleOwner);
        lifecycleOwner.getLifecycle().removeObserver(this);
        clearShow();
    }
}