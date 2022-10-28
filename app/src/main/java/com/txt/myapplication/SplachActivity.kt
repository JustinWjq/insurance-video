package com.txt.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.util.Log

class SplachActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("SplachActivity","${this.isTaskRoot}")
//        if (!this.isTaskRoot){
//            finish()
//        }else{
//
//        }
        setContentView(R.layout.activity_splach)
        Handler().postDelayed({
            MainActivity.gotoActivity(this, "1")
            finish()
        },500)
    }
}