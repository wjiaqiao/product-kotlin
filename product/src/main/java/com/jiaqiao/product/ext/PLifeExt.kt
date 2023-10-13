package com.jiaqiao.product.ext

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.pLifeScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 运行协程范围
 * */
fun ViewModel.launch(block: suspend CoroutineScope.() -> Unit): Job {
    return pLifeScope.launch(block)
}

/**
 * 运行协程范围
 * */
fun ViewModel.launch(
    block: suspend CoroutineScope.() -> Unit,
    start: (() -> Unit)? = null,
    finally: (() -> Unit)? = null,
    coroutineContext: CoroutineContext = EmptyCoroutineContext
): Job {
    return pLifeScope.launch(block, start, finally, coroutineContext)
}

/**
 * 运行在子线程
 * */
fun ViewModel.launchIo(block: suspend CoroutineScope.() -> Unit): Job {
    return pLifeScope.launchIo {
        block.invoke(this)
    }
}

/**
 * 运行在主线程
 * */
fun ViewModel.launchMain(block: suspend CoroutineScope.() -> Unit): Job {
    return pLifeScope.launchMain {
        block.invoke(this)
    }
}

/**
 * 运行在默认线程
 * */
fun ViewModel.launchDefault(block: suspend CoroutineScope.() -> Unit): Job {
    return pLifeScope.launchDefault {
        block.invoke(this)
    }
}

/**
 * 运行协程范围
 * */
fun Lifecycle.launch(block: suspend CoroutineScope.() -> Unit): Job {
    return pLifeScope.launch(block)
}

/**
 * 运行协程范围
 * */
fun Lifecycle.launch(
    block: suspend CoroutineScope.() -> Unit,
    start: (() -> Unit)? = null,
    finally: (() -> Unit)? = null,
    coroutineContext: CoroutineContext = EmptyCoroutineContext
): Job {
    return pLifeScope.launch(block, start, finally, coroutineContext)
}

/**
 * 运行在子线程
 * */
fun Lifecycle.launchIo(block: suspend CoroutineScope.() -> Unit): Job {
    return pLifeScope.launchIo {
        block.invoke(this)
    }
}

/**
 * 运行在主线程
 * */
fun Lifecycle.launchMain(block: suspend CoroutineScope.() -> Unit): Job {
    return pLifeScope.launchMain {
        block.invoke(this)
    }
}

/**
 * 运行在默认线程
 * */
fun Lifecycle.launchDefault(block: suspend CoroutineScope.() -> Unit): Job {
    return pLifeScope.launchDefault {
        block.invoke(this)
    }
}

/**
 * 运行协程范围
 * */
fun LifecycleOwner.launch(block: suspend CoroutineScope.() -> Unit): Job {
    return pLifeScope.launch(block)
}

/**
 * 运行协程范围
 * */
fun LifecycleOwner.launch(
    block: suspend CoroutineScope.() -> Unit,
    start: (() -> Unit)? = null,
    finally: (() -> Unit)? = null,
    coroutineContext: CoroutineContext = EmptyCoroutineContext
): Job {
    return pLifeScope.launch(block, start, finally, coroutineContext)
}

/**
 * 运行在子线程
 * */
fun LifecycleOwner.launchIo(block: suspend CoroutineScope.() -> Unit): Job {
    return pLifeScope.launchIo {
        block.invoke(this)
    }
}

/**
 * 运行在主线程
 * */
fun LifecycleOwner.launchMain(block: suspend CoroutineScope.() -> Unit): Job {
    return pLifeScope.launchMain {
        block.invoke(this)
    }
}

/**
 * 运行在默认线程
 * */
fun LifecycleOwner.launchDefault(block: suspend CoroutineScope.() -> Unit): Job {
    return pLifeScope.launchDefault {
        block.invoke(this)
    }
}