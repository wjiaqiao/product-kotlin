package com.jiaqiao.product.ext

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

/**
 * 创建viewbinding对象
 * */
fun <VB : ViewBinding> AppCompatActivity.createViewBinding(): VB =
    viewBindingClass<VB>(this) { clazz ->
        clazz.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB
    }

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

/**
 * 创建viewbinding对象
 * [layoutInflater]  layout解析器
 * [parent]  父容器
 * [attachToParent]  是否添加进父容器
 * */
fun <VB : ViewBinding> Fragment.createViewBinding(
    layoutInflater: LayoutInflater,
    parent: ViewGroup?,
    attachToParent: Boolean
): VB =
    viewBindingClass<VB>(this) { clazz ->
        clazz.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        ).invoke(null, layoutInflater, parent, attachToParent) as VB
    }

/**
 * 获取viewbinding泛型的class对象
 * [any] viewbinding泛型父类的对象
 * [actualPosi] 泛型的序号，从0开始
 * [block]获取泛型后的回调
 * */
fun <VB : ViewBinding> viewBindingClass(
    any: Any,
    actualPosi: Int = 0,
    block: (Class<VB>) -> VB
): VB {
    val genericSuperclass = any.javaClass.genericSuperclass
    val superclass = any.javaClass.superclass
    while (superclass != null) {
        if (genericSuperclass is ParameterizedType) {
            try {
                return block.invoke(genericSuperclass.actualTypeArguments[actualPosi] as Class<VB>)
            } catch (thr: Throwable) {
                throw thr
            }
        }
    }
    throw IllegalArgumentException("未找到ViewBinding泛型")
}

