package com.jiaqiao.product.ext

import android.net.Uri
import android.widget.ImageView
import com.jiaqiao.product.util.GlideLoad
import java.io.File


/**
 * 加载网络图片，图片加载成功之前显示默认app资源ID
 * [url] 网络图片地址
 * [defResId] app资源ID
 * */
fun ImageView.load(url: String, defResId: Int = -1) {
    GlideLoad.load(this, url, defResId)
}

/**
 * 加载本地手机文件
 * [file] 手机文件
 * [defResId] app资源ID
 * */
fun ImageView.load(file: File, defResId: Int = -1) {
    GlideLoad.load(this, file, defResId)
}

/**
 * 加载app资源图片，图片加载成功之前显示默认app资源ID
 * [resId] app资源ID
 * [defResId] app资源ID
 * */
fun ImageView.load(resId: Int, defResId: Int = -1) {
    GlideLoad.load(this, resId, defResId)
}


/**
 * 加载Uri资源图片，图片加载成功之前显示默认app资源ID
 * [uri] Uri路径
 * [defResId] app资源ID
 * */
fun ImageView.load(uri: Uri, defResId: Int = -1) {
    GlideLoad.load(this, uri, defResId)
}

/**
 * 使用drawable方式加载网络图片，图片加载成功之前显示默认app资源ID
 * [url] 网络图片地址
 * [defResId] app资源ID
 * */
fun ImageView.loadDrawable(url: String, defResId: Int = -1) {
    GlideLoad.loadDrawable(this, url, defResId)
}
