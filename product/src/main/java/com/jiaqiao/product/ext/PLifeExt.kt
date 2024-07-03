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
fun ViewModel.pLaunch(block: suspend CoroutineScope.() -> Unit): Job {
    return pLifeScope.launch(block)
}

/**
 * 运行协程范围
 * */
fun ViewModel.pLaunch(
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
fun ViewModel.pLaunchIo(block: suspend CoroutineScope.() -> Unit): Job {
    return pLifeScope.launchIo {
        block.invoke(this)
    }
}

/**
 * 运行在主线程
 * */
fun ViewModel.pLaunchMain(block: suspend CoroutineScope.() -> Unit): Job {
    return pLifeScope.launchMain {
        block.invoke(this)
    }
}

/**
 * 运行在默认线程
 * */
fun ViewModel.pLaunchDefault(block: suspend CoroutineScope.() -> Unit): Job {
    return pLifeScope.launchDefault {
        block.invoke(this)
    }
}

/**
 * 运行协程范围
 * */
fun Lifecycle.pLaunch(block: suspend CoroutineScope.() -> Unit): Job {
    return pLifeScope.launch(block)
}

/**
 * 运行协程范围
 * */
fun Lifecycle.pLaunch(
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
fun Lifecycle.pLaunchIo(block: suspend CoroutineScope.() -> Unit): Job {
    return pLifeScope.launchIo {
        block.invoke(this)
    }
}

/**
 * 运行在主线程
 * */
fun Lifecycle.pLaunchMain(block: suspend CoroutineScope.() -> Unit): Job {
    return pLifeScope.launchMain {
        block.invoke(this)
    }
}

/**
 * 运行在默认线程
 * */
fun Lifecycle.pLaunchDefault(block: suspend CoroutineScope.() -> Unit): Job {
    return pLifeScope.launchDefault {
        block.invoke(this)
    }
}

/**
 * 运行协程范围
 * */
fun LifecycleOwner.pLaunch(block: suspend CoroutineScope.() -> Unit): Job {
    return pLifeScope.launch(block)
}

/**
 * 运行协程范围
 * */
fun LifecycleOwner.pLaunch(
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
fun LifecycleOwner.pLaunchIo(block: suspend CoroutineScope.() -> Unit): Job {
    return pLifeScope.launchIo {
        block.invoke(this)
    }
}

/**
 * 运行在主线程
 * */
fun LifecycleOwner.pLaunchMain(block: suspend CoroutineScope.() -> Unit): Job {
    return pLifeScope.launchMain {
        block.invoke(this)
    }
}

/**
 * 运行在默认线程
 * */
fun LifecycleOwner.pLaunchDefault(block: suspend CoroutineScope.() -> Unit): Job {
    return pLifeScope.launchDefault {
        block.invoke(this)
    }
}