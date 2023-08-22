package com.jiaqiao.product.util

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object ProductMoshi {

    /**
     * moshi解析器对象
     * */
    val moshi: Moshi by lazy {
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())//使用kotlin反射处理，要加上这个
            .build()
    }

}