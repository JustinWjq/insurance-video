package com.txt.video.base

import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.txt.video.R
import com.txt.video.common.bar.OnKeyboardListener
import com.txt.video.common.bar.TxBarHide
import com.txt.video.common.bar.TximmersionBar

/**
 * Created by JustinWjq
 * @date 2019-12-23.
 * description：
 */
abstract class BaseActivity<V, P : BasePresenter<V>> : AppCompatActivity() {
    public var mPresenter: P? = null

    /**
     * 获取布局id
     * @return 当前需要加载的布局
     */
    protected abstract fun getContentViewId(): Int

    /**
     * 初始化
     * @param savedInstanceState
     */
    protected abstract fun init(savedInstanceState: Bundle?)

    /**
     * 创建Presenter
     * @return 返回当前Presenter
     */
    protected abstract fun createPresenter(): P?


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentViewId())
        mPresenter = createPresenter()
        if (mPresenter != null) {
            mPresenter?.attachView(this as V)
        }
        init(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.detachView()
    }

    protected open fun openImmersionBar() {
        TximmersionBar {
                reset()
                navigationBarColor(R.color.tx_color_424548)
                statusBarColor(R.color.tx_color_424548)
                statusBarDarkFont(false)
                navigationBarDarkIcon(true)
                keyboardEnable(false)
        }
    }

    protected open fun hideStatusBar() {
        TximmersionBar{
            hideBar(TxBarHide.FLAG_HIDE_STATUS_BAR)
            setOnKeyboardListener(object :OnKeyboardListener{
                override fun onKeyboardChange(isPopup: Boolean, keyboardHeight: Int) {

                }

            })
        }
    }

    protected open fun showStatusBar() {
        TximmersionBar{
            hideBar(TxBarHide.FLAG_SHOW_BAR)
            setOnKeyboardListener(object :OnKeyboardListener{
                override fun onKeyboardChange(isPopup: Boolean, keyboardHeight: Int) {

                }

            })
        }
    }

    protected open fun closeImmersionBar() {
        TximmersionBar {
            reset()
            statusBarColor(R.color.tx_color_424548)
            navigationBarColor(R.color.tx_color_424548)
            statusBarDarkFont(true)
            navigationBarDarkIcon(true)
            keyboardEnable(false)
        }

    }

    /**
     * 获取点击事件
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (isHideInput(view, ev)) {
                HideSoftInput(view!!.windowToken)
                view.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 判定是否需要隐藏
     */
    private  fun isHideInput(v: View?, ev: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            return if (ev.x > left && ev.x < right && ev.y > top && ev.y < bottom) {
                false
            } else {
                true
            }
        }
        return false
    }

    /**
     * 隐藏软键盘
     */
    private  fun HideSoftInput(token: IBinder?) {
        if (token != null) {
            val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }


}