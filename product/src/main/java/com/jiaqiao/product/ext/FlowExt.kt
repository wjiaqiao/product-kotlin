package com.jiaqiao.product.ext

import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn

/**
 * flow流运行在子线程中
 * */
fun <T> Flow<T>.flowOnIo(): Flow<T> = this.flowOn(Dispatchers.IO)

/**
 * flow流运行在主线程中
 * */
fun <T> Flow<T>.flowOnMain(): Flow<T> = this.flowOn(Dispatchers.Main)

/**
 * flow流运行在默认线程中
 * */
fun <T> Flow<T>.flowOnDefault(): Flow<T> = this.flowOn(Dispatchers.Default)

/**
 * flow流运行在Unconfined线程中
 * */
fun <T> Flow<T>.flowOnUnconfined(): Flow<T> = this.flowOn(Dispatchers.Unconfined)

/**
 * flow流回调在默认线程中
 * */
fun <T> Flow<T>.collectDef(
    lifecycleOwner: LifecycleOwner,
    collector: FlowCollector<T>? = null
): Job {
    return lifecycleOwner.launchDefault {
        if (collector.isNull()) {
            this@collectDef.collect()
        } else {
            this@collectDef.collect(collector!!)
        }
    }
}

/**
 * flow流回调在主线程中
 * */
fun <T> Flow<T>.collectMain(
    lifecycleOwner: LifecycleOwner,
    collector: FlowCollector<T>? = null
): Job {
    return lifecycleOwner.launchMain {
        if (collector.isNull()) {
            this@collectMain.collect()
        } else {
            this@collectMain.collect(collector!!)
        }
    }
}

/**
 * flow流回调在子线程中
 * */
fun <T> Flow<T>.collectIo(
    lifecycleOwner: LifecycleOwner,
    collector: FlowCollector<T>? = null
): Job {
    return lifecycleOwner.launchIo {
        if (collector.isNull()) {
            this@collectIo.collect()
        } else {
            this@collectIo.collect(collector!!)
        }
    }
}


/**
 * flow流添加日志catch错误输出
 * */
fun <T> Flow<T>.catchLog(action: (suspend FlowCollector<T>.(cause: Throwable) -> Unit)? = null): Flow<T> =
    this.catch {
        it.plogE()
        action?.invoke(this, it)
    }


/**
 * flow流添加日志catch错误输出并发送[value]值
 * */
fun <T> Flow<T>.catchEmit(value: T): Flow<T> =
    this.catch {
        it.plogE()
        emit(value)
    }


/**
 * flow流添加日志catch错误输出，并添加IO运行模式
 * */
fun <T> Flow<T>.catchLogOnIo(): Flow<T> =
    this.catchLog().flowOnIo()


/**
 * flow流添加日志catch错误输出并发送[value]值，并添加IO运行模式
 * */
fun <T> Flow<T>.catchEmitOnIo(value: T): Flow<T> =
    this.catchEmit(value).flowOnIo()