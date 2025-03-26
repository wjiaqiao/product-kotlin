package com.jiaqiao.product.ext

import com.jiaqiao.product.config.PlogConfig

/**
 * 运行在lib中的catch
 */

/**
 * 将Throwable错误日志输出至errorTag中
 * */
inline fun <R> runPlogCatch(block: () -> R): Result<R> {
    return try {
        Result.success(block())
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
        Result.failure(e)
    }
}
