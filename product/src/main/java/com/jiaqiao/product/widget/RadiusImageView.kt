package com.jiaqiao.product.widget

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.jiaqiao.product.R

open class RadiusImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {


    private var isCircle = false // 是否显示为圆形，如果为圆形则设置的corner无效

    private var cornerRadius = 0 // 统一设置圆角半径，优先级高于单独设置每个角的半径

    private var leftTopRadius = 0
    private var rightTopRadius = 0
    private var leftBottomRadius = 0
    private var rightBottomRadius = 0
    private var bgColor = Color.TRANSPARENT

    private var viewWidth = 0
    private var viewHeight = 0
    private var radius = 0f //画圆的圆角

    private var srcRadii = FloatArray(8) //圆角

    // 图片占的矩形区域
    private val rectF by lazy { RectF() }
    private val clipRectF by lazy { RectF() }
    private val path by lazy { Path() }
    private val paint by lazy { Paint() }

    // 图片区域大小的path
    private val srcPath by lazy { Path() }
    private val xfermode by lazy {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        } else {
            PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        }
    }


    init {

        // 读取配置
        attrs?.let {
            context.obtainStyledAttributes(it, R.styleable.RadiusImageView)?.let { array ->
                isCircle = array.getBoolean(R.styleable.RadiusImageView_riv_is_circle, isCircle)
                cornerRadius =
                    array.getDimensionPixelSize(
                        R.styleable.RadiusImageView_riv_radius,
                        cornerRadius
                    )
                leftTopRadius = array.getDimensionPixelSize(
                    R.styleable.RadiusImageView_riv_left_top_radius,
                    cornerRadius
                )
                rightTopRadius = array.getDimensionPixelSize(
                    R.styleable.RadiusImageView_riv_right_top_radius,
                    cornerRadius
                )
                leftBottomRadius = array.getDimensionPixelSize(
                    R.styleable.RadiusImageView_riv_left_bottom_radius,
                    cornerRadius
                )
                rightBottomRadius = array.getDimensionPixelSize(
                    R.styleable.RadiusImageView_riv_right_bottom_radius,
                    cornerRadius
                )
                bgColor = array.getColor(R.styleable.RadiusImageView_riv_background_color, bgColor)
                array.recycle()
            }
        }
        srcRadii = FloatArray(8)
        calculateRadii()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
        initSrcRectF()
    }

    override fun onDraw(canvas: Canvas) {
        val centerx = viewWidth / 2.0f
        val centery = viewHeight / 2.0f
        if (bgColor != Color.TRANSPARENT) {
            if (isCircle) {
                paint.color = bgColor
                canvas.drawArc(
                    centerx - radius,
                    centery - radius,
                    centerx + radius,
                    centery + radius,
                    0f,
                    360f,
                    true,
                    paint
                )
            } else {
                path.reset()
                path.addRoundRect(rectF, srcRadii, Path.Direction.CCW)
                if (bgColor != Color.TRANSPARENT) {
                    paint.color = bgColor
                    canvas.drawPath(path, paint)
                }
            }
        }
        // 使用图形混合模式来显示指定区域的图片
        canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG)
        super.onDraw(canvas)
        paint.reset()
        path.reset()
        if (isCircle) {
            path.addCircle(centerx, centery, radius, Path.Direction.CCW)
        } else {
            path.addRoundRect(rectF, srcRadii, Path.Direction.CCW)
        }
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.xfermode = xfermode
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1) {
            canvas.drawPath(path, paint)
        } else {
            srcPath.reset()
            srcPath.addRect(clipRectF, Path.Direction.CCW)
            // 计算tempPath和path的差集
            srcPath.op(path, Path.Op.DIFFERENCE)
            canvas.drawPath(srcPath, paint)
        }
        paint.xfermode = null

        // 恢复画布
        canvas.restore()
    }


    /**
     * 图片drawable转bitmap
     */
    private fun drawableToBitamp(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        // 当设置不为图片，为颜色时，获取的drawable宽高会有问题，所有当为颜色时候获取控件的宽高
        val w = if (drawable.intrinsicWidth <= 0) width else drawable.intrinsicWidth
        val h = if (drawable.intrinsicHeight <= 0) height else drawable.intrinsicHeight
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, w, h)
        drawable.draw(canvas)
        return bitmap
    }


    /**
     * 计算图片原始区域的RectF
     */
    private fun initSrcRectF() {
        if (isCircle) {
            radius = Math.min(viewWidth, viewHeight) / 2.0f
            rectF[viewWidth / 2.0f - radius + paddingLeft, viewHeight / 2.0f - radius + paddingTop, viewWidth / 2.0f + radius - paddingRight] =
                viewHeight / 2.0f + radius - paddingBottom
        } else {
            rectF[(0 + paddingLeft).toFloat(), (0 + paddingTop).toFloat(), (viewWidth - paddingRight).toFloat()] =
                (viewHeight - paddingBottom).toFloat()
        }
        clipRectF[(0 - 1).toFloat(), (0 - 1).toFloat(), (viewWidth + 1).toFloat()] =
            (viewHeight + 1).toFloat()
    }

    /**
     * 计算RectF的圆角半径
     */
    private fun calculateRadii() {
        if (isCircle) {
            return
        }
        srcRadii[1] = leftTopRadius.toFloat()
        srcRadii[0] = srcRadii[1]
        srcRadii[3] = rightTopRadius.toFloat()
        srcRadii[2] = srcRadii[3]
        srcRadii[5] = rightBottomRadius.toFloat()
        srcRadii[4] = srcRadii[5]
        srcRadii[7] = leftBottomRadius.toFloat()
        srcRadii[6] = srcRadii[7]
    }

    private fun calculateRadiiAndRectF(reset: Boolean) {
        if (reset) {
            cornerRadius = 0
        }
        calculateRadii()
        invalidate()
    }


    fun isCircle(isCircle: Boolean) {
        this.isCircle = isCircle
        initSrcRectF()
        invalidate()
    }

    fun setCornerRadiusDp(cornerRadius: Int) {
        setCornerRadiusPx(dp2px(context, cornerRadius.toFloat()))
    }

    private fun setCornerRadiusPx(cornerRadius: Int) {
        this.cornerRadius = cornerRadius
        leftTopRadius = this.cornerRadius
        this.rightTopRadius = this.cornerRadius
        this.leftBottomRadius = this.cornerRadius
        this.rightBottomRadius = this.cornerRadius
        calculateRadiiAndRectF(false)
    }

    private fun dp2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }


}
