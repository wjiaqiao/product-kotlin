package com.jiaqiao.product.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.Interpolator
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import com.jiaqiao.product.R
import com.jiaqiao.product.ext.dp
import com.jiaqiao.product.util.ProductUtil
import java.io.ByteArrayOutputStream


class ClipImageView : AppCompatImageView {
    private val BASE_SCALE = 1f//初始加载时图片的放大比例
    private var MAX_SCALE = 5f//最大图片的放大比例
    private val MOVE_DISTANCE = 100 //移动100像素
    private val MOVE_TIME = 40 //移动 MOVE_DISTANCE 像素所有消耗的毫秒数
    private val MAX_ANIMATION_TIME = 400 //最大动画时间
    private val SCALE_ANIMATION_TIME = 200 //缩放动画时间
    private val SHOW_BITMAP_MIN_SIZE = 40f //图片放大时的最大高度显示像素
    private var BITMAP_MAX_WIDTH = 0f //图片放大时的最大宽度显示像素
    private var BITMAP_MAX_HEIGHT = 0f //图片放大时的最大高度显示像素
    private val DEFAULT_CLIP_BOX_TYPE = RECT_CLIP_BOX

    private var clipBoxType = DEFAULT_CLIP_BOX_TYPE //裁剪框类型
    private var strokeWidth = 0 //描边的宽度
    private var strokeColor = Color.WHITE //描边的颜色
    private var maskingColor = Color.argb(115, 0, 0, 0) //蒙版颜色
    private var DEFAULT_CLIP_BOX_SIZE = 0
    private var circleRadii = DEFAULT_CLIP_BOX_SIZE
    private var rectWidth = DEFAULT_CLIP_BOX_SIZE
    private var rectHeight = DEFAULT_CLIP_BOX_SIZE
    private var rectRadius = DEFAULT_CLIP_BOX_SIZE
    private var paint: Paint? = null
    private var strokePaint: Paint? = null


    /**
     * 两个手指缩放的阀值，超过才相应操作，单位：像素
     */
    private val distanceFingers = 1f

    /**
     * 记录是拖拉照片模式还是放大缩小照片模式
     */
    private var mode = 0 // 初始状态

    /**
     * 用于记录开始时候的坐标位置
     */
    private val startPoint = PointF()

    /**
     * 用于记录拖拉图片移动的坐标位置
     */
    private var matrixa = Matrix()

    /**
     * 用于记录图片要进行拖拉时候的坐标位置
     */
    private val currentMatrix = Matrix()

    /**
     * 两个手指的开始距离
     */
    private var startDis = 0f

    /**
     * 两个手指的中间点
     */
    private var midPoint: PointF? = null

    /**
     * 第一次加载图片成功的标识
     */
    private var isBitmapFrist = true

    /**
     * 选择裁剪图片的矩形框
     */
    private var selectRectF: RectF? = null

    /**
     * 归位操作的移动动画的上一次动画值，每次开始动画前需要重置成0
     */
    private var lastTranslateInterpolation = 0f

    /**
     * 归位操作的缩放移动动画的上一次动画值，每次开始动画前需要重置成0
     */
    private var lastScaleTranslateInterpolation = 0f

    /**
     * 归位操作的缩放移动动画的上一次缩放的倍数，，每次开始动画前需要重置成1
     *
     *
     *
     *
     * postScale进行缩放时，时相对于上一次的缩放，在动画里每一次的原始缩放倍数都是1，
     * 需要将当前次数的倍数除以上一次的缩放倍数得到当前需要设置的缩放倍数
     */
    private var lastScaleTranslateScale = 1f
    private var touchDownWidth = 0f
    private var touchDownHeight = 0f
    var isCanClip = true
        private set

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        DEFAULT_CLIP_BOX_SIZE = 120.dp
        circleRadii = DEFAULT_CLIP_BOX_SIZE
        rectWidth = DEFAULT_CLIP_BOX_SIZE
        rectHeight = DEFAULT_CLIP_BOX_SIZE
        scaleType = ScaleType.MATRIX
        if (null != attrs) {
            val array = context.obtainStyledAttributes(attrs, R.styleable.ClipImageView)
            clipBoxType = array.getInt(R.styleable.ClipImageView_civ_clip_box_type, clipBoxType)
            circleRadii =
                array.getDimensionPixelSize(R.styleable.ClipImageView_civ_circle_radii, circleRadii)
            rectWidth =
                array.getDimensionPixelSize(R.styleable.ClipImageView_civ_rect_width, rectWidth)
            rectHeight =
                array.getDimensionPixelSize(R.styleable.ClipImageView_civ_rect_height, rectHeight)
            rectRadius =
                array.getDimensionPixelSize(R.styleable.ClipImageView_civ_rect_radius, rectRadius)
            strokeWidth =
                array.getDimensionPixelSize(R.styleable.ClipImageView_civ_stroke_width, strokeWidth)
            strokeColor = array.getColor(R.styleable.ClipImageView_civ_stroke_color, strokeColor)
            maskingColor = array.getColor(R.styleable.ClipImageView_civ_masking_color, maskingColor)
            array.recycle()
        }
        paint = Paint()
        paint!!.style = Paint.Style.FILL
        paint!!.isAntiAlias = true //抗锯齿
        paint!!.color = maskingColor
        strokePaint = Paint()
        strokePaint!!.style = Paint.Style.FILL
        strokePaint!!.isAntiAlias = true //抗锯齿
        strokePaint!!.color = strokeColor
        strokePaint!!.strokeWidth = strokeWidth.toFloat()
        selectRectF = getSelectRectF()
        isCanClip = true
    }

    override fun layout(l: Int, t: Int, r: Int, b: Int) {
        super.layout(l, t, r, b)
        if (isBitmapFrist) {
            isBitmapFrist = bitmapInit()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val bitmapRectF = matrixRectF
        if (isNull(bitmapRectF)) {
            return true
        }
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                isCanClip = false
                startPoint[event.x] = event.y
                //拖拉图片,起始位置不在图片上时不进行操作
                if (!isNull(bitmapRectF) && bitmapRectF.contains(startPoint.x, startPoint.y)) {
                    mode = MODE_DRAG
                }
                // 记录ImageView当前的移动位置
                currentMatrix.set(imageMatrix)
            }

            MotionEvent.ACTION_MOVE -> {
                isCanClip = false
                when (mode) {
                    MODE_DRAG -> translate(event)
                    MODE_ZOOM ->
                        // 放大缩小图片
                        //两手指的中心位置不在图片上时不进行操作
                        if (!isNull(bitmapRectF) && bitmapRectF.contains(
                                midPoint!!.x, midPoint!!.y
                            )
                        ) {
                            distanceScale(event)
                        }
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isCanClip = true
                //                // 当触点离开屏幕，但是屏幕上还有触点(手指)
//            case MotionEvent.ACTION_POINTER_UP:
                //手离开屏幕时进行图片归位操作
                resetBitmap(bitmapRectF)
                mode = 0
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                isCanClip = false
                mode = MODE_ZOOM
                /** 计算两个手指间的距离  */
                startDis = distance(event)
                /** 计算两个手指间的中间点  */
                if (startDis > distanceFingers) { // 两个手指并拢在一起的时候像素大于10
                    midPoint = mid(event)
                    //记录当前ImageView的缩放倍数
                    currentMatrix.set(imageMatrix)
                    touchDownWidth = bitmapRectF.width()
                    touchDownHeight = bitmapRectF.height()
                }
            }
        }
        return true
    }

    /**
     * 计算两个手指间的距离
     */
    private fun distance(event: MotionEvent): Float {
        //屏幕上触摸点不为2时返回0
        if (event.pointerCount != 2) {
            return 0f
        }
        val dx = event.getX(1) - event.getX(0)
        val dy = event.getY(1) - event.getY(0)
        /** 使用勾股定理返回两点之间的距离  */
        return Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()
    }

    /**
     * 计算两个手指间的中间点
     */
    private fun mid(event: MotionEvent): PointF? {
        //屏幕上触摸点不为2时返回0
        if (event.pointerCount != 2) {
            return null
        }
        val midX = (event.getX(1) + event.getX(0)) / 2
        val midY = (event.getY(1) + event.getY(0)) / 2
        return PointF(midX, midY)
    }

    /**
     * 根据当前图片的Matrix获得图片的范围
     *
     * @return
     */
    private val matrixRectF: RectF
        get() {
            val matrix = imageMatrix
            val rect = RectF()
            val d = drawable
            if (null != d) {
                rect[0f, 0f, d.intrinsicWidth.toFloat()] = d.intrinsicHeight.toFloat()
                if (BITMAP_MAX_WIDTH <= 0) {
                    BITMAP_MAX_WIDTH = d.intrinsicWidth.toFloat() / BASE_SCALE * MAX_SCALE
                }
                if (BITMAP_MAX_HEIGHT <= 0) {
                    BITMAP_MAX_HEIGHT = d.intrinsicHeight.toFloat() / BASE_SCALE * MAX_SCALE
                }
                matrix.mapRect(rect)
            }
            return rect
        }

    //第一次加载图片成功
    private fun bitmapInit(): Boolean {
        if (isNull(selectRectF)) {
            selectRectF = getSelectRectF()
        }

        drawable?.also {
            MAX_SCALE = (it.intrinsicWidth.coerceAtLeast(it.intrinsicHeight) / 700).toFloat()
            if (MAX_SCALE < BASE_SCALE) MAX_SCALE = BASE_SCALE
        }

        val bitmapRectF = matrixRectF
        if (isNull(bitmapRectF)) {
            return true
        }
        matrixa.set(imageMatrix)
        val bitmapWidth = bitmapRectF.width()
        val bitmapHeight = bitmapRectF.height()
        val selectWidth = selectRectF!!.width()
        val selectHeight = selectRectF!!.height()
        var dx = 0f //平移x
        var dy = 0f //平移y
        val scale = if (bitmapWidth < bitmapHeight) {
            BASE_SCALE * selectWidth / bitmapWidth
        } else {
            BASE_SCALE * selectHeight / bitmapHeight
        }
        val afterRectF = RectF()
        afterRectF.left = 0f
        afterRectF.top = 0f
        afterRectF.right = bitmapRectF.width() * scale
        afterRectF.bottom = bitmapRectF.height() * scale
        dx = selectRectF!!.centerX() - afterRectF.centerX()
        dy = selectRectF!!.centerY() - afterRectF.centerY()
        if (isMinScale(bitmapRectF, scale)) {
            matrixa.postScale(scale, scale)
        }
        matrixa.postTranslate(dx, dy)
        imageMatrix = matrixa
        return false
    }

    //平移操作
    private fun translate(event: MotionEvent) {
        if (isNull(selectRectF)) {
            return
        }
        val dx = event.x - startPoint.x // 得到x轴的移动距离
        val dy = event.y - startPoint.y // 得到x轴的移动距离
        // 在没有移动之前的位置上进行移动
        matrixa.set(currentMatrix)
        matrixa.postTranslate(dx, dy)
        imageMatrix = matrixa
    }

    //缩放操作
    private fun distanceScale(event: MotionEvent) {
        if (isNull(selectRectF)) {
            return
        }
        if (touchDownWidth <= 0 || touchDownHeight <= 0) {
            return
        }
        val endDis = distance(event) // 结束距离
        if (endDis > distanceFingers) { // 两个手指并拢在一起的时候像素大于阀值
            val scale = endDis / startDis // 得到缩放倍数
            if (isMaxScale(touchDownWidth, touchDownHeight, scale)) {
                matrixa.set(currentMatrix)
                matrixa.postScale(scale, scale, midPoint!!.x, midPoint!!.y)
                imageMatrix = matrixa
            }
        }
    }

    private fun resetBitmap() {
        val bitmapRectF = matrixRectF
        if (!isNull(bitmapRectF)) {
            resetBitmap(bitmapRectF)
        }
    }

    /**
     * 手松开后图片归位操作
     * 先判读图片是否需要归位操作
     */
    private fun resetBitmap(bitmapRectF: RectF) {
        if (isNull(selectRectF)) {
            return
        }
        if (isNull(bitmapRectF)) {
            return
        }

        //bitmap的矩形大小大于待选中的矩形
        if (bitmapRectF.width() >= selectRectF!!.width() && bitmapRectF.height() >= selectRectF!!.height()) {
            //不需要变形，只需要平移
            //bitmap的矩形是否包含待选中的矩形
            if (!bitmapRectF.contains(selectRectF!!)) {
                val spacing = getSpacing(bitmapRectF)
                postTranslate(spacing[0], spacing[1])
            }
        } else {
            //将bitmap的矩形和选择框对齐的偏移量
            var dx = selectRectF!!.centerX() - bitmapRectF.centerX()
            var dy = selectRectF!!.centerY() - bitmapRectF.centerY()
            val bitmapWidth = bitmapRectF.width()
            val bitmapHeight = bitmapRectF.height()
            val selectWidth = selectRectF!!.width()
            val selectHeight = selectRectF!!.height()

//            float minBitmapSize = bitmapWidth > bitmapHeight ? bitmapHeight : bitmapWidth;//bitmap中最小的边长
//            float minSelectSize = selectWidth > selectHeight ? selectHeight : selectWidth;//待选中矩形中最小的边长
            val scale = if (bitmapWidth > bitmapHeight) {
                selectHeight / bitmapHeight
            } else {
                selectWidth / bitmapWidth
            }

            //选择框中不包含bitmap的矩形
            if (!selectRectF!!.contains(bitmapRectF)) {
                //得到缩放后的bitmap矩形计算偏移量才正确
                val afterRectF = RectF()
                val bitmapCenterX = bitmapRectF.centerX()
                val bitmapCenterY = bitmapRectF.centerY()
                val afterWidth = bitmapRectF.width() * scale
                val afterHeight = bitmapRectF.height() * scale
                afterRectF.left = bitmapCenterX - afterWidth / 2
                afterRectF.top = bitmapCenterY - afterHeight / 2
                afterRectF.right = bitmapCenterX + afterWidth / 2
                afterRectF.bottom = bitmapCenterY + afterHeight / 2
                val spacing = getSpacing(afterRectF)
                if (spacing[0] != 0f && spacing[1] != 0f) {
                    dx = spacing[0]
                    dy = spacing[1]
                }
            }
            postScaleTranslate(scale, dx, dy)
        }
    }

    /**
     * 根据选中框和bitmap矩形，计算X，Y轴的偏移量，用于偏移动画
     *
     * @return 两个float的数组, float[0] = X轴的偏移量, float[1] = Y轴的偏移量
     */
    private fun getSpacing(bitmapRectF: RectF): FloatArray {
        val spacing = FloatArray(2)
        if (isNull(selectRectF)) {
            return spacing
        }
        if (isNull(bitmapRectF)) {
            return spacing
        }
        val leftSpacing = selectRectF!!.left - bitmapRectF.left
        val topSpacing = selectRectF!!.top - bitmapRectF.top
        val rightSpacing = selectRectF!!.right - bitmapRectF.right
        val bottomSpacing = selectRectF!!.bottom - bitmapRectF.bottom
        var spacingX =
            if (Math.abs(leftSpacing) < Math.abs(rightSpacing)) leftSpacing else rightSpacing
        var spacingY =
            if (Math.abs(topSpacing) < Math.abs(bottomSpacing)) topSpacing else bottomSpacing

//            if (isOverlapping(bitmapRectF, selectRectF)) {
//                //待选中矩形有部分矩形与bitmap重叠
//                if ((bitmapRectF.contains(selectRectF.left, selectRectF.top) && bitmapRectF.contains(selectRectF.right, selectRectF.top))
//                        || bitmapRectF.contains(selectRectF.left, selectRectF.bottom) && bitmapRectF.contains(selectRectF.right, selectRectF.bottom)) {
//                    //待选中矩形的left，right都在bitmap中时不进行平移操作
//                    spacingX = 0;
//                }
//                if ((bitmapRectF.contains(selectRectF.left, selectRectF.top) && bitmapRectF.contains(selectRectF.left, selectRectF.bottom))
//                        || (bitmapRectF.contains(selectRectF.right, selectRectF.top) && bitmapRectF.contains(selectRectF.right, selectRectF.bottom))) {
//                    //待选中矩形的top,bottom都在bitmap中时不进行平移操作
//                    spacingY = 0;
//                }
//            } else {
//                //没有任何重叠时，选取最小的距离进行偏移，如果x，y的距离相等时不进行操作
//                if (topSpacing <= 0 && bottomSpacing >= 0
//                        && ((leftSpacing > 0 && rightSpacing > 0) || (leftSpacing <= 0 && rightSpacing <= 0))) {
//                    spacingY = 0;
//                }else if (leftSpacing <= 0 && rightSpacing >= 0
//                        && ((topSpacing > 0 && bottomSpacing > 0) || (topSpacing <= 0 && bottomSpacing <= 0))) {
//                    spacingX = 0;
//                }
//            }
        if (topSpacing >= 0 && bottomSpacing <= 0 && (leftSpacing > 0 && rightSpacing > 0 || leftSpacing <= 0 && rightSpacing <= 0)) {
            spacingY = 0f
        } else if (leftSpacing >= 0 && rightSpacing <= 0 && (topSpacing > 0 && bottomSpacing > 0 || topSpacing <= 0 && bottomSpacing <= 0)) {
            spacingX = 0f
        }
        spacing[0] = spacingX
        spacing[1] = spacingY
        return spacing
    }

    //RectF是否可用
    private fun isNull(rectF: RectF?): Boolean {
        return if (null == rectF || rectF.width() == 0f && rectF.height() == 0f) {
            true
        } else {
            false
        }
    }

    //添加移动动画
    private fun postTranslate(spacingX: Float, spacingY: Float) {
        if (spacingX == 0f && spacingY == 0f) {
            return
        }
        val spacing = Math.sqrt((spacingX * spacingX + spacingY * spacingY).toDouble())
            .toFloat() //根据勾股定理计算移动距离
        val duration = getSpacingDuration(spacing)
        if (duration <= 0) {
            return
        }
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = duration.toLong()
        valueAnimator.interpolator = object : Interpolator {
            override fun getInterpolation(input: Float): Float {
                //当前动画插值器的值
                val thisInput = input - lastTranslateInterpolation
                if (thisInput > 0) {
                    matrixa.postTranslate(thisInput * spacingX, thisInput * spacingY)
                    imageMatrix = matrixa
                }
                lastTranslateInterpolation = input
                isCanClip = input >= 1
                return input
            }
        }
        valueAnimator.start()
        lastTranslateInterpolation = 0f
    }

    //缩放时平移动画
    private fun postScaleTranslate(scale: Float, spacingX: Float, spacingY: Float) {
        val defScale = 1f //默认倍数1,
        val inputScale = scale - defScale
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.duration = SCALE_ANIMATION_TIME.toLong()
        valueAnimator.interpolator = object : Interpolator {
            override fun getInterpolation(input: Float): Float {
                //当前动画插值器的值
                val thisInput = input - lastScaleTranslateInterpolation
                if (thisInput > 0) {
                    val bitmapRectF = matrixRectF
                    if (!isNull(bitmapRectF)) {
                        val tempScale = input * inputScale + defScale //当前的缩放目标倍数
                        val thisScale = tempScale / lastScaleTranslateScale
                        if (isMinScale(bitmapRectF, thisScale)) {
                            matrixa.postScale(
                                thisScale, thisScale, bitmapRectF.centerX(), bitmapRectF.centerY()
                            )
                            lastScaleTranslateScale = tempScale
                        }
                    }
                    matrixa.postTranslate(thisInput * spacingX, thisInput * spacingY)
                    imageMatrix = matrixa
                }
                lastScaleTranslateInterpolation = input
                isCanClip = input >= 1
                return input
            }
        }
        valueAnimator.start()
        lastScaleTranslateInterpolation = 0f
        lastScaleTranslateScale = 1f
    }

    //两个rectF是否重叠
    private fun isOverlapping(rectF: RectF, rectF2: RectF): Boolean {
        return if (!rectF.contains(rectF2.left, rectF2.top) && !rectF.contains(
                rectF2.right, rectF2.top
            ) && !rectF.contains(rectF2.left, rectF2.bottom) && !rectF.contains(
                rectF2.right, rectF2.bottom
            )
        ) {
            false
        } else true
    }

    /**
     * 根据滑动距离获取动画时间
     */
    private fun getSpacingDuration(spacing: Float): Int {
        var duration = (1.0f * spacing / MOVE_DISTANCE * MOVE_TIME).toInt()
        if (duration > MAX_ANIMATION_TIME) {
            duration = MAX_ANIMATION_TIME
        }
        return duration
    }

    /**
     * 缩放倍数小于最小倍数
     */
    private fun isMinScale(rectF: RectF, scale: Float): Boolean {
        if (isNull(rectF) || scale <= 0) {
            return false
        }
        val afterWidth = rectF.width() * scale
        val afterHeight = rectF.height() * scale
        return if (afterWidth <= SHOW_BITMAP_MIN_SIZE || afterHeight <= SHOW_BITMAP_MIN_SIZE) {
            false
        } else true
    }

    /**
     * 缩放倍数大于最大分辨率
     */
    private fun isMaxScale(touchDownWidth: Float, touchDownHeight: Float, scale: Float): Boolean {
        if (touchDownWidth <= 0 || touchDownHeight <= 0) {
            return false
        }
        val afterWidth = touchDownWidth * scale
        val afterHeight = touchDownHeight * scale
        if (afterWidth <= SHOW_BITMAP_MIN_SIZE || afterHeight <= SHOW_BITMAP_MIN_SIZE) {
            return false
        }
        return if (afterWidth < BITMAP_MAX_WIDTH && afterHeight < BITMAP_MAX_HEIGHT) {
            true
        } else {
            false
        }
    }

    fun getClipBitmap(): Bitmap? {
        return if (clipBoxType == CIRCLE_CLIP_BOX) {
            clipCircle()
        } else {
            clip()
        }
    }

    /**
     * 剪切图片，返回剪切后的bitmap对象
     *
     * @return
     */
    fun clip(): Bitmap? {
        if (!isCanClip) {
            return null
        }
        if (CIRCLE_CLIP_BOX == clipBoxType) {
            return clipCircle()
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        //将剪裁的图片压缩到500k以下，如果没需求就注释该段代码
        val baos = ByteArrayOutputStream()
        val maxKB = 200 //最大字节数,单位: KB
        var options = 100 //保存的图片自动压缩低于maxKB
        bitmap.compress(Bitmap.CompressFormat.PNG, options, baos)
        while (baos.toByteArray().size / 1024 > maxKB || options < 10) {
            baos.reset()
            options -= 5
            bitmap.compress(Bitmap.CompressFormat.PNG, options, baos)
        }
        val canvas = Canvas(bitmap)
        draw(canvas)
        val backBitmap = Bitmap.createBitmap(
            bitmap,
            selectRectF!!.left.toInt(),
            selectRectF!!.top.toInt(),
            selectRectF!!.width().toInt(),
            selectRectF!!.height().toInt()
        )
        return if (RECT_CLIP_BOX_ROUND == clipBoxType && rectRadius > 0) {
            getRectRadiusBitmap(backBitmap)
        } else {
            backBitmap
        }
    }

    /**
     * 剪切图片，返回剪切后的bitmap对象
     *
     * @return
     */
    fun clipCircle(): Bitmap? {
        if (!isCanClip) {
            return null
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        //将剪裁的图片压缩到500k以下，如果没需求就注释该段代码
        val baos = ByteArrayOutputStream()
        val maxKB = 200 //最大字节数,单位: KB
        var options = 100 //保存的图片自动压缩低于maxKB
        bitmap.compress(Bitmap.CompressFormat.PNG, options, baos)
        while (baos.toByteArray().size / 1024 > maxKB || options < 10) {
            baos.reset()
            options -= 5
            bitmap.compress(Bitmap.CompressFormat.PNG, options, baos)
        }
        val canvas = Canvas(bitmap)
        draw(canvas)
        var backBitmap = Bitmap.createBitmap(
            bitmap,
            selectRectF!!.left.toInt(),
            selectRectF!!.top.toInt(),
            selectRectF!!.width().toInt(),
            selectRectF!!.height().toInt()
        )
        backBitmap = getOvalBitmap(backBitmap)
        return backBitmap
    }

    private val clipRect = RectF() // 预分配并重用
    private val strokeRect = RectF() // 预分配并重用
    private val clipPath = Path() // 预分配并重用
    private val parentPath = Path() // 预分配并重用


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (selectRectF == null) {
            selectRectF = getSelectRectF()
        }
        val circleSize = selectRectF!!.width().toInt() //圆的直径
        val rectangleWidth = selectRectF!!.width().toInt() //矩形宽度
        val rectangleHeight = selectRectF!!.height().toInt() //矩形高度
        val parentWidth = width
        val parentHeight = height
        val centerX = parentWidth / 2
        val centerY = parentHeight / 2

        //绘制父层，并删除矩形内容
        parentPath.reset()
        parentPath.addRect(0f, 0f, parentWidth.toFloat(), parentHeight.toFloat(), Path.Direction.CW)
        when (clipBoxType) {
            CIRCLE_CLIP_BOX -> {
                val strokeCircleSize = circleSize + (
                        if (strokeWidth > 0) strokeWidth * 2 else 0
                        ) //带描边的圆半径，用于裁剪矩形空间
                clipPath.reset()
                clipPath.addCircle(
                    centerX.toFloat(),
                    centerY.toFloat(),
                    (strokeCircleSize / 2).toFloat(),
                    Path.Direction.CW
                )
                clipPath.addCircle(
                    centerX.toFloat(),
                    centerY.toFloat(),
                    (circleSize / 2 + 1).toFloat(),
                    Path.Direction.CCW
                )
                canvas.drawPath(clipPath, strokePaint!!)
                parentPath.addCircle(
                    centerX.toFloat(),
                    centerY.toFloat(),
                    (strokeCircleSize / 2).toFloat(),
                    Path.Direction.CCW
                )
            }
            RECT_CLIP_BOX -> {
                clipPath.reset() //不带描边的矩形，用于绘制外描边
                clipRect.left = (centerX - rectangleWidth / 2).toFloat()
                clipRect.top = (centerY - rectangleHeight / 2).toFloat()
                clipRect.right = (centerX + rectangleWidth / 2).toFloat()
                clipRect.bottom = (centerY + rectangleHeight / 2).toFloat()
                val strokeRectWidth = rectangleWidth + strokeWidth * 2
                val strokeRectHeight = rectangleHeight + strokeWidth * 2
                //带描边的矩形，用于裁剪矩形空间
                strokeRect.left = (centerX - strokeRectWidth / 2).toFloat()
                strokeRect.top = (centerY - strokeRectHeight / 2).toFloat()
                strokeRect.right = (centerX + strokeRectWidth / 2).toFloat()
                strokeRect.bottom = (centerY + strokeRectHeight / 2).toFloat()

                //绘制边框(两个矩形相减)，使用paint的边框会有部分边框绘制在矩形内导致裁剪图片时带边框
                clipPath.reset()
                clipPath.addRect(strokeRect, Path.Direction.CW)
                clipPath.addRect(clipRect, Path.Direction.CCW)
                canvas.drawPath(clipPath, strokePaint!!)
                parentPath.addRect(strokeRect, Path.Direction.CCW)
            }
            RECT_CLIP_BOX_ROUND -> {
                clipRect.left = (centerX - rectangleWidth / 2).toFloat()
                clipRect.top = (centerY - rectangleHeight / 2).toFloat()
                clipRect.right = (centerX + rectangleWidth / 2).toFloat()
                clipRect.bottom = (centerY + rectangleHeight / 2).toFloat()
                val strokeRectWidth = rectangleWidth + strokeWidth * 2
                val strokeRectHeight = rectangleHeight + strokeWidth * 2
                strokeRect.left = (centerX - strokeRectWidth / 2).toFloat()
                strokeRect.top = (centerY - strokeRectHeight / 2).toFloat()
                strokeRect.right = (centerX + strokeRectWidth / 2).toFloat()
                strokeRect.bottom = (centerY + strokeRectHeight / 2).toFloat()

                //绘制边框(两个矩形相减)，使用paint的边框会有部分边框绘制在矩形内导致裁剪图片时带边框
                clipPath.reset()
                clipPath.addRoundRect(
                    strokeRect, rectRadius.toFloat(), rectRadius.toFloat(), Path.Direction.CW
                )
                clipPath.addRoundRect(
                    clipRect, rectRadius.toFloat(), rectRadius.toFloat(), Path.Direction.CCW
                )
                canvas.drawPath(clipPath, strokePaint!!)
                parentPath.addRoundRect(
                    strokeRect, rectRadius.toFloat(), rectRadius.toFloat(), Path.Direction.CCW
                )
            }
            else -> {
                return
            }
        }
        canvas.drawPath(parentPath, paint!!)
    }

    /**
     * 获取选中的矩形
     */
    private fun getSelectRectF(): RectF {
        val select = RectF()
        val width = width
        val height = height
        if (width <= 0 || height <= 0) {
            return select
        }
        val centerX = width / 2
        val centerY = height / 2
        var selectWidth = 0
        var selectHeight = 0
        if (clipBoxType == CIRCLE_CLIP_BOX) {
            selectWidth = circleRadii
            selectHeight = circleRadii
        } else {
            selectWidth = rectWidth
            selectHeight = rectHeight
        }
        select.left = (centerX - selectWidth / 2).toFloat()
        select.top = (centerY - selectHeight / 2).toFloat()
        select.right = (centerX + selectWidth / 2).toFloat()
        select.bottom = (centerY + selectHeight / 2).toFloat()
        return select
    }

    fun setClipBoxType(clipBoxType: Int) {
        this.clipBoxType = clipBoxType
        selectRectF = getSelectRectF()
        if (ProductUtil.isMainThread()) {
            invalidate()
        } else {
            postInvalidate()
        }
    }

    fun setStrokeWidth(strokeWidth: Int) {
        this.strokeWidth = strokeWidth
        invalidate()
    }

    fun setStrokeColor(@ColorInt strokeColor: Int) {
        this.strokeColor = strokeColor
        if (strokePaint != null) {
            strokePaint!!.color = strokeColor
        }
        invalidate()
    }

    fun setMaskingColor(@ColorInt maskingColor: Int) {
        this.maskingColor = maskingColor
        if (paint != null) {
            paint!!.color = maskingColor
        }
        invalidate()
    }

    fun setCircleRadii(circleRadii: Int) {
        this.circleRadii = circleRadii
        selectRectF = getSelectRectF()
        invalidate()
        resetBitmap()
    }

    fun setRectWidth(rectWidth: Int) {
        this.rectWidth = rectWidth
        selectRectF = getSelectRectF()
        invalidate()
        resetBitmap()
    }

    fun setRectHeight(rectHeight: Int) {
        this.rectHeight = rectHeight
        selectRectF = getSelectRectF()
        invalidate()
        resetBitmap()
    }

    fun setRectRadius(rectRadius: Int) {
        this.rectRadius = rectRadius
        invalidate()
        resetBitmap()
    }

    companion object {
        const val CIRCLE_CLIP_BOX = 1 //圆形裁剪框
        const val RECT_CLIP_BOX = 2 //矩形裁剪框
        const val RECT_CLIP_BOX_ROUND = 3 //矩形圆角裁剪框

        /**
         * 拖拉照片模式
         */
        private const val MODE_DRAG = 1

        /**
         * 放大缩小照片模式
         */
        private const val MODE_ZOOM = 2

    }


    /**
     * 获取圆形bitmap
     */
    private fun getOvalBitmap(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(
            bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val clipPaint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        clipPaint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        clipPaint.color = Color.WHITE
        canvas.drawOval(rectF, clipPaint)
        clipPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, clipPaint)
        return output
    }


    /**
     * 获取圆角矩形bitmap
     */
    private fun getRectRadiusBitmap(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(
            bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(output)
        val clipPaint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        clipPaint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        clipPaint.color = Color.WHITE
        canvas.drawRoundRect(rectF, rectRadius.toFloat(), rectRadius.toFloat(), clipPaint)
        clipPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, clipPaint)
        return output
    }

}