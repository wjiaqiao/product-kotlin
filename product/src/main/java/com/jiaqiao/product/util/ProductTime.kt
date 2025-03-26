package com.jiaqiao.product.util

import com.jiaqiao.product.ext.plogE
import com.jiaqiao.product.ext.runPlogCatch
import java.text.SimpleDateFormat
import java.util.*

object ProductTime {

    const val MINUTE_TIME = 1000 * 60L
    const val HOUR_TIME = MINUTE_TIME * 60L
    const val DAY_TIME = HOUR_TIME * 24L
    const val WEEK_TIME = DAY_TIME * 7L
    const val DAY_MINUTE = 24 * 60L

    private const val TIME_FORMAT1 = "yyyy-MM-dd HH:mm:ss:SSS"
    private const val TIME_FORMAT2 = "yyyy/MM/dd HH:mm"
    private const val TIME_FORMAT3 = "yyyy-MM-dd"
    private const val TIME_FORMAT4 = "yyyyMMdd.HH"
    private const val TIME_FORMAT5 = "yyyy-MM-dd HH:mm:ss"

    private const val ONLY_DAY = "dd"
    private const val ONLY_YEAR = "yyyy"
    private const val ONLY_MONTH = "MM"

    fun time1(time: Long): String {
        return getTimeString(TIME_FORMAT1, time)
    }

    fun millis1(time: String): Long {
        return getTimeInMillis(TIME_FORMAT1, time)
    }

    fun time2(time: Long): String {
        return getTimeString(TIME_FORMAT2, time)
    }

    fun millis2(time: String): Long {
        return getTimeInMillis(TIME_FORMAT2, time)
    }

    fun time3(time: Long): String {
        return getTimeString(TIME_FORMAT3, time)
    }

    fun millis3(time: String): Long {
        return getTimeInMillis(TIME_FORMAT3, time)
    }

    fun time4(time: Long): String {
        return getTimeString(TIME_FORMAT4, time)
    }

    fun millis4(time: String): Long {
        return getTimeInMillis(TIME_FORMAT4, time)
    }

    fun time5(time: Long): String {
        return getTimeString(TIME_FORMAT5, time)
    }

    fun millis5(time: String): Long {
        return getTimeInMillis(TIME_FORMAT5, time)
    }

    fun onlyDay(time: Long): String {
        return getTimeString(ONLY_DAY, time)
    }

    fun onlyMonth(time: Long): String {
        return getTimeString(ONLY_MONTH, time)
    }

    fun onlyYear(time: Long): String {
        return getTimeString(ONLY_YEAR, time)
    }


    /**
     * 时间戳转String
     */
    fun getTimeString(formatStr: String, time: Long): String {
        return runPlogCatch {
            SimpleDateFormat(formatStr, Locale.US).format(Date(time))
        }.getOrDefault("")
    }


    /**
     * 时间戳转String
     */
    fun getTimeInMillis(formatStr: String, time: String): Long {
        return runPlogCatch {
            SimpleDateFormat(formatStr, Locale.US).parse(time)?.time ?: 0
        }.getOrDefault(0)
    }

    /**
     * 重置当天0点时间戳
     * */
    fun cleanDay(time: Long): Long {
        val timeCal = Calendar.getInstance()
        timeCal.timeInMillis = time
        timeCal[Calendar.HOUR_OF_DAY] = 0 // 时
        timeCal[Calendar.MINUTE] = 0 // 分
        timeCal[Calendar.SECOND] = 0 // 秒
        timeCal[Calendar.MILLISECOND] = 0 // 毫秒
        return timeCal.timeInMillis
    }



}