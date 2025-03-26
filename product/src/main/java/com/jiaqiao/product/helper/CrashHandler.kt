package com.jiaqiao.product.helper

import com.jiaqiao.product.base.ProductConstants
import com.jiaqiao.product.config.PlogConfig
import com.jiaqiao.product.ext.libPlog
import com.jiaqiao.product.ext.plogE
import com.jiaqiao.product.ext.toFastJsonString
import com.jiaqiao.product.util.DeviceUtil
import com.jiaqiao.product.util.ProductLog
import com.jiaqiao.product.util.ProductTime
import java.io.File
import java.nio.charset.Charset

object CrashHandler : Thread.UncaughtExceptionHandler {

    private var mDefaultHandler: Thread.UncaughtExceptionHandler? = null

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        if (PlogConfig.debug) {
            "发生奔溃".libPlog()
            ex.plogE()
            carsh(thread, ex)
        }
        mDefaultHandler?.uncaughtException(thread, ex)
    }

    /**
     * 初始化
     */
    fun init() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    /**
     * 崩溃操作
     * */
    private fun carsh(thread: Thread, ex: Throwable) {
        if (PlogConfig.saveLogFile) {
            val time = System.currentTimeMillis()
            val sb = StringBuffer()
            sb.append("奔溃时间：${ProductTime.time1(time)}\n")
            sb.append("奔溃线程：${thread.name}\n")
            sb.append("设备型号：${DeviceUtil.getModel()}\n")
            sb.append("设备SDK版本：${DeviceUtil.getSDKVersionCode()}\n")
            sb.append("设备芯片类型:${DeviceUtil.getABIs().toFastJsonString()}\n")
            sb.append("奔溃信息:\n" + ProductLog.getStackTraceInfo(ex) + "\n")
            saveCarshFile(
                time,
                sb.toString()
            )
        }
    }

    /**
     * 将崩溃数据存入文件中
     * */
    private fun saveCarshFile(time: Long, strLog: String) {
        if (strLog.isNullOrEmpty() || ProductConstants.sdExpFilePath.isNullOrEmpty()) {
            return
        }
        try {
            val file = File(
                ProductConstants.sdExpFilePath
                        + File.separator + ProductTime.time1(time) + ".txt"
            )
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }
            file.writeText(strLog, Charset.forName("UTF-8"))
            file.absolutePath.libPlog("奔溃日志保存路径")
        } catch (thr: Throwable) {
            thr.plogE()
        }
    }
}