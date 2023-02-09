package com.txt.video.ui.weight.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
 * des ：用来显示大屏幕的view
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
    ImageView bt_info,bt_info_nice;
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
        bt_info = itemView.findViewById(R.id.bt_info);
        bt_info_nice = itemView.findViewById(R.id.bt_info_nice);
//
        bigscreen.setOnClickListener(this);
        bt_info.setOnClickListener(this);
        bt_info_nice.setOnClickListener(this);
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
        }else if (id == R.id.bt_info ||id == R.id.bt_info_nice){
            if (null != mBigScreenViewCallback) {
                mBigScreenViewCallback.onClickInfo();
            }
        }
    }


    public interface BigScreenViewCallback {
        void onScreenFinishClick();

        void onClickInfo();

        void onMuteAudioClick();

        void onMuteVideoClick();
    }

    public void closeVideo(boolean isClose) {
        TxLogUtils.d("closeVideo------isClose"+isClose);
        if (isClose) {
            iv_video_srccen.setVisibility(VISIBLE);
            iv_video_close.setVisibility(GONE);
            iv_video_srccen.setBackground(ContextCompat.getDrawable(this.getContext(),R.drawable.tx_icon_close_video));
            mCloseVideo.setVisibility(VISIBLE);
        } else {
            iv_video_srccen.setVisibility(GONE);
            iv_video_close.setVisibility(GONE);
            mCloseVideo.setVisibility(GONE);
        }
    }

    public void showScreenIcon(boolean isShow){
        if (isShow) {
            mCloseVideo.setVisibility(VISIBLE);
            iv_video_srccen.setVisibility(VISIBLE);
            iv_video_srccen.setBackground(ContextCompat.getDrawable(this.getContext(),R.drawable.tx_icon_close_screen));
            iv_video_close.setVisibility(GONE);
        }else{
            mCloseVideo.setVisibility(GONE);
            iv_video_srccen.setVisibility(GONE);
            iv_video_close.setVisibility(VISIBLE);
        }
    }


    public void closeVideo(boolean isClose,String url) {
        TxLogUtils.i("closeVideo------isClose"+isClose);
        if (isClose) {
            mCloseVideo.setVisibility(VISIBLE);
            iv_video_close.setVisibility(VISIBLE);
            iv_video_srccen.setVisibility(GONE);
            TxGlide.with(iv_video_close.getContext()).load(url)
                    .placeholder(R.drawable.tx_icon_close_video)
                    .into(iv_video_close);
        } else {
            mCloseVideo.setVisibility(GONE);
            iv_video_srccen.setVisibility(GONE);
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
    public void showInfoIcon(boolean hide){
        bt_info_nice.setVisibility(GONE);
        if (hide){
            bt_info.setVisibility(GONE);
        }else{
            bt_info.setVisibility(VISIBLE);
        }


    }

    public void showInfoIconNice(boolean hide){
        bt_info.setVisibility(GONE);
        if (hide){
            bt_info_nice.setVisibility(GONE);
        }else{
            bt_info_nice.setVisibility(VISIBLE);
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
