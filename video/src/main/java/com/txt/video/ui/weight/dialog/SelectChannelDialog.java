package com.txt.video.ui.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.txt.video.R;
import com.txt.video.common.callback.onShareWhiteBroadDialogListener;
import com.txt.video.trtc.videolayout.Utils;

/**
 * author ：Justin
 * time ：6/25/21.
 * des ：
 */
public class SelectChannelDialog extends Dialog implements View.OnClickListener {
    private onShareWhiteBroadDialogListener mListener;
    private Context mContext;
    private boolean isShare = false;

    public boolean isShare() {
        return isShare;
    }

    public void setShare(boolean share) {
        isShare = share;
    }

    public SelectChannelDialog(Context context) {
        super(context, R.style.tx_MyDialog);
        mContext = context;


    }

    public void setOnShareWhiteBroadDialogListener(onShareWhiteBroadDialogListener listener) {
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tx_dialog_channel);
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = Utils.getWindowWidth(mContext);
        window.setGravity(Gravity.BOTTOM);

        setCanceledOnTouchOutside(true);
        initView();
    }
    TextView tv_endshare,tv_pushshare;
    private void initView(){

        tv_endshare = findViewById(R.id.tv_endshare);
        tv_pushshare = findViewById(R.id.tv_pushshare);
        TextView atv_exit = findViewById(R.id.atv_exit);
        atv_exit.setOnClickListener(this);
        tv_endshare.setOnClickListener(this);
        tv_pushshare.setOnClickListener(this);
    }

    public void setText(String name){
        tv_pushshare.setText("推送（"+name+")");
        tv_endshare.setText("同屏（"+name+")");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (mListener != null) {
            if (id == R.id.tv_endshare) {
                mListener.onCheckFileWhiteBroad();
                dismiss();
            } else if (id == R.id.tv_pushshare) {
                mListener.onCheckBroad();
                dismiss();
            } else if (id == R.id.atv_endshare) {
                mListener.onShareWhiteBroadEnd();
                dismiss();
            }else if (id == R.id.atv_exit) {
                dismiss();
            }
        }

    }
}
