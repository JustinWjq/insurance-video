package com.txt.video.ui.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.txt.video.R;
import com.txt.video.trtc.videolayout.Utils;
import com.txt.video.common.callback.onShareWhiteBroadDialogListener;


/**
 * Created by justin on 2017/8/25.
 */
public class ShareWhiteBroadDialog extends Dialog implements View.OnClickListener {
    private onShareWhiteBroadDialogListener mListener;
    private Context mContext;
    private boolean isShare = false;

    public boolean isShare() {
        return isShare;
    }

    public void setShare(boolean share) {
        isShare = share;
    }

    public ShareWhiteBroadDialog(Context context) {
        super(context, R.style.tx_MyDialog);
        mContext = context;


    }

    public void setOnShareWhiteBroadDialogListener(onShareWhiteBroadDialogListener listener) {
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tx_dialog_sharebroad_online);
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
//        attributes.height = Utils.getWindowHeight(mContext)/2;
        attributes.width = Utils.getWindowWidth(mContext);
        window.setGravity(Gravity.BOTTOM);

        setCanceledOnTouchOutside(true);
        initView();
    }

    AppCompatTextView gotofile;
    AppCompatTextView gotobroad;
    AppCompatTextView endshare;
    View txDivider;
    private void initView(){

         gotofile = findViewById(R.id.atv_gotofile);

         gotobroad = findViewById(R.id.atv_gotobroad);

         endshare = findViewById(R.id.atv_endshare);
        txDivider = findViewById(R.id.tx_divider);

        AppCompatTextView exit = findViewById(R.id.atv_exit);
        gotofile.setOnClickListener(this);
        gotobroad.setOnClickListener(this);
        endshare.setOnClickListener(this);
        exit.setOnClickListener(this);


    }

    public void showExitBroad(boolean isShare){
        if (isShare) {
            txDivider.setVisibility(View.VISIBLE);
            endshare.setVisibility(View.VISIBLE);
        }else{
            txDivider.setVisibility(View.GONE);
            endshare.setVisibility(View.GONE);
        }


    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (mListener != null) {
            if (id == R.id.atv_gotofile) {
                mListener.onCheckFileWhiteBroad();
                dismiss();
            } else if (id == R.id.atv_gotobroad) {
                mListener.onCheckBroad();
                dismiss();
            } else if (id == R.id.atv_endshare) {
                mListener.onEnd();
                dismiss();
            }else if (id == R.id.atv_exit) {
                dismiss();
            }
        }

    }
}
