package com.txt.video.ui.weight.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.txt.video.R;
import com.txt.video.common.adapter.decoration.DividerItemDecoration;
import com.txt.video.common.dialog.common.TxBaseDialog;
import com.txt.video.common.dialog.common.TxCommonDialog;
import com.txt.video.net.bean.UserInfoBean;
import com.txt.video.ui.weight.adapter.UserInfoDialogAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * author ：Justin
 * time ：6/25/21.
 * des ：展示用户信息
 */
public final class TxInfoDialog {

    public static final class Builder
            extends TxCommonDialog.Builder<Builder>
            implements TxBaseDialog.OnShowListener,
            TextView.OnEditorActionListener {

        private OnListener mListener;
        private  RecyclerView rv_file;
        public Builder(Context context) {
            super(context);
            setGravity(Gravity.BOTTOM);
            setThemeStyle(R.style.tx_MyDialog);
            setContentView(R.layout.tx_layout_userinfo);
            setAnimStyle(TxBaseDialog.ANIM_DEFAULT);
            setWidth(context.getResources().getDisplayMetrics().widthPixels);
            rv_file = findViewById(R.id.rv_file);

        }


        public Builder setListener(OnListener listener) {
            mListener = listener;
            return this;
        }

        public Builder setInfo(String info) {
            ArrayList zhanyAl = new ArrayList<UserInfoBean>();
            try {
                JSONObject jsonObject = new JSONObject(info);
                zhanyAl.add(new UserInfoBean("真实姓名", jsonObject.optString("fn")));
                zhanyAl.add(new UserInfoBean("备注名", jsonObject.optString("alias")));
                zhanyAl.add(new UserInfoBean("性别", jsonObject.optString("sex")));
                zhanyAl.add(new UserInfoBean("生日", jsonObject.optString("birthday")));
                zhanyAl.add(new UserInfoBean("手机", jsonObject.optJSONArray("tels").toString()));
                zhanyAl.add(new UserInfoBean("邮箱", jsonObject.optString("email")));
                zhanyAl.add(new UserInfoBean("详细地址", jsonObject.optString("adr")));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            UserInfoDialogAdapter screenShareResourcesAdapter = new UserInfoDialogAdapter();
            screenShareResourcesAdapter.setNewData(zhanyAl);
            rv_file.setAdapter(screenShareResourcesAdapter);
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
