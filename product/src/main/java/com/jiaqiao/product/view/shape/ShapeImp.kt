package com.jiaqiao.product.view.shape

import android.content.Context
import android.util.AttributeSet

open interface ShapeImp {

    fun init(context: Context, attrs: AttributeSet?)

    fun setBackgroundDrawable()

    fun getShapeType(): Int

    fun setShapeType(shapeType: Int)

    fun getRadius(): Int

    fun setRadius(radius: Int)

    fun getLeftTopRadius(): Int

    fun setLeftTopRadius(leftTopRadius: Int)

    fun getRightTopRadius(): Int

    fun setRightTopRadius(rightTopRadius: Int)

    fun getLeftBottomRadius(): Int

    fun setLeftBottomRadius(leftBottomRadius: Int)

    fun getRightBottomRadius(): Int

    fun setRightBottomRadius(rightBottomRadius: Int)

    fun getShapeBackgroundColor(): Int

    fun setShapeBackgroundColor(backgroundColor: Int)

    fun getStrokeColor(): Int

    fun setStrokeColor(strokeColor: Int)

    fun getStrokeWidth(): Int

    fun setStrokeWidth(strokeWidth: Int)

    fun getStrokeDashGap(): Int

    fun setStrokeDashGap(strokeDashGap: Int)

    fun getStrokeDashWidth(): Int

    fun setStrokeDashWidth(strokeDashWidth: Int)

    fun getStartColor(): Int

    fun setStartColor(startColor: Int)

    fun getCenterColor(): Int

    fun setCenterColor(centerColor: Int)

    fun getEndColor(): Int

    fun setEndColor(endColor: Int)

    fun isUseLevel(): Boolean

    fun setUseLevel(useLevel: Boolean)

    fun getAngle(): Int

    fun setAngle(angle: Int)

    fun getGradientType(): Int

    fun setGradientType(gradientType: Int)

    fun getCenterX(): Float

    fun setCenterX(centerX: Float)

    fun getCenterY(): Float

    fun setCenterY(centerY: Float)

    fun getGradientRadius(): Int

    fun setGradientRadius(gradientRadius: Int)
}