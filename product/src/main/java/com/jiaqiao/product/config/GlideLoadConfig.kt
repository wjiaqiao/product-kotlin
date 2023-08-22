package com.jiaqiao.product.config

import android.graphics.drawable.Drawable
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager

/**
 * glide加载器的参数配置
 * */
object GlideLoadConfig {

    /**
     * 加载string类型数据的拦截器
     * */
    var loadStringInterceptor: ((RequestManager, String) -> RequestBuilder<Drawable?>?)? = null

}