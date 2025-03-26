package com.jiaqiao.product.ext

import android.graphics.Paint
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.widget.TextView

/**
 * 设置文本样式为默认
 * */
fun TextView.typefaceNormal(): TextView {
    this.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
    return this
}

/**
 * 设置文本样式为加粗
 * */
fun TextView.typefaceBold(): TextView {
    this.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
    return this
}

/**
 * 设置文本样式为加粗并斜体
 * */
fun TextView.typefaceBoldItalic(): TextView {
    this.typeface = Typeface.defaultFromStyle(Typeface.BOLD_ITALIC)
    return this
}

/**
 * 设置文本样式为斜体
 * */
fun TextView.typefaceItalic(): TextView {
    this.typeface = Typeface.defaultFromStyle(Typeface.ITALIC)
    return this
}

/**
 * 设置文字大小
 * [px] px像素值
 * */
fun TextView.textSizePx(px: Int): TextView {
    return textSizePx(px.toFloat())
}

/**
 * 设置文字大小
 * [dp]  dp值
 * */
fun TextView.textSizeDp(dp: Int): TextView {
    return textSizeDp(dp.toFloat())
}

/**
 * 设置文字大小
 * [sp] sp值
 * */
fun TextView.textSizeSp(sp: Int): TextView {
    return textSizeSp(sp.toFloat())
}

/**
 * 设置文字大小
 * [px] px像素值
 * */
fun TextView.textSizePx(px: Float): TextView {
    this.setTextSize(TypedValue.COMPLEX_UNIT_PX, px)
    return this
}

/**
 * 设置文字大小
 * [dp]  dp值
 * */
fun TextView.textSizeDp(dp: Float): TextView {
    this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dp)
    return this
}

/**
 * 设置文字大小
 * [sp] sp值
 * */
fun TextView.textSizeSp(sp: Float): TextView {
    this.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
    return this
}

/**
 * TextView的onTextChanged回调
 * */
fun TextView.onTextChanged(otcInterval: (CharSequence, Int, Int, Int) -> Unit): TextView {
    val tw = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s.notNull()) {
                otcInterval.invoke(s!!, start, before, count)
            }
        }

        override fun afterTextChanged(s: Editable?) {

        }
    }
    addTextChangedListener(tw)
    return this
}

/**
 * TextView的beforeTextChanged回调
 * */
fun TextView.beforeTextChanged(btcInterval: (CharSequence, Int, Int, Int) -> Unit): TextView {
    val tw = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            if (s.notNull()) {
                btcInterval.invoke(s!!, start, count, after)
            }
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {

        }

    }
    addTextChangedListener(tw)
    return this
}

/**
 * TextView的beforeTextChanged回调
 * */
fun TextView.afterTextChanged(atcInterval: (Editable) -> Unit): TextView {
    val tw = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            if (s.notNull()) {
                atcInterval.invoke(s!!)
            }
        }

    }
    addTextChangedListener(tw)
    return this
}



/**
 * 设置字体加粗
 * [isBold]  字体是否加粗，默认true
 * @return 返回TextView对象
 */
fun TextView.bold(isBold: Boolean = true): TextView {
    paint.isFakeBoldText = isBold
    return this
}

/**
 * 设置字体显示下划线
 * @return 返回TextView对象
 */
fun TextView.underline(): TextView {
    paint.flags = Paint.UNDERLINE_TEXT_FLAG //下划线
    paint.isAntiAlias = true //抗锯齿
    return this
}