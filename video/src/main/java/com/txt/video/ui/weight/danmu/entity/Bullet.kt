package com.txt.video.ui.weight.danmu.entity

const val BULLET_TYPE_NOTICE = 0 // 弹幕公告

const val BULLET_TYPE_NORMAL = 1 // 普通弹幕


data class Bullet(val userName: String?, val content: String, val msgType: Int)