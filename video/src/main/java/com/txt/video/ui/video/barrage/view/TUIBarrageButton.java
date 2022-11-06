package com.txt.video.ui.video.barrage.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.txt.video.R;
import com.txt.video.net.bean.EmojiBean;
import com.txt.video.ui.video.barrage.model.TUIBarrageModel;
import com.txt.video.ui.weight.dialog.EmojiPopup;

//import com.tencent.qcloud.tuikit.tuibarrage.R;

/**
 * 弹幕展开按钮
 */
public class TUIBarrageButton extends FrameLayout {
    private static final String TAG = "TUIBarrageButton";

    private Context            mContext;
    private String             mGroupId;         //用户组ID(房间ID)
    private String             mServiceId;         //
    private TUIBarrageSendView mBarrageSendView; //弹幕发送组件,配合输入法弹出框输入弹幕内容,并发送

    public TUIBarrageButton(Context context) {
        super(context);
    }

    public TUIBarrageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TUIBarrageButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public TUIBarrageButton(Context context, String groupId,String serviceId) {
        this(context);
        this.mContext = context;
        this.mGroupId = groupId;
        this.mServiceId = serviceId;
        initView(context);
    }

    public TUIBarrageSendView getSendView() {
        return mBarrageSendView;
    }

    //获取

    private void initView(final Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.tx_tuibarrage_view_send, this);
        mBarrageSendView = new TUIBarrageSendView(context, mGroupId,mServiceId);
        View iv_linkto_send_dialog = findViewById(R.id.iv_linkto_send_dialog);
        View iv_emoji = findViewById(R.id.iv_emoji);

        iv_linkto_send_dialog.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBarrageSendView.isShowing()) {
                    showSendDialog();
                }
            }
        });

        iv_emoji.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送表情
                EmojiPopup paintThickPopup =new EmojiPopup(
                        TUIBarrageButton.this,
                        R.layout.tx_layout_emoji
                );
                paintThickPopup.setOnCheckDialogListener(new EmojiPopup.onEmojiListener() {
                    @Override
                    public void onEmojiClick(@NonNull EmojiBean emojiBean) {
                        if (null != mBarrageSendView) {
                            TUIBarrageModel barrageModel = mBarrageSendView.createBarrageModel(emojiBean.getCode());
                            mBarrageSendView.sendBarrage(barrageModel);
                            paintThickPopup.dismissTip();
                        }

                    }
                });

                paintThickPopup.show();
            }
        });
    }

    //弹幕发送弹框显示,宽度自适应屏幕
    private void showSendDialog() {
        Window window = mBarrageSendView.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);
        mBarrageSendView.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mBarrageSendView.show();
    }
}
