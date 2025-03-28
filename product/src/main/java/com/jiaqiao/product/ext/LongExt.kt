package com.jiaqiao.product.ext

import com.jiaqiao.product.util.ProductTime
import kotlin.math.pow


/**
 * 对除数0处理，除0返回0
 * */
fun Long.toDiv(other: Int): Long {
    if (other == 0) {
        return 0
    }
    return this / other
}


fun Long.toDiv(other: Double): Double {
    if (other == 0.0) {
        return 0.0
    }
    return this / other
}


fun Long.toDiv(other: Float): Float {
    if (other == 0f) {
        return 0f
    }
    return this / other
}

fun Long.toDiv(other: Long): Long {
    if (other == 0L) {
        return 0L
    }
    return this / other
}

/**
 * 检测int值得合规性，是否处于min和max之间
 * */
fun Long.compliance(min: Long, max: Long): Long {
    return if (min < max) {
        if (this < min) {
            min
        } else if (this > max) {
            max
        } else {
            this
        }
    } else {
        if (this < max) {
            max
        } else if (this > min) {
            min
        } else {
            this
        }
    }
}

/**
 * 返回数据的幂次方
 * */
fun Long.pow(pow: Int): Float {
    return this.toFloat().pow(pow)
}

/**
 * 返回数据的幂次方
 * */
fun Long.pow(pow: Float): Float {
    return this.toFloat().pow(pow)
}

fun Long.toProductTime1(): String {
    return ProductTime.time1(this)
}

fun Long.toProductTime2(): String {
    return ProductTime.time2(this)
}

fun Long.toProductTime3(): String {
    return ProductTime.time3(this)
}

fun Long.toProductTime4(): String {
    return ProductTime.time4(this)
}

fun Long.toProductTime5(): String {
    return ProductTime.time5(this)
}