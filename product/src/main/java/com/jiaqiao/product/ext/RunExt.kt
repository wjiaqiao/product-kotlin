package com.jiaqiao.product.ext

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

//默认是在Main线程运行
fun <T> run(context: CoroutineContext = Dispatchers.Main, action: suspend () -> T): IAwait<T> {
    return try {
        object : IAwait<T> {
            override suspend fun await(): T {
                return withContext(context) { action.invoke() }
            }
        }
    } catch (t: Throwable) {
        throw t
    }
}

fun <T> runMain(action: suspend () -> T): IAwait<T> {
    return try {
        object : IAwait<T> {
            override suspend fun await(): T {
                return withContext(Dispatchers.Main) { action.invoke() }
            }
        }
    } catch (t: Throwable) {
        throw t
    }
}

fun <T> runIo(action: suspend () -> T): IAwait<T> {
    return try {
        object : IAwait<T> {
            override suspend fun await(): T {
                return withContext(Dispatchers.IO) { action.invoke() }
            }
        }
    } catch (t: Throwable) {
        throw t
    }
}

fun <T> runDefault(action: suspend () -> T): IAwait<T> {
    return try {
        object : IAwait<T> {
            override suspend fun await(): T {
                return withContext(Dispatchers.Default) { action.invoke() }
            }
        }
    } catch (t: Throwable) {
        throw t
    }
}

