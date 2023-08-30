package com.jiaqiao.product.widget

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.jiaqiao.product.R
import com.jiaqiao.product.util.ProductUtil
import kotlin.math.abs

/**
 * 切换控件的宽度必须大于高度
 * */
open class SwitchButtonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val ANIM_MOVE = 1
    private val ANIM_GRADIENT = 2

    private val CLOSE = 1
    private val ANIM = 2
    private val OPEN = 3

    //边框颜色
    private var borderColor = Color.GRAY

    //边框宽度
    private var borderWidth = 0
    private var borderWidthHalf = 0f

    //开启状态的背景颜色
    private var openBgColor = Color.DKGRAY

    //关闭状态的背景颜色
    private var closeBgColor = Color.LTGRAY

    //开启状态的滑块颜色
    private var blockOpenColor = Color.BLACK

    //关闭状态的滑块颜色
    private var blockCloseColor = Color.WHITE

    //滑块的边距
    private var blockMargin = -1

    //圆角，默认：高度一半
    private var radius = -1f

    //动画时长，单位：毫秒，默认值：300
    private var effectDuration = 300

    //开关默认状态
    private var defChecked = false

    //点击切换状态
    private var clickToggle = true

    //动画类型
    private var animatorType = ANIM_MOVE

    private var anim: ValueAnimator? = null
    private val animUpdateLis by lazy {
        ValueAnimator.AnimatorUpdateListener {
            if (it.animatedValue is Int) {
                state = 2
                setProgress((it.animatedValue as Int))
                if (progress == 0 || progress == PROGRESS_MAX) {
                    updateProgressState()
                }
            }
        }
    }
    private val animList by lazy {
        object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                updateProgressState()
            }

            override fun onAnimationEnd(animation: Animator) {
                updateProgressState()
            }

            override fun onAnimationCancel(animation: Animator) {
                updateProgressState()
            }

            override fun onAnimationRepeat(animation: Animator) {
                updateProgressState()
            }
        }
    }
    private val viewClickLis by lazy {
        View.OnClickListener {
            if (clickToggle) {
                toggle()
            }
        }
    }

    private val rectF by lazy { RectF() }
    private val blockRectF by lazy { RectF() }
    private val openBgRectF by lazy { RectF() }
    private val closeBgRectF by lazy { RectF() }
    private var bgLeft = 0f
    private var bgTop = 0f
    private var bgWidth = 0f
    private var bgHeight = 0f

    private var openbgMoveX = 0f

    //滑块运动最大距离(不包括滑块的宽度)
    private var blockMaxDistance = 0

    //blockRectF左边起始值
    private var blockRectLeft = 0f
    private val paint by lazy {
        Paint().also {
            it.isAntiAlias = true
            it.style = Paint.Style.FILL
        }
    }
    private val drawPath by lazy { Path() }
    private val borderPath by lazy { Path() }
    private val clipPath by lazy { Path() }
    private val drawRegion by lazy { Region() }

    private val PROGRESS_MAX = 10000

    //动画进度，0~10000，万分比
    private var progress = 0

    //开关状态，1关闭，2动画中（开启或关闭的动画过程中），3开启
    private var state = 0

    private var viewWidth = 0
    private var viewHeight = 0

    //上一次的开关状态
    private var lastChecked = false

    //开关状态不带动画
    var isChecked: Boolean = false
        set(value) {
            field = value
            setCheckedAction(field)
        }
        get() = state == OPEN

    //开关状态变化回调
    var checkedChangeAction: ((Boolean) -> Unit)? = null

    init {
        attrs?.let {
            context.obtainStyledAttributes(it, R.styleable.SwitchButtonView)?.let { array ->
                borderColor =
                    array.getColor(R.styleable.SwitchButtonView_sbv_border_color, borderColor)
                borderWidth = array.getDimensionPixelOffset(
                    R.styleable.SwitchButtonView_sbv_border_width,
                    borderWidth
                )
                closeBgColor = array.getColor(
                    R.styleable.SwitchButtonView_sbv_open_background_color,
                    closeBgColor
                )
                openBgColor = array.getColor(
                    R.styleable.SwitchButtonView_sbv_close_background_color,
                    openBgColor
                )
                blockCloseColor = array.getColor(
                    R.styleable.SwitchButtonView_sbv_block_open_color,
                    blockCloseColor
                )
                blockOpenColor = array.getColor(
                    R.styleable.SwitchButtonView_sbv_block_close_color,
                    blockOpenColor
                )
                blockMargin = array.getDimensionPixelOffset(
                    R.styleable.SwitchButtonView_sbv_block_margin,
                    blockMargin
                )
                radius =
                    array.getDimensionPixelOffset(
                        R.styleable.SwitchButtonView_sbv_radius,
                        radius.toInt()
                    ).toFloat()
                effectDuration = array.getInteger(
                    R.styleable.SwitchButtonView_sbv_effect_duration,
                    effectDuration
                )
                defChecked = array.getBoolean(R.styleable.SwitchButtonView_sbv_checked, defChecked)
                clickToggle =
                    array.getBoolean(R.styleable.SwitchButtonView_sbv_click_toggle, clickToggle)
                animatorType =
                    array.getInteger(R.styleable.SwitchButtonView_sbv_animator_type, animatorType)
                array.recycle()
            }
        }
        if (blockMargin < 0) {
            blockMargin = borderWidth
        }
        borderWidthHalf = borderWidth.toFloat() / 2
        setState(if (defChecked) 3 else 1)
        setOnClickListener(viewClickLis)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
        if (radius < 0) {
            radius = 1.0f * viewHeight / 2
        }
        rectF.left = 0f
        rectF.top = 0f
        rectF.right = viewWidth.toFloat()
        rectF.bottom = viewHeight.toFloat()

        updatePath()
        updateBackground()
        updateBlock()
    }

    private fun updatePath() {
        drawPath.reset()
        drawPath.addRoundRect(
            rectF,
            radius,
            radius, Path.Direction.CCW
        )
        drawRegion.setEmpty()
        drawRegion.setPath(
            drawPath,
            Region(rectF.left.toInt(), rectF.top.toInt(), rectF.right.toInt(), rectF.bottom.toInt())
        )

        borderPath.reset()
        borderPath.addRoundRect(
            rectF,
            radius,
            radius, Path.Direction.CCW
        )
        clipPath.reset()
        clipPath.addRoundRect(
            rectF.left + borderWidth,
            rectF.top + borderWidth,
            rectF.right - borderWidth,
            rectF.bottom - borderWidth,
            radius,
            radius, Path.Direction.CCW
        )
        borderPath.op(clipPath, Path.Op.DIFFERENCE)
    }

    //刷新背景的绘制区域参数
    private fun updateBackground() {
        openBgRectF.left = rectF.left + borderWidth
        openBgRectF.top = rectF.top + borderWidth
        openBgRectF.right = rectF.right - borderWidth
        openBgRectF.bottom = rectF.bottom - borderWidth
        closeBgRectF.set(openBgRectF)
        bgLeft = openBgRectF.left
        bgTop = openBgRectF.top
        bgWidth = openBgRectF.width()
        bgHeight = openBgRectF.height()
        updateOpenBgMove()
    }

    //刷新滑块的绘制区域参数
    private fun updateBlock() {
        blockRectF.top = rectF.top + blockMargin
        blockRectF.bottom = rectF.bottom - blockMargin
        blockRectF.left = rectF.left + blockMargin
        blockRectF.right = blockRectF.left + blockRectF.bottom - blockRectF.top
        blockMaxDistance = (rectF.width() - blockRectF.width() - blockMargin * 2).toInt()
        blockRectLeft = blockRectF.left
        updateOpenBgMove()
    }

    //刷新背景开启时移动最大距离
    private fun updateOpenBgMove() {
        openbgMoveX = (bgWidth - blockRectF.width() / 2) / bgWidth
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (event.action == MotionEvent.ACTION_DOWN
            && !drawRegion.contains(event.x.toInt(), event.y.toInt())
        ) {
            false
        } else {
            super.onTouchEvent(event)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (viewHeight >= viewWidth || radius < 0) {
            return
        }

        //绘制边框
        if (borderWidth > 0) {
            paint.color = borderColor
            canvas.drawPath(borderPath, paint)
        }

        //进度
        val pro = progress.toFloat() / PROGRESS_MAX
        //绘制关闭状态的背景
        paint.color = when (animatorType) {
            ANIM_GRADIENT -> {
                if (openBgColor == closeBgColor) {
                    closeBgColor
                } else {
                    ProductUtil.radioColor(openBgColor, closeBgColor, pro)
                }
            }
            else -> closeBgColor
        }
        canvas.drawRoundRect(openBgRectF, radius, radius, paint)

        if (animatorType == ANIM_MOVE && openBgColor != closeBgColor && progress < PROGRESS_MAX) {
            //绘制开启状态的背景
            paint.color = openBgColor
            val bgW = (1 - pro) * bgWidth
            val bgH = (1 - pro) * bgHeight
            closeBgRectF.left = bgLeft + ((bgWidth - bgW) * openbgMoveX)
            closeBgRectF.top = bgTop + ((bgHeight - bgH) / 2)
            closeBgRectF.right = closeBgRectF.left + bgW
            closeBgRectF.bottom = closeBgRectF.top + bgH
            canvas.drawRoundRect(closeBgRectF, radius, radius, paint)
        }


        //绘制滑块
        paint.color = if (blockOpenColor == blockCloseColor) {
            blockOpenColor
        } else {
            ProductUtil.radioColor(blockOpenColor, blockCloseColor, pro)
        }
        val blockMoveX = blockMaxDistance * pro
        blockRectF.offsetTo(blockRectLeft + blockMoveX, blockRectF.top)
        canvas.drawRoundRect(blockRectF, radius, radius, paint)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnim()
    }

    //根据进度刷新状态
    private fun updateProgressState() {
        setState(
            when (progress) {
                0 -> 1
                PROGRESS_MAX -> 3
                else -> 2
            }
        )
    }

    //设置控件状态
    private fun setState(sta: Int, action: Boolean = true) {
        state = sta
        when (state) {
            1 -> {
                progress = 0
                setLastChected(false, action)
            }
            3 -> {
                progress = PROGRESS_MAX
                setLastChected(true, action)
            }
        }
    }

    //设置进度
    private fun setProgress(pro: Int) {
        if (progress != pro) {
            progress = pro
            invalidate()
        }
    }

    private fun stopAnim() {
        anim?.removeUpdateListener(animUpdateLis)
        anim?.removeListener(animList)
        anim?.cancel()
        anim = null
        updateProgressState()
    }

    //刷新控件带动画
    private fun checkedAnimator(checked: Boolean) {
        stopAnim()
        val target = if (checked) PROGRESS_MAX else 0
        anim = ValueAnimator.ofInt(progress, target).also {
            it.addListener(animList)
            it.addUpdateListener(animUpdateLis)
            it.duration =
                (abs(target - progress).toFloat() / PROGRESS_MAX * effectDuration).toLong()
        }
        anim?.start()
    }

    private fun setLastChected(checked: Boolean, isCallback: Boolean = true) {
        if (lastChecked != checked) {
            lastChecked = checked
            if (isCallback) {
                checkedChangeAction?.invoke(lastChecked)
            }
        }
    }

    //设置开关状态且带动画
    fun setCheckedAnim(checked: Boolean) {
        if (isChecked == checked && state != ANIM) {
            return
        }
        checkedAnimator(checked)
    }

    //切换开关状态
    fun toggle() {
        setCheckedAnim(if (state == ANIM) lastChecked else !lastChecked)
    }

    private fun setCheckedAction(checked: Boolean, action: Boolean = true) {
        if (isChecked == checked && state != ANIM) {
            return
        }
        setState(if (checked) OPEN else CLOSE, action)
        stopAnim()
        invalidate()
    }

    fun setCheckedNoAction(checked: Boolean) {
        setCheckedAction(checked, false)
    }


}