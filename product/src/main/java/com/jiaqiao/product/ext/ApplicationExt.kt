package com.jiaqiao.product.ext

import android.app.Application
import com.jiaqiao.product.util.ProductUtil

/**
 * 判断当前Application所属进程是否是主进程
 * */
fun Application.isMainProcess(): Boolean {
    return ProductUtil.isMainProcess(this)
}