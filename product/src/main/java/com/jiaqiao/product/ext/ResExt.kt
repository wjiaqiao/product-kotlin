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
    return runPlogCatch {
        ProductApp.context.getString(this)
    }.getOrNull() ?: ""
}

/**
 * 获取res资源中的color内容
 * */
fun Int.resColor(): Int {
    return runPlogCatch {
        ContextCompat.getColor(ProductApp.context, this)
    }.getOrNull() ?: Color.WHITE
}

/**
 * 获取res资源中的Drawable
 * */
fun Int.resDrawable(): Drawable? {
    return runPlogCatch {
        ProductApp.context.resources.getDrawable(this, null)
    }.getOrNull()
}

/**
 * 获取res资源中的Bitmap，注意只有图片资源才能获取bitmap，shape资源只能使用drawable获取
 * */
fun Int.resBitmap(): Bitmap? {
    return runPlogCatch {
        BitmapFactory.decodeResource(ProductApp.context.resources,this)
    }.getOrNull()
}