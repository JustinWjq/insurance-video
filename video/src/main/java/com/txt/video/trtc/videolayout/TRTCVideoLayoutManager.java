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
 * 3.堆叠布局：{@link TRTCVideoLayoutManager#makeFloatLayout(boolean)} 思路是初始化一系列的 x、y、padding、margin 组合 LayoutParams 直接对 View 进行定位
 * <p>
 * 4.宫格布局：{@link TRTCVideoLayoutManager#makeGirdLayout(boolean)} 思路与堆叠布局一致，也是初始化一些列的 LayoutParams 直接对 View 进行定位
 * <p>
 * 5.如何实现管理：
 * A. 使用{@link TRTCLayoutEntity} 实体类，保存 {@link TRTCVideoLayout} 的分配信息，能够与对应的用户绑定起来，方便管理与更新UI
 * B. {@link TRTCVideoLayout} 专注实现业务 UI 相关的，控制逻辑放在此类中
 * <p>
 * 6.布局切换，见 {@link TRTCVideoLayoutManager#switchMode()}
 * <p>
 * 7.堆叠布局与宫格布局参数，见{@link Utils} 工具类
 */
public class TRTCVideoLayoutManager extends RelativeLayout implements IVideoLayoutListener {
    public static final int MODE_FLOAT = 1;  // 前后堆叠模式
    public static final int MODE_GRID = 2;  // 九宫格模式
    public static final int MAX_USER = 4;
    private final static String TAG = TRTCVideoLayoutManager.class.getSimpleName();
    public WeakReference<IVideoLayoutManagerListener> mWefListener;
    private ArrayList<TRTCLayoutEntity> mLayoutEntityList;
    private ArrayList<LayoutParams> mFloatParamList;
    private ArrayList<LayoutParams> mGrid4ParamList;
    private ArrayList<LayoutParams> mGrid9ParamList;
    private int mCount = 0;
    private int mMode;
    private String mSelfUserId;

    /**
     * ===============================View相关===============================
     */
    public TRTCVideoLayoutManager(Context context) {
        super(context);
        initView(context);
    }


    public TRTCVideoLayoutManager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }


    public TRTCVideoLayoutManager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        Log.i(TAG, "initView: ");

        mLayoutEntityList = new ArrayList<TRTCLayoutEntity>();
        // 初始化多个 View，以备用
        for (int i = 0; i < MAX_USER; i++) {
            TRTCVideoLayout videoLayout = new TRTCVideoLayout(context);
            videoLayout.setVisibility(View.GONE);
            videoLayout.setBackgroundColor(Color.BLACK);
            videoLayout.setMoveable(false);
            videoLayout.setIVideoLayoutListener(this);
            // 这里不展示其底部的控制菜单
            videoLayout.setBottomControllerVisibility(View.GONE);
            TRTCLayoutEntity entity = new TRTCLayoutEntity();
            entity.layout = videoLayout;
            entity.index = i;
            mLayoutEntityList.add(entity);
        }
        // 默认为堆叠模式
        mMode = MODE_FLOAT;
        this.post(new Runnable() {
            @Override
            public void run() {
                makeFloatLayout(true);
//                makeGirdLayout(true);
            }
        });
    }

    /**
     * ===============================九宫格布局下点击按钮的事件回调===============================
     */

    @Override
    public void onClickFill(TRTCVideoLayout view, boolean enableFill) {
        IVideoLayoutManagerListener listener = mWefListener != null ? mWefListener.get() : null;
        if (listener != null) {
            TRTCLayoutEntity entity = findEntity(view);
            listener.onClickItemFill(entity.userId, entity.streamType, enableFill);
        }
    }

    @Override
    public void onClickMuteAudio(TRTCVideoLayout view, boolean isMute) {
        IVideoLayoutManagerListener listener = mWefListener != null ? mWefListener.get() : null;
        if (listener != null) {
            TRTCLayoutEntity entity = findEntity(view);
            listener.onClickItemMuteAudio(entity.userId, isMute);
        }
    }

    @Override
    public void onClickMuteVideo(TRTCVideoLayout view, boolean isMute) {
        IVideoLayoutManagerListener listener = mWefListener != null ? mWefListener.get() : null;
        if (listener != null) {
            TRTCLayoutEntity entity = findEntity(view);
            listener.onClickItemMuteVideo(entity.userId, entity.streamType, isMute);
        }
    }

    @Override
    public void onClickMuteInSpeakerAudio(TRTCVideoLayout view, boolean isMute) {
        IVideoLayoutManagerListener listener = mWefListener != null ? mWefListener.get() : null;
        if (listener != null) {
            TRTCLayoutEntity entity = findEntity(view);
            listener.onClickItemMuteInSpeakerAudio(entity.userId, isMute);
        }
    }

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
        for (TRTCLayoutEntity layoutEntity : mLayoutEntityList) {
            if (layoutEntity.streamType == streamType && layoutEntity.userId.equals(userId)) {

                return layoutEntity.layout.getVideoView();
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
    public TRTCLayoutEntity findCloudViewViewSetUserName(String userId, int streamType, String userName) {
        if (userId == null) return null;
        for (TRTCLayoutEntity layoutEntity : mLayoutEntityList) {
            if (layoutEntity.streamType == streamType && layoutEntity.userId.equals(userId)) {
                layoutEntity.layout.updateNoVideoLayout(userName, View.GONE);
                return layoutEntity;
            }
        }
        return null;
    }


    //找到指定的videoview
    public TXCloudVideoView findCloudViewView() {
        TRTCLayoutEntity trtcLayoutEntity = mLayoutEntityList.get(1);
        return trtcLayoutEntity.layout.getVideoView();

    }

    public int fingCloudViewCount() {
        int count = 0;
        for (TRTCLayoutEntity layoutEntity : mLayoutEntityList) {
            if (!layoutEntity.userId.isEmpty()) {
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
        //如果是业务员，则放到右上角
        if (userId == mSelfUserId) {
            TRTCLayoutEntity trtcLayoutEntity = mLayoutEntityList.get(1);
            trtcLayoutEntity.userId = userId;
            trtcLayoutEntity.streamType = streamType;
            trtcLayoutEntity.userName = userName;
            trtcLayoutEntity.layout.setVisibility(VISIBLE);
            mCount++;
            trtcLayoutEntity.layout.updateNoVideoLayout(userName, View.GONE);
            return trtcLayoutEntity.layout.getVideoView();
        }


        for (TRTCLayoutEntity layoutEntity : mLayoutEntityList) {
            int i = mLayoutEntityList.indexOf(layoutEntity);
            TxLogUtils.i("mLayoutEntityList.indexOf" + i);

            if (isAllToTop) {
                //如果全部在上，由于第一个
                if (i == 0) {
                    //大屏幕不能显示
                } else {
                    if (layoutEntity.userId.equals("")) {
                        layoutEntity.userId = userId;
                        layoutEntity.streamType = streamType;
                        layoutEntity.userName = userName;
                        layoutEntity.layout.setVisibility(VISIBLE);
                        mCount++;
//                if (mMode == MODE_GRID) {
//                    if (mCount == 5) {
//                        makeGirdLayout(true);
//                    }
//                }
                        layoutEntity.layout.updateNoVideoLayout(userName, View.GONE);
                        return layoutEntity.layout.getVideoView();
                    }
                }

            } else {
                if (layoutEntity.userId.equals("")) {
                    layoutEntity.userId = userId;
                    layoutEntity.streamType = streamType;
                    layoutEntity.userName = userName;
                    layoutEntity.layout.setVisibility(VISIBLE);
                    mCount++;
//                if (mMode == MODE_GRID) {
//                    if (mCount == 5) {
//                        makeGirdLayout(true);
//                    }
//                }
                    layoutEntity.layout.updateNoVideoLayout(userName, View.GONE);
                    return layoutEntity.layout.getVideoView();
                }
            }


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

        for (TRTCLayoutEntity entity : mLayoutEntityList) {
            if (entity.streamType == streamType && userId.equals(entity.userId)) {
                mCount--;
                if (mMode == MODE_GRID) {
                    if (mCount == 4) {
                        makeGirdLayout(true);
                    }
                }
                entity.layout.setVisibility(GONE);
                entity.userId = "";
                entity.streamType = -1;
                break;
            }
        }

        for (int i = 0; i < mLayoutEntityList.size(); i++) {
            TRTCLayoutEntity entity = mLayoutEntityList.get(i);
            // 需要对 View 树的 zOrder 进行重排，否则在 RelativeLayout 下，存在遮挡情况
            bringChildToFront(entity.layout);
        }
    }

    /**
     * 隐藏所有音量的进度条
     */
    public void hideAllAudioVolumeProgressBar() {
        for (TRTCLayoutEntity entity : mLayoutEntityList) {
            entity.layout.setAudioVolumeProgressBarVisibility(View.GONE);
        }
    }

    /**
     * 显示所有音量的进度条
     */
    public void showAllAudioVolumeProgressBar() {
        for (TRTCLayoutEntity entity : mLayoutEntityList) {
            entity.layout.setAudioVolumeProgressBarVisibility(View.VISIBLE);
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
        for (TRTCLayoutEntity entity : mLayoutEntityList) {
            if (entity.layout.getVisibility() == VISIBLE) {
                if (userId.equals(entity.userId)) {
                    entity.layout.setAudioVolumeProgress(audioVolume);
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
        for (TRTCLayoutEntity entity : mLayoutEntityList) {
            if (entity.layout.getVisibility() == VISIBLE) {
                if (userId.equals(entity.userId)) {
                    entity.layout.updateNetworkQuality(quality);
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
        for (TRTCLayoutEntity entity : mLayoutEntityList) {
            if (entity.layout.getVisibility() == VISIBLE) {
                if (userId.equals(entity.userId) && entity.streamType == TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG) {
                    String content = userId;
                    if (userId.equals(mSelfUserId)) {
                        content += "(您自己)";
                    }
                    entity.layout.updateNoVideoLayout(content, bHasVideo ? GONE : VISIBLE);
                    break;
                }
            }
        }
    }

    private TRTCLayoutEntity findEntity(TRTCVideoLayout layout) {
        for (TRTCLayoutEntity entity : mLayoutEntityList) {
            if (entity.layout == layout) return entity;
        }
        return null;
    }

    private TRTCLayoutEntity findEntity(String userId) {
        for (TRTCLayoutEntity entity : mLayoutEntityList) {
            if (entity.userId.equals(userId)) return entity;
        }
        return null;
    }

    /**
     * 切换到九宫格布局
     *
     * @param needUpdate 是否需要更新布局
     */
    private void makeGirdLayout(boolean needUpdate) {
        if (mGrid4ParamList == null || mGrid4ParamList.size() == 0 || mGrid9ParamList == null || mGrid9ParamList.size() == 0) {
            mGrid4ParamList = Utils.initGrid4Param(getContext(), getWidth(), getHeight());
            mGrid9ParamList = Utils.initGrid9Param(getContext(), getWidth(), getHeight());
        }
        if (needUpdate) {
            ArrayList<LayoutParams> paramList;
            if (mCount <= 4) {
                paramList = mGrid4ParamList;
            } else {
                paramList = mGrid9ParamList;
            }
            int layoutIndex = 1;
            for (int i = 0; i < mLayoutEntityList.size(); i++) {
                TRTCLayoutEntity entity = mLayoutEntityList.get(i);
                entity.layout.setMoveable(false);
                entity.layout.setOnClickListener(null);
                // 我自己要放在布局的左上角
                if (entity.userId.equals(mSelfUserId)) {
                    entity.layout.setLayoutParams(paramList.get(0));
                } else if (layoutIndex < paramList.size()) {
                    entity.layout.setLayoutParams(paramList.get(layoutIndex++));
                }
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
            mFloatParamList = Utils.initFloatParamList(getContext(), getWidth(), getHeight());
        }

        // 根据堆叠布局参数，将每个view放到适当的位置
        for (int i = 0; i < mLayoutEntityList.size(); i++) {
            TRTCLayoutEntity entity = mLayoutEntityList.get(i);
            LayoutParams layoutParams = mFloatParamList.get(i);
            entity.layout.setLayoutParams(layoutParams);
            if (i == 0) {
                entity.layout.setMoveable(false);
            } else {
                entity.layout.setMoveable(false);
            }
            addFloatViewClickListener(entity.layout);
            entity.layout.setBottomControllerVisibility(View.GONE);

            if (needAddView) {
                addView(entity.layout);
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
    private void addFloatViewClickListener(final TRTCVideoLayout view) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (TRTCLayoutEntity entity : mLayoutEntityList) {
                    if (entity.layout == v) {
                        if (isAllToTop) {
                            TxLogUtils.i("当前是白板状态，点击小屏，不能切换");
                        } else {
                            if (fingCloudViewCount() == 1) {
                                TxLogUtils.i("当前只有一个用户，点击小屏，不能切换");
                            } else {
                                makeFullVideoView(entity.index);
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
        TRTCLayoutEntity indexEntity = mLayoutEntityList.get(index);
        ViewGroup.LayoutParams indexParams = indexEntity.layout.getLayoutParams();

        TRTCLayoutEntity fullEntity = mLayoutEntityList.get(0);
        ViewGroup.LayoutParams fullVideoParams = fullEntity.layout.getLayoutParams();

        indexEntity.layout.setLayoutParams(fullVideoParams);
        indexEntity.index = 0;

        fullEntity.layout.setLayoutParams(indexParams);
        fullEntity.index = index;

        indexEntity.layout.setMoveable(false);
        indexEntity.layout.setOnClickListener(null);

        fullEntity.layout.setMoveable(false);
        addFloatViewClickListener(fullEntity.layout);

        mLayoutEntityList.set(0, indexEntity); // 将 fromView 塞到 0 的位置
        mLayoutEntityList.set(index, fullEntity);

        for (int i = 0; i < mLayoutEntityList.size(); i++) {
            TRTCLayoutEntity entity = mLayoutEntityList.get(i);
            // 需要对 View 树的 zOrder 进行重排，否则在 RelativeLayout 下，存在遮挡情况
            bringChildToFront(entity.layout);
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

                TRTCLayoutEntity indexEntity = mLayoutEntityList.get(bigScreen);
                ViewGroup.LayoutParams indexParams = indexEntity.layout.getLayoutParams();

                TRTCLayoutEntity fullEntity = mLayoutEntityList.get(0);
                ViewGroup.LayoutParams fullVideoParams = fullEntity.layout.getLayoutParams();


                indexEntity.layout.setLayoutParams(fullVideoParams);
                indexEntity.index = 0;

                fullEntity.layout.setLayoutParams(indexParams);
                fullEntity.index = bigScreen;

                indexEntity.layout.setMoveable(false);
                indexEntity.layout.setOnClickListener(null);

                fullEntity.layout.setMoveable(false);
                addFloatViewClickListener(fullEntity.layout);

                mLayoutEntityList.set(0, indexEntity); // 将 fromView 塞到 0 的位置
                mLayoutEntityList.set(bigScreen, fullEntity);

                for (int i = 0; i < mLayoutEntityList.size(); i++) {
                    TRTCLayoutEntity entity = mLayoutEntityList.get(i);
                    // 需要对 View 树的 zOrder 进行重排，否则在 RelativeLayout 下，存在遮挡情况
                    bringChildToFront(entity.layout);
                }
            } else {
                //第一个大屏幕 切换到最后一个空位置

                //获取到当前 有video的最后一个
                TxLogUtils.i("当前房间人数：" + mCount);
                bigScreen = mCount;

                TRTCLayoutEntity indexEntity = mLayoutEntityList.get(bigScreen);
                ViewGroup.LayoutParams indexParams = indexEntity.layout.getLayoutParams();

                TRTCLayoutEntity fullEntity = mLayoutEntityList.get(0);
                ViewGroup.LayoutParams fullVideoParams = fullEntity.layout.getLayoutParams();


                indexEntity.layout.setLayoutParams(fullVideoParams);
                indexEntity.index = 0;

                fullEntity.layout.setLayoutParams(indexParams);
                fullEntity.index = bigScreen;

                indexEntity.layout.setMoveable(false);
                indexEntity.layout.setOnClickListener(null);

                fullEntity.layout.setMoveable(false);
                addFloatViewClickListener(fullEntity.layout);

                mLayoutEntityList.set(0, indexEntity); // 将 fromView 塞到 0 的位置
                mLayoutEntityList.set(bigScreen, fullEntity);
                for (int i = 0; i < mLayoutEntityList.size(); i++) {
                    TRTCLayoutEntity entity = mLayoutEntityList.get(i);
                    // 需要对 View 树的 zOrder 进行重排，否则在 RelativeLayout 下，存在遮挡情况
                    bringChildToFront(entity.layout);
                }
            }


        }

//        if (index <= 0 || mLayoutEntityList.size() <= index) return;
//        Log.i(TAG, "makeFullVideoView: from = " + index);
//        TRTCLayoutEntity       indexEntity = mLayoutEntityList.get(index);
//        ViewGroup.LayoutParams indexParams = indexEntity.layout.getLayoutParams();
//
//        TRTCLayoutEntity       fullEntity      = mLayoutEntityList.get(0);
//        ViewGroup.LayoutParams fullVideoParams = fullEntity.layout.getLayoutParams();
//
//        indexEntity.layout.setLayoutParams(fullVideoParams);
//        indexEntity.index = 0;
//
//        fullEntity.layout.setLayoutParams(indexParams);
//        fullEntity.index = index;
//
//        indexEntity.layout.setMoveable(false);
//        indexEntity.layout.setOnClickListener(null);
//
//        fullEntity.layout.setMoveable(false);
//        addFloatViewClickListener(fullEntity.layout);
//
//        mLayoutEntityList.set(0, indexEntity); // 将 fromView 塞到 0 的位置
//        mLayoutEntityList.set(index, fullEntity);
//
//        for (int i = 0; i < mLayoutEntityList.size(); i++) {
//            TRTCLayoutEntity entity = mLayoutEntityList.get(i);
//            // 需要对 View 树的 zOrder 进行重排，否则在 RelativeLayout 下，存在遮挡情况
//            bringChildToFront(entity.layout);
//        }
    }

    private static class TRTCLayoutEntity {
        public TRTCVideoLayout layout;
        public int index = -1;
        public String userId = "";
        public int streamType = -1;
        public String userName = "";
    }
}
