package com.jiaqiao.product.widget

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import com.jiaqiao.product.R
import com.jiaqiao.product.widget.shape.ShapeTextView

open class ProductTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ShapeTextView(context, attrs, defStyleAttr) {

    var bold = -1f
        set(value) {
            if (value != field) {
                field = value
                updateTextBold()
            }
        }
    var underline = false
        set(value) {
            if (value != field) {
                field = value
                updateLineStyle()
            }
        }
    var strikethrough = false
        set(value) {
            if (value != field) {
                field = value
                updateLineStyle()
            }
        }

    private var defFlags = 0

    init {
        paint.isAntiAlias = true //抗锯齿
        defFlags = paint.flags
        attrs?.let {
            context.obtainStyledAttributes(it, R.styleable.ProductTextView)?.let { array ->

                bold = array.getFloat(R.styleable.ProductTextView_ptv_text_bold, 0f)
                underline = array.getBoolean(R.styleable.ProductTextView_ptv_text_underline, underline)
                strikethrough =
                    array.getBoolean(R.styleable.ProductTextView_ptv_text_strikethrough, strikethrough)

                array.recycle()
            }
        }

    }

    /**
     * 设置下划线、中划线状态
     * */
    private fun updateLineStyle() {
        var newFlags = defFlags
        if (underline) {
            newFlags = newFlags or Paint.UNDERLINE_TEXT_FLAG
        }
        if (strikethrough) {
            newFlags = newFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
        paint.flags = newFlags
        invalidate()
    }

    /**
     * 刷新文字粗细程度，字宽在0~1.0f之间
     * */
    private fun updateTextBold() {
        paint.style = Paint.Style.FILL_AND_STROKE
        val relBold = when {
            bold < 0 -> {
                0f
            }
            bold > 1 -> {
                1f
            }
            else -> {
                bold
            }
        }
        paint.strokeWidth = relBold * 1.5f
        textScaleX = 1 + relBold * 0.025f
        invalidate()
    }

}