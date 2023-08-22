package com.jiaqiao.product.ext

/**
 * 强转为boolean类型
 * */
fun Any?.booleanDef(def: Boolean = false): Boolean {
    return if (isNull()) {
        def
    } else {
        try {
            this as Boolean
        } catch (thr: Throwable) {
            def
        }
    }
}

/**
 * 强转为string类型
 * */
fun Any?.stringDef(def: String = ""): String {
    return if (isNull()) {
        def
    } else {
        try {
            this as String
        } catch (thr: Throwable) {
            def
        }
    }
}

/**
 * 强转为int类型
 * */
fun Any?.intDef(def: Int = 0): Int {
    return if (isNull()) {
        def
    } else {
        try {
            this as Int
        } catch (thr: Throwable) {
            def
        }
    }
}

/**
 * 强转为float类型
 * */
fun Any?.floatDef(def: Float = 0f): Float {
    return if (isNull()) {
        def
    } else {
        try {
            this as Float
        } catch (thr: Throwable) {
            def
        }
    }
}

/**
 * 强转为double类型
 * */
fun Any?.doubleDef(def: Double = 0.0): Double {
    return if (isNull()) {
        def
    } else {
        try {
            this as Double
        } catch (thr: Throwable) {
            def
        }
    }
}