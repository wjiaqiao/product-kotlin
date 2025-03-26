package com.jiaqiao.product.ext

import kotlin.random.Random

/**
 * 判断true
 * */
fun Boolean?.isTrue():Boolean{
    return this == true
}

/**
 * 判断false
 * */
fun Boolean?.isFalse():Boolean{
    return !this.isTrue()
}

/**
 * 获取随机的boolean值
 * */
fun Boolean.random(): Boolean {
    return Random.nextBoolean()
}
