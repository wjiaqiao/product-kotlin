package com.jiaqiao.product.ext


/**
 *
 * 判断list中是否包含position项
 *
 * [position] 序号
 * */
fun <T> Collection<T>.hasPosition(position: Int): Boolean {
    return position in indices
}