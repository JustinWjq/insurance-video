package com.txt.video.ui.weight.danmu.util

import android.content.res.Resources
import android.util.TypedValue

fun Float.d2p() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
)

fun Int.d2p() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
).toInt()