package com.txt.video.trtc.videolayout.list;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.txt.video.R;
import com.txt.video.TXSdk;
import com.txt.video.common.glide.TxGlide;

/**
 * Created by JustinWjq
 *
 * @date 2020/9/10.
 * description：
 */
public class SelfViewHolder extends RecyclerView.ViewHolder {
    private TextView mUserNameTv;
    private MeetingVideoView mViewVideo;
    //        private CircleImageView  mUserHeadImg;
    private MemberEntity mMemberEntity;
    private FrameLayout mVideoContainer;
    private FrameLayout mNoVideoContainer;
    private ImageView mUserSignal;
    private ImageView mPbAudioVolume;
    private ImageView mIvVideClose;
    private ImageView mIvIconHost;

    public SelfViewHolder(View itemView) {
        super(itemView);
        initView(itemView);
    }

    public MemberEntity getMemberEntity() {
        return mMemberEntity;
    }

    public FrameLayout getVideoContainer() {
        return mVideoContainer;
    }

    public MeetingVideoView getViewVideo() {
        return mViewVideo;
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

    public void showHost(boolean isShow) {

        if (isShow) {
            mIvIconHost.setVisibility(View.VISIBLE);
        } else {
            mIvIconHost.setVisibility(View.GONE);
        }

    }

    public void showHost(boolean isShow, String iconPath) {

        if (isShow) {
            mIvIconHost.setVisibility(View.VISIBLE);
            TxGlide.with(TXSdk.getInstance().application).load(iconPath).into(mIvIconHost);
        } else {
            mIvIconHost.setVisibility(View.GONE);
        }

    }


    public void showNoVideo(boolean isShow, boolean isVideoClose) {

        mIvVideClose.setBackground(isVideoClose ? ContextCompat.getDrawable(itemView.getContext(), R.drawable.tx_icon_close_video) : ContextCompat.getDrawable(itemView.getContext(), R.drawable.tx_icon_close_screen));

        mNoVideoContainer.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void addVideoView() {
        if (mMemberEntity == null) {
            return;
        }
        mViewVideo = mMemberEntity.getMeetingVideoView();
        ViewGroup viewGroup = (ViewGroup) mViewVideo.getParent();
        if (viewGroup != null && viewGroup != mVideoContainer) {
            viewGroup.removeView(mViewVideo);
        }
        if (viewGroup != mVideoContainer) {
            mVideoContainer.addView(mViewVideo);
            mViewVideo.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        }
    }

    public void removeVideoView() {
        mVideoContainer.removeAllViews();
    }


    public void changeUserName(MemberEntity model) {
        mUserNameTv.setText(model.getUserName());
        if (!model.getUserRoleIconPath().isEmpty()) {
            showHost(true, model.getUserRoleIconPath());
        } else {
            showHost(false, model.getUserRoleIconPath());
        }

    }

    public void bind(final MemberEntity model,
                     final MemberListAdapter.ListCallback listener) {
        mMemberEntity = model;
        changeUserName(model);
        if (!TextUtils.isEmpty(model.getUserAvatar())) {
//            Picasso.get().load(model.getUserAvatar()).placeholder(R.drawable.meeting_head).into(mUserHeadImg);
        }
        //            addVideoView();
        mMemberEntity.getMeetingVideoView().setWaitBindGroup(mVideoContainer);
        mVideoContainer.removeAllViews();
        if (model.isVideoAvailable()) {
            mVideoContainer.setVisibility(View.VISIBLE);
//                mUserHeadImg.setVisibility(View.GONE);
        } else {
            mVideoContainer.setVisibility(View.GONE);
//                mUserHeadImg.setVisibility(View.VISIBLE);
        }
        if (model.isMuteVideo()) {
            showNoVideo(true, true);
        } else {
            showNoVideo(false, true);
        }
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
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(getLayoutPosition());
            }
        });
//        showHost(model.isHost());
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
        if (model.isMuteVideo()) {
            showNoVideo(true, true);
        } else {
            showNoVideo(false, true);
        }
        showHost(model.isHost());
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
//        int widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
//        itemView.findViewById(R.id.item_view).setLayoutParams(new ViewGroup.LayoutParams(widthPixels/2, widthPixels/2));

    }
}