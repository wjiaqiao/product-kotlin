package com.example.productkotlin.base

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.jiaqiao.product.config.PlogConfig
import com.jiaqiao.product.ext.plog

class AppApplication : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

    override fun onCreate() {
        super.onCreate()
        PlogConfig.debug = true
        PlogConfig.saveLogFile = true
        PlogConfig.tag1 = "product_app_lib"
        PlogConfig.errorTag = "app_error_log"
        "AppApplication.onCreate".plog()
    }

}