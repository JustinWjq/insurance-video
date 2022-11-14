package com.txt.video.ui.video.plview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
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

import com.tencent.trtc.TRTCCloudDef;
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

/**
 * 用来展示横屏和竖屏的视频布局切换
 */
public class V2VideoLayout extends RelativeLayout implements View.OnClickListener, ITxPlDisplayView {
    public WeakReference<IVideoLayoutListener> mWefListener;
    private OnClickListener mClickListener;
    private GestureDetector mSimpleOnGestureListener;
    private boolean mMoveable;
    private boolean mEnableFill = false;
    private boolean mEnableAudio = true;
    private boolean mEnableVideo = true;

    private static final String TAG = "V2VideoLayout";

    public V2VideoLayout(Context context) {
        this(context, null);
    }

    public V2VideoLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFuncLayout();
        initGestureListener();
    }

    public V2VideoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFuncLayout();
        initGestureListener();
    }

    ViewGroup mVgFuc;
    RecyclerView trtc_video_view_layout;
    FrameLayout bigscreen;
    LinearLayout trtc_fl_no_video;
    LinearLayout ll_videolayouttwo;
    RelativeLayout rl_left;
    TextView bigscreen_trtc_tv_content;
    TextView bigscreen_trtc_tv_content1;
    ImageView trtc_icon_host;
    ImageView trtc_icon_host1;
    ImageView bigscreen_trtc_pb_audio;
    ImageView bigscreen_trtc_pb_audio1;
    private CircleImageView mUserHeadImg;
    private TextView mIvVideClose;
    private void initFuncLayout() {

        mVgFuc = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.tx_layout_trtc_videolayoutvtwo, this, true);
        trtc_video_view_layout = mVgFuc.findViewById(R.id.trtc_video_view_layout1);
        bigscreen = mVgFuc.findViewById(R.id.bigscreen);
        ll_videolayouttwo = mVgFuc.findViewById(R.id.ll_videolayouttwo);
        rl_left = mVgFuc.findViewById(R.id.rl_left);

        trtc_fl_no_video = mVgFuc.findViewById(R.id.trtc_fl_no_video);
        bigscreen_trtc_tv_content = (TextView) mVgFuc.findViewById(R.id.trtc_tv_content);
        bigscreen_trtc_tv_content1 = (TextView) mVgFuc.findViewById(R.id.trtc_tv_content1);
        trtc_icon_host = (ImageView) mVgFuc.findViewById(R.id.trtc_icon_host);
        trtc_icon_host1 = (ImageView) mVgFuc.findViewById(R.id.trtc_icon_host1);
        mUserHeadImg = (CircleImageView) mVgFuc.findViewById(R.id.iv_video_head);
        mIvVideClose = (TextView) mVgFuc.findViewById(R.id.iv_video_close);
        bigscreen_trtc_pb_audio = (ImageView) mVgFuc.findViewById(R.id.trtc_pb_audio);
        bigscreen_trtc_pb_audio1 = (ImageView) mVgFuc.findViewById(R.id.trtc_pb_audio1);

        ViewGroup.LayoutParams layoutParams = trtc_video_view_layout.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        trtc_video_view_layout.setLayoutParams(layoutParams);
    }


    private void initGestureListener() {
        mSimpleOnGestureListener = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (mClickListener != null) {
                    mClickListener.onClick(V2VideoLayout.this);
                }
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (!mMoveable) return false;
                ViewGroup.LayoutParams params = V2VideoLayout.this.getLayoutParams();
                // 当 TRTCVideoView 的父容器是 RelativeLayout 的时候，可以实现拖动
                if (params instanceof LayoutParams) {
                    LayoutParams layoutParams = (LayoutParams) V2VideoLayout.this.getLayoutParams();
                    int newX = (int) (layoutParams.leftMargin + (e2.getX() - e1.getX()));
                    int newY = (int) (layoutParams.topMargin + (e2.getY() - e1.getY()));

                    layoutParams.leftMargin = newX;
                    layoutParams.topMargin = newY;

                    V2VideoLayout.this.setLayoutParams(layoutParams);
                }
                return true;
            }
        });
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mSimpleOnGestureListener.onTouchEvent(event);
            }
        });
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mClickListener = l;
    }


    @Override
    public void onClick(View v) {
        IVideoLayoutListener listener = mWefListener != null ? mWefListener.get() : null;
        if (listener == null) return;
        int id = v.getId();
        if (id == R.id.trtc_btn_fill) {
            mEnableFill = !mEnableFill;
            if (mEnableFill) {
                v.setBackgroundResource(R.drawable.tx_fill_scale);
            } else {
                v.setBackgroundResource(R.drawable.tx_fill_adjust);
            }
            listener.onClickFill(this, mEnableFill);
        } else if (id == R.id.trtc_btn_mute_audio) {
            mEnableAudio = !mEnableAudio;
            if (mEnableAudio) {
                v.setBackgroundResource(R.drawable.tx_remote_audio_enable);
            } else {
                v.setBackgroundResource(R.drawable.tx_remote_audio_disable);
            }
            listener.onClickMuteAudio(this, !mEnableAudio);
        } else if (id == R.id.trtc_btn_mute_video) {
            mEnableVideo = !mEnableVideo;
            if (mEnableVideo) {
                v.setBackgroundResource(R.drawable.tx_remote_video_enable);
            } else {
                v.setBackgroundResource(R.drawable.tx_remote_video_disable);
            }
            listener.onClickMuteVideo(this, !mEnableVideo);
        } else if (id == R.id.mute_in_speaker) {
            listener.onClickMuteInSpeakerAudio(this, ((ToggleButton) v).isChecked());
        }
    }

    public void setIVideoLayoutListener(IVideoLayoutListener listener) {
        if (listener == null) {
            mWefListener = null;
        } else {
            mWefListener = new WeakReference<IVideoLayoutListener>(listener);
        }
    }

    private boolean isLand = true;

    @Override
    public void switchScreen(boolean checkToPro) {
        TxLogUtils.i(TAG, "switchScreen");
        TxLogUtils.i(TAG, "mMemberEntityList.size()" + mMemberEntityList.size());
        TxLogUtils.i(TAG, "bigMeetingEntity.isNull" + (null == bigMeetingEntity));
        TxLogUtils.i(TAG, "mCurrentUitype" + mCurrentUitype.name());
        //需要区分下当前的状态
        switch (mCurrentUitype) {
            case PRO_BIG_RV_NOBIGVIDEO://如果竖屏幕没有大画面，那么有可能为一个人，也有可能为两个人
                checkNoVideoLanUi();
                mCurrentUitype = UITYPE.LAND_BIG_RV_NOBIGVIDEO;
                break;
            case PRO_BIG_RV_HASBIGVIDEO://如果竖屏幕有大画面，那么有可能为两个人，也有可能为两个人以上
                //判断人数
                if (mMemberEntityList.size() >= 3) {
                    changeHasVideoUi();
                    checkSmallVideoToBigVideo(mMemberEntityList.get(0));
                    mCurrentUitype = UITYPE.LAND_BIG_RV_HASBIGVIDEO;
                } else {
                    checkNoVideoLanUi();
                    addMemberEntity(0,bigMeetingEntity);
                    mMemberListAdapter.notifyItemInserted(0);
                    mCurrentUitype = UITYPE.LAND_BIG_RV_NOBIGVIDEO;
                }
              break;
            case LAND_BIG_RV_NOBIGVIDEO://四个人（包含四个人）情况以下
                //切到竖屏的时候，需要判断两个人的情况有无摄像头
                //切换竖屏幕
                //判断是否有视频开启
                boolean isMute = true;
                for (MemberEntity memberEntity : mMemberEntityList) {
                    if (!memberEntity.isMuteVideo()) {
                        isMute = false;
                    }
                }
                if (!isMute || mMemberEntityList.size() > 2) {//当前有视频开启
                    //需要展示一个大屏幕。一个小屏幕
                    TxLogUtils.i(TAG, "switchScreen--isLand---isMute" + isMute);
                    mCurrentUitype = UITYPE.PRO_BIG_RV_HASBIGVIDEO;
                    changProHasBigVideo();
                    checkSmallVideoToBigVideo(mMemberEntityList.get(0));
                } else {
                    TxLogUtils.i(TAG, "switchScreen--isLand---isMute" + isMute);
                    mCurrentUitype = UITYPE.PRO_BIG_RV_NOBIGVIDEO;

                }
                break;
            case LAND_BIG_RV_HASBIGVIDEO://五个人以上情况
                //切换竖屏幕
                //需要展示一个大屏幕。一个小屏幕
                mCurrentUitype = UITYPE.PRO_BIG_RV_HASBIGVIDEO;
                changProHasBigVideo();

                break;

            default:

        }

        isLand = !isLand;

    }

    /**
     * 切换成竖屏的ui布局
     */
    private void changProHasBigVideo() {
        TxLogUtils.i(TAG, "changProHasBigVideo");
        ll_videolayouttwo.setOrientation(LinearLayout.VERTICAL);
        ViewGroup.LayoutParams rl_leftlayoutParams = rl_left.getLayoutParams();
        rl_leftlayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        rl_leftlayoutParams.height = DisplayUtils.INSTANCE.getScreenHeight(getContext()) / 2;
        rl_left.setLayoutParams(rl_leftlayoutParams);
        rl_left.setVisibility(VISIBLE);


        ViewGroup.LayoutParams layoutParams = trtc_video_view_layout.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = DisplayUtils.INSTANCE.getScreenHeight(getContext()) / 4;
        trtc_video_view_layout.setLayoutManager(meetingPageLayoutManager1);
        postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 200);

    }

    private void changeProNoBigVideo() {
        ll_videolayouttwo.setOrientation(LinearLayout.VERTICAL);
        rl_left.setVisibility(GONE);

        ViewGroup.LayoutParams layoutParams = trtc_video_view_layout.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        trtc_video_view_layout.setLayoutManager(pageLayoutManager);

    }


    //横屏 5个人切成4个人
    private void checkNoVideoLanUi() {
        ll_videolayouttwo.setOrientation(LinearLayout.HORIZONTAL);
        trtc_video_view_layout.setLayoutManager(pageLayoutManager);
        ViewGroup.LayoutParams layoutParams = trtc_video_view_layout.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        rl_left.setVisibility(GONE);
    }

    /**
     * 切换成横屏的ui布局
     */
    private void changeHasVideoUi() {
        ll_videolayouttwo.setOrientation(LinearLayout.HORIZONTAL);
        trtc_video_view_layout.setLayoutManager(verticalmeetingpagelayoutmanager1);
        ViewGroup.LayoutParams layoutParams = trtc_video_view_layout.getLayoutParams();
        layoutParams.width = DisplayUtils.INSTANCE.getScreenWidth(getContext()) / 10 * 3;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        ViewGroup.LayoutParams rl_leftlayoutParams = rl_left.getLayoutParams();
        rl_leftlayoutParams.width = DisplayUtils.INSTANCE.getScreenWidth(getContext()) / 10 * 7;
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

    public void notifyItemChangedPld(@NotNull String userId, int position, @NotNull String payload) {
        if (payload ==MemberListAdapter.VOLUME_SHOW){
            changeBigScreenViewVoice(0);
            return;
        }
        TxLogUtils.i(TAG,"notifyItemChanged"+"island" +isLand);
        TxLogUtils.i(TAG,"notifyItemChanged"+"mCurrentUitype" +mCurrentUitype.name());
        TxLogUtils.i(TAG,"notifyItemChanged"+"mMemberEntityList.size()" +mMemberEntityList.size());
        if (isLand) {
            switch (mCurrentUitype) {
                case LAND_BIG_RV_NOBIGVIDEO:
                    if (null != mMemberListAdapter) {
                        //如果大屏幕
                        mMemberListAdapter.notifyItemChanged(position, payload);
                    }
                    break;
                case LAND_BIG_RV_HASBIGVIDEO:
                    if (bigMeetingEntity != null
                            && userId == bigMeetingEntity.getUserId()) {
                        muteBigVideo();
                    } else {
                        if (null != mMemberListAdapter) {
                            //如果大屏幕
                            mMemberListAdapter.notifyItemChanged(position, payload);
                        }
                    }
                    break;
                default:
            }

        } else {
            //两个人情况需要特殊处理
            switch (mCurrentUitype) {
                case PRO_BIG_RV_HASBIGVIDEO:
                    //这还有种关视频的情况。需要切成两个小屏的情况
                    if (mMemberEntityList.size() == 2||mMemberEntityList.size() == 1) {
                        //切换竖屏幕
                        //判断是否有视频开启
                        boolean isMute = true;
                        for (MemberEntity memberEntity : mMemberEntityList) {
                            if (!memberEntity.isMuteVideo()) {
                                isMute = false;
                            }
                        }
                        if (bigMeetingEntity != null && !bigMeetingEntity.isMuteVideo()) {
                            isMute = false;
                        }
                        if (isMute) {
                            TxLogUtils.i(TAG, "switchScreen--isLand---isMute" + isMute);
                            mCurrentUitype = UITYPE.PRO_BIG_RV_NOBIGVIDEO;
                            changeProNoBigVideo();
                            checkBigVideoToList();
                        } else {
                            if (bigMeetingEntity != null
                                    && userId == bigMeetingEntity.getUserId()) {
                                muteBigVideo();
                            } else {
                                if (null != mMemberListAdapter) {
                                    //如果大屏幕
                                    mMemberListAdapter.notifyItemChanged(position, payload);
                                }
                            }
                        }
                    } else {
                        if (bigMeetingEntity != null
                                && userId == bigMeetingEntity.getUserId()) {
                            muteBigVideo();
                        } else {
                            if (null != mMemberListAdapter) {
                                //如果大屏幕
                                mMemberListAdapter.notifyItemChanged(position, payload);
                            }
                        }
                    }

                    break;
                case PRO_BIG_RV_NOBIGVIDEO:
                    if (mMemberEntityList.size() == 2) {
                        //切换竖屏幕
                        //判断是否有视频开启
                        boolean isMute = true;
                        for (MemberEntity memberEntity : mMemberEntityList) {
                            if (!memberEntity.isMuteVideo()) {
                                isMute = false;
                            }
                        }
                        if (bigMeetingEntity != null && !bigMeetingEntity.isMuteVideo()) {
                            isMute = false;
                        }
                        if (!isMute && mCurrentUitype != UITYPE.PRO_BIG_RV_HASBIGVIDEO) {//当前有视频开启
                            //需要展示一个大屏幕。一个小屏幕
                            TxLogUtils.i(TAG, "switchScreen--isLand---isMute" + isMute);
                            mCurrentUitype = UITYPE.PRO_BIG_RV_HASBIGVIDEO;
                            changProHasBigVideo();
                            checkSmallVideoToBigVideo(mMemberEntityList.get(0));
                        } else {
                            if (bigMeetingEntity != null
                                    && userId == bigMeetingEntity.getUserId()) {
                                muteBigVideo();
                            } else {
                                if (null != mMemberListAdapter) {
                                    //如果大屏幕
                                    mMemberListAdapter.notifyItemChanged(position, payload);
                                }
                            }

                        }
                    } else {
                        if (bigMeetingEntity != null
                                && userId == bigMeetingEntity.getUserId()) {
                            muteBigVideo();
                        } else {
                            if (null != mMemberListAdapter) {
                                //如果大屏幕
                                mMemberListAdapter.notifyItemChanged(position, payload);
                            }
                        }
                    }
                    break;
                default:
            }
        }


    }

    enum UITYPE {
        LAND_BIG_RV_NOBIGVIDEO,//
        LAND_BIG_RV_HASBIGVIDEO,//
        PRO_BIG_RV_NOBIGVIDEO,//
        PRO_BIG_RV_HASBIGVIDEO//
    }

    private UITYPE mCurrentUitype = UITYPE.LAND_BIG_RV_NOBIGVIDEO;

    @Override
    public void notifyItemInsertedPld(int position) {
        if (null != mMemberListAdapter) {
            //判断人数
            TxLogUtils.i(TAG, "notifyItemInserted" + mMemberEntityList.size());
            TxLogUtils.i(TAG, "notifyItemInserted---isLand" + isLand + "" + mCurrentUitype.name());
            if (isLand) {
                if (mMemberEntityList.size() == 5 && mCurrentUitype != UITYPE.LAND_BIG_RV_HASBIGVIDEO) {
                    //显示大屏幕和recyclerview
                    mCurrentUitype = UITYPE.LAND_BIG_RV_HASBIGVIDEO;
                    changeHasVideoUi();
                    checkSmallVideoToBigVideo(mMemberEntityList.get(0));
                }
                if (mCurrentUitype == UITYPE.LAND_BIG_RV_HASBIGVIDEO) {
                    mCurrentUitype = UITYPE.LAND_BIG_RV_HASBIGVIDEO;
                    mMemberListAdapter.notifyItemInserted(position);
                } else {
                    mCurrentUitype = UITYPE.LAND_BIG_RV_NOBIGVIDEO;
                    mMemberListAdapter.notifyItemInserted(position);
                }

            } else {
                //这里存在第二个人进来的时候，开着视频就需要切换视图了
                if (mCurrentUitype == UITYPE.PRO_BIG_RV_HASBIGVIDEO) {
                    mMemberListAdapter.notifyItemInserted(position);
                    mCurrentUitype = UITYPE.PRO_BIG_RV_HASBIGVIDEO;
                } else {
                    if (mMemberEntityList.size() == 2) {
                        boolean isMute = true;
                        for (MemberEntity memberEntity : mMemberEntityList) {
                            if (!memberEntity.isMuteVideo()) {
                                isMute = false;
                            }
                        }
                        if (bigMeetingEntity != null && !bigMeetingEntity.isMuteVideo()) {
                            isMute = false;
                        }
                        if (!isMute && mCurrentUitype != UITYPE.PRO_BIG_RV_HASBIGVIDEO) {//当前有视频开启
                            //需要展示一个大屏幕。一个小屏幕
                            TxLogUtils.i(TAG, "switchScreen--isLand---isMute" + isMute);
                            mCurrentUitype = UITYPE.PRO_BIG_RV_HASBIGVIDEO;
                            changProHasBigVideo();
                            rl_left.setVisibility(VISIBLE);
                            checkSmallVideoToBigVideo(mMemberEntityList.get(0));
                        } else {
                            mMemberListAdapter.notifyItemInserted(position);
                            mCurrentUitype = UITYPE.PRO_BIG_RV_NOBIGVIDEO;
                        }

                    }
                    if (mMemberEntityList.size() == 3 && mCurrentUitype == UITYPE.PRO_BIG_RV_NOBIGVIDEO) {
                        //需要展示一个大屏幕。一个小屏幕
                        mCurrentUitype = UITYPE.PRO_BIG_RV_HASBIGVIDEO;
                        changProHasBigVideo();
                        rl_left.setVisibility(VISIBLE);
                        checkSmallVideoToBigVideo(mMemberEntityList.get(0));
                    } else {
                        mMemberListAdapter.notifyItemInserted(position);
                        mCurrentUitype = UITYPE.PRO_BIG_RV_NOBIGVIDEO;
                    }

                }


            }

        }
    }


    @Override
    public void notifyItemRemovedPld(int position) {
        TxLogUtils.i(TAG, "notifyItemRemoved" + position);
        if (null != mMemberListAdapter) {
            if (isLand) {
                //判断人数
                if (mMemberEntityList.size() == 3 && mCurrentUitype == UITYPE.LAND_BIG_RV_HASBIGVIDEO) {
                    mCurrentUitype = UITYPE.LAND_BIG_RV_NOBIGVIDEO;
                    //切换成四个人的LAND模式
                    //todo
                    checkBigVideoToList();
                    checkNoVideoLanUi();
                } else {
                    mMemberListAdapter.notifyItemRemoved(position);
                }
            } else {
                //todo
                if (mCurrentUitype == UITYPE.PRO_BIG_RV_HASBIGVIDEO) {
                    //判断 两个人和三个人
                    if (mMemberEntityList.size()==0){
                        checkBigVideoToList();
                        changeProNoBigVideo();
                    }else{

                    }

                }else{
                    mMemberListAdapter.notifyItemRemoved(position);
                }

            }

        }
    }

    public interface IVideoLayoutListener {
        void onClickFill(V2VideoLayout view, boolean enableFill);

        void onClickMuteAudio(V2VideoLayout view, boolean isMute);

        void onClickMuteVideo(V2VideoLayout view, boolean isMute);

        void onClickMuteInSpeakerAudio(V2VideoLayout view, boolean isMute);
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
    //用户第一个视频切换成大屏幕
    void checkSmallVideoToBigVideo(MemberEntity memberEntity) {

        if (memberEntity != null) {
            bigMeetingEntity = memberEntity;
            MeetingVideoView meetingVideoView = memberEntity.getMeetingVideoView();
            TxLogUtils.i(TAG, "bigMeetingEntity.isMuteVideo()" + bigMeetingEntity.isMuteVideo());
            meetingVideoView.detach();
            meetingVideoView.setWaitBindGroup(bigscreen);
            meetingVideoView.refreshParent();
            changeBigVideo(bigMeetingEntity);
            bigscreen.setVisibility(View.VISIBLE);
//            if (null != mTRTCRemoteUserManager) {
//                mTRTCRemoteUserManager.setRemoteFillMode(
//                        bigMeetingEntity.getUserId(),
//                        TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG,
//                        false
//                );
//            }
//            val index = .removeMemberEntity(userId!!)
//            if (index >= 0) {
//            }
            removeMemberEntity(memberEntity.getUserId());
            mMemberListAdapter.notifyItemRemoved(0);


        }

    }

    void muteBigVideo() {
        if (!bigMeetingEntity.isMuteVideo()) {
            trtc_fl_no_video.setVisibility(View.GONE);
            bigscreen.setVisibility(View.VISIBLE);
        } else {
            trtc_fl_no_video.setVisibility(View.VISIBLE);
            bigscreen.setVisibility(View.GONE);
        }
    }


    //---------以下方法用来处理大屏的业务ui展示
    void changeBigVideo(MemberEntity bigMeetingEntity) {
        muteBigVideo();
        changeBigScreenViewName(
                bigMeetingEntity.getUserName(),
                bigMeetingEntity.getUserRole(),
                bigMeetingEntity.getUserRoleIconPath()
        );
        changeBigScreenViewVoice(50);
    }

    void changeBigScreenViewName(String text, String userRole, String userRoleIconPath) {
        String userName = text ;
        if (text.length()>2) {
            userName = userName.substring(userName.length()-2, userName.length());
        }
        mIvVideClose.setText(userName);
        mUserHeadImg.setCircleBackgroundColor(ContextCompat.getColor(TXSdk.getInstance().application,R.color.tx_color_e6b980));

        bigscreen_trtc_tv_content.setText(text);
        bigscreen_trtc_tv_content1.setText(text);
        int visible;
        if (userRole == "owner" || userRole == "assistant") {
            TxGlide.with(TXSdk.getInstance().application).load(userRoleIconPath)
                    .into(trtc_icon_host);
            TxGlide.with(TXSdk.getInstance().application).load(userRoleIconPath)
                    .into(trtc_icon_host1);
            visible = View.VISIBLE;
        } else {
            visible = View.GONE;
        }
        trtc_icon_host.setVisibility(visible);
        trtc_icon_host1.setVisibility(visible);

        changeBigScreenViewVoice(10);
    }


    public void changeBigScreenViewVoice(int progress) {
        if (null == bigMeetingEntity) {
            return;
        }
        if (!bigMeetingEntity.isShowAudioEvaluation()) {
            bigscreen_trtc_pb_audio.setImageResource(R.drawable.tx_icon_volume_small_mute);
            bigscreen_trtc_pb_audio1.setImageResource(R.drawable.tx_icon_volume_small_mute);
            return;
        }
        if (bigscreen_trtc_pb_audio != null) {
            //1-19
            //20-39
            //40-59
            //60-79
            //80-100
            if (progress == -1) {
                bigscreen_trtc_pb_audio.setImageResource(R.drawable.tx_icon_volume_small_mute);
                bigscreen_trtc_pb_audio1.setImageResource(R.drawable.tx_icon_volume_small_mute);
            } else if (progress == 0) {
                bigscreen_trtc_pb_audio.setImageResource(R.drawable.tx_icon_volume_small_1);
                bigscreen_trtc_pb_audio1.setImageResource(R.drawable.tx_icon_volume_small_1);
            } else if (progress >= 1 && progress <= 19) {
                bigscreen_trtc_pb_audio.setImageResource(R.drawable.tx_icon_volume_small_1);
                bigscreen_trtc_pb_audio1.setImageResource(R.drawable.tx_icon_volume_small_1);
            } else if (progress >= 20 && progress <= 39) {
                bigscreen_trtc_pb_audio.setImageResource(R.drawable.tx_icon_volume_small_2);
                bigscreen_trtc_pb_audio1.setImageResource(R.drawable.tx_icon_volume_small_2);
            } else if (progress >= 40 && progress <= 59) {
                bigscreen_trtc_pb_audio.setImageResource(R.drawable.tx_icon_volume_small_3);
                bigscreen_trtc_pb_audio1.setImageResource(R.drawable.tx_icon_volume_small_3);
            } else if (progress >= 60 && progress <= 79) {
                bigscreen_trtc_pb_audio.setImageResource(R.drawable.tx_icon_volume_small_5);
                bigscreen_trtc_pb_audio1.setImageResource(R.drawable.tx_icon_volume_small_5);
            } else if (progress >= 80 && progress <= 100) {
                bigscreen_trtc_pb_audio.setImageResource(R.drawable.tx_icon_volume_small_6);
                bigscreen_trtc_pb_audio1.setImageResource(R.drawable.tx_icon_volume_small_6);
            }


        }
    }

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
}
