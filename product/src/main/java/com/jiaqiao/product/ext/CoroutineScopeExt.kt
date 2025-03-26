package com.jiaqiao.product.ext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * CoroutineScope创建运行在主线程中的携程
 * */
fun CoroutineScope.launchMain(block: suspend CoroutineScope.() -> Unit): Job {
    return this.launch(context = Dispatchers.Main, block =  {
        runPlogCatch { block.invoke(this) }
    })
}

/**
 * CoroutineScope创建运行在子线程中的携程
 * */
fun CoroutineScope.launchIo(block: suspend CoroutineScope.() -> Unit): Job {
    return this.launch(context = Dispatchers.IO, block = {
        runPlogCatch { block.invoke(this) }
    })
}


/**
 * CoroutineScope创建运行在子线程中的携程
 * */
fun CoroutineScope.launchDefault(block: suspend CoroutineScope.() -> Unit): Job {
    return this.launch(context = Dispatchers.Default, block = {
        runPlogCatch { block.invoke(this) }
    })
}