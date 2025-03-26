package com.jiaqiao.product.ext

import java.util.Locale


/**
 * 将byteArray转成二进制string
 * */
fun ByteArray.toBinaryString(): String {
    val stringBuffer = StringBuffer()
    for (bt in this) {
        stringBuffer.append(bt.toBinaryString())
    }
    return stringBuffer.toString()
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
