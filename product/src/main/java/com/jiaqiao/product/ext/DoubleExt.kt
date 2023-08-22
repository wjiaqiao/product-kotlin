package com.jiaqiao.product.ext

import com.jiaqiao.product.util.NumUtil


/**
 * 对除数0处理，除0返回0
 * */
fun Double.toDiv(other: Int): Double {
    if (other == 0) {
        return 0.0
    }
    return this / other
}


fun Double.toDiv(other: Double): Double {
    if (other == 0.0) {
        return 0.0
    }
    return this / other
}

fun Double.toDiv(other: Long): Double {
    if (other == 0L) {
        return 0.0
    }
    return this / other
}

fun Double.toDiv(other: Float): Double {
    if (other == 0f) {
        return 0.0
    }
    return this / other
}

/**
 * Double转换成四舍五入的int数据
 * */
fun Double.toRoundingInt() = this.toDecimals0().toIntDef(0)

/**
 * 转换成string格式，最多保留几位小数
 * [isHalfUp] 是否启动四舍五入
 * */
fun Double.toDecimals0(isHalfUp: Boolean = true) = NumUtil.format(this, 0, isHalfUp)
fun Double.toDecimals1(isHalfUp: Boolean = true) = NumUtil.format(this, 1, isHalfUp)
fun Double.toDecimals2(isHalfUp: Boolean = true) = NumUtil.format(this, 2, isHalfUp)
fun Double.toDecimals3(isHalfUp: Boolean = true) = NumUtil.format(this, 3, isHalfUp)
fun Double.toDecimals4(isHalfUp: Boolean = true) = NumUtil.format(this, 4, isHalfUp)
fun Double.toDecimals5(isHalfUp: Boolean = true) = NumUtil.format(this, 5, isHalfUp)
fun Double.toDecimals6(isHalfUp: Boolean = true) = NumUtil.format(this, 6, isHalfUp)
fun Double.toDecimals7(isHalfUp: Boolean = true) = NumUtil.format(this, 7, isHalfUp)
fun Double.toDecimals8(isHalfUp: Boolean = true) = NumUtil.format(this, 8, isHalfUp)


/**
 * 保留小数位
 * */
fun Double.toDecimals0Float() = toDecimals0(false).toDoubleDef(0.0)
fun Double.toDecimals1Float() = toDecimals1(false).toDoubleDef(0.0)
fun Double.toDecimals2Float() = toDecimals2(false).toDoubleDef(0.0)
fun Double.toDecimals3Float() = toDecimals3(false).toDoubleDef(0.0)
fun Double.toDecimals4Float() = toDecimals4(false).toDoubleDef(0.0)
fun Double.toDecimals5Float() = toDecimals5(false).toDoubleDef(0.0)
fun Double.toDecimals6Float() = toDecimals6(false).toDoubleDef(0.0)
fun Double.toDecimals7Float() = toDecimals7(false).toDoubleDef(0.0)
fun Double.toDecimals8Float() = toDecimals8(false).toDoubleDef(0.0)

/**
 * 检测Float值得合规性，是否处于min和max之间
 * */
fun Double.compliance(min: Double, max: Double): Double {
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