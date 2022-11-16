package com.txt.video.ui.weight.dialog;

import android.content.Context;
import android.graphics.PorterDuff;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.txt.video.R;
import com.txt.video.common.action.AnimAction;
import com.txt.video.common.adapter.base.entity.MultiItemEntity;
import com.txt.video.common.dialog.common.TxBaseDialog;
import com.txt.video.net.bean.ChatBean;
import com.txt.video.trtc.videolayout.list.MemberEntity;
import com.txt.video.ui.video.barrage.model.TUIBarrageModel;
import com.txt.video.ui.video.remoteuser.RemoteUserListView;
import com.txt.video.ui.weight.adapter.ChatAdapter;
import com.txt.video.ui.weight.easyfloat.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;


/**
 *
 *    time   : 2018/12/2
 *    desc   : 群聊弹窗
 */
public final class TxUserChatDialog {

    public static final class Builder
            extends TxBaseDialog.Builder<Builder> {

        @Nullable
        private TxUserChatDialogif mListener;

        private RecyclerView mRemoteUserListView;
        private ArrayList<MultiItemEntity> mList = new ArrayList<>() ;
        private ChatAdapter chatAdapter ;
        private ConstraintLayout tx_chat ;
        private  EditText et_sendmsg;
        private final InputMethodManager   mInputMethodManager;
        private  Context   mContext;
        public Builder(Context context) {
            super(context);
            this.mContext = context;
            setContentView(R.layout.tx_dialog_chat);
            mRemoteUserListView = findViewById(R.id.rv_chat_list);
            tx_chat = (ConstraintLayout)findViewById(R.id.tx_chat);
            findViewById(R.id.iv_close).setOnClickListener(this);
            findViewById(R.id.tv_sendmsg).setOnClickListener(this);
            mInputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            et_sendmsg = (EditText) findViewById(R.id.et_sendmsg);
            et_sendmsg.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            et_sendmsg.setSingleLine(false);
            //修改下划线颜色为透明
            et_sendmsg.getBackground().setColorFilter(context.getResources().getColor(R.color.tx_color_tuichorus_transparent),
                    PorterDuff.Mode.CLEAR);

            chatAdapter = new ChatAdapter(mList);
            mRemoteUserListView.setAdapter(chatAdapter);
        }


        public Builder setListener(TxUserChatDialogif listener) {
            mListener = listener;
            return this;
        }

        //setRemoteUser
        public Builder setRemoteUser(List<MultiItemEntity> memberEntityList) {
            chatAdapter.setNewData(memberEntityList);
            return this;
        }

        public Builder notifyDataSetChanged() {
            if (null != chatAdapter) {
                chatAdapter.notifyDataSetChanged();
                mRemoteUserListView.smoothScrollToPosition(chatAdapter.getItemCount());
            }
            return this;
        }

        public Builder showLand(Boolean showLand){
            if (showLand) {
                setGravity(Gravity.RIGHT);
                setAnimStyle(AnimAction.ANIM_RIGHT);
                setWidth( DisplayUtils.INSTANCE.dp2px(mContext,330f));
//                setHeight(getResources().getDisplayMetrics().heightPixels);
                tx_chat.setBackgroundResource(R.drawable.tx_shape_round_topleft_15);
            }else {
                setGravity(Gravity.BOTTOM);
                setAnimStyle(AnimAction.ANIM_BOTTOM);
                setWidth(getResources().getDisplayMetrics().widthPixels);
                setHeight(getResources().getDisplayMetrics().heightPixels-100);
                tx_chat.setBackgroundResource(R.drawable.tx_shape_round_topright_15);
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
                if (mListener == null) {
                    return;
                }
                String msg = et_sendmsg.getText().toString().trim();
                if (!TextUtils.isEmpty(msg)) {

                    mListener.onSendMsg(msg);
                    mInputMethodManager.showSoftInput(et_sendmsg, InputMethodManager.SHOW_FORCED);
                    mInputMethodManager.hideSoftInputFromWindow(et_sendmsg.getWindowToken(), 0);
                    et_sendmsg.setText("");
                } else {
//                    Toast.makeText(c, R.string.tx_tuibarrage_warning_not_empty, Toast.LENGTH_LONG).show();
                }


            }
        }
    }

    public interface TxUserChatDialogif {
        void onFinishClick();

        void onSendMsg(String msg);

    }

}