package com.jiaqiao.product.ext

import kotlinx.coroutines.Job

/**
 * 判断是否cancel，减少重复cancel的次数
 * */
fun Job?.toProduceCancel() {
    if (this.notNull() && this?.isCancelled.isFalse()) {
        this?.cancel()
    }
}