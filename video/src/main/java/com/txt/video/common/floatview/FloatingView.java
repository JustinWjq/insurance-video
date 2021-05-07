package com.txt.video.common.floatview;

/**
 * Created by JustinWjq
 *
 * @date 2020/9/2.
 * description：
 */

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.txt.video.net.utils.TxLogUtils;

import java.util.List;

import static android.view.Gravity.RIGHT;

/**
 * 悬浮球，点击可以弹出菜单
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class FloatingView extends FrameLayout implements GestureDetector.OnGestureListener, View.OnClickListener {

    private Context mContext;
    private WindowManager mWindowManager;
    private GestureDetector mGestureDetector;
    private WindowManager.LayoutParams mLayoutParams;
    private float mLastX;
    private float mLastY;
    private PopupWindow mPopupWindow;
    private long mTapOutsideTime;
    private boolean mIsShowing = false;

    public FloatingView(Context context) {
        super(context);
        this.mContext = context;
        this.mGestureDetector = new GestureDetector(context, this);
    }

    public FloatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        this.mGestureDetector = new GestureDetector(context, this);
    }

    public FloatingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.mGestureDetector = new GestureDetector(context, this);
    }

    /**
     * 悬浮球
     *
     * @param context   建议使用application context避免activity泄漏
     * @param viewResId Resid
     */
    public FloatingView(Context context, int viewResId) {
        super(context);
        this.mContext = context;
        View.inflate(context, viewResId, this);
        this.mGestureDetector = new GestureDetector(context, this);
    }

    public void showView(View view) {
        showView(view, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public void showView(View view, int width, int height) {
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        //TYPE_TOAST仅适用于4.4+系统，假如要支持更低版本使用TYPE_SYSTEM_ALERT(需要在manifest中声明权限)
        //7.1（包含）及以上系统对TYPE_TOAST做了限制
        int type ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else  {
            type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        mLayoutParams = new WindowManager.LayoutParams(type);
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.flags |= WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        //layoutParams.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS; //no limit适用于超出屏幕的情况，若添加此flag需要增加边界检测逻辑
        mLayoutParams.width = width;
        mLayoutParams.height = height;
        mLayoutParams.gravity = RIGHT;
        mLayoutParams.format = PixelFormat.TRANSLUCENT;
        mWindowManager.addView(view, mLayoutParams);
    }

    public void hideView() {
        if (null != mWindowManager) {
            mWindowManager.removeViewImmediate(this);
        }
        mWindowManager = null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        mLastX = e.getRawX();
        mLastY = e.getRawY();
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float nowX, nowY, tranX, tranY;
        // 获取移动时的X，Y坐标
        nowX = e2.getRawX();
        nowY = e2.getRawY();
        // 计算XY坐标偏移量
        tranX = nowX - mLastX;
        tranY = nowY - mLastY;
        // 移动悬浮窗
        mLayoutParams.x += tranX;
        mLayoutParams.y += tranY;
        //更新悬浮窗位置
        mWindowManager.updateViewLayout(this, mLayoutParams);
        //记录当前坐标作为下一次计算的上一次移动的位置坐标
        mLastX = nowX;
        mLastY = nowY;
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    /**
     * 设置弹出菜单
     *
     * @param id resource id，根据resource id inflate 菜单
     */
    public void setPopupWindow(int id) {
        mPopupWindow = new PopupWindow(this);
        mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(false);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setContentView(LayoutInflater.from(getContext()).inflate(id, null));
        mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    mPopupWindow.dismiss();
                    mTapOutsideTime = System.currentTimeMillis();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 获取 popupWindow 顶层 view
     */
    public View getPopupView() {
        return mPopupWindow.getContentView();
    }

    /**
     * 注册点击回调
     */
    public void setOnPopupItemClickListener(View.OnClickListener listener) {
        if (mPopupWindow == null)
            return;

        ViewGroup layout = (ViewGroup) mPopupWindow.getContentView();
        for (int i = 0; i < layout.getChildCount(); i++) {
            layout.getChildAt(i).setOnClickListener(listener);
        }
    }

    /**
     * 显示悬浮球
     */
    public void show() {
        if (!mIsShowing) {
            showView(this);
        }
        mIsShowing = true;
    }

    /**
     * 关闭悬浮球
     */
    public void dismiss() {
        if (mIsShowing) {
            hideView();
        }
        mIsShowing = false;
        // 清空 listener
        ViewGroup layout = (ViewGroup) mPopupWindow.getContentView();
        for (int i = 0; i < layout.getChildCount(); i++) {
            layout.getChildAt(i).setOnClickListener(null);
        }
    }

    /**
     * 单击显示popupWindow 用户点击触发
     *
     * @param e motionEvent
     * @return false 本身不对点击事件进行消费
     */
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        TxLogUtils.i("onSingleTapUp---" + e.getAction());
//        if (null != mPopupWindow)
//            mPopupWindow.dismiss();
        // 避免单击悬浮球不断出现 popupwindows
//        if (!(System.currentTimeMillis() - mTapOutsideTime < 80)) {
//            mPopupWindow.showAtLocation(this, Gravity.NO_GRAVITY, 100, 0);
//        }

        if (mTouchButtonClickListener != null) {
            mTouchButtonClickListener.onClick();
        }
        return false;
    }

    @SuppressLint("MissingPermission")
    public void setTopApp(Context context) {
        //获取ActivityManager
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获得当前运行的task(任务)
        List<ActivityManager.RunningTaskInfo> taskInfoList = null;
        if (activityManager != null) {
            taskInfoList = activityManager.getRunningTasks(100);
        }
        if (taskInfoList != null) {
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                //找到本应用的 task，并将它切换到前台
                if (taskInfo.topActivity != null && taskInfo.topActivity.getPackageName().equals(context.getPackageName())) {
                    activityManager.moveTaskToFront(taskInfo.id, 0);
                    break;
                }
            }
        }
    }



    @Override
    public void onClick(View v) {

    }

    private OnTouchButtonClickListener mTouchButtonClickListener;

    public void setTouchButtonClickListener(OnTouchButtonClickListener touchButtonClickListener) {
        mTouchButtonClickListener = touchButtonClickListener;
    }

    public interface OnTouchButtonClickListener {
        void onClick();
    }
}
