package com.txt.video.common.callback

import com.txt.video.trtc.videolayout.list.MemberEntity

/**
 * Created by JustinWjq
 * @date 2020/8/28.
 * description：
 */
open interface onMuteDialogListener {
    fun onMuteVideo( memberEntity: MemberEntity)
    fun onMuteAudio( memberEntity: MemberEntity)
    fun onMoveOutRomm(memberEntity: MemberEntity)
}