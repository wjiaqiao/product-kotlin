package com.jiaqiao.product.ext

import android.graphics.Typeface
import android.util.TypedValue
import android.widget.TextView

/**
 * 设置文本样式为默认
 * */
fun TextView.typefaceNormal(): TextView {
    this.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
    return this
}

/**
 * 设置文本样式为加粗
 * */
fun TextView.typefaceBold(): TextView {
    this.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
    return this
}

/**
 * 设置文本样式为加粗并斜体
 * */
fun TextView.typefaceBoldItalic(): TextView {
    this.typeface = Typeface.defaultFromStyle(Typeface.BOLD_ITALIC)
    return this
}

/**
 * 设置文本样式为斜体
 * */
fun TextView.typefaceItalic(): TextView {
    this.typeface = Typeface.defaultFromStyle(Typeface.ITALIC)
    return this
}

/**
 * 设置文字大小
 * [px] px像素值
 * */
fun TextView.textSizePx(px: Int): TextView {
    return textSizePx(px.toFloat())
}

/**
 * 设置文字大小
 * [dp]  dp值
 * */
fun TextView.textSizeDp(dp: Int): TextView {
    return textSizeDp(dp.toFloat())
}

/**
 * 设置文字大小
 * [sp] sp值
 * */
fun TextView.textSizeSp(sp: Int): TextView {
    return textSizeSp(sp.toFloat())
}

/**
 * 设置文字大小
 * [px] px像素值
 * */
fun TextView.textSizePx(px: Float): TextView {
    this.setTextSize(TypedValue.COMPLEX_UNIT_PX, px)
    return this
}

/**
 * 设置文字大小
 * [dp]  dp值
 * */
fun TextView.textSizeDp(dp: Float): TextView {
    this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dp)
    return this
}

/**
 * 设置文字大小
 * [sp] sp值
 * */
fun TextView.textSizeSp(sp: Float): TextView {
    this.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
    return this
}