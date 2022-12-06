package com.txt.video.common.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * 作者　: hegaojian
 * 时间　: 2020/3/10
 * 描述　: 时间工具类
 */

object DatetimeUtil {

    public val DATE_PATTERN = "yyyy-MM-dd"
    public var DATE_PATTERN_SS = "yyyy-MM-dd HH:mm:ss"
    public var DATE_PATTERN_MM = "yyyy-MM-dd HH:mm"

    /**
     * 获取现在时刻
     */
    val now: Date
        get() = Date(Date().time)
    /**
     * 获取现在时刻
     */
    val nows: Date
        get() = formatDate(
            DATE_PATTERN,
            now
        )


    /**
     * Date to Strin
     */
    @SuppressLint("SimpleDateFormat")
    fun formatDate(date: Date?, formatStyle: String): String {
        return if (date != null) {
            val sdf = SimpleDateFormat(formatStyle)
            sdf.format(date)
        } else {
            ""
        }

    }
    /**
     * Date to Strin
     */
    @SuppressLint("SimpleDateFormat")
    fun formatDate(date: Long, formatStyle: String): String {
        val sdf = SimpleDateFormat(formatStyle)
        return sdf.format(Date(date))

    }

    fun formatDate(formatStyle: String, formatStr: String): Date {
        val format = SimpleDateFormat(formatStyle, Locale.CHINA)
        return try {
            val date = Date()
            date.time = format.parse(formatStr).time
            date
        } catch (e: Exception) {
            println(e.message)
            nows
        }

    }

    /**
     * Date to Date
     */
    @SuppressLint("SimpleDateFormat")
    fun formatDate(formatStyle: String, date: Date?): Date {
        if (date != null) {
            val sdf = SimpleDateFormat(formatStyle)
            val formatDate = sdf.format(date)
            try {
                return sdf.parse(formatDate)
            } catch (e: ParseException) {
                e.printStackTrace()
                return Date()
            }

        } else {
            return Date()
        }
    }

    /**
     * 将时间戳转换为时间
     */
    fun stampToDate(s: String): Date {
        val lt = s.toLong()
        return Date(lt)
    }

    /**
     * 获得指定时间的日期
     */
    fun getCustomTime(dateStr: String): Date {
        return formatDate(
            DATE_PATTERN,
            dateStr
        )
    }

    @Throws(ParseException::class)
    fun UTCToCST(UTCStr: String?): String? {
        var date: Date? = null
        val sdf =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        date = sdf.parse(UTCStr)
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar[Calendar.HOUR] = calendar[Calendar.HOUR] + 8
        //calendar.getTime() 返回的是Date类型，也可以使用calendar.getTimeInMillis()获取时间戳
        val simpleDateFormat =
            SimpleDateFormat("MM-dd HH:mm:ss") //yyyy-MM-dd
        return simpleDateFormat.format(calendar.timeInMillis)
    }

    fun getFormatHMS(time: Long): String {
        val l = time / 1000;

        return String.format("%02d:%02d:%02d", l / 3600, l / 60, l % 60)
    }

}
