package com.jiaqiao.product.ext

/**
 * 运行在lib中的catch
 */
inline fun <R> runPCatch(block: () -> R): R? {
    return try {
        block()
    } catch (e: Throwable) {
        e.plogE()
        null
    }
}