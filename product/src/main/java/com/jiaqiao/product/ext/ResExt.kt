package com.jiaqiao.product.ext

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.jiaqiao.product.util.ProductApp

/**
 * 获取res资源中的string内容
 * */
fun Int.resString(): String {
    return kotlin.runCatching {
        ProductApp.context.getString(this)
    }.onFailure {
        it.plogE()
    }.getOrNull() ?: ""
}

/**
 * 获取res资源中的color内容
 * */
fun Int.resColor(): Int {
    return kotlin.runCatching {
        ContextCompat.getColor(ProductApp.context, this)
    }.onFailure {
        it.plogE()
    }.getOrNull() ?: Color.WHITE
}

/**
 * 获取res资源中的Drawable
 * */
fun Int.resDrawable(): Drawable? {
    return kotlin.runCatching {
        ProductApp.context.resources.getDrawable(this, null)
    }.onFailure {
        it.plogE()
    }.getOrNull()
}

/**
 * 获取res资源中的Bitmap，注意只有图片资源才能获取bitmap，shape资源只能使用drawable获取
 * */
fun Int.resBitmap(): Bitmap? {
    return kotlin.runCatching {
        BitmapFactory.decodeResource(ProductApp.context.resources,this)
    }.onFailure {
        it.plogE()
    }.getOrNull()
}