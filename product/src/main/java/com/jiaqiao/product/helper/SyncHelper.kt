package com.jiaqiao.product.helper

import com.jiaqiao.product.ext.libPlog
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.resume

object SyncHelper {

    /**
     * 同步转异步并增加超时操作
     */
    private suspend fun timoutAndSncy(str: String): Int? {
        return try {
            runBlocking {
                withTimeout(1000 * 2) {
                    suspendCancellableCoroutine { canCor ->
                        var back = false
                        if (!back) {
                            back = true
                            canCor.resume(1)
                        }
                    }
                }
            }
        } catch (thr: Throwable) {
            thr.libPlog()
            null
        }

    }
}