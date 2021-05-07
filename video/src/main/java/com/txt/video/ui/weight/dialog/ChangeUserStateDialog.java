package com.txt.video.ui.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.txt.video.R;
import com.txt.video.trtc.videolayout.list.MemberEntity;
import com.txt.video.common.callback.onMuteDialogListener;


/**
 * Created by justin on 2017/8/25.
 */
public class ChangeUserStateDialog extends Dialog implements View.OnClickListener {
    private onMuteDialogListener mListener;
    private Context mContext;
    private MemberEntity mMemberEntity;

    public MemberEntity getMemberEntity() {
        return mMemberEntity;
    }

    public ChangeUserStateDialog(Context context) {
        super(context, R.style.tx_MyDialog);
        this.mContext = context;

    }

    public void setOnMuteDialogListener(onMuteDialogListener listener) {
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tx_dialog_changeuserstate);
        Window window = getWindow();
        if (window!=null) {
            window.setGravity(Gravity.CENTER);
        }
        setCanceledOnTouchOutside(true);
        initView();
    }

    TextView mTvTitle;
    AppCompatTextView mAtvMuteAudio;
    ImageView mIvMuteAudio;
    AppCompatTextView mAtvMuteVideo;
    ImageView mIvMuteVideo;

    private void initView(){

        mTvTitle = findViewById(R.id.tv_title);

        mAtvMuteAudio = findViewById(R.id.atv_muteaudio);

        mIvMuteAudio  = findViewById(R.id.iv_muteaudio);

        mAtvMuteVideo = findViewById(R.id.atv_mutevideo);

        mIvMuteVideo = findViewById(R.id.iv_mutevideo);

        ImageView iv_close = findViewById(R.id.iv_close);

        LinearLayout ll_muteaudio = findViewById(R.id.ll_muteaudio);
        LinearLayout ll_mutevideo = findViewById(R.id.ll_mutevideo);

        mTvTitle.setOnClickListener(this);
        mAtvMuteAudio.setOnClickListener(this);
        mAtvMuteVideo.setOnClickListener(this);
        ll_muteaudio.setOnClickListener(this);
        ll_mutevideo.setOnClickListener(this);

        iv_close.setOnClickListener(this);
    }

    public String getUserId(){
        if (null!=  mMemberEntity) {
            return mMemberEntity.getUserId();
        }else{
            return "";
        }
    }

    public void changeLay(MemberEntity mMemberEntity){
        this.mMemberEntity = mMemberEntity;
        String userName = mMemberEntity.getUserName();
        boolean muteVideo = mMemberEntity.isMuteVideo();
        boolean muteAudio = mMemberEntity.isMuteAudio();
        if (mTvTitle!=null) {
            mTvTitle.setText(userName);
        }
        if (muteAudio){
            if (mAtvMuteAudio!=null) {
                mAtvMuteAudio.setText("关闭静音");
            }
            if (mIvMuteAudio!=null) {
                mIvMuteAudio.setImageResource(R.drawable.tx_icon_remoteuser_mic_off);
            }
        }else{
            if (mAtvMuteAudio!=null) {
                mAtvMuteAudio.setText("开启静音");
            }
            if (mIvMuteAudio!=null) {
                mIvMuteAudio.setImageResource(R.drawable.tx_icon_remoteuser_mic_on);
            }

        }

        if (muteVideo){
            if (mAtvMuteVideo!=null) {
                mAtvMuteVideo.setText("开启视频");
            }
            if (mIvMuteVideo!=null) {
                mIvMuteVideo.setImageResource(R.drawable.tx_icon_remoteuser_camera_off);
            }
        }else{
            if (mAtvMuteVideo!=null) {
                mAtvMuteVideo.setText("关闭视频");
            }
            if (mIvMuteVideo!=null) {
                mIvMuteVideo.setImageResource(R.drawable.tx_icon_remoteuser_camera_on);
            }

        }


    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (mListener != null) {
            if (id == R.id.ll_mutevideo) {
                mListener.onMuteVideo(mMemberEntity);
                dismiss();
            } else if (id == R.id.ll_muteaudio) {
                mListener.onMuteAudio(mMemberEntity);
                dismiss();
            } else if (id == R.id.atv_endshare) {
                dismiss();
            }else if (id == R.id.iv_close) {
                dismiss();
            }
        }

    }
}
