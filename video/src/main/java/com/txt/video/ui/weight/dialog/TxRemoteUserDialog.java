package com.txt.video.ui.weight.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.txt.video.R;
import com.txt.video.common.action.AnimAction;
import com.txt.video.common.dialog.common.TxBaseDialog;
import com.txt.video.common.dialog.common.TxCommonDialog;
import com.txt.video.trtc.videolayout.list.MemberEntity;
import com.txt.video.ui.video.remoteuser.RemoteUserListView;
import com.txt.video.ui.weight.easyfloat.utils.DisplayUtils;

import java.util.List;


/**
 *
 *    time   : 2018/12/2
 *    desc   : 成员管理
 */
public final class TxRemoteUserDialog {

    public static final class Builder
            extends TxBaseDialog.Builder<Builder> {

        @Nullable
        private RemoteUserListView.RemoteUserListCallback mListener;

        private RemoteUserListView mRemoteUserListView;
        private Context mContext;
        public Builder(Context context) {
            super(context);
            mContext = context;
            setContentView(R.layout.tx_viewstub_remote_user_list);

            mRemoteUserListView = findViewById(R.id.view_remote_user);
            mRemoteUserListView.findViewById(R.id.iv_close).setOnClickListener(this);
            mRemoteUserListView.findViewById(R.id.iv_close1).setOnClickListener(this);
        }


        public Builder setListener(RemoteUserListView.RemoteUserListCallback listener) {
            mListener = listener;
            mRemoteUserListView.setRemoteUserListCallback(listener);
            return this;
        }

        //setRemoteUser
        public Builder setRemoteUser(List<MemberEntity> memberEntityList) {
            mRemoteUserListView.setRemoteUser(memberEntityList);
            return this;
        }

        public Builder notifyDataSetChanged(){
            mRemoteUserListView.notifyDataSetChanged();
            return this;
        }

        public Builder selectAudioBtn(boolean isSelect){
            mRemoteUserListView.selectAudioBtn(isSelect);
            return this;
        }

        public Builder showLand( Boolean showLand){
            if (showLand) {
                mRemoteUserListView.findViewById(R.id.iv_close).setVisibility(View.GONE);
                mRemoteUserListView.findViewById(R.id.iv_close1).setVisibility(View.VISIBLE);
                setGravity(Gravity.RIGHT);
                setAnimStyle(AnimAction.ANIM_RIGHT);

                setWidth( DisplayUtils.INSTANCE.dp2px(mContext,330f));
//                setHeight(getResources().getDisplayMetrics().heightPixels);
                mRemoteUserListView.setBackgroundResource(R.drawable.tx_shape_round_topleft_15);
            }else {
                mRemoteUserListView.findViewById(R.id.iv_close).setVisibility(View.VISIBLE);
                mRemoteUserListView.findViewById(R.id.iv_close1).setVisibility(View.GONE);
                setGravity(Gravity.BOTTOM);
                setAnimStyle(AnimAction.ANIM_BOTTOM);
                setWidth(getResources().getDisplayMetrics().widthPixels);
                setHeight(getResources().getDisplayMetrics().heightPixels-100);
                mRemoteUserListView.setBackgroundResource(R.drawable.tx_shape_round_topright_15);
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
            if (viewId == R.id.tv_ui_confirm) {
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
            } else if (viewId == R.id.iv_close||viewId == R.id.iv_close1) {
                dismiss();
                if (mListener == null) {
                    return;
                }
//                mListener.onCancel(getDialog());
            }
        }
    }


}