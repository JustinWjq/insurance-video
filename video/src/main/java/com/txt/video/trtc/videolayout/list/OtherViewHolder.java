package com.txt.video.trtc.videolayout.list;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.txt.video.R;
import com.txt.video.TXSdk;
import com.txt.video.common.glide.TxGlide;
import com.txt.video.net.utils.TxLogUtils;

/**
 * Created by JustinWjq
 *
 * @date 2020/9/10.
 * description：
 */
public class OtherViewHolder extends RecyclerView.ViewHolder {
    private final GestureDetector mSimpleOnGestureListener = new GestureDetector(itemView.getContext(), new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            if (mListCallback != null) {
                mListCallback.onItemClick(getLayoutPosition());
            }
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mListCallback != null) {
                mListCallback.onItemDoubleClick(getLayoutPosition());
            }
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    });
    private TextView mUserNameTv;
    //    private       CircleImageView mUserHeadImg;
    private MemberEntity mMemberEntity;
    private FrameLayout mVideoContainer;
    private FrameLayout mNoVideoContainer;
    private ImageView mUserSignal;
    private ImageView mPbAudioVolume;
    private ImageView mIvVideClose;
    private ImageView mIvIconHost;
    private ConstraintLayout item_view;
    private boolean isPlaying = false;

    public OtherViewHolder(View itemView) {
        super(itemView);
        initView(itemView);
    }

    public void setQuality(int quality) {
        if (quality == MemberEntity.QUALITY_GOOD) {
            mUserSignal.setVisibility(View.VISIBLE);
            mUserSignal.setImageResource(R.drawable.tx_signal6);
        } else if (quality == MemberEntity.QUALITY_NORMAL) {
            mUserSignal.setVisibility(View.VISIBLE);
            mUserSignal.setImageResource(R.drawable.tx_signal3);
        } else if (quality == MemberEntity.QUALITY_BAD) {
            mUserSignal.setVisibility(View.VISIBLE);
            mUserSignal.setImageResource(R.drawable.tx_signal1);
        } else {
            mUserSignal.setVisibility(View.GONE);
        }
    }

    public void setVolume(int progress) {
        if (!mMemberEntity.isShowAudioEvaluation()) {
            mPbAudioVolume.setImageResource(R.drawable.tx_icon_volume_mute);
            return;
        }
        if (mPbAudioVolume != null) {
            //1-19
            //20-39
            //40-59
            //60-79
            //80-100
            if (progress == -1) {
                mPbAudioVolume.setImageResource(R.drawable.tx_icon_volume_mute);
            } else if (progress == 0) {
                mPbAudioVolume.setImageResource(R.drawable.tx_icon_volume_0);
            } else if (progress >= 1 && progress <= 19) {
                mPbAudioVolume.setImageResource(R.drawable.tx_icon_volume_1);
            } else if (progress >= 20 && progress <= 39) {
                mPbAudioVolume.setImageResource(R.drawable.tx_icon_volume_2);
            } else if (progress >= 40 && progress <= 59) {
                mPbAudioVolume.setImageResource(R.drawable.tx_icon_volume_3);
            } else if (progress >= 60 && progress <= 79) {
                mPbAudioVolume.setImageResource(R.drawable.tx_icon_volume_4);
            } else if (progress >= 80 && progress <= 100) {
                mPbAudioVolume.setImageResource(R.drawable.tx_icon_volume_5);
            }


        }
    }

    public void showVolume(boolean isShow) {

        mPbAudioVolume.setVisibility(View.VISIBLE);
        if (isShow) {

        } else {
            mPbAudioVolume.setImageResource(R.drawable.tx_icon_volume_mute);
        }

    }

    public void showHost(boolean isShow){

        if (isShow) {
            mIvIconHost.setVisibility(View.VISIBLE);
        }else{
            mIvIconHost.setVisibility(View.GONE);
        }

    }
    public void showHost(boolean isShow,String iconPath){

        if (isShow) {
            mIvIconHost.setVisibility(View.VISIBLE);
            TxGlide.with(TXSdk.getInstance().application).load(iconPath).into(mIvIconHost);
        }else{
            mIvIconHost.setVisibility(View.GONE);
        }

    }

    public void showNoVideo(boolean isShow, boolean isVideoClose) {
        if (isShow) {
            mIvVideClose.setBackground(isVideoClose ? ContextCompat.getDrawable(itemView.getContext(), R.drawable.tx_icon_close_video) : ContextCompat.getDrawable(itemView.getContext(), R.drawable.tx_icon_close_screen));

        }else{

        }
        mNoVideoContainer.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void addVideoView(MeetingVideoView meetingVideoView){
        meetingVideoView.addViewToViewGroup(mVideoContainer);
    }

    public void changeUserName(MemberEntity model){
        mUserNameTv.setText(model.getUserName());
        if (!model.getUserRoleIconPath().isEmpty()) {
            showHost(true,model.getUserRoleIconPath());
        }else{
            showHost(false,model.getUserRoleIconPath());
        }
    }

    public void bind(final MemberEntity model){
        mMemberEntity = model;
        changeUserName(model);
        if (model.getQuality() == MemberEntity.QUALITY_GOOD) {
            mUserSignal.setVisibility(View.VISIBLE);
            mUserSignal.setImageResource(R.drawable.tx_signal6);
        } else if (model.getQuality() == MemberEntity.QUALITY_NORMAL) {
            mUserSignal.setVisibility(View.VISIBLE);
            mUserSignal.setImageResource(R.drawable.tx_signal3);
        } else if (model.getQuality() == MemberEntity.QUALITY_BAD) {
            mUserSignal.setVisibility(View.VISIBLE);
            mUserSignal.setImageResource(R.drawable.tx_signal1);
        } else {
            mUserSignal.setVisibility(View.GONE);
        }
        showVolume(model.isShowAudioEvaluation());
        showNoVideo(!model.isVideoAvailable(), true);
//        showHost(model.isHost());
    }
    private MemberListAdapter.ListCallback mListCallback;

    public void bind(final MemberEntity model,
                     final MemberListAdapter.ListCallback listener) {
        mListCallback = listener;
        mMemberEntity = model;
        changeUserName(model);
        TxLogUtils.d("bind: " + mMemberEntity.getUserId() + " mVideoContainer " + mVideoContainer);
        MeetingVideoView videoView = mMemberEntity.getMeetingVideoView();
        if (videoView != null) {
        }
        videoView.setWaitBindGroup(mVideoContainer);
        mVideoContainer.removeAllViews();
        //展示其他数据
        if (!TextUtils.isEmpty(model.getUserAvatar())) {
//                Picasso.get().load(model.getUserAvatar()).placeholder(R.drawable.meeting_head).into(mUserHeadImg);
        } else {
//                mUserHeadImg.setImageResource(R.drawable.meeting_head);
        }
        mUserNameTv.setText(model.getUserName());
        if (model.getQuality() == MemberEntity.QUALITY_GOOD) {
            mUserSignal.setVisibility(View.VISIBLE);
            mUserSignal.setImageResource(R.drawable.tx_signal6);
        } else if (model.getQuality() == MemberEntity.QUALITY_NORMAL) {
            mUserSignal.setVisibility(View.VISIBLE);
            mUserSignal.setImageResource(R.drawable.tx_signal3);
        } else if (model.getQuality() == MemberEntity.QUALITY_BAD) {
            mUserSignal.setVisibility(View.VISIBLE);
            mUserSignal.setImageResource(R.drawable.tx_signal1);
        } else {
            mUserSignal.setVisibility(View.GONE);
        }
        showVolume(model.isShowAudioEvaluation());
        showNoVideo(!model.isVideoAvailable(), true);

//        item_view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return mSimpleOnGestureListener.onTouchEvent(event);
//            }
//        });

        mVideoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(getLayoutPosition());
                }
            }
        });
        mNoVideoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(getLayoutPosition());
                }
            }
        });
//        showHost(model.isHost());
    }

    private void initView(final View itemView) {
        mUserNameTv = (TextView) itemView.findViewById(R.id.trtc_tv_content);
        mVideoContainer = (FrameLayout) itemView.findViewById(R.id.trtc_tc_cloud_view);
        mNoVideoContainer = (FrameLayout) itemView.findViewById(R.id.trtc_fl_no_video);
        mIvVideClose = (ImageView) itemView.findViewById(R.id.iv_video_close);
//            mUserHeadImg = (CircleImageView) itemView.findViewById(R.id.img_user_head);
        mUserSignal = (ImageView) itemView.findViewById(R.id.trtc_iv_nos);
        mPbAudioVolume = (ImageView) itemView.findViewById(R.id.trtc_pb_audio);
        mIvIconHost = (ImageView) itemView.findViewById(R.id.trtc_icon_host);
    }
}
