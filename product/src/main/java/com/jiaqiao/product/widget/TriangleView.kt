package com.jiaqiao.product.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.jiaqiao.product.R
import com.jiaqiao.product.ext.isNull
import com.jiaqiao.product.ext.notNull
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin

/**
 *
 * 绘制三角形，顶角支持圆角绘制
 *
 * */
open class TriangleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    //三角形颜色
    var triangleColor = Color.BLACK
        set(value) {
            field = value
            paint.color = triangleColor
            invalidate()
        }

    //三角形圆角
    var triangleRadius = 0
        set(value) {
            field = value
            resetPath()
            invalidate()
        }
    private val paint by lazy { Paint() } //绘制三角形画笔
    private var path: Path? = null //绘制三角形path

    init {

        attrs?.let {
            val array = context.obtainStyledAttributes(it, R.styleable.TriangleView)
            triangleColor =
                array.getColor(R.styleable.TriangleView_tv_triangle_color, triangleColor)
            triangleRadius = array.getDimensionPixelOffset(
                R.styleable.TriangleView_tv_triangle_radius,
                triangleRadius
            )
            array.recycle()
        }

        paint.isAntiAlias = true
        paint.color = triangleColor

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        resetPath()
    }

    private fun resetPath() {
        if (path.notNull()) {
            path = null
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        if (path.isNull()) {
            val left = paddingLeft
            val top = paddingTop
            val width = right - getLeft() - paddingLeft - paddingRight
            val height = bottom - getTop() - paddingTop - paddingBottom
            path = Path()
            path?.let {
                if (triangleRadius <= 0) {
                    it.moveTo((left + 0).toFloat(), (top + height).toFloat())
                    it.lineTo((left + width).toFloat(), (top + height).toFloat())
                    it.lineTo((left + width / 2).toFloat(), (top + 0).toFloat())
                    it.close()
                } else {
                    it.moveTo((left + 0).toFloat(), (top + height).toFloat())
                    it.lineTo((left + width).toFloat(), (top + height).toFloat())
                    val rad = atan(width / 2 / height.toFloat()).toDouble()
                    val quadHei = triangleRadius * cos(rad)
                    val quadWid = triangleRadius * sin(rad)
                    it.lineTo(
                        (left + width / 2 + quadWid).toFloat(),
                        (top + 0 + quadHei).toFloat()
                    )
                    it.quadTo(
                        (left + width / 2).toFloat(),
                        (top + 0).toFloat(),
                        (left + width / 2 - quadWid).toFloat(),
                        (top + 0 + quadHei).toFloat()
                    )
                    it.close()
                }
            }
        }
        canvas.drawPath(path!!, paint)


    }


}