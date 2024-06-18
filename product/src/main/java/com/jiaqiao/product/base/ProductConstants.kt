package com.jiaqiao.product.base

import com.jiaqiao.product.BuildConfig

/**
 * 配置参数
 * */
object ProductConstants {



    const val versionCode = BuildConfig.VERSION_CODE //SDK版本号
    const val versionName = BuildConfig.VERSION_NAME //SDK版本名




    var appFilePath = "" //app内部存储文件夹
    var sdAppFilePath = "" //sd卡app存储文件夹
    var sdCacheFilePath = "" //sd卡app缓存文件夹
    var sdLogcatFilePath = ""//保存日志文件夹
    var sdLogFilePath = ""//普通日志存储路径
    var sdExpFilePath = ""//奔溃日志存储路径
    var sdRootFilePath = "" //sd卡根目录存储文件夹

}