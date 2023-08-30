package com.jiaqiao.product.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.jiaqiao.product.R
import com.jiaqiao.product.ext.notNull

/**
 * 自定义的按钮，支持默认样式、点击样式、禁用样式
 */
open class ButtonStateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private val defaultRadius = 0 //默认圆角
    var radius = 0 //四个圆角
        set(value) {
            if (field != value) {
                field = value
                refreshDrawable()
            }
        }
    var leftTopRadius = 0 //顶部左边圆角
        set(value) {
            if (field != value) {
                field = value
                refreshDrawable()
            }
        }
    var rightTopRadius = 0 //顶部右边圆角
        set(value) {
            if (field != value) {
                field = value
                refreshDrawable()
            }
        }
    var leftBottomRadius = 0 //底部左边圆角
        set(value) {
            if (field != value) {
                field = value
                refreshDrawable()
            }
        }
    var rightBottomRadius = 0 //底部右边圆角
        set(value) {
            if (field != value) {
                field = value
                refreshDrawable()
            }
        }
    var defBgColor = Int.MIN_VALUE
        //默认背景颜色
        set(value) {
            if (field != value) {
                field = value
                refreshDrawable()
            }
        }
    var touchBgColor = Int.MIN_VALUE
        //触摸时背景颜色
        set(value) {
            if (field != value) {
                field = value
                refreshDrawable()
            }
        }
    var disenabledBgColor = Int.MIN_VALUE
        //按钮禁用时背景颜色
        set(value) {
            if (field != value) {
                field = value
                refreshDrawable()
            }
        }

    //圆角集合用于绘制
    private val radiusf by lazy { FloatArray(8) }

    init {


        attrs?.let {
            val array = context.obtainStyledAttributes(it, R.styleable.ButtonStateView)

            radius = array.getDimensionPixelOffset(
                R.styleable.ButtonStateView_bsv_radius,
                defaultRadius
            )
            leftTopRadius = array.getDimensionPixelOffset(
                R.styleable.ButtonStateView_bsv_left_top_radius,
                defaultRadius
            )
            rightTopRadius = array.getDimensionPixelOffset(
                R.styleable.ButtonStateView_bsv_right_top_radius,
                defaultRadius
            )
            rightBottomRadius = array.getDimensionPixelOffset(
                R.styleable.ButtonStateView_bsv_right_bottom_radius,
                defaultRadius
            )
            leftBottomRadius = array.getDimensionPixelOffset(
                R.styleable.ButtonStateView_bsv_left_bottom_radius,
                defaultRadius
            )
            defBgColor =
                array.getColor(R.styleable.ButtonStateView_bsv_default_bg_color, defBgColor)
            touchBgColor =
                array.getColor(R.styleable.ButtonStateView_bsv_touch_bg_color, touchBgColor)
            disenabledBgColor = array.getColor(
                R.styleable.ButtonStateView_bsv_disenable_bg_color,
                disenabledBgColor
            )
            array.recycle()
        }
        initRadius()
        setBgDrawable()
    }

    //初始化数据源
    private fun initRadius() {
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

    private fun refreshDrawable() {
        if (isAttachedToWindow) {
            initRadius()
            setBgDrawable()
        }
    }

    //设置不同状态的背景Drawable
    private fun setBgDrawable() {
        val backDrawable = background
        if (backDrawable.notNull() && backDrawable is ColorDrawable && defBgColor == Int.MIN_VALUE) {
            defBgColor = backDrawable.color
        }
        if (defBgColor == Int.MIN_VALUE
            && touchBgColor == Int.MIN_VALUE
            && disenabledBgColor == Int.MIN_VALUE
        )
            return
        var drawable = StateListDrawable()

        val realDisenabledBgColor = getRealDisenabledBgColor()
        if (realDisenabledBgColor != Int.MIN_VALUE) {
            //创建禁用状态的 Drawable
            drawable.addState(intArrayOf(-android.R.attr.state_enabled), GradientDrawable().also {
                it.setColor(realDisenabledBgColor)
                it.cornerRadii = radiusf
            })
        }

        val realTouchBgColor = getRealTouchBgColor()
        if (realTouchBgColor != Int.MIN_VALUE) {
            //创建按下状态的 Drawable
            drawable.addState(intArrayOf(android.R.attr.state_pressed), GradientDrawable().also {
                it.setColor(realTouchBgColor)
                it.cornerRadii = radiusf
            })
        }

        if (defBgColor != Int.MIN_VALUE) {
            // 创建默认状态的 Drawable
            drawable.addState(intArrayOf(), GradientDrawable().also {
                it.setColor(defBgColor)
                it.cornerRadii = radiusf
            })
        }

        background = drawable
    }

    private fun getRealTouchBgColor(): Int {
        return if (touchBgColor == Int.MIN_VALUE && defBgColor != Int.MIN_VALUE) {
            Color.argb(
                (Color.alpha(defBgColor) * 0.6f).toInt(),
                Color.red(defBgColor),
                Color.green(defBgColor),
                Color.blue(defBgColor)
            )
        } else Int.MIN_VALUE
    }

    private fun getRealDisenabledBgColor(): Int {
        return if (disenabledBgColor == Int.MIN_VALUE && defBgColor != Int.MIN_VALUE) {
            Color.argb(
                (Color.alpha(defBgColor) * 0.3f).toInt(),
                Color.red(defBgColor),
                Color.green(defBgColor),
                Color.blue(defBgColor)
            )
        } else Int.MIN_VALUE
    }

    override fun setBackgroundColor(color: Int) {
//        super.setBackgroundColor(color)
        defBgColor = color
    }

}