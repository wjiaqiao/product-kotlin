package com.jiaqiao.product.ext

import android.os.Handler
import android.os.Looper

private val mainHandler by lazy { Handler(Looper.getMainLooper()) }

/**
 * 运行在主线程中
 * */
fun runMainLooper(action: () -> Unit) {
    if (Looper.getMainLooper() == Looper.myLooper()) {
        action.invoke()
    } else {
        mainHandler.post {
            action.invoke()
        }
    }
}