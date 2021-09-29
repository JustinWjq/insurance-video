package com.txt.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.txt.video.TXSdk
import com.txt.video.common.callback.onSDKListener
import kotlinx.android.synthetic.main.activity_meeting.*

class MeetingActivity : AppCompatActivity() {
    enum class TYPE {
        CREATEROOM, JOINROOM, RESERVATIONROOM
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meeting)
        val l = System.currentTimeMillis() / 1000
        Log.i("currentTimeMillis", "" + l)
        val encrypt: String = SignUtils.Encrypt(  "test_org2" + l)
        tv_refuse.setOnClickListener {
            TXSdk.getInstance().setAgentInRoomStatus(
                "wjqdev123",
                "他123123",
                "60769a9061eaf65e9e29465b",
                "9878",
                "refused",
                "test_org2",
                encrypt,
                object : onSDKListener {
                    override fun onResultSuccess(result: String) {

                    }

                    override fun onResultFail(errCode: Int, errMsg: String) {

                    }

                }
            )
        }
        tv_invited.setOnClickListener {
            TXSdk.getInstance().setAgentInRoomStatus(
                "wjqdev123",
                "他123123",
                "60769a9061eaf65e9e29465b",
                "9878",
                "invited",
                "test_org2",
                encrypt,
                object : onSDKListener {
                    override fun onResultSuccess(result: String) {

                    }

                    override fun onResultFail(errCode: Int, errMsg: String) {

                    }

                }
            )
        }
    }

    public fun onMeetClick(v: View) {
        val type = when (v.id) {
            R.id.tv_createroom -> {
                "1"
            }
            R.id.tv_joinroom -> {
                "2"
            }
            R.id.tv_reservationroom -> {
                "3"
            }

            else -> {
                "1"
            }
        }

        MainActivity.gotoActivity(this, type)
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 12301) {
            val roomId = data?.getStringExtra("roomId")
            tx_roomid.text = "邀请码：$roomId"
        }
    }
}