package com.jiaqiao.product.ext

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding


/**
 * 创建viewbinding对象
 * */
fun <VB : ViewBinding> Dialog.createViewBinding(): VB {
    return viewBindingClass(this) { clazz ->
        clazz.getMethod("inflate", LayoutInflater::class.java)
            .invoke(null, this.context.layoutInflater()) as VB
    }
}

/**
 * 创建viewbinding对象
 * */
fun <VB : ViewBinding> Dialog.createViewBindingAndInflateParent(root: View): VB {
    return viewBindingClass(this) { clazz ->
        clazz.getMethod(
            "inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java
        ).invoke(null, this.context.layoutInflater(), root, true) as VB
    }
}