package com.txt.video.ui.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.txt.video.R;
import com.txt.video.common.callback.onExitDialogListener;


/**
 * Created by justin on 2017/8/25.
 */
public class ExitDialog extends Dialog {
    private onExitDialogListener mListener;
    private Context mContext;

    String cancelStr;
    String titleStr;
    String confirmStr;
    String contentStr;

    TextView tv_cancel;
    TextView tv_title;
    TextView tv_confirm;
    TextView tv_content;
    View xpopup_divider_h;

    public ExitDialog(Context context,
                      String cancelStr,
                      String titleStr,
                      String confirmStr,
                      String contentStr) {
        super(context, R.style.tx_MyDialog);
        mContext = context;
        this.cancelStr = cancelStr;
        this.titleStr = titleStr;
        this.confirmStr = confirmStr;
        this.contentStr = contentStr;

    }

    public void setOnConfirmlickListener(onExitDialogListener listener) {
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tx_dialog_control);
        setCanceledOnTouchOutside(true);
        initView();
    }



    public void setTv_cancel(String cancelStr) {
        tv_cancel.setText(cancelStr);
    }

    public void setTv_title(String titleStr) {
        tv_title.setText(titleStr);
    }

    public void setTv_confirm(String confirmStr) {
        tv_confirm.setText(confirmStr);
    }

    public void setTv_content(String contentStr) {
        if (contentStr.isEmpty()){
            tv_content.setVisibility(View.GONE);
        }else{
            tv_content.setVisibility(View.VISIBLE);
            tv_content.setText(contentStr);

        }

    }

    public void setOnlyConfirm(String confirmStr){

        xpopup_divider_h.setVisibility(View.GONE);
        tv_cancel.setVisibility(View.GONE);
        tv_confirm.setText(confirmStr);

    }

    private void initView() {
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_title = findViewById(R.id.tv_title);
        tv_confirm = findViewById(R.id.tv_confirm);
        tv_content = findViewById(R.id.tv_content);
        xpopup_divider_h = findViewById(R.id.xpopup_divider_h);

        setTv_cancel(cancelStr);
        setTv_title(titleStr);
        setTv_confirm(confirmStr);

        setTv_content(contentStr);


        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onConfirm();
                    dismiss();
                }
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onTemporarilyLeave();
                    dismiss();
                }
            }
        });


    }


}
