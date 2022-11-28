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
 *    desc   : 消息对话框---单个确认框
 */
public final class TxMessageDialog1 {

    public static final class Builder
            extends TxBaseDialog.Builder<Builder> {

        @Nullable
        private OnListener mListener;

        private final TextView mMessageView;
        private final TextView tv_ui_confirm;
        private final TextView tv_content;

        public Builder(Context context) {
            super(context);
            setContentView(R.layout.tx_message1_dialog);
            setCanceledOnTouchOutside(false);
            mMessageView = findViewById(R.id.tv_ui_title);
            tv_ui_confirm = findViewById(R.id.tv_ui_confirm);
            tv_content = findViewById(R.id.tv_content);
            tv_ui_confirm.setOnClickListener(this);
        }

        public Builder setMessage(CharSequence text) {
            tv_content.setText(text);
            return this;
        }

        public Builder setTitle(@StringRes int id) {
            return setMessage(getString(id));
        }
        public Builder setTitle(CharSequence text) {
            mMessageView.setText(text);
            return this;
        }


        public Builder setConfirm(CharSequence text) {
            tv_ui_confirm.setText(text);
            return  this;
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
                dismiss();
                if (mListener == null) {
                    return;
                }
                mListener.onConfirm(getDialog());
            } else if (viewId == R.id.tv_ui_cancel) {
                dismiss();
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
