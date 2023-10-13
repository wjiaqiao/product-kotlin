package com.jiaqiao.product.util

import com.jiaqiao.product.ext.plog
import com.jiaqiao.product.ext.runPCatch
import java.util.concurrent.*

/**
 * 内嵌线程池，根据手机CPU核心数量确定最大线程数量
 */
object ProductThreadPool {

    //手机cpu数量
    private val CPU_COUNT = Runtime.getRuntime().availableProcessors()

    //核心线程数为手机CPU数量+1
    private val CORE_POOL_SIZE = CPU_COUNT + 1

    //最大线程数为手机CPU数量×2+1
    private val MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1

    //线程活跃时间（单位：秒），超时线程会被回收
    private const val KEEP_ALIVE_TIME = 3L

    private var isRunWaitTreadPool = false

    //线程池
    private val threadPool by lazy {
        ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAXIMUM_POOL_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.SECONDS,
            LinkedBlockingQueue<Runnable>(),
            Executors.defaultThreadFactory(),
            RejectedExecutionHandler { _, _ ->

            }
        )
    }

    //在线程池中运行线程
    fun run(run: () -> Unit) {
        threadPool.execute {
            runPCatch {
                run.invoke()
            }
        }
    }

    //关闭线程池后并尝试终止正在执行的线程
    fun close() {
        kotlin.runCatching {
            threadPool.shutdownNow()
        }
    }

    /**
     * 运行线程并等待执行完成
     * @return 返回false线程运行失败或报错，返回true线程运行完成
     */
    fun runAndWait(list: MutableList<Runnable>): Boolean {
        if (isRunWaitTreadPool || list.isNullOrEmpty()) {
            return false
        }
        val latch = CountDownLatch(list.size)
        isRunWaitTreadPool = true
        list.forEach {
            threadPool.execute {
                try {
                    it.run()
                } catch (thr: Throwable) {
                    thr.plog()
                } finally {
                    latch.countDown()
                }
            }
        }
        return try {
            latch.await() // 等待所有线程完成
            isRunWaitTreadPool = false
            true
        } catch (e: Throwable) {
            e.plog()
            false
        }
    }

}