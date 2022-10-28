package com.txt.video.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.txt.video.R
import com.txt.video.common.action.ActivityAction
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


}