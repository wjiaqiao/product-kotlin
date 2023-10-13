package com.jiaqiao.product.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.remove
import com.jiaqiao.product.base.PCoroutineScope
import com.jiaqiao.product.ext.isNull
import com.jiaqiao.product.ext.plogE
import com.jiaqiao.product.ext.runMainLooper
import kotlinx.coroutines.*
import java.io.Closeable
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


/**
 * 此类用于开启协程，并自动捕获异常
 *
 * 在FragmentActivity、 ViewModel环境下，使用 pLifeScope.launch 方式开启协程，会在页面销毁时，自动关闭协程  (注意：这里的pLifeScope是变量，不是类名)
 *
 * 其它环境下，需要拿到 [PLifeScope.launch]方法的返回值后，手动调用[Job.cancel]方法关闭协程
 *
 */
class PLifeScope(var autoRecreate: Boolean = false) : Closeable {

    private var lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
    private var lifecycle: Lifecycle? = null
    private var key = Int.MIN_VALUE
    private var isClose = false
    private val nullJob by lazy { Job() }

    constructor(
        lifecycle: Lifecycle,
        lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
        autoRecreate: Boolean = false,
        key: Int = Int.MIN_VALUE
    ) : this() {
        this.lifecycle = lifecycle
        this.lifeEvent = lifeEvent
        this.autoRecreate = autoRecreate
        this.key = key
        addObserver()
    }

    private fun addObserver() {
        if (lifecycle.isNull()) {
            return
        }
        runMainLooper {
            try {
                lifecycle?.addObserver(object : LifecycleEventObserver {
                    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                        if (lifeEvent == event) {
                            if (key != Int.MIN_VALUE) {
                                remove(key)
                            }
                            close()
                            lifecycle?.removeObserver(this)
                        }
                    }
                })
            } catch (thr: Throwable) {
                thr.plogE()
            }
        }
    }

    private var coroutineScope = PCoroutineScope()


    fun launchIo(block: suspend CoroutineScope.() -> Unit): Job {
        return launch(block, null, coroutineContext = Dispatchers.IO)
    }

    fun launchMain(block: suspend CoroutineScope.() -> Unit): Job {
        return launch(block, null, coroutineContext = Dispatchers.Main)
    }

    fun launchDefault(block: suspend CoroutineScope.() -> Unit): Job {
        return launch(block, null, coroutineContext = Dispatchers.Default)
    }

    fun launch(block: suspend CoroutineScope.() -> Unit): Job {
        return launch(block, null)
    }

    /**
     * [block]    协程代码块，运行在主线程
     * [error]  异常回调，运行在主线程
     * [start]  协程开始回调，运行在主线程
     * [finally] 协程结束回调，不管成功/失败，都会回调，运行在主线程
     */
    fun launch(
        block: suspend CoroutineScope.() -> Unit,
        start: (() -> Unit)? = null,
        finally: (() -> Unit)? = null,
        coroutineContext: CoroutineContext = EmptyCoroutineContext
    ): Job {
        if (isClose && !autoRecreate) {
            return nullJob
        }
        if (isClose) isClose = false
        return coroutineScope.launch(context = coroutineContext) {
            try {
                coroutineScope {
                    if (isActive) {
                        start?.invoke()
                    }
                    if (isActive) {
                        block()
                    }
                }
            } catch (thr: Throwable) {
                if (thr::class.java.toString()
                        .contains("kotlinx.coroutines.JobCancellationException", true)
                ) {

                } else {
                    thr.plogE()
                }
            } finally {
                if (isActive) {
                    finally?.invoke()
                }
            }
        }.apply {
            coroutineScope.addJob(this)
        }
    }

    /**
     * 关闭作用域中的所有协程
     * */
    override fun close() {
        isClose = true
        try {
            coroutineScope.close()
        } catch (thr: Throwable) {
            thr.plogE()
        }
        try {
            if (autoRecreate) {
                coroutineScope.cancleChildren()
            } else {
                coroutineScope.cancel()
            }
        } catch (thr: Throwable) {
            thr.plogE()
        }
    }
}
