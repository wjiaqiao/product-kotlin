package com.jiaqiao.product.ext

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONArray
import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.TypeReference
import com.jiaqiao.product.config.PlogConfig

/**
 * 将对象转换成String类型的json
 * */
fun <T> T?.toFastJsonString(): String {
    return if (this == null) {
        ""
    } else {
        runPlogCatch {
            JSON.toJSONString(this)
        }.getOrDefault("")
    }
}

/**
 * 将string转换成T对象
 * */
fun <T> String?.toFastJsonParse(): T? {
    return if (this == null) {
        null
    } else {
        runPlogCatch {
            JSON.parseObject(this, object : TypeReference<T>() {})
        }.getOrNull()
    }
}

/**
 * 将string转换成JSONObject对象
 * */
fun String?.toJSONObject(): JSONObject? {
    return if (this == null) {
        null
    } else {
        runPlogCatch {
            JSONObject.parseObject(this)
        }.getOrNull()
    }
}

/**
 * 将string转换成JSONArray对象
 * */
fun String?.toJSONArray(): JSONArray? {
    return if (this == null) {
        null
    } else {
        runPlogCatch {
            JSONArray.parseArray(this)
        }.getOrNull()
    }
}