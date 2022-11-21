package com.txt.video.trtc.videolayout.list;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.txt.video.R;
import com.txt.video.TXSdk;
import com.txt.video.common.CircleImageView;
import com.txt.video.common.glide.TxGlide;
import com.txt.video.net.utils.TxLogUtils;

/**
 * Created by JustinWjq
 *
 * @date 2020/9/10.
 * description：
 */
public class SelfViewHolder extends RecyclerView.ViewHolder {
    private TextView mUserNameTv, mUserNameTv1;
    private MeetingVideoView mViewVideo;
    private CircleImageView mUserHeadImg;
    private MemberEntity mMemberEntity;
    private FrameLayout mVideoContainer;
    private RelativeLayout mNoVideoContainer;
    private ImageView mPbAudioVolume, mPbAudioVolume1;
    private TextView mIvVideClose;
    private ImageView mIvIconHost, mIvIconHost1;
    private ImageView ivSelf;

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


    public void setVolume(int progress) {
        if (!mMemberEntity.isShowAudioEvaluation()) {
            mPbAudioVolume.setImageResource(R.drawable.tx_icon_volume_small_mute);
            mPbAudioVolume1.setImageResource(R.drawable.tx_icon_volume_small_mute);
            return;
        }
        if (mPbAudioVolume != null) {
            //1-19
            //20-39
            //40-59
            //60-79
            //80-100
            if (progress == -1) {
                mPbAudioVolume.setImageResource(R.drawable.tx_icon_volume_small_mute);
                mPbAudioVolume1.setImageResource(R.drawable.tx_icon_volume_small_mute);
            } else if (progress == 0) {
                mPbAudioVolume.setImageResource(R.drawable.tx_icon_volume_small_1);
                mPbAudioVolume1.setImageResource(R.drawable.tx_icon_volume_small_1);
            } else if (progress >= 1 && progress <= 19) {
                mPbAudioVolume.setImageResource(R.drawable.tx_icon_volume_small_1);
                mPbAudioVolume1.setImageResource(R.drawable.tx_icon_volume_small_1);
            } else if (progress >= 20 && progress <= 39) {
                mPbAudioVolume.setImageResource(R.drawable.tx_icon_volume_small_2);
                mPbAudioVolume1.setImageResource(R.drawable.tx_icon_volume_small_2);
            } else if (progress >= 40 && progress <= 59) {
                mPbAudioVolume.setImageResource(R.drawable.tx_icon_volume_small_3);
                mPbAudioVolume1.setImageResource(R.drawable.tx_icon_volume_small_3);
            } else if (progress >= 60 && progress <= 79) {
                mPbAudioVolume.setImageResource(R.drawable.tx_icon_volume_small_5);
                mPbAudioVolume1.setImageResource(R.drawable.tx_icon_volume_small_5);
            } else if (progress >= 80 && progress <= 100) {
                mPbAudioVolume.setImageResource(R.drawable.tx_icon_volume_small_6);
                mPbAudioVolume1.setImageResource(R.drawable.tx_icon_volume_small_6);
            }


        }
    }

    public void showVolume(boolean isShow) {

        mPbAudioVolume.setVisibility(View.VISIBLE);
        if (isShow) {

        } else {
            mPbAudioVolume.setImageResource(R.drawable.tx_icon_volume_small_mute);
            mPbAudioVolume1.setImageResource(R.drawable.tx_icon_volume_small_mute);
        }

    }

    public void showHost(boolean isShow) {

        if (isShow) {
            mIvIconHost.setVisibility(View.VISIBLE);
            mIvIconHost1.setVisibility(View.VISIBLE);
        } else {
            mIvIconHost.setVisibility(View.GONE);
            mIvIconHost1.setVisibility(View.GONE);
        }

    }

    public void showHost(boolean isShow, String iconPath) {

        if (isShow) {
            mIvIconHost.setVisibility(View.VISIBLE);
            mIvIconHost1.setVisibility(View.VISIBLE);
            TxGlide.with(TXSdk.getInstance().application).load(iconPath).into(mIvIconHost);
            TxGlide.with(TXSdk.getInstance().application).load(iconPath).into(mIvIconHost1);
        } else {
            mIvIconHost.setVisibility(View.GONE);
            mIvIconHost1.setVisibility(View.GONE);
        }

    }


    public void showNoVideo(boolean isShow, boolean isVideoClose) {
        if (mMemberEntity.getUserHead().isEmpty()) {
            String userName = mMemberEntity.getUserName();
            if (mMemberEntity.getUserName().length() > 2) {
                userName = userName.substring(userName.length() - 2, userName.length());
            }
            mIvVideClose.setText(userName);
            mIvVideClose.setVisibility(View.VISIBLE);
            TxGlide.with(TXSdk.getInstance().application).load("")
                    .into(mUserHeadImg);
            mUserHeadImg.setCircleBackgroundColor(ContextCompat.getColor(TXSdk.getInstance().application, R.color.tx_color_e6b980));
        } else {
            mIvVideClose.setVisibility(View.GONE);
            TxGlide.with(TXSdk.getInstance().application).load(mMemberEntity.getUserHead())
                    .into(mUserHeadImg);
        }
        //判断当前list数量只有一个的时候就显示背景图

        TxLogUtils.i("SelfViewHolder","showNoVideo" );
        mNoVideoContainer.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void hideBg(boolean isHide){
        TxLogUtils.i("SelfViewHolder","" + isHide);
        if (!isHide) {
            if (mMemberEntity.isHost()) {
                ivSelf.setVisibility(View.VISIBLE);
            }else{
                ivSelf.setVisibility(View.GONE);
            }
        } else {
            ivSelf.setVisibility(View.GONE);
        }
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
        mUserNameTv1.setText(model.getUserName());
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


        showVolume(model.isShowAudioEvaluation());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(getLayoutPosition());
            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(getLayoutPosition());
            }
        });
//        showHost(model.isHost());
        hideBg(false);
    }

    //iv_video_head
    private void initView(final View itemView) {

        mVideoContainer = (FrameLayout) itemView.findViewById(R.id.trtc_tc_cloud_view);
        mNoVideoContainer = (RelativeLayout) itemView.findViewById(R.id.trtc_fl_no_video);
        mIvVideClose = (TextView) itemView.findViewById(R.id.iv_video_close);
        mUserHeadImg = (CircleImageView) itemView.findViewById(R.id.iv_video_head);
        mPbAudioVolume = (ImageView) itemView.findViewById(R.id.trtc_pb_audio);
        mPbAudioVolume1 = (ImageView) itemView.findViewById(R.id.trtc_pb_audio1);
        mIvIconHost = (ImageView) itemView.findViewById(R.id.trtc_icon_host);
        mIvIconHost1 = (ImageView) itemView.findViewById(R.id.trtc_icon_host1);
        mUserNameTv = (TextView) itemView.findViewById(R.id.trtc_tv_content);
        mUserNameTv1 = (TextView) itemView.findViewById(R.id.trtc_tv_content1);
        ivSelf = (ImageView) itemView.findViewById(R.id.iv_self);
    }
}