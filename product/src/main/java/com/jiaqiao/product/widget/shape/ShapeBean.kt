package com.jiaqiao.product.widget.shape

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import com.jiaqiao.product.R
import com.jiaqiao.product.ext.notNull
import com.jiaqiao.product.ext.notNullAndEmpty

open class ShapeBean(
    val view: View,
    val context: Context,
    val attributeSet: AttributeSet? = null,
    val attrs: IntArray? = null
) : ShapeImp {

    private val defaultRadius = 0 //默认圆角
    private var isSetDrawable = false
    private var shapeType = GradientDrawable.RECTANGLE
    private var radius = 0 //四个圆角
    private var leftTopRadius = 0 //顶部左边圆角
    private var rightTopRadius = 0 //顶部右边圆角
    private var leftBottomRadius = 0 //底部左边圆角
    private var rightBottomRadius = 0 //底部右边圆角
    private var backgroundColor = Int.MIN_VALUE

    //圆角集合用于绘制
    private val radiusf by lazy { FloatArray(8) }
    private var strokeColor = Color.TRANSPARENT
    private var strokeWidth = 0
    private var strokeDashGap = 0
    private var strokeDashWidth = 0

    private var startColor = -1
    private var centerColor = -1
    private var endColor = -1
    private var useLevel = false
    private var angle = 270
    private var gradientType = GradientDrawable.LINEAR_GRADIENT
    private var centerX = 0.5f
    private var centerY = 0.5f
    private var gradientRadius = 0

    init {
        init(context, attributeSet)
    }

    override fun init(context: Context, attributeSet: AttributeSet?) {
        // 读取配置
        if (attributeSet.notNull() && attrs.notNull()) {
            val array = context.obtainStyledAttributes(attributeSet, attrs!!)
            shapeType = array.getInt(R.styleable.ShapeView_sp_type, GradientDrawable.RECTANGLE)
            radius = array.getDimensionPixelOffset(
                R.styleable.ShapeView_sp_radius,
                defaultRadius
            )
            leftTopRadius = array.getDimensionPixelOffset(
                R.styleable.ShapeView_sp_left_top_radius,
                defaultRadius
            )
            rightTopRadius = array.getDimensionPixelOffset(
                R.styleable.ShapeView_sp_right_top_radius,
                defaultRadius
            )
            rightBottomRadius = array.getDimensionPixelOffset(
                R.styleable.ShapeView_sp_right_bottom_radius,
                defaultRadius
            )
            leftBottomRadius = array.getDimensionPixelOffset(
                R.styleable.ShapeView_sp_left_bottom_radius,
                defaultRadius
            )
            backgroundColor =
                array.getColor(R.styleable.ShapeView_sp_background_color, Int.MIN_VALUE)
            strokeColor =
                array.getColor(R.styleable.ShapeView_sp_stroke_color, Color.TRANSPARENT)
            strokeWidth =
                array.getDimensionPixelOffset(R.styleable.ShapeView_sp_stroke_width, 0)
            strokeDashGap =
                array.getDimensionPixelOffset(R.styleable.ShapeView_sp_stroke_dash_gap, 0)
            strokeDashWidth =
                array.getDimensionPixelOffset(R.styleable.ShapeView_sp_stroke_dash_width, 0)
            startColor = array.getColor(R.styleable.ShapeView_sp_gradient_start_color, -1)
            centerColor = array.getColor(R.styleable.ShapeView_sp_gradient_center_Color, -1)
            endColor = array.getColor(R.styleable.ShapeView_sp_gradient_end_Color, -1)
            useLevel = array.getBoolean(R.styleable.ShapeView_sp_gradient_use_level, false)
            angle = array.getInt(R.styleable.ShapeView_sp_gradient_angle, 270)
            gradientType = array.getInt(
                R.styleable.ShapeView_sp_gradient_type,
                GradientDrawable.LINEAR_GRADIENT
            )
            centerX = array.getFloat(R.styleable.ShapeView_sp_gradient_center_x, 0.5f)
            centerY = array.getFloat(R.styleable.ShapeView_sp_gradient_center_y, 0.5f)
            gradientRadius = array.getDimensionPixelOffset(
                R.styleable.ShapeView_sp_gradient_gradient_radius,
                -1
            )

            //如果四个角的值没有设置，那么就使用通用的radius的值。
            if (defaultRadius == leftTopRadius) {
                leftTopRadius = radius
            }
            if (defaultRadius == rightTopRadius) {
                rightTopRadius = radius
            }
            if (defaultRadius == rightBottomRadius) {
                rightBottomRadius = radius
            }
            if (defaultRadius == leftBottomRadius) {
                leftBottomRadius = radius
            }
            if (array.hasValue(R.styleable.ShapeView_sp_type)
                || array.hasValue(R.styleable.ShapeView_sp_radius)
                || array.hasValue(R.styleable.ShapeView_sp_left_top_radius)
                || array.hasValue(R.styleable.ShapeView_sp_right_top_radius)
                || array.hasValue(R.styleable.ShapeView_sp_right_bottom_radius)
                || array.hasValue(R.styleable.ShapeView_sp_left_bottom_radius)
                || array.hasValue(R.styleable.ShapeView_sp_background_color)
                || array.hasValue(R.styleable.ShapeView_sp_stroke_color)
                || array.hasValue(R.styleable.ShapeView_sp_stroke_width)
                || array.hasValue(R.styleable.ShapeView_sp_stroke_dash_gap)
                || array.hasValue(R.styleable.ShapeView_sp_stroke_dash_width)
                || array.hasValue(R.styleable.ShapeView_sp_gradient_start_color)
                || array.hasValue(R.styleable.ShapeView_sp_gradient_center_Color)
                || array.hasValue(R.styleable.ShapeView_sp_gradient_end_Color)
                || array.hasValue(R.styleable.ShapeView_sp_gradient_use_level)
                || array.hasValue(R.styleable.ShapeView_sp_gradient_angle)
                || array.hasValue(R.styleable.ShapeView_sp_gradient_type)
                || array.hasValue(R.styleable.ShapeView_sp_gradient_center_x)
                || array.hasValue(R.styleable.ShapeView_sp_gradient_center_y)
                || array.hasValue(R.styleable.ShapeView_sp_gradient_gradient_radius)
            ) {
                isSetDrawable = true
            }
            array.recycle()
        }
        initDate()
    }


    //初始化数据源
    private fun initDate() {
        if (leftTopRadius < 0) {
            leftTopRadius = 0
        }
        if (rightTopRadius < 0) {
            rightTopRadius = 0
        }
        if (rightBottomRadius < 0) {
            rightBottomRadius = 0
        }
        if (leftBottomRadius < 0) {
            leftBottomRadius = 0
        }
        radiusf[0] = leftTopRadius.toFloat()
        radiusf[1] = radiusf[0]
        radiusf[2] = rightTopRadius.toFloat()
        radiusf[3] = radiusf[2]
        radiusf[4] = rightBottomRadius.toFloat()
        radiusf[5] = radiusf[4]
        radiusf[6] = leftBottomRadius.toFloat()
        radiusf[7] = radiusf[6]
    }


    override fun setBackgroundDrawable() {
        if (!isSetDrawable) {
            return
        }
        var gradientDrawable = GradientDrawable()
        val backDrawable = view.background
        if (backDrawable.notNull()) {
            if (backDrawable is ColorDrawable) {
                gradientDrawable.setColor(backDrawable.color)
                gradientDrawable.alpha = backDrawable.alpha
            } else if (backDrawable is GradientDrawable) {
                gradientDrawable = backDrawable
            }
        }
        if (shapeType >= 0) {
            gradientDrawable.shape = shapeType
        }
        if (backgroundColor != Int.MIN_VALUE) {
            gradientDrawable.setColor(backgroundColor) //设置单色背景
        }
        if (radiusf.isNotEmpty()) {
            gradientDrawable.cornerRadii = radiusf
        }
        if (strokeWidth >= 0) {
            gradientDrawable.setStroke(
                strokeWidth,
                strokeColor,
                strokeDashWidth.toFloat(),
                strokeDashGap.toFloat()
            )
        }
        if (useLevel) {
            gradientDrawable.useLevel = useLevel
        }
        gradientDrawable.setGradientCenter(centerX, centerY)
        gradientDrawable.gradientType = gradientType
        val colorsList: MutableList<Int> = ArrayList()
        if (startColor != -1) {
            colorsList.add(startColor)
        }
        if (centerColor != -1) {
            colorsList.add(centerColor)
        }
        if (endColor != -1) {
            colorsList.add(endColor)
        }
        if (colorsList.notNullAndEmpty()) {
            if (colorsList.size == 1) {
                gradientDrawable.setColor(colorsList[0])
            } else {
                val colors = IntArray(colorsList.size)
                for (i in colorsList.indices) {
                    colors[i] = colorsList[i]
                }
                gradientDrawable.colors = colors
            }
        }
        if (gradientType == GradientDrawable.LINEAR_GRADIENT) {
            if (angle > 360) {
                angle %= 360
            }
            if (angle != 0 && angle % 45 != 0) {
                angle = angle / 45 * 45
            }
            when (angle) {
                0 -> gradientDrawable.orientation = GradientDrawable.Orientation.LEFT_RIGHT
                45 -> gradientDrawable.orientation = GradientDrawable.Orientation.BL_TR
                90 -> gradientDrawable.orientation = GradientDrawable.Orientation.BOTTOM_TOP
                135 -> gradientDrawable.orientation = GradientDrawable.Orientation.BR_TL
                180 -> gradientDrawable.orientation = GradientDrawable.Orientation.RIGHT_LEFT
                225 -> gradientDrawable.orientation = GradientDrawable.Orientation.TR_BL
                270 -> gradientDrawable.orientation = GradientDrawable.Orientation.TOP_BOTTOM
                315 -> gradientDrawable.orientation = GradientDrawable.Orientation.TL_BR
            }
        } else {
            gradientDrawable.gradientRadius = gradientRadius.toFloat()
        }
        gradientDrawable.level = 10000
        view.background = gradientDrawable
    }

    override fun getShapeType(): Int {
        return shapeType
    }

    override fun setShapeType(shapeType: Int) {
        this.shapeType = shapeType
        isSetDrawable = true
        setBackgroundDrawable()
    }

    override fun getRadius(): Int {
        return radius
    }

    override fun setRadius(radius: Int) {
        this.radius = radius
        leftTopRadius = this.radius
        rightTopRadius = this.radius
        rightBottomRadius = this.radius
        leftBottomRadius = this.radius
        isSetDrawable = true
        initDate()
        setBackgroundDrawable()
    }

    override fun getLeftTopRadius(): Int {
        return leftTopRadius
    }

    override fun setLeftTopRadius(leftTopRadius: Int) {
        this.leftTopRadius = leftTopRadius
        isSetDrawable = true
        initDate()
        setBackgroundDrawable()
    }

    override fun getRightTopRadius(): Int {
        return rightTopRadius
    }

    override fun setRightTopRadius(rightTopRadius: Int) {
        this.rightTopRadius = rightTopRadius
        isSetDrawable = true
        initDate()
        setBackgroundDrawable()
    }

    override fun getLeftBottomRadius(): Int {
        return leftBottomRadius
    }

    override fun setLeftBottomRadius(leftBottomRadius: Int) {
        this.leftBottomRadius = leftBottomRadius
        isSetDrawable = true
        initDate()
        setBackgroundDrawable()
    }

    override fun getRightBottomRadius(): Int {
        return rightBottomRadius
    }

    override fun setRightBottomRadius(rightBottomRadius: Int) {
        this.rightBottomRadius = rightBottomRadius
        isSetDrawable = true
        initDate()
        setBackgroundDrawable()
    }

    override fun getShapeBackgroundColor(): Int {
        return backgroundColor
    }

    override fun setShapeBackgroundColor(backgroundColor: Int) {
        this.backgroundColor = backgroundColor
        isSetDrawable = true
        setBackgroundDrawable()
    }

    override fun getStrokeColor(): Int {
        return strokeColor
    }

    override fun setStrokeColor(strokeColor: Int) {
        this.strokeColor = strokeColor
        isSetDrawable = true
        setBackgroundDrawable()
    }

    override fun getStrokeWidth(): Int {
        return strokeWidth
    }

    override fun setStrokeWidth(strokeWidth: Int) {
        this.strokeWidth = strokeWidth
        isSetDrawable = true
        setBackgroundDrawable()
    }

    override fun getStrokeDashGap(): Int {
        return strokeDashGap
    }

    override fun setStrokeDashGap(strokeDashGap: Int) {
        this.strokeDashGap = strokeDashGap
        isSetDrawable = true
        setBackgroundDrawable()
    }

    override fun getStrokeDashWidth(): Int {
        return strokeDashWidth
    }

    override fun setStrokeDashWidth(strokeDashWidth: Int) {
        this.strokeDashWidth = strokeDashWidth
        isSetDrawable = true
        setBackgroundDrawable()
    }

    override fun getStartColor(): Int {
        return startColor
    }

    override fun setStartColor(startColor: Int) {
        this.startColor = startColor
        isSetDrawable = true
        setBackgroundDrawable()
    }

    override fun getCenterColor(): Int {
        return centerColor
    }

    override fun setCenterColor(centerColor: Int) {
        this.centerColor = centerColor
        isSetDrawable = true
        setBackgroundDrawable()
    }

    override fun getEndColor(): Int {
        return endColor
    }

    override fun setEndColor(endColor: Int) {
        this.endColor = endColor
        isSetDrawable = true
        setBackgroundDrawable()
    }

    override fun isUseLevel(): Boolean {
        return useLevel
    }

    override fun setUseLevel(useLevel: Boolean) {
        this.useLevel = useLevel
        isSetDrawable = true
        setBackgroundDrawable()
    }

    override fun getAngle(): Int {
        return angle
    }

    override fun setAngle(angle: Int) {
        this.angle = angle
        isSetDrawable = true
        setBackgroundDrawable()
    }

    override fun getGradientType(): Int {
        return gradientType
    }

    override fun setGradientType(gradientType: Int) {
        this.gradientType = gradientType
        isSetDrawable = true
        setBackgroundDrawable()
    }

    override fun getCenterX(): Float {
        return centerX
    }

    override fun setCenterX(centerX: Float) {
        this.centerX = centerX
        isSetDrawable = true
        setBackgroundDrawable()
    }

    override fun getCenterY(): Float {
        return centerY
    }

    override fun setCenterY(centerY: Float) {
        this.centerY = centerY
        isSetDrawable = true
        setBackgroundDrawable()
    }

    override fun getGradientRadius(): Int {
        return gradientRadius
    }

    override fun setGradientRadius(gradientRadius: Int) {
        this.gradientRadius = gradientRadius
        isSetDrawable = true
        setBackgroundDrawable()
    }

}