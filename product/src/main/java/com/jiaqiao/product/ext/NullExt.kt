package com.jiaqiao.product.ext

/**
 * 判断是否为空
 * */
fun Any?.isNull(): Boolean {
    return when (this) {
        null -> true
        is List<*> -> this.isEmpty()
        else -> false
    }
}

/**
 *判断是否不为空
 **/
fun Any?.notNull(): Boolean {
    return !this.isNull()
}

/**
 * 判断Collection是否不为空和null
 * */
fun <T> Collection<T>?.notNullAndEmpty(): Boolean {
    return !isNullOrEmpty()
}

/**
 * 判断CharSequence是否不为空和null
 * */
fun CharSequence?.notNullAndEmpty(): Boolean {
    return !isNullOrEmpty()
}

/**
 * 判断CharSequence是否不为空和null
 * */
fun CharSequence?.notNullAndBlank(): Boolean {
    return !isNullOrBlank()
}

/**
 * 判断Map是否不为空和null
 * */
fun <K, V> Map<out K, V>?.notNullAndEmpty(): Boolean {
    return !this.isNullOrEmpty()
}


/**
 * 判断Array是否不为空和null
 * */
fun <T> Array<out T>?.notNullAndEmpty(): Boolean {
    return !this.isNullOrEmpty()
}