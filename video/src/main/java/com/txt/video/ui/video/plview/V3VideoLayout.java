package com.txt.video.ui.video.plview;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.txt.video.R;
import com.txt.video.TXSdk;
import com.txt.video.common.CircleImageView;
import com.txt.video.common.glide.TxGlide;
import com.txt.video.net.utils.TxLogUtils;
import com.txt.video.trtc.remoteuser.TRTCRemoteUserManager;
import com.txt.video.trtc.videolayout.list.MeetingPageLayoutManager;
import com.txt.video.trtc.videolayout.list.MeetingPageLayoutManager1;
import com.txt.video.trtc.videolayout.list.MeetingVideoView;
import com.txt.video.trtc.videolayout.list.MemberEntity;
import com.txt.video.trtc.videolayout.list.MemberListAdapter;
import com.txt.video.ui.weight.easyfloat.utils.DisplayUtils;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * 用来展示横屏和竖屏的视频布局切换
 */
public class V3VideoLayout extends RelativeLayout implements  ITxPlDisplayView {
    private static final String TAG = "V2VideoLayout";

    public V3VideoLayout(Context context) {
        this(context, null);
    }

    public V3VideoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFuncLayout();
    }

    public V3VideoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFuncLayout();
    }

    ViewGroup mVgFuc;
    RecyclerView trtc_video_view_layout;
    LinearLayout ll_videolayouttwo;
    RelativeLayout rl_left;
    RelativeLayout rl_video_view_layout;
    ImageView iv_self;
    private void initFuncLayout() {

        mVgFuc = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.tx_layout_trtc_videolayoutvthree, this, true);
        trtc_video_view_layout = mVgFuc.findViewById(R.id.trtc_video_view_layout1);
        rl_video_view_layout = mVgFuc.findViewById(R.id.rl_video_view_layout);
        ll_videolayouttwo = mVgFuc.findViewById(R.id.ll_videolayouttwo);
        rl_left = mVgFuc.findViewById(R.id.rl_left);

        iv_self = (ImageView) mVgFuc.findViewById(R.id.iv_self);
        iv_self.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnVideoLayout) {
                    mOnVideoLayout.onClick();
                }
            }
        });

        rl_video_view_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnVideoLayout) {
                    mOnVideoLayout.onClick();
                }
            }
        });

    }

    private boolean isLand = true;

    @Override
    public void switchScreen(boolean checkToPro) {
        TxLogUtils.i(TAG, "switchScreen");
        TxLogUtils.i(TAG, "mMemberEntityList.size()" + mMemberEntityList.size());
        TxLogUtils.i(TAG, "bigMeetingEntity.isNull" + (null == bigMeetingEntity));
        //需要区分下当前的状态
        if (isLand){
            changProHasBigVideo();
        }else{
            changeHasVideoUi();
        }


        isLand = !isLand;

    }

    /**
     * 切换成竖屏的ui布局
     */
    private void changProHasBigVideo() {
        TxLogUtils.i(TAG, "changProHasBigVideo");
        ll_videolayouttwo.setOrientation(LinearLayout.VERTICAL);
        ll_videolayouttwo.setGravity(Gravity.CENTER);
        ViewGroup.LayoutParams rl_leftlayoutParams = rl_left.getLayoutParams();
        rl_leftlayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        rl_leftlayoutParams.height = DisplayUtils.INSTANCE.getScreenHeight(getContext()) / 3;
        rl_left.setLayoutParams(rl_leftlayoutParams);
        rl_left.setVisibility(VISIBLE);


        ViewGroup.LayoutParams layoutParams = trtc_video_view_layout.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = DisplayUtils.INSTANCE.getScreenHeight(getContext()) / 4;
        trtc_video_view_layout.setLayoutManager(meetingPageLayoutManager1);

    }


    /**
     * 切换成横屏的ui布局
     */
    private void changeHasVideoUi() {
        ll_videolayouttwo.setOrientation(LinearLayout.HORIZONTAL);
        trtc_video_view_layout.setLayoutManager(verticalmeetingpagelayoutmanager1);
        ViewGroup.LayoutParams layoutParams = trtc_video_view_layout.getLayoutParams();
        int screenSize = DisplayUtils.INSTANCE.getScreenWidth(getContext());
        TxLogUtils.i("DisplayUtils.INSTANCE----" +  DisplayUtils.INSTANCE.hasNavigationBar(getContext()));

        layoutParams.width =  screenSize / 10 * 3;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        trtc_video_view_layout.setLayoutParams(layoutParams);


        ViewGroup.LayoutParams rl_leftlayoutParams = rl_left.getLayoutParams();
        rl_leftlayoutParams.width = screenSize / 10 * 7;
        rl_leftlayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        rl_left.setLayoutParams(rl_leftlayoutParams);
        rl_left.setVisibility(VISIBLE);

    }

    @Override
    public void notifyItemChangedPld(int position, @Nullable Object payload) {
        if (null != mMemberListAdapter) {
            //如果大屏幕
            mMemberListAdapter.notifyItemChanged(position, payload);
        }
    }


    @Override
    public void notifyItemChangedPld(int position) {
        if (null != mMemberListAdapter) {
            mMemberListAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void notifyItemInsertedPld(int position) {
        if (null != mMemberListAdapter) {
            //判断人数
            TxLogUtils.i(TAG, "notifyItemInserted" + mMemberEntityList.size());
            mMemberListAdapter.notifyItemInserted(position);
            if ( mMemberEntityList.size()>3){
                rl_video_view_layout.setClickable(false);
            }

        }
    }


    @Override
    public void notifyItemRemovedPld(int position) {
        TxLogUtils.i(TAG, "notifyItemRemoved" + position);
        if (null != mMemberListAdapter) {
            mMemberListAdapter.notifyItemRemoved(position);
            if ( mMemberEntityList.size()<4){
                rl_video_view_layout.setClickable(true);
            }

        }
    }



    public interface IBusListener {
        void onItemVisible(int fromItem, int toItem);

        void onRvItemClick(int position);
    }

    public IBusListener mIBusListener;

    public void setPageListener(IBusListener iBusListener) {
        this.mIBusListener = iBusListener;
    }

    private MeetingPageLayoutManager pageLayoutManager;
    private MemberListAdapter mMemberListAdapter;

    private ArrayList<MemberEntity> mMemberEntityList;
    private HashMap<String, MemberEntity> mStringMemberEntityMap;

    public void setData(HashMap<String, MemberEntity> stringMemberEntityMap) {
        this.mStringMemberEntityMap = stringMemberEntityMap;
    }

    private TRTCRemoteUserManager mTRTCRemoteUserManager;

    public void setTRTCRemoteUserManager(TRTCRemoteUserManager mTRTCRemoteUserManager) {
        this.mTRTCRemoteUserManager = mTRTCRemoteUserManager;
    }

    private MeetingPageLayoutManager1 meetingPageLayoutManager1;
    private MeetingPageLayoutManager1 verticalmeetingpagelayoutmanager1;

    public void initAdapter(ArrayList<MemberEntity> memberEntityList) {
        this.mMemberEntityList = memberEntityList;
        pageLayoutManager = new MeetingPageLayoutManager(2, 2, MeetingPageLayoutManager.VERTICAL);
        pageLayoutManager.setPageListener(new MeetingPageLayoutManager.PageListener() {

            @Override
            public void onPageSizeChanged(int pageSize) {

            }

            @Override
            public void onPageSelect(int pageIndex) {

            }

            @Override
            public void onItemVisible(int fromItem, int toItem) {
                if (null != mIBusListener) {
                    mIBusListener.onItemVisible(fromItem, toItem);
                }
            }
        });


        mMemberListAdapter =
                new MemberListAdapter(
                        this.getContext(),
                        mMemberEntityList,
                        new MemberListAdapter.ListCallback() {

                            @Override
                            public void onItemClick(int position) {
                                if (null != mIBusListener) {
                                    mIBusListener.onRvItemClick(position);
                                }
                            }

                            @Override
                            public void onItemDoubleClick(int position) {

                            }
                        });

        trtc_video_view_layout.setHasFixedSize(true);

        trtc_video_view_layout.setLayoutManager(pageLayoutManager);
        trtc_video_view_layout.setAdapter(mMemberListAdapter);
        mMemberListAdapter.setData(mMemberEntityList);

        meetingPageLayoutManager1 = new MeetingPageLayoutManager1(1, 4, MeetingPageLayoutManager1.HORIZONTAL);
        meetingPageLayoutManager1.setPageListener(new MeetingPageLayoutManager1.PageListener() {
            @Override
            public void onPageSizeChanged(int pageSize) {

            }

            @Override
            public void onPageSelect(int pageIndex) {

            }

            @Override
            public void onItemVisible(int fromItem, int toItem) {
                if (null != mIBusListener) {
                    mIBusListener.onItemVisible(fromItem, toItem);
                }
            }
        });
        verticalmeetingpagelayoutmanager1 = new MeetingPageLayoutManager1(3, 1, MeetingPageLayoutManager1.VERTICAL);
        verticalmeetingpagelayoutmanager1.setPageListener(new MeetingPageLayoutManager1.PageListener() {
            @Override
            public void onPageSizeChanged(int pageSize) {

            }

            @Override
            public void onPageSelect(int pageIndex) {

            }

            @Override
            public void onItemVisible(int fromItem, int toItem) {
                if (null != mIBusListener) {
                    mIBusListener.onItemVisible(fromItem, toItem);
                }
            }
        });
        changeHasVideoUi();
    }


    public MemberListAdapter getMemberListAdapter() {
        return this.mMemberListAdapter;
    }


    void checkBigVideoToList() {
        if (null != bigMeetingEntity) {
            bigMeetingEntity.getMeetingVideoView().detach();
            addMemberEntity(0,bigMeetingEntity);
            mMemberListAdapter.notifyItemInserted(0);
        }
    }

    MemberEntity bigMeetingEntity;

    public MemberEntity getBigMeetingEntity() {
        return bigMeetingEntity;
    }

    public void setBigMeetingEntity(MemberEntity bigMeetingEntity) {
        this.bigMeetingEntity = bigMeetingEntity;
    }

    //---------以下方法用来处理大屏的业务ui展示




    int removeMemberEntity(String userId) {
        MemberEntity entity = mStringMemberEntityMap.remove(userId);
        if (entity != null) {
            int i = mMemberEntityList.indexOf(entity);
            if (i >= 0) {
                mMemberEntityList.remove(entity);
            } else {
                return -1;
            }
            return i;
        }
        return -1;
    }

    void addMemberEntity(int positon ,  MemberEntity entity) {
        mMemberEntityList.add(positon, entity);
        mStringMemberEntityMap.put(entity.getUserId(), entity);
    }


    public void showBg(String bgUrl){
        if (bgUrl.isEmpty()){
            //   android:src="@drawable/tx_bg"
            iv_self.setImageResource(R.drawable.tx_bg);
        }else{
            TxGlide.with(TXSdk.getInstance().application).load(bgUrl)
                    .into(iv_self);
        }
        iv_self.setVisibility(VISIBLE);
    }
    private onVideoLayout mOnVideoLayout;
    public void setOnClickListener(onVideoLayout monVideoLayout){
        this.mOnVideoLayout = monVideoLayout;
    }

   public interface onVideoLayout{
        void onClick();
    }
}
