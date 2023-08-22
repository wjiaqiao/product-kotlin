package com.jiaqiao.product.ext

import com.jiaqiao.product.config.PlogConfig
import com.jiaqiao.product.util.ProductMoshi


/**
 * 将对象转换成String类型的json
 * */
inline fun <reified T> T?.toMoshiString(): String {
    return if (this == null) {
        ""
    } else {
        kotlin.runCatching {
            ProductMoshi.moshi.adapter(T::class.java).toJson(this)
        }.onFailure {
            if (PlogConfig.debug) {
                it.plogE()
            }
        }.getOrDefault("")
    }
}

/**
 * 将string转换成T对象
 * */
inline fun <reified T> String?.toMoshiParse(): T? {
    return if (this == null) {
        null
    } else {
        try {
            ProductMoshi.moshi.adapter(T::class.java).fromJson(this!!)
        } catch (thr: Throwable) {
            if (PlogConfig.debug) {
                thr.plogE()
            }
            null
        }
    }
}