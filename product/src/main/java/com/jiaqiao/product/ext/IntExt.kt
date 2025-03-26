package com.jiaqiao.product.ext

import kotlin.math.pow

/**
 * 对除数0处理，除0返回0
 * */
fun Int.toDiv(other: Int): Int {
    if (other == 0) {
        return 0
    }
    return this / other
}


fun Int.toDiv(other: Double): Double {
    if (other == 0.0) {
        return 0.0
    }
    return this / other
}


fun Int.toDiv(other: Float): Float {
    if (other == 0f) {
        return 0f
    }
    return this / other
}

fun Int.toDiv(other: Long): Long {
    if (other == 0L) {
        return 0L
    }
    return this / other
}


/**
 * 检测int值得合规性，是否处于min和max之间
 * */
fun Int.compliance(min: Int, max: Int): Int {
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
fun Int.pow(pow: Int): Float {
    return this.toFloat().pow(pow)
}

/**
 * 返回数据的幂次方
 * */
fun Int.pow(pow: Float): Float {
    return this.toFloat().pow(pow)
}


/**
 * 一个数值ByteArray, int最大值是4位
 * */
fun Int.toByteArray(maxWidth: Int): ByteArray {
    if (maxWidth > 4 || this.isNull()) {
        return byteArrayOf()
    }
    val byteArray = ByteArray(maxWidth)
    for (i in 0 until maxWidth) {
        byteArray[i] = ((this shr (maxWidth - 1 - i) * 8) and 0xff).toByte()
    }
    return byteArray
}

/**
 * int转 16 进制的string
 * */
fun Int.toHex(maxLength: Int = 1) = this.toByteArray(maxLength).toHex()

/**
 * int转 2 进制的string
 * */
fun Int.toBinary(maxLength: Int = 1) = this.toByteArray(maxLength).toBinaryString()


