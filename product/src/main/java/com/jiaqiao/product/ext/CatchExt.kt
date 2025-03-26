package com.jiaqiao.product.ext

import com.jiaqiao.product.config.PlogConfig

/**
 * 运行在lib中的catch
 */
inline fun <R> runPCatch(block: () -> R): R? {
    return try {
        block()
    } catch (e: Throwable) {
        if (PlogConfig.debug && PlogConfig.errorTag.notNullAndEmpty()) {
            if (e.javaClass.toString().contains("kotlinx.coroutines.JobCancellationException")
                    .isTrue()
//                || e.javaClass.toString().contains("kotlinx.coroutines.TimeoutCancellationException")
//                    .isTrue()
            ) {

            } else {
                e.plogE()
            }
        }
        null
    }
}