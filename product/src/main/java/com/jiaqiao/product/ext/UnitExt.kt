package com.jiaqiao.product.ext

import android.util.TypedValue
import com.jiaqiao.product.util.ProductApp

/**
 * dp值转px值
 * */
val Int.dp
    get() = run { this.toFloat().dp }


/**
 * sp值转px值
 * */
val Int.sp
    get() = run { this.toFloat().sp }


/**
 * dp值转px值
 * */
val Float.dp
    get() = this.dpF.toInt()


/**
 * sp值转px值
 * */
val Float.sp
    get() = this.spF.toInt()


/**
 * dp值转px值
 * */
val Double.dp
    get() = run { this.toFloat().dp }


/**
 * sp值转px值
 * */
val Double.sp
    get() = run { this.toFloat().sp }


/**
 * dp值转px值
 * */
val Int.dpF
    get() = run { this.toFloat().dpF }


/**
 * sp值转px值
 * */
val Int.spF
    get() = run { this.toFloat().spF }


/**
 * dp值转px值
 * */
val Float.dpF
    get() = run {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            ProductApp.context.resources.displayMetrics
        )
    }


/**
 * sp值转px值
 * */
val Float.spF
    get() = run {
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this,
            ProductApp.context.resources.displayMetrics
        )
    }


/**
 * dp值转px值
 * */
val Double.dpF
    get() = run { this.toFloat().dpF }


/**
 * sp值转px值
 * */
val Double.spF
    get() = run { this.toFloat().spF }

