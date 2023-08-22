package com.jiaqiao.product.ext

import java.math.BigInteger
import kotlin.random.Random

/**
 * 计算列表中的平均值
 * */
@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
@kotlin.jvm.JvmName("avgOfInt")
inline fun <T> Iterable<T>.avgOf(selector: (T) -> Int): Int {
    var sum = 0
    var size = 0
    for (element in this) {
        sum += selector(element)
        size++
    }
    return if (size <= 0) 0 else sum / size
}

/**
 * 计算列表中的平均值
 * */
@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
@kotlin.jvm.JvmName("avgOfLong")
inline fun <T> Iterable<T>.avgOf(selector: (T) -> Long): Long {
    var sum: Long = 0.toLong()
    var size = 0
    for (element in this) {
        sum += selector(element)
        size++
    }
    return if (size <= 0) 0.toLong() else sum / size
}

/**
 * 计算列表中的平均值
 * */
@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
@kotlin.jvm.JvmName("avgOfUInt")
inline fun <T> Iterable<T>.avgOf(selector: (T) -> UInt): UInt {
    var sum: UInt = 0.toUInt()
    var size = 0
    for (element in this) {
        sum += selector(element)
        size++
    }
    return if (size <= 0) 0.toUInt() else sum / size.toUInt()
}

/**
 * 计算列表中的平均值
 * */
@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
@kotlin.jvm.JvmName("avgOfULong")
inline fun <T> Iterable<T>.avgOf(selector: (T) -> ULong): ULong {
    var sum: ULong = 0.toULong()
    var size = 0
    for (element in this) {
        sum += selector(element)
        size++
    }
    return if (size <= 0) 0.toULong() else sum / size.toUInt()
}

/**
 * 计算列表中的平均值
 * */
@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
@kotlin.jvm.JvmName("avgOfFloat")
inline fun <T> Iterable<T>.avgOf(selector: (T) -> Float): Float {
    var sum: Float = 0.toFloat()
    var size = 0
    for (element in this) {
        sum += selector(element)
        size++
    }
    return if (size <= 0) 0.toFloat() else sum / size
}

/**
 * 计算列表中的平均值
 * */
@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
@kotlin.jvm.JvmName("avgOfDouble")
inline fun <T> Iterable<T>.avgOf(selector: (T) -> Double): Double {
    var sum: Double = 0.toDouble()
    var size = 0
    for (element in this) {
        sum += selector(element)
        size++
    }
    return if (size <= 0) 0.toDouble() else sum / size
}

/**
 * 计算列表中的平均值
 * */
@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
@kotlin.jvm.JvmName("avgOfBigInteger")
inline fun <T> Iterable<T>.avgOf(selector: (T) -> BigInteger): BigInteger {
    var sum: BigInteger = 0.toBigInteger()
    var size = 0
    for (element in this) {
        sum += selector(element)
        size++
    }
    return if (size <= 0) 0.toBigInteger() else sum.div(size.toBigInteger())
}


/**
 * 计算列表中的最小、平均、最大值，返回IntArray分别为最小、平均、最大值
 * */
@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
@kotlin.jvm.JvmName("calculateOfIntArray")
inline fun <T> Iterable<T>.calculateOf(selector: (T) -> Int): IntArray {
    val call = intArrayOf(0, 0, 0)
    val iterator = iterator()
    if (iterator.hasNext()) {
        var value = selector(iterator.next())
        var sum = value
        var max = value
        var min = value
        var size = 1
        while (iterator.hasNext()) {
            val v = selector(iterator.next())
            if (min > v) {
                min = v
            }
            if (max < v) {
                max = v
            }
            sum += v
            size++
        }
        call[0] = min
        call[1] = sum / size
        call[2] = max
    }
    return call
}

/**
 * 计算列表中的最小、平均、最大值，返回LongArray分别为最小、平均、最大值
 * */
@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
@kotlin.jvm.JvmName("calculateOfLongArray")
inline fun <T> Iterable<T>.calculateOf(selector: (T) -> Long): LongArray {
    val call = longArrayOf(0, 0, 0)
    val iterator = iterator()
    if (iterator.hasNext()) {
        var value = selector(iterator.next())
        var sum = value
        var max = value
        var min = value
        var size = 1
        while (iterator.hasNext()) {
            val v = selector(iterator.next())
            if (min > v) {
                min = v
            }
            if (max < v) {
                max = v
            }
            sum += v
            size++
        }
        call[0] = min
        call[1] = sum / size
        call[2] = max
    }
    return call
}

/**
 * 计算列表中的最小、平均、最大值，返回UIntArray分别为最小、平均、最大值
 * */
@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
@kotlin.jvm.JvmName("calculateOfUIntArray")
inline fun <T> Iterable<T>.calculateOf(selector: (T) -> UInt): UIntArray {
    val call = uintArrayOf(0.toUInt(), 0.toUInt(), 0.toUInt())
    val iterator = iterator()
    if (iterator.hasNext()) {
        var value = selector(iterator.next())
        var sum = value
        var max = value
        var min = value
        var size = 1
        while (iterator.hasNext()) {
            val v = selector(iterator.next())
            if (min > v) {
                min = v
            }
            if (max < v) {
                max = v
            }
            sum += v
            size++
        }
        call[0] = min
        call[1] = sum / size.toUInt()
        call[2] = max
    }
    return call
}

/**
 * 计算列表中的最小、平均、最大值，返回ULongArray分别为最小、平均、最大值
 * */
@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
@kotlin.jvm.JvmName("calculateOfULongArray")
inline fun <T> Iterable<T>.calculateOf(selector: (T) -> ULong): ULongArray {
    val call = ulongArrayOf(0.toULong(), 0.toULong(), 0.toULong())
    val iterator = iterator()
    if (iterator.hasNext()) {
        var value = selector(iterator.next())
        var sum = value
        var max = value
        var min = value
        var size = 1
        while (iterator.hasNext()) {
            val v = selector(iterator.next())
            if (min > v) {
                min = v
            }
            if (max < v) {
                max = v
            }
            sum += v
            size++
        }
        call[0] = min
        call[1] = sum / size.toULong()
        call[2] = max
    }
    return call
}

/**
 * 计算列表中的最小、平均、最大值，返回FloatArray分别为最小、平均、最大值
 * */
@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
@kotlin.jvm.JvmName("calculateOfFloatArray")
inline fun <T> Iterable<T>.calculateOf(selector: (T) -> Float): FloatArray {
    val call = floatArrayOf(0.toFloat(), 0.toFloat(), 0.toFloat())
    val iterator = iterator()
    if (iterator.hasNext()) {
        var value = selector(iterator.next())
        var sum = value
        var max = value
        var min = value
        var size = 1
        while (iterator.hasNext()) {
            val v = selector(iterator.next())
            if (min > v) {
                min = v
            }
            if (max < v) {
                max = v
            }
            sum += v
            size++
        }
        call[0] = min
        call[1] = sum / size
        call[2] = max
    }
    return call
}

/**
 * 计算列表中的最小、平均、最大值，返回DoubleArray分别为最小、平均、最大值
 * */
@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
@kotlin.jvm.JvmName("calculateOfDoubleArray")
inline fun <T> Iterable<T>.calculateOf(selector: (T) -> Double): DoubleArray {
    val call = doubleArrayOf(0.toDouble(), 0.toDouble(), 0.toDouble())
    val iterator = iterator()
    if (iterator.hasNext()) {
        var value = selector(iterator.next())
        var sum = value
        var max = value
        var min = value
        var size = 1
        while (iterator.hasNext()) {
            val v = selector(iterator.next())
            if (min > v) {
                min = v
            }
            if (max < v) {
                max = v
            }
            sum += v
            size++
        }
        call[0] = min
        call[1] = sum / size
        call[2] = max
    }
    return call
}

/**
 * 计算列表中的最小、平均、最大值，返回Array<BigInteger>分别为最小、平均、最大值
 * */
@OptIn(kotlin.experimental.ExperimentalTypeInference::class)
@OverloadResolutionByLambdaReturnType
@kotlin.jvm.JvmName("calculateOfArrayBigInteger")
inline fun <T> Iterable<T>.calculateOf(selector: (T) -> BigInteger): Array<BigInteger> {
    val call = arrayOf(0.toBigInteger(), 0.toBigInteger(), 0.toBigInteger())
    val iterator = iterator()
    if (iterator.hasNext()) {
        var value = selector(iterator.next())
        var sum = value
        var max = value
        var min = value
        var size = 1
        while (iterator.hasNext()) {
            val v = selector(iterator.next())
            if (min > v) {
                min = v
            }
            if (max < v) {
                max = v
            }
            sum += v
            size++
        }
        call[0] = min
        call[1] = sum / size.toBigInteger()
        call[2] = max
    }
    return call
}

/**
 * 获取限定float范围的随机值
 * */
fun ClosedFloatingPointRange<Float>.random(): Float {
    return random(Random)
}

/**
 * 获取限定float范围的随机值
 * [random] 随机对象
 * */
fun ClosedFloatingPointRange<Float>.random(random: Random): Float {
    return random.nextFloat() * (endInclusive - start) + start
}

/**
 * 获取限定Double范围的随机值
 * */
fun ClosedFloatingPointRange<Double>.random(): Double {
    return random(Random)
}

/**
 * 获取限定Double范围的随机值
 * [random] 随机对象
 * */
fun ClosedFloatingPointRange<Double>.random(random: Random): Double {
    return random.nextFloat() * (endInclusive - start) + start
}

/**
 * 获取随机的boolean值
 * */
fun Boolean.random(): Boolean {
    return Random.nextBoolean()
}


