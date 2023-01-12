package com.txt.myapplication

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
import com.txt.video.common.callback.StartVideoResultOnListener
import com.txt.video.common.callback.onCreateRoomListener
import com.txt.video.common.callback.onFileClickListener
import com.txt.video.common.utils.ToastUtils
import com.txt.video.net.bean.FileSdkBean
import com.txt.video.net.bean.FileType
import com.txt.video.ui.video.RoomControlConfig
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity(), View.OnClickListener  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        if (BuildConfig.DEBUG)
            et.setText("1050020635123")
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
                TXSdk.Environment.DEV, TXSdk.Environment.TEST -> "sunshineLifeOrg"
                else -> "txuat"
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
    }


    @SuppressLint("SetTextI18n")
    fun changeUI() {
        et_account.setText(
            when (TXSdk.getInstance().environment) {
                TXSdk.Environment.DEV, TXSdk.Environment.TEST -> "sunshineLifeOrg"
                else -> "txuat"
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
        val loginName = et.text.toString()
        val orgAccount = et_account.text.toString()
        if (loginName.isEmpty()) {
            Toast.makeText(this@MainActivity, "请填入账号！", Toast.LENGTH_SHORT).show()
            return
        }
        if (orgAccount.isEmpty()) {
            Toast.makeText(this@MainActivity, "请填入组织代码！", Toast.LENGTH_SHORT).show()
            return
        }
        //加密方式
        val l = System.currentTimeMillis() / 1000
        Log.i("currentTimeMillis", "" + l)
        val encrypt: String = SignUtils.Encrypt(orgAccount + "" + l)
        if (type == "2") {
            return
        }
        if (isCreateRoom) {
        } else {

            TXSdk.getInstance().userNickname =  "&channel=txMeeting"+
                    "&userCode=${loginName}"+
                    "&userName=${loginName}"
            TXSdk.getInstance().startVideo(
                this,
                loginName,
                loginName,
                orgAccount,
                encrypt,
                object :
                    StartVideoResultOnListener {
                    override fun onResultSuccess() {

                    }

                    override fun onResultFail(errCode: Int, errMsg: String) {
                        Toast.makeText(this@MainActivity,errMsg,Toast.LENGTH_LONG).show()
                    }

                })
        }

        TXSdk.getInstance().addOnFileClickListener(object :onFileClickListener{
            override fun onSuccess(roomId : Int) {
             //点击文件按钮跳转
                val mFileSdkBeanVideo =  FileSdkBean (FileType.h5,
                    "https://precisemkttest.sinosig.com/resourceNginx/H5Project/qnbProjectV3/index.html#/planMain/planIndex",
                    "{\"agentInfo\":{\"agentkind\":\"NA\",\"agentlevelcode\":1,\"birthday\":\"499622400000\",\"branchtype\":\"1\",\"email\":\"qixuechao-lhq@sinosig.com\",\"manageComName\":\"阳光人寿保险股份有限公司北京分公司\",\"managecom\":\"8601\",\"mobile\":\"18618128372\",\"orgType\":\"4\",\"remark\":0,\"sex\":true,\"usercode\":\"1050020635\",\"username\":\"刘二哈\",\"usertype\":\"S\"},\"faceAI\":true,\"firstLogin\":false,\"homeMainMenu\":[{\"carryicon\":\"https://l106.oss-cn-szfinance.aliyuncs.com/icon/homebottom/1b.png\",\"jumpaddress\":\"\",\"menuname\":\"首页\",\"uncheckedicon\":\"https://l106.oss-cn-szfinance.aliyuncs.com/icon/homebottom/1a.png\"},{\"carryicon\":\"https://l106.oss-cn-szfinance.aliyuncs.com/icon/homebottom/2b.png\",\"jumpaddress\":\"http://10.8.207.39:8001/#/home\",\"menuname\":\"学习平台\",\"uncheckedicon\":\"https://l106.oss-cn-szfinance.aliyuncs.com/icon/homebottom/2a.png\"},{\"carryicon\":\"https://l106.oss-cn-szfinance.aliyuncs.com/icon/homebottom/4b.png\",\"jumpaddress\":\"\",\"menuname\":\"展业夹\",\"uncheckedicon\":\"https://l106.oss-cn-szfinance.aliyuncs.com/icon/homebottom/4a.png\"},{\"carryicon\":\"https://l106.oss-cn-szfinance.aliyuncs.com/icon/homebottom/5b.png\",\"jumpaddress\":\"\",\"menuname\":\"我的\",\"uncheckedicon\":\"https://l106.oss-cn-szfinance.aliyuncs.com/icon/homebottom/5a.png\"}],\"homegroup\":{\"url\":\"/homegroup/homegroup\"},\"id\":\"d817d5fdd8d24821a05103420744ea8e\",\"isTemp\":\"N\",\"isUpPass\":0,\"logmessage\":{\"count\":1,\"hotCount\":1,\"pendingCount\":1,\"performanceCount\":1,\"systemCount\":1},\"manaController\":0,\"mobile\":\"18618128372\",\"newsmsCheck\":false,\"oneMonthNotLogin\":false,\"phoneInfo\":{\"commonlyPhoneCount\":1,\"commonlyPhoneStatus\":true},\"servicePuk\":\"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC9GoCVoPiztYNS+tTXFCRwot15vD+8LhC98OaEuN0rUknPi8FWCOuy6N6HBRsBgojyVdqdgWGgF+OjzzYvQhehQcYK2c54vqYJVFOEGdWmd2eKfm3MBuFPXfCioexs2pQ6X2JZKWWlzB53AekhPRsrkR1YLzsjnmx0ZxllcQLhrQIDAQAB\",\"smsCheck\":false,\"timeSpan\":\"100000000\",\"token\":\"T0dsS0wzcEJkVTFyVWpGSmRrdEpSMGxZYUhwMWRuSTJkRkpTWlVWa1luVjVVbE5oV0ZKMFJuUjBhWFFyWVdoWmVVRllhWGM0Y0dwUWFuTmxiVFZNYVdZeFkwcGhkblpqT0ZOM1dWcGxWWEV2VGpBM01uYzlQUT09\",\"userRoles\":[\"COMMON\",\"NASHR\",\"NASHR1\"],\"warnCount\":0}");
                mFileSdkBeanVideo.h5Name ="长链测试地址"

//                val mFileSdkBeanVideo =  FileSdkBean (FileType.video,
//                    "https://slupl106.sinosig.com/visit/file/d4072a7c10a3448ba5b797b1d703cf62.mp4")
//
//                mFileSdkBeanVideo.h5Name ="长链测试地址"

//                val arrayList = ArrayList<String>()
//                arrayList.add("https://gdrb-dingsun-test-1255383806.cos.ap-shanghai.myqcloud.com/%E7%88%B1%E5%BF%83%E4%BA%BA%E5%AF%BF%E5%AE%88%E6%8A%A4%E7%A5%9E2.0%E7%BB%88%E8%BA%AB%E5%AF%BF%E9%99%A9%E6%9D%A1%E6%AC%BE.jpg")
//                arrayList.add("https://gdrb-dingsun-test-1255383806.cos.ap-shanghai.myqcloud.com/%E7%94%B5%E5%AD%90%E6%8A%95%E4%BF%9D%E5%8D%95.jpg")
//                val arrayListWord = ArrayList<String>()
//                arrayListWord.add("11111111111111111111111111111111119999999999999999999999999999999999999999999999999999999999")
//                arrayListWord.add("2222222222222222222222222222222")
//                var mFileSdkBeanVideo =  FileSdkBean(FileType.pics,arrayList)
//                mFileSdkBeanVideo.picsWord = arrayListWord
                TXSdk.getInstance().addFileToSdk(mFileSdkBeanVideo)
            }

            override fun onEndRoom() {
                Log.i("TXSdk","onEndRoom")
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