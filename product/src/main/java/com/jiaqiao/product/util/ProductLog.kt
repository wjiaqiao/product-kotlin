package com.jiaqiao.product.util

import android.util.Log
import com.jiaqiao.product.base.ProductConstants
import com.jiaqiao.product.config.PlogConfig
import com.jiaqiao.product.ext.*
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.io.Writer
import java.nio.charset.Charset
import java.util.concurrent.Executors
import java.util.logging.Logger

object ProductLog {


    //线程池
    private var threadPool = Executors.newSingleThreadExecutor()

    init {
        checkJob()
    }

    //取消协程及job
    fun cancel() {
        runPlogCatch {
            threadPool.shutdownNow()
        }
        threadPool = Executors.newSingleThreadExecutor()
    }

    /**
     * 检查日志写入文件线程的状态
     * */
    fun checkJob() {
        if (PlogConfig.debug) {

        } else {
            cancel()
        }
    }

    /**
     * 将日志写入文件中
     * */
    private fun realWriteFile(log: String) {
        try {
            val file = File(
                ProductConstants.sdLogFilePath
                        + File.separator + ProductTime.time3(System.currentTimeMillis()) + ".txt"
            )
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }
            file.appendText(
                log + "\n",
                Charset.forName("UTF-8")
            )
        } catch (_: Throwable) {

        }
    }

    /**
     * 输出日志
     * [tag] 日志标签tag
     * [anyObj] 日志对象
     * */
    fun log(tag: String, anyObj: Any?) {
        if (anyObj.notNull() && anyObj is Throwable) {
            e(anyObj)
        } else {
            log(
                tag, if (anyObj.isNull()) {
                    "null"
                } else {
                    anyObj.toString()
                }
            )
        }
    }

    /**
     * 输出日志的堆栈信息
     * [tag] 日志标签tag
     * [log] 具体日志内容
     * */
    fun info(tag: String, log: String) {
        if (!PlogConfig.debug || tag.isNullOrEmpty()) {
            return
        }
        val sts = Thread.currentThread().stackTrace ?: return
        log(tag, "------------------------", false)
        for (st in sts) {
            if (st.isNativeMethod) {
                continue
            }
            if (st.className == Thread::class.java.name) {
                continue
            }
            if (st.className == Logger::class.java.name) {
                continue
            }
            if (st.className == ProductLog::class.java.name) {
                continue
            }
            if (st.className.contains(".ext.")) {
                continue
            }
            log(
                tag,
                "[ " + Thread.currentThread().name + ": ${st.className} --> ("
                        + st.fileName + ":" + st.lineNumber + ") "
                        + st.methodName + " ]",
                false
            )
        }
        log(tag, "------------------------", false)
        log(tag, log)
    }

    /**
     * 输出日志
     * [tag] 日志标签tag
     * [log] 具体日志内容
     * */
    fun log(tag: String, log: String, stackInfo: Boolean = true) {
        if (!PlogConfig.debug || tag.isNullOrEmpty()) {
            return
        }
        val funName = if (stackInfo) getFunctionName() else ""
        if (PlogConfig.saveLogFile) {
            if (!funName.isNullOrEmpty()) {
                writeSdLog(tag, "$funName - $log")
            } else {
                writeSdLog(tag, log)
            }
        }
        if (PlogConfig.logInterceptor.notNull()) {
            PlogConfig.logInterceptor?.invoke(tag, log)
            return
        }
        try {
            if (funName.isNotEmpty()) {
                val length = log.length
                if (length > PlogConfig.logLineMaxLength) {
                    val size = Math.ceil((length / PlogConfig.logLineMaxLength).toDouble())
                        .toInt() + 1
                    Log.i(tag, funName + "length=" + length)
                    Log.i(tag, funName + "lineNum=" + size)
                    for (i in 0 until size) {
                        if (i == 0) {
                            Log.i(tag, "$funName -start- ")
                            Log.i(tag, funName + log.substring(0, PlogConfig.logLineMaxLength))
                        } else if (i > 0 && i < size - 1) {
                            Log.i(
                                tag,
                                funName + log.substring(
                                    PlogConfig.logLineMaxLength * i,
                                    PlogConfig.logLineMaxLength * (i + 1)
                                )
                            )
                        } else if (i == size - 1) {
                            Log.i(
                                tag,
                                funName + log.substring(PlogConfig.logLineMaxLength * i, length)
                            )
                            Log.i(tag, "$funName -end- ")
                        }
                    }
                } else {
                    Log.i(tag, "$funName - $log")
                }
            } else {
                Log.i(tag, log)
            }
        } catch (thr: Throwable) {
            if (PlogConfig.debug) {
                thr.printStackTrace()
            }
        }
    }

    /**
     * 输出报错日志
     * */
    fun e(thr: Throwable?) {
        if (!PlogConfig.debug || thr.isNull()) {
            return
        }
        thr!!.printStackTrace()
        log(PlogConfig.tag1, "Throwable.localizedMessage => " + thr!!.localizedMessage)
        log(PlogConfig.errorTag, "\n${getStackTraceInfo(thr)}")
    }


    /**
     * 发送至保存文件的队列
     * */
    private fun writeSdLog(tag: String, log: String) {
        if (!PlogConfig.debug || tag.isNullOrEmpty() || log.isNullOrEmpty() || !PlogConfig.saveLogFile) {
            return
        }
        try {
            threadPool.execute {
                realWriteFile(ProductTime.time1(System.currentTimeMillis()) + "/$tag: $log")
            }
        } catch (thr: Throwable) {
            if (PlogConfig.debug) {
                thr.printStackTrace()
            }
        }
    }

    /**
     * 获取运行代码的位置信息
     * */
    private fun getFunctionName(): String {
        val sts = Thread.currentThread().stackTrace ?: return ""
        for (st: StackTraceElement in sts) {
            if (st.isNativeMethod) {
                continue
            }
            if (st.className == Thread::class.java.name) {
                continue
            }
            if (st.className == Logger::class.java.name) {
                continue
            }
            if (st.className == ProductLog::class.java.name) {
                continue
            }
            if (st.className.contains(".ext.")) {
                continue
            }
            if (PlogConfig.functionClassName.notNullAndEmpty() && PlogConfig.functionClassName.contains(
                    st.className
                )
            ) {
                continue
            }
            return ("[ " + Thread.currentThread().name + ": ("
                    + st.fileName + ":" + st.lineNumber + ") "
                    + st.methodName + " ]")
        }
        return ""
    }

    /**
     * 获取错误的信息
     *
     * [throwable]
     * @return
     */
    fun getStackTraceInfo(thr: Throwable): String {
        var pw: PrintWriter? = null
        val writer: Writer = StringWriter()
        try {
            pw = PrintWriter(writer)
            thr.printStackTrace(pw)
        } catch (thr: Throwable) {
            return ""
        } finally {
            pw?.close()
        }
        return writer.toString()
    }

    /**
     * 删除所有日志文件
     * */
    fun deleteLogFile() {
        if (ProductConstants.sdLogcatFilePath.notNullAndEmpty()) {
            File(ProductConstants.sdLogcatFilePath).productDeleteAll()
        }
    }

}