package com.jiaqiao.product.util

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.jiaqiao.product.config.GlideLoadConfig
import com.jiaqiao.product.ext.isNull
import com.jiaqiao.product.ext.notNull
import com.jiaqiao.product.ext.plog
import java.io.File

/**
 * 控制imageview的展示工具类
 */
object GlideLoad {

    /**
     * 设置imageviwe
     *
     * [imageView]      展示出的图片控件
     * [url]            图片的网络地址
     * [defResourcesId] 默认资源ID
     */
    fun load(imageView: ImageView, url: String, defResourcesId: Int = -1) {
        val rm = Glide.with(imageView)
        var rbd = if (GlideLoadConfig.loadStringInterceptor.notNull()) {
            GlideLoadConfig.loadStringInterceptor?.invoke(rm, url)
        } else {
            rm.load(url)
        }
        if (rbd.notNull()) {
            if (defResourcesId > 0) {
                val requestOptions = RequestOptions()
                requestOptions.error(defResourcesId).placeholder(defResourcesId)
                    .fallback(defResourcesId)
                rbd!!.apply(requestOptions)
            }
            rbd!!.into(imageView)
        }
    }

    /**
     * 设置imageviwe
     *
     * [imageView]      展示出的图片控件
     * [file]           图片的file文件
     * [defResourcesId] 默认资源ID
     */
    fun load(imageView: ImageView, file: File?, defResourcesId: Int = -1) {
        var rbd = Glide.with(imageView).load(file)
        if (defResourcesId > 0) {
            val requestOptions = RequestOptions()
            requestOptions.error(defResourcesId).placeholder(defResourcesId)
                .fallback(defResourcesId)
            rbd.apply(requestOptions)
        }
        rbd.into(imageView)
    }

    /**
     * 设置imageviwe
     *
     * [imageView]      展示出的图片控件
     * [uri]            图片的uri
     * [defResourcesId] 默认资源ID
     */
    fun load(imageView: ImageView, uri: Uri?, defResourcesId: Int = -1) {
        var rbd = Glide.with(imageView).load(uri)
        if (defResourcesId > 0) {
            val requestOptions = RequestOptions()
            requestOptions.error(defResourcesId).placeholder(defResourcesId)
                .fallback(defResourcesId)
            rbd.apply(requestOptions)
        }
        rbd.into(imageView)
    }

    /**
     * 设置imageviwe
     *
     * [imageView]      展示出的图片控件
     * [resId]          资源ID
     * [defResourcesId] 默认资源ID
     */
    fun load(imageView: ImageView, resId: Int, defResourcesId: Int = -1) {
        var rbd = Glide.with(imageView).load(resId)
        if (defResourcesId > 0) {
            val requestOptions = RequestOptions()
            requestOptions.error(defResourcesId).placeholder(defResourcesId)
                .fallback(defResourcesId)
            rbd.apply(requestOptions)
        }
        rbd.into(imageView)
    }

    /**
     * 设置imageviwe
     *
     * [imageView]      展示出的图片控件
     * [drawable]       资源
     * [defResourcesId] 默认资源ID
     */
    fun load(imageView: ImageView, drawable: Drawable?, defResourcesId: Int = -1) {
        if (drawable.isNull()) {
            Glide.with(imageView).clear(imageView)
            imageView.setImageBitmap(null)
            return
        }
        var rbd = Glide.with(imageView).load(drawable)
        if (defResourcesId > 0) {
            val requestOptions = RequestOptions()
            requestOptions.error(defResourcesId).placeholder(defResourcesId)
                .fallback(defResourcesId)
            rbd.apply(requestOptions)
        }
        rbd.into(imageView)
    }

    /**
     * 设置imageviwe
     *
     * [imageView]      展示出的图片控件
     * [url]            图片的网络地址
     * [defResourcesId] 默认资源ID
     */
    fun loadDrawable(imageView: ImageView, url: String, defResourcesId: Int = -1) {
        val rm = Glide.with(imageView)
        var rbd = if (GlideLoadConfig.loadStringInterceptor.notNull()) {
            GlideLoadConfig.loadStringInterceptor?.invoke(rm, url)
        } else {
            rm.load(url)
        }
        if (rbd.notNull()) {
            if (defResourcesId > 0) {
                val requestOptions = RequestOptions()
                requestOptions.error(defResourcesId).placeholder(defResourcesId)
                    .fallback(defResourcesId)
                rbd!!.apply(requestOptions)
            }
            rbd!!.into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    imageView.setImageDrawable(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
        }
    }

}