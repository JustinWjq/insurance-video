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
                    "http://sfss-uat.sinosig.com:8080/proposal/index.html#/ProductListPage?channelCode=100&R=43117089&isRay=true",
                    "{\n" +
                            "    \"agentInfo\":{\n" +
                            "        \"agentkind\":\"NA\",\n" +
                            "        \"agentlevelcode\":1,\n" +
                            "        \"birthday\":\"788889600000\",\n" +
                            "        \"branchtype\":\"1\",\n" +
                            "        \"email\":\"qixuechao-lhq@sinosig.com\",\n" +
                            "        \"fgsName\":\"重庆分公司\",\n" +
                            "        \"fullname\":\"阳光人寿保险股份有限公司重庆分公司\",\n" +
                            "        \"gradecode\":\"TA\",\n" +
                            "        \"manageComName\":\"阳光人寿保险股份有限公司北京分公司\",\n" +
                            "        \"managecom\":\"8601\",\n" +
                            "        \"mobile\":\"18618128372\",\n" +
                            "        \"orgType\":\"4\",\n" +
                            "        \"remark\":0,\n" +
                            "        \"sex\":false,\n" +
                            "        \"usercode\":\"1030016957\",\n" +
                            "        \"username\":\"汪健\",\n" +
                            "        \"usertype\":\"S\"\n" +
                            "    },\n" +
                            "    \"faceAI\":true,\n" +
                            "    \"firstLogin\":false,\n" +
                            "    \"homeMainMenu\":[\n" +
                            "        {\n" +
                            "            \"carryicon\":\"https://l106.oss-cn-szfinance.aliyuncs.com/icon/homebottom/1b.png\",\n" +
                            "            \"jumpaddress\":\"\",\n" +
                            "            \"menuname\":\"首页\",\n" +
                            "            \"uncheckedicon\":\"https://l106.oss-cn-szfinance.aliyuncs.com/icon/homebottom/1a.png\"\n" +
                            "        },\n" +
                            "        {\n" +
                            "            \"carryicon\":\"https://l106.oss-cn-szfinance.aliyuncs.com/icon/homebottom/2b.png\",\n" +
                            "            \"jumpaddress\":\"http://10.8.207.39:8001/#/home\",\n" +
                            "            \"menuname\":\"学习平台\",\n" +
                            "            \"uncheckedicon\":\"https://l106.oss-cn-szfinance.aliyuncs.com/icon/homebottom/2a.png\"\n" +
                            "        },\n" +
                            "        {\n" +
                            "            \"carryicon\":\"https://l106.oss-cn-szfinance.aliyuncs.com/icon/homebottom/4b.png\",\n" +
                            "            \"jumpaddress\":\"\",\n" +
                            "            \"menuname\":\"展业夹\",\n" +
                            "            \"uncheckedicon\":\"https://l106.oss-cn-szfinance.aliyuncs.com/icon/homebottom/4a.png\"\n" +
                            "        },\n" +
                            "        {\n" +
                            "            \"carryicon\":\"https://l106.oss-cn-szfinance.aliyuncs.com/icon/homebottom/5b.png\",\n" +
                            "            \"jumpaddress\":\"\",\n" +
                            "            \"menuname\":\"我的\",\n" +
                            "            \"uncheckedicon\":\"https://l106.oss-cn-szfinance.aliyuncs.com/icon/homebottom/5a.png\"\n" +
                            "        }\n" +
                            "    ],\n" +
                            "    \"homegroup\":{\n" +
                            "        \"url\":\"/homegroup/homegroup\"\n" +
                            "    },\n" +
                            "    \"id\":\"eb8fe50a26884f8298f7a1d7c724369a\",\n" +
                            "    \"isTemp\":\"N\",\n" +
                            "    \"isUpPass\":0,\n" +
                            "    \"logmessage\":{\n" +
                            "        \"count\":1,\n" +
                            "        \"hotCount\":1,\n" +
                            "        \"pendingCount\":1,\n" +
                            "        \"performanceCount\":1,\n" +
                            "        \"systemCount\":1\n" +
                            "    },\n" +
                            "    \"manaController\":0,\n" +
                            "    \"mobile\":\"18618128372\",\n" +
                            "    \"newsmsCheck\":false,\n" +
                            "    \"oneMonthNotLogin\":false,\n" +
                            "    \"phoneInfo\":{\n" +
                            "        \"commonlyPhoneCount\":0,\n" +
                            "        \"commonlyPhoneStatus\":true\n" +
                            "    },\n" +
                            "    \"servicePuk\":\"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC9GoCVoPiztYNS+tTXFCRwot15vD+8LhC98OaEuN0rUknPi8FWCOuy6N6HBRsBgojyVdqdgWGgF+OjzzYvQhehQcYK2c54vqYJVFOEGdWmd2eKfm3MBuFPXfCioexs2pQ6X2JZKWWlzB53AekhPRsrkR1YLzsjnmx0ZxllcQLhrQIDAQAB\",\n" +
                            "    \"smsCheck\":false,\n" +
                            "    \"timeSpan\":\"100000000\",\n" +
                            "    \"token\":\"TTBsaFVFVkhVVkpXTjB4Qk5rZEJRak14ZDJzeU4xbFlTRWczVG5oR1NXWkRZVGxVV0dRdmJ6aFVhblV3Ymt3M1pGTnZPRmhPTUhSVU4xSkNMMFZsVkc1amNVazVSV3hWTUdacWJISjBiVFI1TUZNeFlXYzlQUT09\",\n" +
                            "    \"userRoles\":[\n" +
                            "        \"NASHR1\",\n" +
                            "        \"SHR\"\n" +
                            "    ],\n" +
                            "    \"warnCount\":0\n" +
                            "}");
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