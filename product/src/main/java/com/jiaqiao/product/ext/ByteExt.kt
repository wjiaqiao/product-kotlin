package com.jiaqiao.product.ext

import java.util.*

/**
 * byte转二进制字符串，只转换单个byte
 */
fun Byte.toBinaryString(): String {
    return String.format("%8s", Integer.toBinaryString(this.toInt() and 0xFF))
        .replace(" ", "0")
}

/**
 * 将byte数组转换成方便阅读hex数据
 *
 * [isAddNone]  是否添加空格间隔byte
 * @return 返回hex数据
 */
fun ByteArray.toHex(isAddNone: Boolean = true): String {
    val sbuf = StringBuffer()
    for (i in this.indices) {
        if (i != 0 && isAddNone) {
            sbuf.append(" ")
        }
        var temp = Integer.toHexString(this[i].toInt() and 0xFF)
        if (temp.length == 1) {
            temp = "0$temp"
        }
        sbuf.append(temp.uppercase(Locale.getDefault()))
    }
    return sbuf.toString()
}
