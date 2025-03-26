package com.jiaqiao.product.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import com.jiaqiao.product.R
import com.jiaqiao.product.util.ProductUiUtil

/**
 * 添加状态栏高度padding或margin的FrameLayout
 * */
open class StatusFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    //状态栏位置占用padding
    private val PADDING = 0

    //状态栏位置占用margin
    private val MARGIN = 1

    var inStatusBarType = 1
        set(value) {
            if (field != value) {
                field = value
                setStatusBar()
            }
        }
    var inStatusBar = true
        set(value) {
            if (field != value) {
                field = value
                setStatusBar()
            }
        }

    private var befInStatusBar = false
    private var thisPaddingTop = 0
    private var thisMarginTop = 0
    private var thisHeight = 0

    init {
        inStatusBar = true
        attrs?.let {
            context.obtainStyledAttributes(attrs, R.styleable.StatusFrameLayout).let { typedArray ->
                inStatusBar =
                    typedArray.getBoolean(
                        R.styleable.StatusFrameLayout_sfl_in_status_bar,
                        inStatusBar
                    )
                inStatusBarType = typedArray.getInt(
                    R.styleable.StatusFrameLayout_sfl_in_status_bar_type,
                    inStatusBarType
                )
                typedArray.recycle()
            }
        }
        befInStatusBar = !inStatusBar
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        updateMarginPadding()
        setStatusBar()
    }

    //刷新padding，margin，高度
    private fun updateMarginPadding() {
        thisPaddingTop = paddingTop
        when (layoutParams) {
            is MarginLayoutParams -> {
                val marginLayoutParams = layoutParams as MarginLayoutParams
                thisMarginTop = marginLayoutParams.topMargin
                thisHeight = marginLayoutParams.height
            }

            is ViewGroup.LayoutParams -> {
                val layoutParams = layoutParams as ViewGroup.LayoutParams
                thisMarginTop = 0
                thisHeight = layoutParams.height
            }

            else -> {
                thisMarginTop = 0
                thisHeight = 0
            }
        }
    }

    /**
     * 设置statusbar状态，只在view附加进windows时执行一次
     * */
    private fun setStatusBar() {
        if (!isAttachedToWindow) {
            return
        }
        val statusBarHeight = if (!inStatusBar) {
            ProductUiUtil.getStatusBarHeight(context)
        } else {
            0
        }
        val befStbHeight = if (!befInStatusBar) {
            statusBarHeight
        } else {
            0
        }
        when (inStatusBarType) {
            PADDING -> {
                setPadding(
                    paddingLeft, thisPaddingTop - befStbHeight + statusBarHeight,
                    paddingRight, paddingBottom
                )
            }

            MARGIN -> {
                val marginLayoutParams = this.layoutParams as MarginLayoutParams
                marginLayoutParams.setMargins(
                    marginLayoutParams.leftMargin,
                    thisMarginTop - befStbHeight + statusBarHeight,
                    marginLayoutParams.rightMargin,
                    marginLayoutParams.bottomMargin
                )
                this.layoutParams = marginLayoutParams
            }
        }
        befInStatusBar = inStatusBar
    }
}