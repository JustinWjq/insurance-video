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
            et.setText("wjqdev123")
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
            TXSdk.getInstance().startVideo(
                this,
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
            override fun onSuccess() {
             //点击文件按钮跳转
//                val mFileSdkBeanh5 =  FileSdkBean (FileType.h5,"https://www.baidu.com/?tn=64075107_1_dg","");
//                mFileSdkBeanh5.h5Name ="长链测试地址"

                val mFileSdkBeanVideo =  FileSdkBean (FileType.video,"https://cos.ap-shenzhen-fsi.myqcloud.com/wisdom-exhibition-1301905869/oNFPK5ahJut2-Acwe8wS3xBb_VAk/1667801806842.mp4")
                mFileSdkBeanVideo.h5Name ="长链测试地址"

//                val arrayList = ArrayList<String>()
//                arrayList.add("https://gdrb-dingsun-test-1255383806.cos.ap-shanghai.myqcloud.com/%E7%88%B1%E5%BF%83%E4%BA%BA%E5%AF%BF%E5%AE%88%E6%8A%A4%E7%A5%9E2.0%E7%BB%88%E8%BA%AB%E5%AF%BF%E9%99%A9%E6%9D%A1%E6%AC%BE.jpg")
//                arrayList.add("https://gdrb-dingsun-test-1255383806.cos.ap-shanghai.myqcloud.com/%E7%88%B1%E5%BF%83%E4%BA%BA%E5%AF%BF%E5%AE%88%E6%8A%A4%E7%A5%9E2.0%E7%BB%88%E8%BA%AB%E5%AF%BF%E9%99%A9%E6%9D%A1%E6%AC%BE.jpg")
//                val arrayListWord = ArrayList<String>()
//                arrayListWord.add("13123123")
//                arrayListWord.add("131231231231")
//                var mFileSdkBeanVideo =  FileSdkBean(FileType.pics,arrayList)
//                mFileSdkBeanVideo.picsWord = arrayListWord
                TXSdk.getInstance().addFileToSdk(mFileSdkBeanVideo)
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