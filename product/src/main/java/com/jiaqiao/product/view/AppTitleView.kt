package com.jiaqiao.product.view

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.jiaqiao.product.R
import com.jiaqiao.product.databinding.AppTitleViewLayBinding
import com.jiaqiao.product.ext.*
import com.jiaqiao.product.util.ProductUtil
import com.jiaqiao.product.util.ViewBindingUtil

open class AppTitleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    companion object {
        const val NONE = 0
        const val BACK = 1
        const val HIDE = 2
    }

    private var inStatusBar = true
    private var leftBackType = NONE
    private var centerTextStr = ""
    private var centerTextSize = 0f
    private var centerTextColor = Color.WHITE
    private var centerTextGravity = Gravity.CENTER_VERTICAL or Gravity.START

    //返回上一级的点击事件
    private val onLeftViewClickBackListener by lazy {
        OnClickListener {
            if (context.notNull() && context is Activity) {
                if (ProductUtil.isActive(context)) {
                    context.onBackPressed()
                }
            }
        }
    }

    //外部点击事件
    private var otherClickLis: OnClickListener? = null

    private val viewBind = AppTitleViewLayBinding.inflate(context.layoutInflater(), this, true)

    init {
        centerTextSize = 18.sp.toFloat()
        attrs?.let {
            context.obtainStyledAttributes(attrs, R.styleable.AppTitleView)?.let { typedArray ->
                inStatusBar =
                    typedArray.getBoolean(R.styleable.AppTitleView_atv_in_status_bar, inStatusBar)
                leftBackType = typedArray.getInt(
                    R.styleable.AppTitleView_atv_arrow_click_type,
                    NONE
                )
                centerTextStr =
                    typedArray.getString(R.styleable.AppTitleView_atv_center_text) ?: ""
                centerTextSize =
                    typedArray.getDimension(
                        R.styleable.AppTitleView_atv_center_text_size,
                        centerTextSize
                    )
                centerTextColor = typedArray.getColor(
                    R.styleable.AppTitleView_atv_center_text_color,
                    ContextCompat.getColor(context, R.color.text_3)
                )
                centerTextGravity = typedArray.getInt(
                    R.styleable.AppTitleView_atv_center_text_gravity,
                    centerTextGravity
                )
                typedArray.recycle()
            }
        }

        if (!inStatusBar) {
            viewBind.parentView.setMarginTop(context.statusBarHeight())
        }
        updateArrowLeft()
        updateTextState()
    }

    /**
     * 代码设置控件高度为wrap_content
     * */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val par = parent
        val parentHeight = if (par is ViewGroup) {
            par.measuredHeight
        } else {
            0
        }
        super.onMeasure(
            widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(parentHeight, MeasureSpec.AT_MOST)
        )
    }

    //刷新文本状态
    private fun updateTextState() {
        viewBind.centerText.text = centerTextStr
        viewBind.centerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, centerTextSize)
        viewBind.centerText.setTextColor(centerTextColor)
        viewBind.centerText.gravity = centerTextGravity
    }

    //刷新返回键操作
    private fun updateArrowLeft() {
        when (leftBackType) {
            NONE -> {
                viewBind.arrowLeft.setOnClickListener(otherClickLis)
            }
            BACK -> {
                viewBind.arrowLeft.setOnClickListener(onLeftViewClickBackListener)
            }
            HIDE -> {
                viewBind.arrowLeft.invisible()
                viewBind.arrowLeft.setOnClickListener(null)
            }
        }
    }

    //设置返回键的点击事件类型
    fun setArrowLeft(type: Int) {
        leftBackType = type
        updateArrowLeft()
    }

    fun setArrowLeftClickListener(clickLis: OnClickListener) {
        otherClickLis = clickLis
        if (leftBackType == NONE) {
            viewBind.arrowLeft.setOnClickListener(otherClickLis)
        }
    }

    //设置文本内容
    fun setText(resId: Int) {
        centerTextStr = context.getString(resId)
        viewBind.centerText.text = centerTextStr
    }

    //设置文本内容
    fun setText(text: String) {
        centerTextStr = text
        viewBind.centerText.text = centerTextStr
    }

    //设置文本字号
    fun setTextSize(px: Int) {
        centerTextSize = px.toFloat()
        viewBind.centerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, centerTextSize)
    }

    //设置文本字号
    fun setTextSize(px: Float) {
        centerTextSize = px
        viewBind.centerText.setTextSize(TypedValue.COMPLEX_UNIT_PX, centerTextSize)
    }

    //设置文本颜色
    fun setTextColor(@ColorInt color: Int) {
        centerTextColor = color
        viewBind.centerText.setTextColor(centerTextColor)
    }

    //设置文本颜色
    fun setTextColorResId(resId: Int) {
        centerTextColor = ContextCompat.getColor(context, resId)
        viewBind.centerText.setTextColor(centerTextColor)
    }

    //设置文本对齐方式
    fun setTextGravity(gravity: Int) {
        centerTextGravity = gravity
        viewBind.centerText.gravity = centerTextGravity
    }

}