package com.jiaqiao.product.ext

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding


/**
 * 创建viewbinding对象
 * */
fun <VB : ViewBinding> AppCompatActivity.createViewBinding(): VB =
    viewBindingClass<VB>(this) { clazz ->
        clazz.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB
    }
