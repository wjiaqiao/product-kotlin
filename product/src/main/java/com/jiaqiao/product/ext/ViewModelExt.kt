package com.jiaqiao.product.ext

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


/**
 * 运行协程范围
 * */
fun ViewModel.launch(block: suspend CoroutineScope.() -> Unit): Job {
    return launch(block, EmptyCoroutineContext)
}

/**
 * 运行协程范围
 * */
fun ViewModel.launch(
    block: suspend CoroutineScope.() -> Unit,
    coroutineContext: CoroutineContext = EmptyCoroutineContext
): Job {
    return viewModelScope.launch(coroutineContext, block = block)
}

/**
 * 运行在子线程
 * */
fun ViewModel.launchIo(block: suspend CoroutineScope.() -> Unit): Job {
    return launch(block, Dispatchers.IO)
}

/**
 * 运行在主线程
 * */
fun ViewModel.launchMain(block: suspend CoroutineScope.() -> Unit): Job {
    return launch(block, Dispatchers.Main)
}

/**
 * 运行在默认线程
 * */
fun ViewModel.launchDefault(block: suspend CoroutineScope.() -> Unit): Job {
    return launch(block, Dispatchers.Default)
}
