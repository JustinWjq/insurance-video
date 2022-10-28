package com.txt.video.common.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.txt.video.R;
import com.txt.video.common.dialog.common.TxBaseDialog;
import com.txt.video.common.dialog.common.TxCommonDialog;
import com.txt.video.net.bean.ResourceConditionsBean;
import com.txt.video.net.bean.ResourcegsConditionsBean;
import com.txt.video.ui.weight.adapter.ScreenShareResourcesAdapter;
import com.txt.video.ui.weight.easyfloat.utils.DisplayUtils;

import java.util.ArrayList;

/**
 * author ：Justin
 * time ：6/25/21.
 * des ：
 */
public final class TxSearchcriteriaDialog {

    public static final class Builder
            extends TxCommonDialog.Builder<Builder>
            implements TxBaseDialog.OnShowListener,
            TextView.OnEditorActionListener {

        private OnListener mListener;

        public Builder(Context context) {
            super(context);
            setGravity(Gravity.RIGHT);
            setHeight(context.getResources().getDisplayMetrics().heightPixels);
            setThemeStyle(R.style.tx_MyDialog);
            setContentView(R.layout.tx_layout_searchcriteria);
            setAnimStyle(TxBaseDialog.ANIM_RIGHT);
            RecyclerView rv_file = findViewById(R.id.rv_file);
            RecyclerView rv_kequn = findViewById(R.id.rv_kequn);
            ArrayList zhanyAl = new ArrayList<ResourceConditionsBean>();
            zhanyAl.add(new ResourcegsConditionsBean("讲理念", "1",true));
            zhanyAl.add(new ResourcegsConditionsBean("讲方案", "1",false));
            zhanyAl.add(new ResourcegsConditionsBean("讲产品", "1",false));
            zhanyAl.add(new ResourcegsConditionsBean("讲建议书", "1",false));
            ScreenShareResourcesAdapter screenShareResourcesAdapter = new ScreenShareResourcesAdapter();
            ScreenShareResourcesAdapter screenShareResourcesAdapter1 = new ScreenShareResourcesAdapter();
            screenShareResourcesAdapter.setNewData(zhanyAl);
            screenShareResourcesAdapter1.setNewData(zhanyAl);
            rv_file.setAdapter(screenShareResourcesAdapter);
            rv_kequn.setAdapter(screenShareResourcesAdapter1);
        }


        public Builder setListener(OnListener listener) {
            mListener = listener;
            return this;
        }

        /**
         * {@link TxBaseDialog.OnShowListener}
         */
        @Override
        public void onShow(TxBaseDialog dialog) {
            //  postDelayed(() -> showKeyboard(mContext), 500);
        }

        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            if (viewId == R.id.tv_ui_confirm) {
                //重置
            } else if (viewId == R.id.tv_ui_cancel) {
                autoDismiss();
                if (mListener != null) {
                    mListener.onCancel(getDialog());
                }
            }
        }

        /**
         * {@link TextView.OnEditorActionListener}
         */
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // 模拟点击确认按钮
                onClick(findViewById(R.id.tv_ui_confirm));
                return true;
            }
            return false;
        }
    }

    public interface OnListener {

        /**
         * 点击确定时回调
         */
        void onConfirm(TxBaseDialog dialog, String name, String url);

        /**
         * 点击取消时回调
         */
        default void onCancel(TxBaseDialog dialog) {
        }
    }
}