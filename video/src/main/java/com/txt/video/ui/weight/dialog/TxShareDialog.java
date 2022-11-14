package com.txt.video.ui.weight.dialog;

import android.content.Context;
import android.graphics.PorterDuff;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.txt.video.R;
import com.txt.video.common.action.AnimAction;
import com.txt.video.common.adapter.base.entity.MultiItemEntity;
import com.txt.video.common.callback.onShareDialogListener;
import com.txt.video.common.dialog.common.TxBaseDialog;
import com.txt.video.trtc.videolayout.Utils;
import com.txt.video.ui.weight.adapter.ChatAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 *
 *    time   : 2018/12/2
 *    desc   : 群聊弹窗
 */
public final class TxShareDialog {

    public static final class Builder
            extends TxBaseDialog.Builder<Builder> {

        @Nullable
        private onShareDialogListener mListener;
        TextView tv_top;

        public Builder(Context context) {
            super(context);
            setContentView(R.layout.tx_dialog_share);
            View tv_uploadpic = findViewById(R.id.tv_uploadpic);
            View tv_uploadfile = findViewById(R.id.tv_uploadfile);
            View tv_share_fd = findViewById(R.id.tv_share_fd);
            View tv_title = findViewById(R.id.tv_title);
            tv_top = findViewById(R.id.tv_top);
            tv_uploadpic.setOnClickListener(this);
            tv_uploadfile.setOnClickListener(this);
            tv_share_fd.setOnClickListener(this);
            tv_title.setOnClickListener(this);
        }


        public Builder setListener(onShareDialogListener listener) {
            mListener = listener;
            return this;
        }


        public Builder showLand(Boolean showLand){
            if (showLand) {
                setGravity(Gravity.CENTER);
                setWidth(getResources().getDisplayMetrics().widthPixels/3);
            }else {
                setGravity(Gravity.CENTER);
                setWidth(getResources().getDisplayMetrics().widthPixels/3*2);
            }

            return this;
        }


        @Override
        public TxBaseDialog create() {
            // 如果内容为空就抛出异常
            return super.create();
        }

        @Override
        public void onClick(View view) {
            int viewId = view.getId();
            if (viewId == R.id.iv_close) {
                dismiss();
                if (mListener == null) {
                    return;
                }
//                mListener.onConfirm(getDialog());
            } else if (viewId == R.id.tv_ui_cancel) {
                dismiss();
                if (mListener == null) {
                    return;
                }
//                mListener.onCancel(getDialog());
            }else  if (viewId == R.id.tv_sendmsg){
                dismiss();
                if (mListener == null) {
                    return;
                }
            }else  if (viewId == R.id.tv_uploadpic) {
                dismiss();
                if (mListener == null) {
                    return;
                }
                mListener.onConfirmWx();
            } else if (viewId == R.id.tv_uploadfile) {
                dismiss();
                if (mListener == null) {
                    return;
                }
                mListener.onConfirmMSG();
            }else if (viewId == R.id.tv_share_fd){
                dismiss();
                if (mListener == null) {
                    return;
                }
                mListener.onConfirmFd();
            }else if (viewId == R.id.tv_title) {
                dismiss();
                if (mListener == null) {
                    return;
                }
            }
        }
    }

    public interface TxUserChatDialogif {
        void onFinishClick();

        void onSendMsg(String msg);

    }

}