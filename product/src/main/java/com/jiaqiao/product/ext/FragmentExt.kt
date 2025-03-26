package com.jiaqiao.product.ext

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.jiaqiao.product.helper.contract.IntentContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

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
 * 动态注册intent回调器
 * [contract] 输入输出的ActivityResultContract
 * [callback] 结果回调器
 */
fun <I, O> Fragment.registerResult(
    contract: ActivityResultContract<I, O>,
    callback: ActivityResultCallback<O>
): ActivityResultLauncher<I> {
    val resultLauncher = requireActivity().activityResultRegistry.register(
        (this::class.java.name + contract::class.java.simpleName),
        contract,
        callback
    )
    lifecycle.addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (Lifecycle.Event.ON_DESTROY == event) {
                resultLauncher.unregister()
            }
        }
    })
    return resultLauncher
}

/**
 * 跳转intent并回调结果
 * [toIntent] 待跳转的intent
 * [resultAction] 结果回调器，int -> resultCode（结果码）,Intent -> intent（存放结果返回的数据）
 */
fun Fragment.intentResult(
    toIntent: Intent,
    resultAction: (Int, Intent?) -> Unit
) {
    registerResult(IntentContract()) {
        resultAction.invoke(it.first, it.second)
    }.launch(toIntent)
}


/**
 * 运行协程范围
 * */
fun Fragment.launch(block: suspend CoroutineScope.() -> Unit): Job {
    return launch(block, EmptyCoroutineContext)
}

/**
 * 运行协程范围
 * */
fun Fragment.launch(
    block: suspend CoroutineScope.() -> Unit,
    coroutineContext: CoroutineContext = EmptyCoroutineContext
): Job {
    return lifecycleScope.launch(coroutineContext, block = block)
}

/**
 * 运行在子线程
 * */
fun Fragment.launchIo(block: suspend CoroutineScope.() -> Unit): Job {
    return launch(block, Dispatchers.IO)
}

/**
 * 运行在主线程
 * */
fun Fragment.launchMain(block: suspend CoroutineScope.() -> Unit): Job {
    return launch(block, Dispatchers.Main)
}

/**
 * 运行在默认线程
 * */
fun Fragment.launchDefault(block: suspend CoroutineScope.() -> Unit): Job {
    return launch(block, Dispatchers.Default)
}
