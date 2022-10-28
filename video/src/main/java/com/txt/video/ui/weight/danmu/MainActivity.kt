package com.txt.video.ui.weight.danmu

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.txt.video.R
import com.txt.video.common.glide.TxGlide
import com.txt.video.ui.weight.danmu.adapter.BulletAdapter
import com.txt.video.ui.weight.danmu.dialog.InputDialog
import com.txt.video.ui.weight.danmu.entity.BULLET_TYPE_NORMAL
import com.txt.video.ui.weight.danmu.entity.Bullet
import com.txt.video.ui.weight.danmu.view.BulletView
import com.txt.video.ui.weight.danmu.view.LiveCardView

class MainActivity : AppCompatActivity() {

    private lateinit var viewBullet: BulletView
    private lateinit var tvBullet: TextView
    private lateinit var ivGoods: ImageView
    private lateinit var viewCard: LiveCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
        setContentView(R.layout.tx_activity_test)
        val iv_bg = findViewById<ImageView>(R.id.iv_bg)

        viewBullet = findViewById(R.id.view_bullet)
        viewCard = findViewById(R.id.view_card)
        tvBullet = findViewById(R.id.tv_bullet)
        ivGoods = findViewById(R.id.iv_goods)

        tvBullet.setOnClickListener { showInputDialog() }
        ivGoods.setOnClickListener { addCard() }
        viewBullet.setAdapter(BulletAdapter())
        viewCard.setOnVisibilityListener {
            if (it == View.VISIBLE) {
                viewBullet.liveCardChange(viewCard.getCardHeight())
            } else {
                viewBullet.liveCardChange(0)
            }
        }
    }

    private fun addCard() {
        if (viewCard.visibility == View.VISIBLE) {
            viewCard.dismissCard()

        } else {
            viewCard.showCard()

            Handler().postDelayed({ viewCard.dismissCard()},500)
        }



    }

    private fun showInputDialog() {
        InputDialog().apply {
            setKeyboardListener { _, margin ->
                viewBullet.keyboardChange(margin)
            }
            setOnTextSendListener {
                viewBullet.addMessage(Bullet("wangzai", it, BULLET_TYPE_NORMAL))
            }
        }.show(supportFragmentManager, "InputDialog")
    }
}