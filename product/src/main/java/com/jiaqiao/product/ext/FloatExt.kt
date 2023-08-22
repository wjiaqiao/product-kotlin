package com.jiaqiao.product.ext

import com.jiaqiao.product.util.NumUtil

/**
 * 对除数0处理，除0返回0
 * */
fun Float.toDiv(other: Int): Float {
    if (other == 0) {
        return 0f
    }
    return this / other
}


fun Float.toDiv(other: Double): Double {
    if (other == 0.0) {
        return 0.0
    }
    return this / other
}


fun Float.toDiv(other: Float): Float {
    if (other == 0f) {
        return 0f
    }
    return this / other
}

fun Float.toDiv(other: Long): Float {
    if (other == 0L) {
        return 0f
    }
    return this / other
}


/**
 * float转换成四舍五入的int数据
 * */
fun Float.toRoundingInt(): Int {
    return this.toDecimals0().toIntDef(0)
}

/**
 * 转换成string格式，最多保留几位小数
 * [isHalfUp] 是否启动四舍五入
 * */
fun Float.toDecimals0(isHalfUp: Boolean = true) = NumUtil.format(this, 0, isHalfUp)
fun Float.toDecimals1(isHalfUp: Boolean = true) = NumUtil.format(this, 1, isHalfUp)
fun Float.toDecimals2(isHalfUp: Boolean = true) = NumUtil.format(this, 2, isHalfUp)
fun Float.toDecimals3(isHalfUp: Boolean = true) = NumUtil.format(this, 3, isHalfUp)
fun Float.toDecimals4(isHalfUp: Boolean = true) = NumUtil.format(this, 4, isHalfUp)
fun Float.toDecimals5(isHalfUp: Boolean = true) = NumUtil.format(this, 5, isHalfUp)
fun Float.toDecimals6(isHalfUp: Boolean = true) = NumUtil.format(this, 6, isHalfUp)
fun Float.toDecimals7(isHalfUp: Boolean = true) = NumUtil.format(this, 7, isHalfUp)
fun Float.toDecimals8(isHalfUp: Boolean = true) = NumUtil.format(this, 8, isHalfUp)


/**
 * 保留小数位
 * */
fun Float.toDecimals0Float() = toDecimals0(false).toFloatDef(0f)
fun Float.toDecimals1Float() = toDecimals1(false).toFloatDef(0f)
fun Float.toDecimals2Float() = toDecimals2(false).toFloatDef(0f)
fun Float.toDecimals3Float() = toDecimals3(false).toFloatDef(0f)
fun Float.toDecimals4Float() = toDecimals4(false).toFloatDef(0f)
fun Float.toDecimals5Float() = toDecimals5(false).toFloatDef(0f)
fun Float.toDecimals6Float() = toDecimals6(false).toFloatDef(0f)
fun Float.toDecimals7Float() = toDecimals7(false).toFloatDef(0f)
fun Float.toDecimals8Float() = toDecimals8(false).toFloatDef(0f)


/**
 * 检测Float值得合规性，是否处于min和max之间
 * */
fun Float.compliance(min: Float, max: Float): Float {
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