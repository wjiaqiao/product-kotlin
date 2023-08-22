package com.jiaqiao.product.ext

import com.jiaqiao.product.util.ProductApp
import com.jiaqiao.product.util.ProductUiUtil


/**
 * 显示toast提醒
 * */
fun String.toast() {
    ProductUiUtil.toast(ProductApp.context, this)
}

/**
 * 长时间显示toast提醒
 * */
fun String.toastLong() {
    ProductUiUtil.toastLong(ProductApp.context, this)
}

/**
 * 显示toast提醒
 * */
fun Int.toast() {
    ProductUiUtil.toast(ProductApp.context, this)
}

/**
 * 长时间显示toast提醒
 * */
fun Int.toastLong() {
    ProductUiUtil.toastLong(ProductApp.context, this)
}