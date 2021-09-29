package com.txt.myapplication

import android.app.Application
import android.content.Context
import com.tencent.wemeet.tmsdk.TMSDK
import com.tencent.wemeet.tmsdk.TMSDK.getAccountService
import com.tencent.wemeet.tmsdk.TMSDK.getPreMeetingService
import com.tencent.wemeet.tmsdk.callback.AuthenticationCallback
import com.tencent.wemeet.tmsdk.callback.PreMeetingCallback
import com.tencent.wemeet.tmsdk.callback.SDKCallback
import com.tencent.wemeet.tmsdk.data.InitParams
import com.txt.video.TXSdk
import com.txt.video.net.utils.TxLogUtils


class WemeetSdkUtil {



    // 普通会议
    val MEETING_TYPE_NORMAL = 0

    // 快速会议
    val MEETING_TYPE_QUICK_MEETING = 1

    companion object instance {
        var sdk_id = "16243445920"
        var sdk_token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJUZW5jZW50IE1lZXRpbmciLCJpc3MiOiIxNjI0MzQ0NTkyMCIsImlhdCI6MTYzMjYxODc2NywiZXhwIjoxNjM0Nzc4NzY3LCJuYmYiOjE2MzI2MTg3Njd9.Yy5Nw1UBoRS0WEu84PiqFi3Q_bgMobfzsjs5-coH_3w"
        var sso_url = "https://ysp-idp.id.meeting.qq.com/cidp/custom/ai-e5d30afb7dae4b8f894a065849304aa0/ai-8ac683fc0cdd464891c1bb1310163032?id_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiIxNjI0MzQ0NTkyMCIsInN1YiI6InN0YXJuZXRzc2giLCJuYW1lIjoi5pa95Lmm6LGqIiwiaWF0IjoxNjMyODk3MDczLCJleHAiOjE2MzI4OTczNzMsIm5iZiI6MTYzMjg5NzA3M30.IHKdNJA9dkn3l2gcgY4p-14t4YGpIaeVlKVeS0TY0H7az-i1OVwDaliLma3kttBApZUL5ONGANu_5R59jy7qxV0NABKvvd3acCI6EpWYmO5mdxTdQ7d2ljpDNaSHRgLHQNC6vXDNJOpQMDr0CpS4sbhrZmecCaIQk2vkYAkLQl0"
        var listener: OnInitCallBackListener? = null

        interface OnInitCallBackListener {
            fun initResult(boolean: Boolean, string: String)
        }

        //初始化 绑定应⽤进程（所有进程调⽤)
        fun attachBaseContext(app: Application) {
            //腾讯会议
            TMSDK.initOnApplicationCreate(app)
        }

        fun initCallBack() {
            getAccountService().setCallback(object : AuthenticationCallback {
                override fun onLogin(code: Int, msg: String?) {
                    //账户登录的回调
                    TxLogUtils.i("onLogin ----code$code ---- msg$msg")
                    if (code == 0) {
                        listener?.let {
                            it.initResult(true, "$code  $msg")
                            intoHome(TXSdk.getInstance().application, listener)
                        }
                    }


                }

                override fun onLogout(type: Int, code: Int, msg: String?) {
                    //账户登出的回调
                    TxLogUtils.i("onLogout ----code$code ---- msg$msg")
                }
            })

            getPreMeetingService().setCallback(object : PreMeetingCallback {
                override fun onJoinMeeting(resultCode: Int, msg: String, meetingCode: String) {
                    //⼊会的回调
                    TxLogUtils.i("onJoinMeeting ----resultCode$resultCode ---- msg$msg ----meetingCode$meetingCode")
                }

                override fun onShowScreenCastResult(code: Int, msg: String) {
                    //说明：打开⽆线投屏的回调。
                    TxLogUtils.i("onShowScreenCastResult ----code$code ---- msg$msg ---")
                }
            })

        }

        //登陆授权管理
        fun authorizationManagement(context: Context, listener: OnInitCallBackListener?) {

            this.listener = listener

            if (!TMSDK.isInitialized()) {
                initSdk(context, sdk_id, sdk_token)
                initCallBack()
            }


        }

        //初始化SDK
        private fun initSdk(
            context: Context,
            sdkId: String,
            sdkToken: String
        ) {


            TMSDK.initialize(
                InitParams.Builder("网络会议")
                    .setSaasParams(
                        sdkId,
                        sdkToken
                    ).build(),
                object : SDKCallback {
                    override fun onSDKInitializeResult(code: Int, msg: String) {
                        TxLogUtils.i("onSDKInitializeResult ----code$code ---- msg$msg --")
                        if (code == 0) {
                            //获取登录url
                            getAccountService().login(sso_url)
                        } else {
                            listener?.initResult(
                                false,
                                "$code  $msg"
                            )
                        }
                    }

                    override fun onSDKError(code: Int, msg: String) {
                        TxLogUtils.i("onSDKError ----code$code ---- msg$msg --")
                        listener?.initResult(
                            false,
                            "$code  $msg"
                        )
                    }
                }
            )
        }

        //进⼊会议主⻚
        fun intoHome(context: Context, listener: OnInitCallBackListener?) {
            if (TMSDK.isInitialized()) {
                //此操作在未登录状态下会⾃动触发登陆流程
                getPreMeetingService().showPreMeetingView()
                listener?.initResult(true, "")
            } else {
                authorizationManagement(context, listener)
            }
        }

        fun logoOut() {
            //登出
            getAccountService().logout()
        }


    }


}