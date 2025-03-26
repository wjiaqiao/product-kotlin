package com.jiaqiao.product.ext

/**
 * byte转二进制字符串，只转换单个byte
 */
fun Byte.toBinaryString(): String {
    return String.format("%8s", Integer.toBinaryString(this.toInt() and 0xFF))
        .replace(" ", "0")
}
