package com.txt.video.ui.weight.dialog;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.txt.video.R;
import com.txt.video.common.dialog.common.TxBaseDialog;
import com.txt.video.common.dialog.common.TxCommonDialog;

/**
 * author ：Justin
 * time ：6/25/21.
 * des ：
 */
public final class InputDialog {

    public static final class Builder
            extends TxCommonDialog.Builder<Builder>
            implements TxBaseDialog.OnShowListener,
            TextView.OnEditorActionListener {

        private OnListener mListener;
        private final EditText mInputView,mInputView1;

        public Builder(Context context) {
            super(context);
            setCustomView(R.layout.tx_dialog_input);

            mInputView = findViewById(R.id.tv_input_message);
            mInputView1 = findViewById(R.id.tv_input_message1);
            mInputView.setOnEditorActionListener(this);
            mInputView1.setOnEditorActionListener(this);

            addOnShowListener(this);
        }

        public Builder setHint(@StringRes int id) {
            return setHint(getString(id));
        }
        public Builder setHint(CharSequence text) {
            mInputView.setHint(text);
            return this;
        }

        public Builder setContent(@StringRes int id) {
            return setContent(getString(id));
        }
        public Builder setContent(CharSequence text) {
            mInputView.setText(text);
            int index = mInputView.getText().toString().length();
            if (index > 0) {
                mInputView.requestFocus();
                mInputView.setSelection(index);
            }
            return this;
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
            postDelayed(() -> showKeyboard(mInputView), 500);
        }

        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            if (viewId == R.id.tv_ui_confirm) {
                autoDismiss();
                if (mListener != null) {
                    mListener.onConfirm(getDialog(), mInputView.getText().toString(),mInputView1.getText().toString());
                }
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
        void onConfirm(TxBaseDialog dialog, String name,String url);

        /**
         * 点击取消时回调
         */
        default void onCancel(TxBaseDialog dialog) {}
    }
}