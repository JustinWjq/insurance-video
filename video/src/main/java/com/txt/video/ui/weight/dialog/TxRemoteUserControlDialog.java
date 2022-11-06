package com.txt.video.ui.weight.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.AppCompatTextView;

import com.txt.video.R;
import com.txt.video.common.action.AnimAction;
import com.txt.video.common.callback.onMuteDialogListener;
import com.txt.video.common.dialog.common.TxBaseDialog;
import com.txt.video.common.dialog.common.TxCommonDialog;
import com.txt.video.trtc.videolayout.list.MemberEntity;


/**
 * time   : 2018/12/2
 * desc   : 成员控制
 */
public final class TxRemoteUserControlDialog {

    public static final class Builder
            extends TxBaseDialog.Builder<Builder> {

        @Nullable
        private onMuteDialogListener mListener;


        public Builder(Context context) {
            super(context);
            setContentView(R.layout.tx_dialog_remote_user_control);
            initView();
        }


        public Builder setListener(onMuteDialogListener listener) {
            mListener = listener;
            return this;
        }
        public Builder showLand(Boolean showLand){
            if (showLand) {
                setGravity(Gravity.RIGHT);
                setXOffset(50);
                setAnimStyle(AnimAction.ANIM_RIGHT);
                setWidth(getResources().getDisplayMetrics().widthPixels/2-100);
//                setHeight(getResources().getDisplayMetrics().heightPixels);
//                mRemoteUserListView.setBackgroundResource(R.drawable.tx_shape_round_topleft_15);
            }else {
                setGravity(Gravity.CENTER);
//                setAnimStyle(AnimAction.ANIM_BOTTOM);
                setWidth(getResources().getDisplayMetrics().widthPixels/3*2);
//                setHeight(getResources().getDisplayMetrics().heightPixels-100);
//                mRemoteUserListView.setBackgroundResource(R.drawable.tx_shape_round_topright_15);
            }

            return this;


        }

        @Override
        public TxBaseDialog create() {

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
            } else if (viewId == R.id.tv_cancel) {
                dismiss();
                if (mListener == null) {
                    return;
                }
            } else if (viewId == R.id.atv_mutevideo) {
                dismiss();
                if (mListener == null) {
                    return;
                }
                mListener.onMuteVideo(mMemberEntity);
            } else if (viewId == R.id.atv_muteaudio) {
                dismiss();
                if (mListener == null) {
                    return;
                }
                mListener.onMuteAudio(mMemberEntity);
            }
        }


        TextView mTvTitle;
        TextView mIvTitle;
        TextView mAtvMuteAudio;
        TextView mAtvMuteVideo;

        private void initView() {

            mTvTitle = findViewById(R.id.tv_user_name);

            mIvTitle = findViewById(R.id.tv_icon_name);

            mAtvMuteAudio = findViewById(R.id.atv_muteaudio);

            mAtvMuteVideo = findViewById(R.id.atv_mutevideo);
            TextView tv_cancel = findViewById(R.id.tv_cancel);

            TextView iv_close = findViewById(R.id.iv_close);

            mTvTitle.setOnClickListener(this);
            mAtvMuteAudio.setOnClickListener(this);
            mAtvMuteVideo.setOnClickListener(this);
            tv_cancel.setOnClickListener(this);

            iv_close.setOnClickListener(this);
        }

        private MemberEntity mMemberEntity;

        public MemberEntity getMemberEntity() {
            return mMemberEntity;
        }

        public String getUserId() {
            if (null != mMemberEntity) {
                return mMemberEntity.getUserId();
            } else {
                return "";
            }
        }

        public Builder changeLay(MemberEntity mMemberEntity) {
            this.mMemberEntity = mMemberEntity;
            String userName = mMemberEntity.getUserName();
            boolean muteVideo = mMemberEntity.isMuteVideo();
            boolean muteAudio = mMemberEntity.isMuteAudio();
            if (mTvTitle != null) {
                mTvTitle.setText(userName);
                if (mMemberEntity.getUserName().length() > 2) {
                    mIvTitle.setText(userName.substring(userName.length() - 2, userName.length()));
                } else {
                    mIvTitle.setText(userName);
                }
            }
            if (muteAudio) {
                if (mAtvMuteAudio != null) {
                    mAtvMuteAudio.setText("解除静音");
                }

            } else {
                if (mAtvMuteAudio != null) {
                    mAtvMuteAudio.setText("静音");
                }


            }

            if (muteVideo) {
                if (mAtvMuteVideo != null) {
                    mAtvMuteVideo.setText("打开摄像头");
                }

            } else {
                if (mAtvMuteVideo != null) {
                    mAtvMuteVideo.setText("关闭摄像头");
                }

            }

            return this;
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
        default void onCancel(TxBaseDialog dialog) {
        }
    }
}