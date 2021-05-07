package com.txt.myapplication

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.tencent.bugly.crashreport.CrashReport
import com.txt.video.TXSdk
import com.txt.video.net.bean.TxConfig

/**
 * Created by JustinWjq
 * @date 2020/8/31.
 * description：
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val txConfig = TxConfig()
        txConfig.wxKey = "wx8e6096173bff1149"
        //dev 小程序开发版本  TEST 体验版本 RELEASE 正式颁布
        txConfig.miniprogramType = TXSdk.Environment.TEST
        //小程序跳转参数
//        txConfig.userName = "gh_fe0a27ed0ba5"
        txConfig.userName = "gh_9fd3da8ad9f6"
        txConfig.miniprogramTitle = ""//显示小程序描述
        txConfig.miniprogramDescription = ""//显示小程序描述
        //2。域名环境 3。log显示 4。分享小程序的配置
        TXSdk.getInstance().init(this, TXSdk.Environment.TEST, true, txConfig)
        //设置演示demo 为true 为了一个手机装两个app，一个是客户的，一个是演示的app，区分隐式跳转
        TXSdk.getInstance().isDemo = true
        CrashReport.initCrashReport(this, "8351c98a70", true)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

    override fun onTerminate() {
        TXSdk.getInstance().unInit()
        super.onTerminate()
    }

}