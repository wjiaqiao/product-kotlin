package com.jiaqiao.product.ext

import com.jiaqiao.product.util.PLifeScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

private val appPLifeScope by lazy { PLifeScope() }

/**
 * 运行在子线程
 * */
fun scopeLaunchIo(block: suspend CoroutineScope.() -> Unit): Job {
    return appPLifeScope.launchIo(block)
}

/**
 * 运行在主线程
 * */
fun scopeLaunchMain(block: suspend CoroutineScope.() -> Unit): Job {
    return appPLifeScope.launchMain(block)
}

/**
 * 运行在默认线程
 * */
fun scopeLaunchDefault(block: suspend CoroutineScope.() -> Unit): Job {
    return appPLifeScope.launchDefault(block)
}

/**
 * 运行协程范围
 * */
fun scopeLaunch(block: suspend CoroutineScope.() -> Unit): Job {
    return appPLifeScope.launch(block)
}

/**
 * 关闭已开启的所有协程
 */
fun scopeClose(){
    appPLifeScope.close()
}