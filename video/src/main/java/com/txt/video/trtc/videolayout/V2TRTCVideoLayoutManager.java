package com.txt.video.trtc.videolayout;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.trtc.TRTCCloudDef;
import com.txt.video.net.utils.TxLogUtils;
import com.txt.video.trtc.videolayout.list.MemberEntity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Module:   TRTCVideoViewLayout
 * <p>
 * Function: {@link TXCloudVideoView} 的管理类
 * <p>
 * 1.在多人通话中，您的布局可能会比较复杂，Demo 也是如此，因此需要统一的管理类进行管理，这样子有利于写出高可维护的代码
 * <p>
 * 2.Demo 中提供堆叠布局、宫格布局两种展示方式；若您的项目也有相关的 UI 交互，您可以参考实现代码，能够快速集成。
 * <p>
 * 3.堆叠布局：{@link V2TRTCVideoLayoutManager#makeFloatLayout(boolean)} 思路是初始化一系列的 x、y、padding、margin 组合 LayoutParams 直接对 View 进行定位
 * <p>
 * 4.宫格布局：{@link V2TRTCVideoLayoutManager#makeGirdLayout(boolean)} 思路与堆叠布局一致，也是初始化一些列的 LayoutParams 直接对 View 进行定位
 * <p>
 * 5.如何实现管理：
 * A. 使用{@link V2MemberEntity} 实体类，保存 {@link V2TRTCVideoLayout} 的分配信息，能够与对应的用户绑定起来，方便管理与更新UI
 * B. {@link V2TRTCVideoLayout } 专注实现业务 UI 相关的，控制逻辑放在此类中
 * <p>
 * 6.布局切换，见 {@link V2TRTCVideoLayoutManager#switchMode()}
 * <p>
 * 7.堆叠布局与宫格布局参数，见{@link Utils} 工具类
 */
public class V2TRTCVideoLayoutManager extends RelativeLayout {
    public static final int MODE_FLOAT = 1;  // 前后堆叠模式
    public static final int MODE_GRID = 2;  // 九宫格模式
    public static final int MAX_USER = 4;
    private final static String TAG = V2TRTCVideoLayoutManager.class.getSimpleName();
    public WeakReference<IVideoLayoutManagerListener> mWefListener;
    private ArrayList<MemberEntity> mLayoutEntityList = new ArrayList<>();
    private ArrayList<LayoutParams> mFloatParamList;
    private ArrayList<LayoutParams> mGrid1ParamList;
    private ArrayList<LayoutParams> mGrid2ParamList;
    private ArrayList<LayoutParams> mGrid3ParamList;
    private int mCount = 0;
    private int mMode;
    private String mSelfUserId;

    /**
     * ===============================View相关===============================
     */
    public V2TRTCVideoLayoutManager(Context context) {
        super(context);
//        initView(context);
    }


    public V2TRTCVideoLayoutManager(Context context, AttributeSet attrs) {
        super(context, attrs);
//        initView(context);
    }


    public V2TRTCVideoLayoutManager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        initView(context);
    }

    private void initView(Context context) {
        Log.i(TAG, "initView: ");

        mLayoutEntityList = new ArrayList<MemberEntity>();
        // 初始化多个 View，以备用
        for (int i = 0; i < MAX_USER; i++) {
            V2TRTCVideoLayout videoLayout = new V2TRTCVideoLayout(context);
            videoLayout.setVisibility(View.GONE);
            videoLayout.setBackgroundColor(Color.BLACK);
//            videoLayout.setMoveable(false);
//            videoLayout.setIVideoLayoutListener(this);
//            // 这里不展示其底部的控制菜单
//            videoLayout.setBottomControllerVisibility(View.GONE);
            MemberEntity entity = new MemberEntity();
            entity.setLayout(videoLayout);
            entity.setIndex(i);
            mLayoutEntityList.add(entity);
        }
        // 默认为堆叠模式
        mMode = MODE_GRID;
        this.post(new Runnable() {
            @Override
            public void run() {
//                makeFloatLayout(true);
                makeGirdLayout(true);
            }
        });
    }


    public void setVideoData(MemberEntity mMemberEntity) {

        mLayoutEntityList.add(mMemberEntity);
//        makeGirdLayout(true);
        this.post(new Runnable() {
            @Override
            public void run() {
//                makeFloatLayout(true);
//                makeGirdLayout(true);
            }
        });
    }

    /**
     * ===============================九宫格布局下点击按钮的事件回调===============================
     */
//
//    @Override
//    public void onClickFill(MeetingVideoView view, boolean enableFill) {
//        IVideoLayoutManagerListener listener = mWefListener != null ? mWefListener.get() : null;
//        if (listener != null) {
//            MemberEntity entity = findEntity(view);
//            listener.onClickItemFill(entity.getUserId(), entity.getStreamType(), enableFill);
//        }
//    }
//
//    @Override
//    public void onClickMuteAudio(V2TRTCVideoLayout view, boolean isMute) {
//        IVideoLayoutManagerListener listener = mWefListener != null ? mWefListener.get() : null;
//        if (listener != null) {
//            MemberEntity entity = findEntity(view);
//            listener.onClickItemMuteAudio(entity.getUserId(), isMute);
//        }
//    }
//
//    @Override
//    public void onClickMuteVideo(V2TRTCVideoLayout view, boolean isMute) {
//        IVideoLayoutManagerListener listener = mWefListener != null ? mWefListener.get() : null;
//        if (listener != null) {
//            MemberEntity entity = findEntity(view);
//            listener.onClickItemMuteVideo(entity.getUserId(), entity.getStreamType(), isMute);
//        }
//    }
//
//    @Override
//    public void onClickMuteInSpeakerAudio(V2TRTCVideoLayout view, boolean isMute) {
//        IVideoLayoutManagerListener listener = mWefListener != null ? mWefListener.get() : null;
//        if (listener != null) {
//            MemberEntity entity = findEntity(view);
//            listener.onClickItemMuteInSpeakerAudio(entity.getUserId(), isMute);
//        }
//    }

    /**
     * ===============================Manager对外相关方法===============================
     */
    public void setIVideoLayoutListener(IVideoLayoutManagerListener listener) {
        if (listener == null) {
            mWefListener = null;
        } else {
            mWefListener = new WeakReference<>(listener);
        }
    }

    public void setMySelfUserId(String userId) {
        mSelfUserId = userId;
    }

    /**
     * 宫格布局与悬浮布局切换
     *
     * @return
     */
    public int switchMode() {
        if (mMode == MODE_FLOAT) {
            mMode = MODE_GRID;
            makeGirdLayout(true);
        } else {
            mMode = MODE_FLOAT;
            makeFloatLayout(false);
        }
        return mMode;
    }

    /**
     * 根据 userId 和视频类型，找到已经分配的 View
     *
     * @param userId
     * @param streamType
     * @return
     */
    public TXCloudVideoView findCloudViewView(String userId, int streamType) {
        if (userId == null) return null;
        for (MemberEntity layoutEntity : mLayoutEntityList) {
            if (layoutEntity.getStreamType() == streamType && layoutEntity.getUserId().equals(userId)) {

                return layoutEntity.getLayout().getVideoView();
            }
        }
        return null;
    }

    /**
     * 根据 userId 和视频类型，找到已经分配的 业务布局
     *
     * @param userId
     * @param streamType
     * @return
     */
    public MemberEntity findCloudViewViewSetUserName(String userId, int streamType, String userName) {
        if (userId == null) return null;
        for (MemberEntity layoutEntity : mLayoutEntityList) {
            if (layoutEntity.getStreamType() == streamType && layoutEntity.getUserId().equals(userId)) {
                layoutEntity.getLayout().updateNoVideoLayout(userName, View.GONE);
                return layoutEntity;
            }
        }
        return null;
    }


    //找到指定的videoview
    public TXCloudVideoView findCloudViewView() {
        MemberEntity trtcLayoutEntity = mLayoutEntityList.get(1);
        return trtcLayoutEntity.getLayout().getVideoView();

    }

    public int fingCloudViewCount() {
        int count = 0;
        for (MemberEntity layoutEntity : mLayoutEntityList) {
            if (!layoutEntity.getUserId().isEmpty()) {
                count++;
            }
        }
        return count;
    }


    /**
     * 根据 userId 和 视频类型（大、小、辅路）画面分配对应的 view
     *
     * @param userId
     * @param streamType
     * @return
     */
    public TXCloudVideoView allocCloudVideoView(String userId, int streamType, String userName) {
        if (userId == null) return null;

        for (MemberEntity layoutEntity : mLayoutEntityList) {
            int i = mLayoutEntityList.indexOf(layoutEntity);
            TxLogUtils.i("mLayoutEntityList.indexOf" + i);
            layoutEntity.setUserId(userId);
            layoutEntity.setStreamType(streamType);
            layoutEntity.setUserName(userName);
            layoutEntity.getLayout().setVisibility(VISIBLE);
            mCount++;
            makeGirdLayout(true);
            layoutEntity.getLayout().updateNoVideoLayout(userName, View.VISIBLE);
            return layoutEntity.getLayout().getVideoView();


        }
        return null;
    }

    /**
     * 根据 userId 和 视频类型，回收对应的 view
     *
     * @param userId
     * @param streamType
     */
    public void recyclerCloudViewView(String userId, int streamType) {
        if (userId == null) return;
//        if (mMode == MODE_FLOAT) {
////            mCount ==1;
//
//            TRTCLayoutEntity entity = mLayoutEntityList.get(0);
//            // 当前离开的是处于0号位的人，那么需要将我换到这个位置
//            if (userId.equals(entity.userId) && entity.streamType == streamType) {
//                TRTCLayoutEntity myEntity = findEntity(mSelfUserId);
//                if (myEntity != null) {
//                    makeFullVideoView(myEntity.index);
//                }
//            }
//        } else {
//        }
        //todo
        // 如果第一个布局为空，并且当前布局存在两个，则把最后一个移到大屏幕上。

        for (MemberEntity entity : mLayoutEntityList) {
            if (entity.getStreamType() == streamType && userId.equals(entity.getUserId())) {
                mCount--;
                if (mMode == MODE_GRID) {
                    if (mCount == 4) {
                        makeGirdLayout(true);
                    }
                }
                entity.getLayout().setVisibility(GONE);
                entity.setUserId("");
                entity.setStreamType(-1);
                break;
            }
        }

        for (int i = 0; i < mLayoutEntityList.size(); i++) {
            MemberEntity entity = mLayoutEntityList.get(i);
            // 需要对 View 树的 zOrder 进行重排，否则在 RelativeLayout 下，存在遮挡情况
            bringChildToFront(entity.getLayout());
        }
    }

    /**
     * 隐藏所有音量的进度条
     */
    public void hideAllAudioVolumeProgressBar() {
        for (MemberEntity entity : mLayoutEntityList) {
            entity.getLayout().setAudioVolumeProgressBarVisibility(View.GONE);
        }
    }

    /**
     * 显示所有音量的进度条
     */
    public void showAllAudioVolumeProgressBar() {
        for (MemberEntity entity : mLayoutEntityList) {
            entity.getLayout().setAudioVolumeProgressBarVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置当前音量
     *
     * @param userId
     * @param audioVolume
     */
    public void updateAudioVolume(String userId, int audioVolume) {
        if (userId == null) return;
        for (MemberEntity entity : mLayoutEntityList) {
            if (entity.getLayout().getVisibility() == VISIBLE) {
                if (userId.equals(entity.getUserId())) {
                    entity.getLayout().setAudioVolumeProgress(audioVolume);
                }
            }
        }
    }

    /**
     * 更新网络质量
     *
     * @param userId
     * @param quality
     */
    public void updateNetworkQuality(String userId, int quality) {
        if (userId == null) return;
        for (MemberEntity entity : mLayoutEntityList) {
            if (entity.getLayout().getVisibility() == VISIBLE) {
                if (userId.equals(entity.getUserId())) {
                    entity.getLayout().updateNetworkQuality(quality);
                }
            }
        }
    }

    /**
     * 更新当前视频状态
     *
     * @param userId
     * @param bHasVideo
     */
    public void updateVideoStatus(String userId, boolean bHasVideo) {
        if (userId == null) return;
        for (MemberEntity entity : mLayoutEntityList) {
            if (entity.getLayout().getVisibility() == VISIBLE) {
                if (userId.equals(entity.getUserId()) && entity.getStreamType() == TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG) {
                    String content = userId;
                    if (userId.equals(mSelfUserId)) {
                        content += "(您自己)";
                    }
                    entity.getLayout().updateNoVideoLayout(content, bHasVideo ? GONE : VISIBLE);
                    break;
                }
            }
        }
    }

    private MemberEntity findEntity(V2TRTCVideoLayout layout) {
        for (MemberEntity entity : mLayoutEntityList) {
            if (entity.getLayout() == layout) return entity;
        }
        return null;
    }

    private MemberEntity findEntity(String userId) {
        for (MemberEntity entity : mLayoutEntityList) {
            if (entity.getUserId().equals(userId)) return entity;
        }
        return null;
    }

    /**
     * 切换到九宫格布局
     *
     * @param needUpdate 是否需要更新布局
     */
    private void makeGirdLayout(boolean needUpdate) {
        if (mGrid1ParamList == null || mGrid1ParamList.size() == 0 || mGrid2ParamList == null || mGrid2ParamList.size() == 0) {
            mGrid1ParamList = V2VideoUtils.initGridOneParam(getContext(), getWidth(), getHeight());
            mGrid2ParamList = V2VideoUtils.initGrid2Param(getContext(), getWidth(), getHeight());
            mGrid3ParamList = V2VideoUtils.initGrid3Param(getContext(), getWidth(), getHeight());
        }
        if (needUpdate) {
            ArrayList<LayoutParams> paramList;
            if (mCount == 1) {
                paramList = mGrid1ParamList;
            } else if (mCount == 2) {
                paramList = mGrid2ParamList;
            } else {
                paramList = mGrid2ParamList;
            }
            int layoutIndex = 1;
            for (int i = 0; i < mLayoutEntityList.size(); i++) {
                MemberEntity entity = mLayoutEntityList.get(i);
                entity.getLayout().setMoveable(false);
                entity.getLayout().setOnClickListener(null);
                // 我自己要放在布局的左上角
                if (entity.getUserId().equals(mSelfUserId)) {
                    entity.getLayout().setLayoutParams(paramList.get(0));
                } else {
                    entity.getLayout().setLayoutParams(paramList.get(i));
                }

                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (null != entity.getLayout().getParent()) {

                        } else {

                            addView(entity.getLayout());
                        }


                    }
                }, 200);

            }
        }
    }


    /**
     * ===============================九宫格布局相关===============================
     */

    /**
     * 切换到堆叠布局：
     * 1. 如果堆叠布局参数未初始化先进行初始化：大画面+左右各三个画面
     * 2. 修改布局参数
     *
     * @param needAddView
     */
    private void makeFloatLayout(boolean needAddView) {
        // 初始化堆叠布局的参数
        if (mFloatParamList == null || mFloatParamList.size() == 0) {
            mFloatParamList = V2VideoUtils.initFloatParamList(getContext(), getWidth(), getHeight());
        }

        // 根据堆叠布局参数，将每个view放到适当的位置
        for (int i = 0; i < mLayoutEntityList.size(); i++) {
            MemberEntity entity = mLayoutEntityList.get(i);
            LayoutParams layoutParams = mFloatParamList.get(i);
            entity.getLayout().setLayoutParams(layoutParams);
            if (i == 0) {
                entity.getLayout().setMoveable(false);
            } else {
                entity.getLayout().setMoveable(false);
            }
            addFloatViewClickListener(entity.getLayout());
            entity.getLayout().setBottomControllerVisibility(View.GONE);

            if (needAddView) {
                addView(entity.getLayout());
            }
        }
    }

    /**
     * ===============================堆叠布局相关===============================
     */

    /**
     * 对堆叠布局情况下的 View 添加监听器
     * <p>
     * 用于点击切换两个 View 的位置
     *
     * @param view
     */
    private void addFloatViewClickListener(final V2TRTCVideoLayout view) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (MemberEntity entity : mLayoutEntityList) {
                    if (entity.getLayout() == v) {
                        if (isAllToTop) {
                            TxLogUtils.i("当前是白板状态，点击小屏，不能切换");
                        } else {
                            if (fingCloudViewCount() == 1) {
                                TxLogUtils.i("当前只有一个用户，点击小屏，不能切换");
                            } else {
                                makeFullVideoView(entity.getIndex());
                            }

                        }

                        break;
                    }
                }
            }
        });
    }

    /**
     * 堆叠模式下，将 index 号的 view 换到 0 号位，全屏化渲染
     *
     * @param index
     */
    private void makeFullVideoView(int index) {// 1 -> 0
        if (index <= 0 || mLayoutEntityList.size() <= index) return;
        Log.i(TAG, "makeFullVideoView: from = " + index);
        MemberEntity indexEntity = mLayoutEntityList.get(index);
        ViewGroup.LayoutParams indexParams = indexEntity.getLayout().getLayoutParams();

        MemberEntity fullEntity = mLayoutEntityList.get(0);
        ViewGroup.LayoutParams fullVideoParams = fullEntity.getLayout().getLayoutParams();

        indexEntity.getLayout().setLayoutParams(fullVideoParams);
        indexEntity.setIndex(0);

        fullEntity.getLayout().setLayoutParams(indexParams);
        fullEntity.setIndex(index);

        indexEntity.getLayout().setMoveable(false);
        indexEntity.getLayout().setOnClickListener(null);

        fullEntity.getLayout().setMoveable(false);
        addFloatViewClickListener(fullEntity.getLayout());

        mLayoutEntityList.set(0, indexEntity); // 将 fromView 塞到 0 的位置
        mLayoutEntityList.set(index, fullEntity);

        for (int i = 0; i < mLayoutEntityList.size(); i++) {
            MemberEntity entity = mLayoutEntityList.get(i);
            // 需要对 View 树的 zOrder 进行重排，否则在 RelativeLayout 下，存在遮挡情况
            bringChildToFront(entity.getLayout());
        }
    }

    //当前大屏切换到小屏幕的最后一个
    //记录大屏幕切换上来的 索引
    private int bigScreen = 0;
    private boolean isAllToTop = false;

    public void switchVideoViewTolast(int index, boolean toLast) {// 1 -> 0
        isAllToTop = toLast;
        if (index == 0) {
            if (toLast) {
                //第一个大屏幕 切换到最后一个空位置

                //获取到当前 有video的最后一个
                TxLogUtils.i("当前房间人数：" + mCount);
                bigScreen = mCount;

                MemberEntity indexEntity = mLayoutEntityList.get(bigScreen);
                ViewGroup.LayoutParams indexParams = indexEntity.getLayout().getLayoutParams();

                MemberEntity fullEntity = mLayoutEntityList.get(0);
                ViewGroup.LayoutParams fullVideoParams = fullEntity.getLayout().getLayoutParams();


                indexEntity.getLayout().setLayoutParams(fullVideoParams);
                indexEntity.setIndex(0);

                fullEntity.getLayout().setLayoutParams(indexParams);
                fullEntity.setIndex(bigScreen);

                indexEntity.getLayout().setMoveable(false);
                indexEntity.getLayout().setOnClickListener(null);

                fullEntity.getLayout().setMoveable(false);
                addFloatViewClickListener(fullEntity.getLayout());

                mLayoutEntityList.set(0, indexEntity); // 将 fromView 塞到 0 的位置
                mLayoutEntityList.set(bigScreen, fullEntity);

                for (int i = 0; i < mLayoutEntityList.size(); i++) {
                    MemberEntity entity = mLayoutEntityList.get(i);
                    // 需要对 View 树的 zOrder 进行重排，否则在 RelativeLayout 下，存在遮挡情况
                    bringChildToFront(entity.getLayout());
                }
            } else {
                //第一个大屏幕 切换到最后一个空位置

                //获取到当前 有video的最后一个
                TxLogUtils.i("当前房间人数：" + mCount);
                bigScreen = mCount;

                MemberEntity indexEntity = mLayoutEntityList.get(bigScreen);
                ViewGroup.LayoutParams indexParams = indexEntity.getLayout().getLayoutParams();

                MemberEntity fullEntity = mLayoutEntityList.get(0);
                ViewGroup.LayoutParams fullVideoParams = fullEntity.getLayout().getLayoutParams();


                indexEntity.getLayout().setLayoutParams(fullVideoParams);
                indexEntity.setIndex(0);

                fullEntity.getLayout().setLayoutParams(indexParams);
                fullEntity.setIndex(bigScreen);

                indexEntity.getLayout().setMoveable(false);
                indexEntity.getLayout().setOnClickListener(null);

                fullEntity.getLayout().setMoveable(false);
                addFloatViewClickListener(fullEntity.getLayout());

                mLayoutEntityList.set(0, indexEntity); // 将 fromView 塞到 0 的位置
                mLayoutEntityList.set(bigScreen, fullEntity);
                for (int i = 0; i < mLayoutEntityList.size(); i++) {
                    MemberEntity entity = mLayoutEntityList.get(i);
                    // 需要对 View 树的 zOrder 进行重排，否则在 RelativeLayout 下，存在遮挡情况
                    bringChildToFront(entity.getLayout());
                }
            }


        }
    }

}
