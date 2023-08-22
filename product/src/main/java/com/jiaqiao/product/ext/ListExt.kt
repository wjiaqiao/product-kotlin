package com.jiaqiao.product.ext

import com.jiaqiao.product.util.ProductUtil


/**
 *
 * 判断list中是否包含position项
 *
 * [position] 序号
 * */
fun <T> Collection<T>.hasPosition(position: Int): Boolean {
    return ProductUtil.hasPosition(size,position)
}