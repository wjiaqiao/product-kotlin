package com.jiaqiao.product.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * 协程作用域工具
 * */
object CoroutineScopeUtil {

    //创建默认主线程的协程作用域
    fun createMainScope() = CoroutineScope(Dispatchers.Main)

    //创建默认io线程的协程作用域
    fun createIoScope() = CoroutineScope(Dispatchers.IO)


}