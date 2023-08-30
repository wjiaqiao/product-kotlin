package com.jiaqiao.product.widget.shape

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.jiaqiao.product.R
import com.jiaqiao.product.ext.notNull

open class ShapeLinearLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val mShapeBean by lazy {
        if (attrs.notNull()) {
            ShapeBean(this, context, attrs, R.styleable.ShapeLinearLayout)
        } else {
            ShapeBean(this, context)
        }
    }

    init {
        mShapeBean
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setBackgroundDrawable()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setBackgroundDrawable()
    }


    private fun setBackgroundDrawable() {
        mShapeBean.setBackgroundDrawable()
    }

    fun getShapeType(): Int {
        return mShapeBean.getShapeType()
    }

    fun setShapeType(shapeType: Int) {
        mShapeBean.setShapeType(shapeType)
    }


    fun getRadius(): Int {
        return mShapeBean.getRadius()
    }

    fun setRadius(radius: Int) {
        mShapeBean.setRadius(radius)
    }


    fun getLeftTopRadius(): Int {
        return mShapeBean.getLeftTopRadius()
    }

    fun setLeftTopRadius(leftTopRadius: Int) {
        mShapeBean.setLeftTopRadius(leftTopRadius)
    }

    fun getRightTopRadius(): Int {
        return mShapeBean.getRightTopRadius()
    }

    fun setRightTopRadius(rightTopRadius: Int) {
        mShapeBean.setRightTopRadius(rightTopRadius)

    }

    fun getLeftBottomRadius(): Int {
        return mShapeBean.getLeftBottomRadius()
    }

    fun setLeftBottomRadius(leftBottomRadius: Int) {
        mShapeBean.setLeftBottomRadius(leftBottomRadius)
    }

    fun getRightBottomRadius(): Int {
        return mShapeBean.getRightBottomRadius()
    }

    fun setRightBottomRadius(rightBottomRadius: Int) {
        mShapeBean.setRightBottomRadius(rightBottomRadius)
    }

    fun getShapeBackgroundColor(): Int {
        return mShapeBean.getShapeBackgroundColor()
    }

    fun setShapeBackgroundColor(backgroundColor: Int) {
        mShapeBean.setShapeBackgroundColor(backgroundColor)
    }

    fun getStrokeColor(): Int {
        return mShapeBean.getStrokeColor()
    }

    fun setStrokeColor(strokeColor: Int) {
        mShapeBean.setStrokeColor(strokeColor)
    }

    fun getStrokeWidth(): Int {
        return mShapeBean.getStrokeWidth()
    }

    fun setStrokeWidth(strokeWidth: Int) {
        mShapeBean.setStrokeWidth(strokeWidth)
    }

    fun getStrokeDashGap(): Int {
        return mShapeBean.getStrokeDashGap()
    }

    fun setStrokeDashGap(strokeDashGap: Int) {
        mShapeBean.setStrokeDashGap(strokeDashGap)
    }

    fun getStrokeDashWidth(): Int {
        return mShapeBean.getStrokeDashWidth()
    }

    fun setStrokeDashWidth(strokeDashWidth: Int) {
        mShapeBean.setStrokeDashWidth(strokeDashWidth)
    }

    fun getStartColor(): Int {
        return mShapeBean.getStartColor()
    }

    fun setStartColor(startColor: Int) {
        mShapeBean.setStartColor(startColor)
    }

    fun getCenterColor(): Int {
        return mShapeBean.getCenterColor()
    }

    fun setCenterColor(centerColor: Int) {
        mShapeBean.setCenterColor(centerColor)
    }

    fun getEndColor(): Int {
        return mShapeBean.getEndColor()
    }

    fun setEndColor(endColor: Int) {
        mShapeBean.setEndColor(endColor)
    }

    fun isUseLevel(): Boolean {
        return mShapeBean.isUseLevel()
    }

    fun setUseLevel(useLevel: Boolean) {
        mShapeBean.setUseLevel(useLevel)
    }

    fun getAngle(): Int {
        return mShapeBean.getAngle()
    }

    fun setAngle(angle: Int) {
        mShapeBean.setAngle(angle)
    }

    fun getGradientType(): Int {
        return mShapeBean.getGradientType()
    }

    fun setGradientType(gradientType: Int) {
        mShapeBean.setGradientType(gradientType)
    }

    fun getCenterX(): Float {
        return mShapeBean.getCenterX()
    }

    fun setCenterX(centerX: Float) {
        mShapeBean.setCenterX(centerX)
    }

    fun getCenterY(): Float {
        return mShapeBean.getCenterY()
    }

    fun setCenterY(centerY: Float) {
        mShapeBean.setCenterY(centerY)
    }

    fun getGradientRadius(): Int {
        return mShapeBean.getGradientRadius()
    }

    fun setGradientRadius(gradientRadius: Int) {

        mShapeBean.setGradientRadius(gradientRadius)
    }

}