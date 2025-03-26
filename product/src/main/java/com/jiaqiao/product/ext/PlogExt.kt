package com.jiaqiao.product.ext

import com.jiaqiao.product.config.PlogConfig
import com.jiaqiao.product.util.ProductLog


/**
 * 输出日志堆栈
 * [tag] 前置标签
 * */
fun Any.plogInfo(tag: Any? = null) {
    if (tag.isNull())
        ProductLog.info(PlogConfig.tag1, this.toString()) else ProductLog.info(
        PlogConfig.tag1,
        ("${tag.toString()}:${this}")
    )
}

/**
 * 输出日志
 * [tag] 前置标签
 * */
fun Any.plog(tag: Any? = null) {
    if (tag.isNull())
        ProductLog.log(PlogConfig.tag1, this) else ProductLog.log(
        PlogConfig.tag1,
        ("${tag.toString()}:${this}")
    )
}

/**
 * 输出日志
 * [tag] 前置标签
 * */
fun Any.plog2(tag: Any? = null) {
    if (tag.isNull())
        ProductLog.log(PlogConfig.tag2, this) else ProductLog.log(
        PlogConfig.tag2,
        ("${tag.toString()}:${this}")
    )
}

/**
 * 输出日志
 * [tag] 前置标签
 * */
fun Any.plog3(tag: Any? = null) {
    if (tag.isNull())
        ProductLog.log(PlogConfig.tag3, this) else ProductLog.log(
        PlogConfig.tag3,
        ("${tag.toString()}:${this}")
    )
}

/**
 * 输出日志
 * [tag] 前置标签
 * */
fun Any.plog4(tag: Any? = null) {
    if (tag.isNull())
        ProductLog.log(PlogConfig.tag4, this) else ProductLog.log(
        PlogConfig.tag4,
        ("${tag.toString()}:${this}")
    )
}

/**
 * 输出日志
 * [tag] 前置标签
 * */
fun Any.plog5(tag: Any? = null) {
    if (tag.isNull())
        ProductLog.log(PlogConfig.tag5, this) else ProductLog.log(
        PlogConfig.tag5,
        ("${tag.toString()}:${this}")
    )
}

/**
 * 输出日志
 * [tag] 前置标签
 * */
fun Any.libPlog(tag: Any? = null) {
    if (tag.isNull())
        ProductLog.log(PlogConfig.libTag1, this) else ProductLog.log(
        PlogConfig.libTag1,
        ("${tag.toString()}:${this}")
    )
}

/**
 * 输出日志
 * [tag] 前置标签
 * */
fun Any.libPlog2(tag: Any? = null) {
    if (tag.isNull())
        ProductLog.log(PlogConfig.libTag2, this) else ProductLog.log(
        PlogConfig.libTag2,
        ("${tag.toString()}:${this}")
    )
}

/**
 * 输出日志
 * [tag] 前置标签
 * */
fun Any.libPlog3(tag: Any? = null) {
    if (tag.isNull())
        ProductLog.log(PlogConfig.libTag3, this) else ProductLog.log(
        PlogConfig.libTag3,
        ("${tag.toString()}:${this}")
    )
}

/**
 * 输出日志
 * [tag] 前置标签
 * */
fun Any.libPlog4(tag: Any? = null) {
    if (tag.isNull())
        ProductLog.log(PlogConfig.libTag4, this) else ProductLog.log(
        PlogConfig.libTag4,
        ("${tag.toString()}:${this}")
    )
}

/**
 * 输出日志
 * [tag] 前置标签
 * */
fun Any.libPlog5(tag: Any? = null) {
    if (tag.isNull())
        ProductLog.log(PlogConfig.libTag5, this) else ProductLog.log(
        PlogConfig.libTag5,
        ("${tag.toString()}:${this}")
    )
}

/**
 * 输出日志
 * */
fun Throwable?.plogE() {
    ProductLog.e(this)
}

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