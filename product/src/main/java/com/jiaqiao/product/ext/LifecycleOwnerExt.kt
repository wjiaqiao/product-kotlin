package com.jiaqiao.product.ext

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


/** 
 * 运行协程范围
 * */
fun LifecycleOwner.launch(block: suspend CoroutineScope.() -> Unit): Job {
    return launch(block, EmptyCoroutineContext)
}

/** 
 * 运行协程范围
 * */
fun LifecycleOwner.launch(
    block: suspend CoroutineScope.() -> Unit,
    coroutineContext: CoroutineContext = EmptyCoroutineContext
): Job {
    return lifecycleScope.launch(coroutineContext, block =  {
        runPlogCatch { block.invoke(this) }
    })
}

/** 
 * 运行在子线程
 * */
fun LifecycleOwner.launchIo(block: suspend CoroutineScope.() -> Unit): Job {
    return launch(block, Dispatchers.IO)
}

/** 
 * 运行在主线程
 * */
fun LifecycleOwner.launchMain(block: suspend CoroutineScope.() -> Unit): Job {
    return launch(block, Dispatchers.Main)
}

/** 
 * 运行在默认线程
 * */
fun LifecycleOwner.launchDefault(block: suspend CoroutineScope.() -> Unit): Job {
    return launch(block, Dispatchers.Default)
}

