package com.jiaqiao.product.ext

import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType


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

