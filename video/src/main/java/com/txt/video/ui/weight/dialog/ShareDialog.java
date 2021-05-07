package com.txt.video.ui.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.txt.video.R;
import com.txt.video.trtc.videolayout.Utils;
import com.txt.video.ui.weight.adapter.ExpandableItemAdapter;
import com.txt.video.common.callback.onShareDialogListener;


/**
 * Created by justin on 2017/8/25.
 */
public class ShareDialog extends Dialog implements View.OnClickListener {
    private onShareDialogListener mListener;
    private Context mContext;

    private ExpandableItemAdapter baseQuickAdapter;

    public ShareDialog(Context context) {
        super(context, R.style.tx_MyDialog);
        mContext = context;


    }

    public void setOnConfirmlickListener(onShareDialogListener listener) {
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tx_dialog_share);
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
//        attributes.height = Utils.getWindowHeight(mContext)/2;
        attributes.width = Utils.getWindowWidth(mContext);
        window.setGravity(Gravity.BOTTOM);

        setCanceledOnTouchOutside(true);
        initView();
    }

    TextView tv_top;

    private void initView() {
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

    public void setShareContent(String shareNum){
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("会议中支持"+shareNum+"位成员同时在线,\n超出数量后，后面成员将无法进入会话！");
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(mContext, R.color.tx_color_ce1b2b));
        spannableStringBuilder.setSpan(foregroundColorSpan,5,5+shareNum.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_top.setText(spannableStringBuilder);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (mListener != null) {
            if (id == R.id.tv_uploadpic) {
                mListener.onConfirmWx();
                dismiss();
            } else if (id == R.id.tv_uploadfile) {
                mListener.onConfirmMSG();
                dismiss();
            }else if (id == R.id.tv_share_fd){
                mListener.onConfirmFd();
                dismiss();
            }else if (id == R.id.tv_title) {
                dismiss();
            }
        }

    }
}
