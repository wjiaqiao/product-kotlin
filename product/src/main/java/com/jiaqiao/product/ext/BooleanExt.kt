package com.jiaqiao.product.ext

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
