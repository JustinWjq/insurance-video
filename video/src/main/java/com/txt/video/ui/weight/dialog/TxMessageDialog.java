package com.txt.video.ui.weight.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.txt.video.R;
import com.txt.video.common.dialog.common.TxBaseDialog;
import com.txt.video.common.dialog.common.TxCommonDialog;


/**
 *
 *    time   : 2018/12/2
 *    desc   : 消息对话框
 */
public final class TxMessageDialog {

    public static final class Builder
            extends TxCommonDialog.Builder<Builder> {

        @Nullable
        private OnListener mListener;

        private final TextView mMessageView;

        public Builder(Context context) {
            super(context);
            setCustomView(R.layout.tx_message_dialog);
            setCanceledOnTouchOutside(false);
            mMessageView = findViewById(R.id.tv_message_message);
        }

        public Builder setMessage(@StringRes int id) {
            return setMessage(getString(id));
        }
        public Builder setMessage(CharSequence text) {
            mMessageView.setText(text);
            return this;
        }

        public Builder setListener(OnListener listener) {
            mListener = listener;
            return this;
        }

        @Override
        public TxBaseDialog create() {
            // 如果内容为空就抛出异常
            if ("".equals(mMessageView.getText().toString())) {
                throw new IllegalArgumentException("Dialog message not null");
            }
            return super.create();
        }

        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            if (viewId == R.id.tv_ui_confirm) {
                autoDismiss();
                if (mListener == null) {
                    return;
                }
                mListener.onConfirm(getDialog());
            } else if (viewId == R.id.tv_ui_cancel) {
                autoDismiss();
                if (mListener == null) {
                    return;
                }
                mListener.onCancel(getDialog());
            }
        }
    }

    public interface OnListener {

        /**
         * 点击确定时回调
         */
        void onConfirm(TxBaseDialog dialog);

        /**
         * 点击取消时回调
         */
        default void onCancel(TxBaseDialog dialog) {}
    }
}