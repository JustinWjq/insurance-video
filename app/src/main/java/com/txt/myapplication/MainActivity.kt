package com.txt.myapplication

//import com.txt.video.widget.utils.ToastUtils
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.SimpleCallback
import com.txt.video.TXSdk
//import com.txt.video.net.utils.TxLogUtils
//import com.txt.video.net.utils.TxLogUtils
import com.txt.video.common.callback.StartVideoResultOnListener
import com.txt.video.common.callback.onCreateRoomListener
import com.txt.video.common.callback.onFriendBtListener
import com.txt.video.net.utils.TxLogUtils
import com.txt.video.ui.video.RoomControlConfig
//import com.txt.video.common.utils.ToastUtils
//import com.txt.video.net.utils.TxLogUtils
//import com.txt.video.widget.dialog.ShareWhiteBroadDialog
//import com.txt.video.widget.utils.AndroidSystemUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity(), View.OnClickListener  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        if (BuildConfig.DEBUG)
            et.setText("18200409")
        initView()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    var type = ""
    fun initView() {
        if (intent != null) {
            type = if (intent?.getStringExtra("type") != null) {
                intent?.getStringExtra("type")!!
            } else {
                ""
            }
            TYPE = type
        } else {
            type = TYPE
        }
        var btStr = when (type) {
            "2" -> {
                group1.visibility = View.VISIBLE
                group2.visibility = View.VISIBLE
                "加入会议"
            }
            "3" -> {
                group1.visibility = View.GONE
                group2.visibility = View.VISIBLE
                "预约会议"
            }
            else -> {
                group1.visibility = View.GONE
                group2.visibility = View.VISIBLE
                "创建会议"
            }
        }
        bt.text = btStr
        et_account.setText(
            when (TXSdk.getInstance().environment) {
                TXSdk.Environment.DEV, TXSdk.Environment.TEST -> "test_org2"
                else -> "gsc_test"
            }
        )
        bt.setOnClickListener {
//            TxLogUtils.i("AndroidSystemUtil.getDevice"+AndroidSystemUtil.getDevice())
            when (type) {
                "2" -> { //加入房间
                    startSDK(isCreateRoom = true)
                }
                "3" -> {
                    startSDK(isCreateRoom = true)
                }
                else -> {
                    startSDK(isCreateRoom = false)
                }
            }


        }

        tv_config.setOnClickListener(this)
        check_bt.setOnClickListener(this)

        changeUI()
        tv_gototxmeet.setOnClickListener {
//            WemeetSdkUtil.intoHome(this,object : WemeetSdkUtil.instance.OnInitCallBackListener{
//                override fun initResult(boolean: Boolean, string: String) {
//                    TxLogUtils.i("initResult ----$boolean ---- $string")
//                }
//
//            })
        }
    }


    @SuppressLint("SetTextI18n")
    fun changeUI() {
        et_account.setText(
            when (TXSdk.getInstance().environment) {
                TXSdk.Environment.DEV, TXSdk.Environment.TEST -> "test_org2"
                else -> "gsc_test"
            }
        )
        var sdkVersion = "SDK：" + TXSdk.getInstance().sdkVersion
        var appEnv = sdkVersion + "\n" + when (TXSdk.getInstance().environment) {
            TXSdk.Environment.DEV -> "App：开发环境"
            TXSdk.Environment.TEST -> "App：测试环境"
            else -> "App：正式环境"
        }
        tv_dep.text = appEnv + "\n" + when (TXSdk.getInstance().txConfig.miniprogramType) {
            TXSdk.Environment.DEV -> {
                "小程序：开发版本"
            }
            TXSdk.Environment.TEST -> {
                "小程序：体验版本"
            }
            else -> "小程序：正式版本"
        }
        var appEnv1 = when (TXSdk.getInstance().environment) {
            TXSdk.Environment.DEV -> "/开发环境"
            TXSdk.Environment.TEST -> "/测试环境"
            else -> "/正式环境"
        }
        check_bt.text = when (TXSdk.getInstance().txConfig.miniprogramType) {
            TXSdk.Environment.DEV -> {
                "开发版本" + appEnv1
            }
            TXSdk.Environment.TEST -> {
                "体验版本" + appEnv1
            }
            else -> "正式版本$appEnv1"
        }
    }


    private fun startSDK(isCreateRoom: Boolean) {
        val businessData = JSONObject().apply {
            put("latitude", 123.1231231)
            put("longitude", 21.123123)
            put("accuracy", 1000)
            put("province", "上海市")
            put("city", "上海市")
            put("adr", "上海市")
//            put("userHead","https://cos.ap-shenzhen-fsi.myqcloud.com/wisdom-exhibition-1301905869/wjqdev123456/1612146168774.jpeg")
            put("userHead","https://cos.ap-shenzhen-fsi.myqcloud.com/wisdom-exhibition-1301905869/5fa21f563b165e74b0f3bc26/1628480799658.jpg")
//            put("userHead","")
            }
        val loginName = et.text.toString()
        val roomid = et_roomid.text.toString()



        val orgAccount = et_account.text.toString()
//        val orgAccount = "gscjg"
        if (loginName.isEmpty()) {
            Toast.makeText(this@MainActivity, "请填入账号！", Toast.LENGTH_SHORT).show()
            return
        }
        if (orgAccount.isEmpty()) {
            Toast.makeText(this@MainActivity, "请填入组织代码！", Toast.LENGTH_SHORT).show()
            return
        }
        val l = System.currentTimeMillis() / 1000
        Log.i("currentTimeMillis", "" + l)
        val encrypt: String = SignUtils.Encrypt(orgAccount + "" + l)
        if (type == "2") {
            TXSdk.getInstance()
                .joinRoom(
                    this,
                    roomid,
                    loginName,
                    "测试$loginName",
                    orgAccount,
                    encrypt,
                    businessData,
                    RoomControlConfig.Builder().enableVideo(RoomConfig.showVideo).setVideoMode(RoomConfig.videoMode).build(),
                    object : StartVideoResultOnListener {
                        override fun onResultSuccess() {

                        }

                        override fun onResultFail(errCode: Int, errMsg: String) {
                            Toast.makeText(this@MainActivity, errMsg, Toast.LENGTH_SHORT).show()
                        }
                    })
            return
        }
        if (isCreateRoom) {
            TXSdk.getInstance().createRoom(
                loginName,
                orgAccount,
                encrypt,
                object :
                    onCreateRoomListener {
                    override fun onResultSuccess(roomId: String) {
                        Log.i("roomId", roomId)
                        Toast.makeText(this@MainActivity, "预约成功", Toast.LENGTH_SHORT).show()
                        intent.putExtra("roomId", roomId)
                        setResult(12301, intent)
                        finish()
                    }

                    override fun onResultFail(errCode: Int, errMsg: String) {
                    }


                })
        } else {
            TXSdk.getInstance().startTXVideo(
                this,
                loginName,
                orgAccount,
                encrypt,
                businessData,
                RoomControlConfig.Builder().enableVideo(RoomConfig.showVideo).setVideoMode(RoomConfig.videoMode).build(),
                object :
                    StartVideoResultOnListener {
                    override fun onResultSuccess() {

                    }

                    override fun onResultFail(errCode: Int, errMsg: String) {

                    }

                })
        }

        TXSdk.getInstance().addOnFriendBtListener(object :onFriendBtListener{
            override fun onSuccess(roomId: String, serviceId: String, inviteAccount: String) {
//                ToastUtils.showShort("$roomId\n${serviceId}\n${inviteAccount}")
                tx_roomid.text ="$roomId"
//                startActivity(Intent(this@MainActivity,DemoActivity::class.java))
            }

            override fun onFail(errCode: Int, errMsg: String) {

            }

        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_config, R.id.check_bt -> {
                XPopup.Builder(this)
                    .setPopupCallback(object : SimpleCallback() {
                        override fun onDismiss() {
                            super.onDismiss()
                            changeUI()

                        }
                    })
                    .hasStatusBarShadow(true)
                    .autoOpenSoftInput(true)
                    .asCustom(CustomFullScreenPopup(this))
                    .show()
            }
            else -> {
            }
        }
    }

    companion object {
        var TYPE = ""
        fun gotoActivity(context: Activity, type: String) {
            context.startActivityForResult(
                Intent(context, MainActivity::class.java).apply {
                    putExtra("type", type)
                },
                12300
            )

        }
    }


}