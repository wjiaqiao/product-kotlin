package com.jiaqiao.product.util

import android.content.Context
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding

object ViewBindingUtil {

    /**
     * 反射获取viewbinding对象
     *
     * [classs] viewbinding的class
     * [context]  context对象
     *
     * */
    fun <T : ViewBinding> create(classs: Class<T>, context: Context): T {
        val method = classs.getDeclaredMethod("inflate", LayoutInflater::class.java)
        return method.invoke(null, LayoutInflater.from(context)) as T
    }

}