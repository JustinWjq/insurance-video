package com.txt.video.ui.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import com.txt.video.R;
import com.txt.video.common.callback.onExitDialogListener;


/**
 * Created by justin on 2017/8/25.
 */
public class CommonDialog extends Dialog {
    private onExitDialogListener mListener;
    private Context mContext;

    String cancelStr;
    String titleStr;
    String confirmStr;
    String contentStr;
    String type;

    TextView tv_cancel;
    TextView tv_title;
    TextView tv_confirm;
    TextView tv_content;
    TextView tv_tip1;
    TextView tv_onlytext;
    View xpopup_divider_h;

    public CommonDialog(Context context) {
        super(context, R.style.tx_MyDialog);
        mContext = context;
    }

    public void setOnConfirmlickListener(onExitDialogListener listener) {
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tx_dialog_common);
        setCanceledOnTouchOutside(false);
        initView();
    }


    public void setTv_cancel(String cancelStr) {
        tv_onlytext.setVisibility(View.GONE);
        if (cancelStr.isEmpty()) {
            tv_cancel.setVisibility(View.GONE);
        } else {
            xpopup_divider_h.setVisibility(View.VISIBLE);
            tv_cancel.setVisibility(View.VISIBLE);
            tv_cancel.setText(cancelStr);
        }
    }

    public void setTv_title(String titleStr) {
        tv_title.setText(titleStr);
    }

    public void setTv_confirm(String confirmStr) {
        tv_onlytext.setVisibility(View.GONE);
        if (confirmStr.isEmpty()) {
            tv_confirm.setVisibility(View.GONE);
        } else {
            tv_confirm.setVisibility(View.VISIBLE);
            tv_confirm.setText(confirmStr);
        }

    }

    public void setTv_content(String contentStr) {
        if (contentStr.isEmpty()) {
            tv_content.setVisibility(View.GONE);
        } else {
            tv_content.setVisibility(View.VISIBLE);
            tv_content.setText(contentStr);

        }

    }

    public void setOnlyConfirm(String confirmStr) {

        xpopup_divider_h.setVisibility(View.GONE);
        tv_cancel.setVisibility(View.GONE);
        tv_confirm.setVisibility(View.GONE);
        tv_onlytext.setVisibility(View.VISIBLE);
        tv_onlytext.setText(confirmStr);

    }

    //展示录像计时器
    private CountDownTimer timer = null;
    private int mCurrentTimer = 0;

    public void startCommonTimer(final int notifyEndTime, String tipStr) {
        mCurrentTimer = 0;
        tv_tip1.setVisibility(View.VISIBLE);
        tv_tip1.setText(tipStr);
        if (timer == null) {
            timer = new CountDownTimer(notifyEndTime*1000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    int l = notifyEndTime - (mCurrentTimer / 1000);
                    setTv_cancel("结束会话("+l+"s)");

                    mCurrentTimer= mCurrentTimer+1000;
                }

                @Override
                public void onFinish() {
                    if (mListener != null) {
                        mCurrentTimer = 0;
                        mListener.end();
                        dismiss();
                    }
                }
            };


        }
        timer.start();

    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopTimer();
    }

    private void stopTimer() {
        mCurrentTimer = 0;
        if (timer!=null){
            tv_tip1.setVisibility(View.GONE);
            timer.cancel();
            timer = null;
        }

    }


    private void initView() {
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_title = findViewById(R.id.tv_title);
        tv_confirm = findViewById(R.id.tv_confirm);
        tv_content = findViewById(R.id.tv_content);
        xpopup_divider_h = findViewById(R.id.xpopup_divider_h);
        tv_tip1 = findViewById(R.id.tv_tip);
        tv_onlytext = findViewById(R.id.tv_onlytext);


        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mCurrentTimer = 0;
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
        tv_onlytext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
//                    mListener.onTemporarilyLeave();
                    dismiss();
                }
            }
        });

    }


}
