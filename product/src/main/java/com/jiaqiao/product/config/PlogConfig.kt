package com.jiaqiao.product.config

import com.jiaqiao.product.util.ProductLog

/**
 * 日志系统的配置参数
 * */
object PlogConfig {

    //debug模式才会输出并保存日志文件
    var debug = false
        set(value) {
            if (field != value) {
                field = value
                ProductLog.checkJob()
            }
        }

    //是否将日志保存为文件
    var saveLogFile = false
        set(value) {
            if (field != value) {
                field = value
                ProductLog.checkJob()
            }
        }

    //日志拦截器
    var logInterceptor: ((String, String) -> Unit)? = null

    //日志分行长度
    var logLineMaxLength = 2000

    //日志方法定位过滤列表
    val functionClassName by lazy { mutableListOf<String>() }


    var tag1 = ""
    var tag2 = ""
    var tag3 = ""
    var tag4 = ""
    var tag5 = ""
    val libTag1 = "product_kotlin"
    val libTag2 = ""
    val libTag3 = ""
    val libTag4 = ""
    val libTag5 = ""
    var errorTag = ""

}