package com.txt.video.ui.weight.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;

import com.txt.video.R;
import com.txt.video.TXSdk;
import com.txt.video.common.glide.TxGlide;
import com.txt.video.net.utils.TxLogUtils;

/**
 * @author ：Justin
 * time ：2021/3/1.
 * des ：用来显示投屏的view
 */
public class BigScreenView extends RelativeLayout implements View.OnClickListener {
    public BigScreenView(@NonNull Context context) {
        this(context, null);
    }

    public BigScreenView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.tx_view_bigscreenview, this);
        initView(this);
    }

    public BigScreenView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    FrameLayout bigscreen;
    FrameLayout mCloseVideo;
    Group mGroup;
    ImageView mIconHostPath, mAudioIB,iv_video_close,iv_video_srccen;
    TextView mName;
    private BigScreenViewCallback mBigScreenViewCallback;

    public void initView(View itemView) {
        bigscreen = itemView.findViewById(R.id.trtc_tc_cloud_view);
        mIconHostPath = itemView.findViewById(R.id.trtc_icon_host);
        mAudioIB = itemView.findViewById(R.id.trtc_pb_audio);
        mName = itemView.findViewById(R.id.trtc_tv_content);
        mCloseVideo = itemView.findViewById(R.id.trtc_fl_no_video);
        iv_video_close = itemView.findViewById(R.id.iv_video_close);
        iv_video_srccen = itemView.findViewById(R.id.iv_video_srccen);
//
        bigscreen.setOnClickListener(this);
//        mVideoIB.setOnClickListener(this);
//        mAudioIB.setOnClickListener(this);
//        mSwitchIB.setOnClickListener(this);
//        mHangUpIB.setOnClickListener(this);

    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == View.VISIBLE) {
        } else {

        }
    }


    public FrameLayout getBigScreenView() {
        return bigscreen;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }


    public void setBigScreenCallBack(BigScreenViewCallback bigScreenViewCallback) {
        this.mBigScreenViewCallback = bigScreenViewCallback;
    }

    public void removeBigScreenCallBack() {
        this.mBigScreenViewCallback = null;
    }

    public void muteVideo(boolean isMute) {
       closeVideo(isMute);
    }

    public void muteAudio(boolean isMute) {
        mAudioIB.setSelected(isMute);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();


        if (id == R.id.trtc_tc_cloud_view) {
            if (null != mBigScreenViewCallback) {
                mBigScreenViewCallback.onScreenFinishClick();
            }
        }
    }


    public interface BigScreenViewCallback {
        void onScreenFinishClick();

        void onSwitch();

        void onMuteAudioClick();

        void onMuteVideoClick();
    }

    public void closeVideo(boolean isClose) {
        TxLogUtils.d("closeVideo1234"+"isClose"+isClose);
        if (isClose) {
            iv_video_close.setVisibility(VISIBLE);
            ViewGroup.LayoutParams layoutParams = iv_video_close.getLayoutParams();
            layoutParams.width = 300;
            layoutParams.height = 440;
            iv_video_close.setLayoutParams(layoutParams);
            iv_video_close.setBackground(ContextCompat.getDrawable(this.getContext(),R.drawable.tx_icon_close_video));
            mCloseVideo.setVisibility(VISIBLE);
        } else {
            iv_video_close.setVisibility(GONE);
            mCloseVideo.setVisibility(GONE);
        }
    }

    public void showScreenIcon(boolean isShow){
        if (isShow) {
            mCloseVideo.setVisibility(VISIBLE);
            iv_video_srccen.setVisibility(VISIBLE);
            iv_video_close.setVisibility(GONE);
        }else{
            mCloseVideo.setVisibility(GONE);
            iv_video_srccen.setVisibility(GONE);
            iv_video_close.setVisibility(VISIBLE);
        }
    }


    public void closeVideo(boolean isClose,String url) {
        TxLogUtils.d("closeVideo1234"+url+"isClose"+isClose);
        if (isClose) {
            mCloseVideo.setVisibility(VISIBLE);
            iv_video_close.setVisibility(VISIBLE);

            TxGlide.with(iv_video_close.getContext()).load(url)
                    .placeholder(R.drawable.tx_icon_close_video)
                    .into(iv_video_close);
        } else {
            mCloseVideo.setVisibility(GONE);
            iv_video_close.setVisibility(GONE);
        }
    }

    public void changeBigScreenViewName(String text, String userRole, String userRoleIconPath) {
        mName.setText(text);
        if (userRole.equals("owner") || userRole.equals("assistant")) {
            mIconHostPath.setVisibility(View.VISIBLE);
            TxGlide.with(TXSdk.getInstance().application).load(userRoleIconPath)
                    .into(mIconHostPath);

        } else {
            mIconHostPath.setVisibility(View.INVISIBLE);

        }

    }

    public void changeBigScreenViewVoice(boolean isShowAudioEvaluation, int volume) {
        if (!isShowAudioEvaluation) {
            mAudioIB.setImageResource(
                    R.drawable.tx_icon_volume_mute
            );
        } else {
            int drawable;
            if (-1 == volume) {
                drawable = R.drawable.tx_icon_volume_mute;
            } else if (0 == volume) {
                drawable = R.drawable.tx_icon_volume_0;
            } else if (1 <= volume && 19 >= volume) {
                drawable = R.drawable.tx_icon_volume_1;
            } else if (20 <= volume && 39 >= volume) {
                drawable = R.drawable.tx_icon_volume_2;
            } else if (40 <= volume && 59 >= volume) {
                drawable = R.drawable.tx_icon_volume_3;
            } else if (60 <= volume && 79 >= volume) {
                drawable = R.drawable.tx_icon_volume_4;
            } else if (80 <= volume && 100 >= volume) {
                drawable = R.drawable.tx_icon_volume_5;
            } else {
                drawable = R.drawable.tx_icon_volume_4;
            }

            mAudioIB.setImageResource(
                    drawable
            );
        }

    }

}
