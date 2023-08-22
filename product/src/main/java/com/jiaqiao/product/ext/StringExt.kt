package com.jiaqiao.product.ext

fun String.toIntDef(def: Int = 0): Int {
    return this.toIntOrNull() ?: def
}

fun String.toFloatDef(def: Float = 0f): Float {
    return this.toFloatOrNull() ?: def
}

fun String.toDoubleDef(def: Double = 0.0): Double {
    return this.toDoubleOrNull() ?: def
}

fun String.toShortDef(def: Short = 0): Short {
    return this.toShortOrNull() ?: def
}
