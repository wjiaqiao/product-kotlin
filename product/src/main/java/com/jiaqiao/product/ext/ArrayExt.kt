package com.jiaqiao.product.ext

import com.jiaqiao.product.util.ProductUtil


/**
 *
 * 判断Array中是否包含position项
 *
 * [position] 序号
 * */
fun <T> Array<out T>.hasPosition(position: Int): Boolean {
    return ProductUtil.hasPosition(size, position)
}