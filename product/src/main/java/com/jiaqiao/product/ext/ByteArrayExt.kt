package com.jiaqiao.product.ext


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
