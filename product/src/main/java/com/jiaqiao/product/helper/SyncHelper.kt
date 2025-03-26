package com.jiaqiao.product.helper

import com.jiaqiao.product.ext.launchIo
import com.jiaqiao.product.ext.libPlog
import com.jiaqiao.product.util.CoroutineScopeUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.resume

object SyncHelper {

    /**
     * 同步转异步并增加超时操作
     */
    private suspend fun timeoutAndSync(str: String): Int? {
        return try {
            runBlocking {
                withTimeout(1000 * 2) {
                    suspendCancellableCoroutine { canCor ->
                        canCor.resume(1)
                    }
                }
            }
        } catch (thr: Throwable) {
            thr.libPlog()
            null
        }
    }

    /**
     * 协程并发操作示例
     */
    private fun concurrent() {
        CoroutineScopeUtil.createIoScope().launchIo {

            val run1 = this.async(Dispatchers.IO) {
                delay(1000)
            }
            val run2 = this.async(Dispatchers.IO) {
                delay(1000 * 2)
            }
            val run3 = this.async(Dispatchers.IO) {
                delay(1000)
            }

            val result1 = run1.await()
            val result2 = run2.await()
            val result3 = run3.await()
            //3个子线程协程执行完成，result为返回的结果

        }
    }

}