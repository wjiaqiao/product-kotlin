package com.jiaqiao.product.ext

import android.graphics.*
import com.jiaqiao.product.util.ProductUtil
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer


/**
 *将bitmap转换成ByteArray
 * @return 返回bytearray
 **/
fun Bitmap?.toByteArray(): ByteArray {
    return if (isNull()) {
        byteArrayOf()
    } else {
        val buf: ByteBuffer = ByteBuffer.allocate(this!!.byteCount)
        this.copyPixelsToBuffer(buf)
        buf.array()
    }
}

/**
 * 将bitmap缩放至指定宽高，如果缩放前后的宽高比不一致会导致bitmap变形
 * [toWidth] 缩放后的宽度
 * [toHeight] 缩放后的高度
 * @return 缩放后bitmap
 * */
fun Bitmap.zoomTo(toWidth: Int, toHeight: Int): Bitmap {
    // 计算缩放比例
    val scaleWidth = toWidth.toFloat() / width
    val scaleHeight = toHeight.toFloat() / height
    // 取得想要缩放的matrix参数
    val matrix = Matrix()
    matrix.postScale(scaleWidth, scaleHeight)
    return Bitmap.createBitmap(
        this, 0, 0, width, height, matrix,
        true
    )
}

/**
 * 将bitmap保存为png图片
 * [file] 保存png图片的路径
 * @return 返回保存结果
 * */
fun Bitmap.savePng(filePath: String): Boolean {
    return savePng(File(filePath))
}

/**
 * 将bitmap保存为png图片
 * [file] 保存png图片的路径
 * @return 返回保存结果
 * */
fun Bitmap.savePng(file: File): Boolean {
    return kotlin.runCatching {
        file.parentFile.createFloder()
        val bos = BufferedOutputStream(FileOutputStream(file))
        this.compress(Bitmap.CompressFormat.PNG, 100, bos)
        bos.flush()
        bos.close()
        true
    }.onFailure {
        it.plogE()
    }.getOrDefault(false)
}

/**
 * 将bitmap保存为jpeg图片
 * [file] 保存jpeg图片的路径
 * @return 返回保存结果
 * */
fun Bitmap.saveJpeg(filePath: String): Boolean {
    return saveJpeg(File(filePath))
}

/**
 * 将bitmap保存为jpeg图片
 * [file] 保存jpeg图片的路径
 * @return 返回保存结果
 * */
fun Bitmap.saveJpeg(file: File): Boolean {
    return kotlin.runCatching {
        file.parentFile.createFloder()
        val bos = BufferedOutputStream(FileOutputStream(file))
        this.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        bos.flush()
        bos.close()
        true
    }.onFailure {
        it.plogE()
    }.getOrDefault(false)
}

/**
 * 回收bitmap释放内存，回收后bitmap将无法使用，在确保后续不使用bitmap的情况下回收
 * */
fun Bitmap?.toRecycle() {
    if (notNull() && this?.isRecycled.isTrue()) {
        this?.recycle()
    }
}

/**
 * Bitmap添加背景色
 * [color] 背景色色值
 * */
fun Bitmap.addBgColor(color: Int): Bitmap {
    val paint = Paint().also {
        it.isAntiAlias = true
        it.isDither = true
    }
    paint.color = color //背景色
    val bitmapAft = Bitmap.createBitmap(
        width,
        height, config
    )
    val canvas = Canvas(bitmapAft)
    canvas.drawRect(
        0f,
        0f,
        width.toFloat(),
        height.toFloat(),
        paint
    )
    canvas.drawBitmap(
        this,
        Rect(0, 0, width, height),
        Rect(0, 0, bitmapAft.width, bitmapAft.height),
        paint
    )
    return bitmapAft
}

/**
 * 将[coverBitmap]放置在bitmap图层上方
 * [coverBitmap] 顶部图层bitmap
 * */
fun Bitmap.coverBitmap(coverBitmap: Bitmap): Bitmap {
    val paint = Paint().also {
        it.isAntiAlias = true
        it.isDither = true
    }
    val bitmapAft = Bitmap.createBitmap(
        width,
        height, config
    )
    val canvas = Canvas(bitmapAft)
    canvas.drawBitmap(
        this,
        Rect(0, 0, width, height),
        Rect(0, 0, bitmapAft.width, bitmapAft.height),
        paint
    )
    canvas.drawBitmap(
        coverBitmap,
        Rect(0, 0, coverBitmap.width, coverBitmap.height),
        Rect(0, 0, bitmapAft.width, bitmapAft.height),
        paint
    )
    return bitmapAft
}

/**
 * 根据宽高比例裁剪bitmap
 * [widthScale] 宽度比例
 * [heightScale] 高度比例
 * */
fun Bitmap.cropScale(widthScale: Int, heightScale: Int): Bitmap {
    val paint = Paint().also {
        it.isAntiAlias = true
        it.isDither = true
    }
    val infoMaxPiex = ProductUtil.getScaleMaxPiex(width, height, widthScale, heightScale)
    val realWidth = infoMaxPiex[0]
    val realHeight = infoMaxPiex[1]
    return if (realWidth == width && realHeight == height) {
        val bitmapAft = Bitmap.createBitmap(
            width,
            height, config
        )
        val canvas = Canvas(bitmapAft)
        canvas.drawBitmap(
            this,
            Rect(0, 0, width, height),
            Rect(0, 0, bitmapAft.width, bitmapAft.height),
            paint
        )
        bitmapAft
    } else {
        val bitmapAft = Bitmap.createBitmap(
            realWidth,
            realHeight, config
        )
        val canvas = Canvas(bitmapAft)
        val cw = width / 2
        val ch = height / 2
        val acw = bitmapAft.width / 2
        val ach = bitmapAft.height / 2
        canvas.drawBitmap(
            this,
            Rect(cw - acw, ch - ach, cw + acw, ch + ach),
            Rect(0, 0, bitmapAft.width, bitmapAft.height),
            paint
        )
        bitmapAft
    }
}


/**
 * 裁剪成圆形，原图不是正方形时以宽为准顶部裁剪
 *
 * [bgColor] 图片裁剪背景颜色，默认透明色
 * */
fun Bitmap.cropRound(bgColor: Int = Color.TRANSPARENT): Bitmap {
    val circleBitmap = Bitmap.createBitmap(width, width, config)
    val canvas = Canvas(circleBitmap)
    val paint = Paint().also {
        it.isAntiAlias = true
        it.isDither = true
    }
    val rect = Rect(0, 0, width, width)
    val rectF = RectF(0f, 0f, width.toFloat(), width.toFloat())
    var roundPx = width.toFloat()
    paint.isAntiAlias = true
    canvas.drawColor(bgColor)
    paint.color = Color.WHITE
    canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, rect, rect, paint)
    return circleBitmap
}

/**
 * 将bitmap裁剪对应圆角
 * [radius] 圆角，单位：像素
 * */
fun Bitmap.radius(radius: Int): Bitmap {
    val circleBitmap = Bitmap.createBitmap(width, height, config)
    val canvas = Canvas(circleBitmap)
    val paint = Paint().also {
        it.isAntiAlias = true
        it.isDither = true
    }
    val rect = Rect(0, 0, width, height)
    val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
    var roundPx = radius.toFloat()
    paint.isAntiAlias = true
    canvas.drawColor(Color.TRANSPARENT)
    paint.color = Color.WHITE
    canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, rect, rect, paint)
    return circleBitmap
}

/**
 * 等比例缩放至指定宽度，高度等比例计算
 * [toWidth] 缩放后的宽度
 * */
fun Bitmap.ratioOfWidth(toWidth: Int): Bitmap {
    return zoomTo(toWidth, (1.0f * height / width * toWidth).toInt())
}

/**
 * 等比例缩放至指定高度，宽度等比例计算
 * [toHeight] 缩放后的高度
 * */
fun Bitmap.ratioOfHeight(toHeight: Int): Bitmap {
    return zoomTo((1.0f * width /  height * toHeight).toInt(), toHeight)
}