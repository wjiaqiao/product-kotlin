package com.jiaqiao.product.util

import java.math.RoundingMode
import java.text.NumberFormat

/**
 * number数据的格式化工具
 */
object NumUtil {


    fun format(value: Float, fractionDigits: Int, isHalfUp: Boolean): String {
        return format(
            value.toDouble(),
            fractionDigits,
            isHalfUp
        )
    }

    fun format(value: Double, fractionDigits: Int, isHalfUp: Boolean): String {
        return format(
            value,
            false,
            1,
            fractionDigits,
            isHalfUp
        )
    }

    private fun format(
        value: Double,
        isGrouping: Boolean,
        minIntegerDigits: Int,
        fractionDigits: Int,
        isHalfUp: Boolean
    ): String {
        val nf = NumberFormat.getInstance()
        nf.isGroupingUsed = isGrouping
        nf.roundingMode = if (isHalfUp) RoundingMode.HALF_UP else RoundingMode.DOWN
        nf.minimumIntegerDigits = minIntegerDigits
        nf.minimumFractionDigits = fractionDigits
        nf.maximumFractionDigits = fractionDigits
        return nf.format(value)
    }

}