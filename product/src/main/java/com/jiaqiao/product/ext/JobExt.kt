package com.jiaqiao.product.ext

import kotlinx.coroutines.Job

/**
 * 判断是否cancle，减少重复cancle的次数
 * */
fun Job?.toProduceCancel() {
    if (this.notNull() && this?.isCancelled.isFalse()) {
        this?.cancel()
    }
}