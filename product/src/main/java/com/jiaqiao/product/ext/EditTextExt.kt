package com.jiaqiao.product.ext

import android.content.Context
import android.text.InputFilter
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


/**
 * 添加InputFilter过滤器
 * */
fun EditText.addFilters(inputFilters: Array<InputFilter>): EditText {
    val size = filters.size
    if (size > 0) {
        val newFil = arrayOfNulls<InputFilter>(size + inputFilters.size)
        for (i in 0 until size) {
            newFil[i] = filters[i]
        }
        for (i in size until size + inputFilters.size) {
            newFil[i] = inputFilters[i - size]
        }
        filters = newFil
    } else {
        filters = inputFilters
    }
    return this
}

/**
 * 添加InputFilter过滤器
 * */
fun EditText.addFilter(inputFilter: InputFilter): EditText {
    val size = filters.size
    if (size > 0) {
        val newFil = arrayOfNulls<InputFilter>(size + 1)
        for (i in 0 until size) {
            newFil[i] = filters[i]
        }
        newFil[size] = inputFilter
        filters = newFil
    } else {
        filters = arrayOf<InputFilter>(inputFilter)
    }
    return this
}

/**
 * 获取输入框的内容长度
 */
fun EditText.calculateLength(): Int {
    var varLength = 0
    text.toString().toCharArray().forEach {
        /**
         * 增加中文标点范围 ，标点范围有待详细化
         * 中文字符范围0x4e00 0x9fbb
         * */
        varLength += if (it.code in 0x2E80..0xFE4F || it.code in 0xA13F..0xAA40 || it.code >= 0x80) {
            2
        } else {
            1
        }
    }
    return varLength
}


/**
 * 在EditText上展示或隐藏键盘
 * [isShow]  展示或隐藏键盘，默认true
 * @return 返回EditText对象
 */
fun EditText.softInput(isShow: Boolean = true): EditText {
    if (isShow) {
        requestFocus()
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
        inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        if (text.notNullAndEmpty()) {
            setSelection(text.length)
        }
    } else {
        clearFocus()
        (context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            windowToken,
            0
        )
    }
    return this
}

