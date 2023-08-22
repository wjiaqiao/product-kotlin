package com.jiaqiao.product.ext

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONArray
import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.TypeReference
import com.jiaqiao.product.config.PlogConfig

/**
 * 将对象转换成String类型的json
 * */
inline fun <T> T?.toFastJsonString(): String {
    return if (this == null) {
        ""
    } else {
        kotlin.runCatching {
            JSON.toJSONString(this)
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
inline fun <T> String?.toFastJsonParse(): T? {
    return if (this == null) {
        null
    } else {
        try {
            JSON.parseObject(this, object : TypeReference<T>() {})
        } catch (thr: Throwable) {
            if (PlogConfig.debug) {
                thr.plogE()
            }
            null
        }
    }
}

/**
 * 将string转换成JSONObject对象
 * */
inline fun String?.toJSONObject(): JSONObject? {
    return if (this == null) {
        null
    } else {
        try {
            JSONObject.parseObject(this)
        } catch (thr: Throwable) {
            if (PlogConfig.debug) {
                thr.plogE()
            }
            null
        }
    }
}

/**
 * 将string转换成JSONArray对象
 * */
inline fun String?.toJSONArray(): JSONArray? {
    return if (this == null) {
        null
    } else {
        try {
            JSONArray.parseArray(this)
        } catch (thr: Throwable) {
            if (PlogConfig.debug) {
                thr.plogE()
            }
            null
        }
    }
}