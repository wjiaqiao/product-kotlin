package com.jiaqiao.product.ext

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.jiaqiao.product.helper.contract.IntentContract
import com.jiaqiao.product.util.ProductUiUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 动态注册intent回调器
 * [contract] 输入输出的ActivityResultContract
 * [callback] 结果回调器
 */
fun <I, O> ComponentActivity.registerResult(
    contract: ActivityResultContract<I, O>,
    callback: ActivityResultCallback<O>
): ActivityResultLauncher<I> {
    val resultLauncher = activityResultRegistry.register(
        (this.hashCode().toString() + contract::class.java.simpleName),
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
fun ComponentActivity.intentResult(
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
fun ComponentActivity.launch(block: suspend CoroutineScope.() -> Unit): Job {
    return launch(block, EmptyCoroutineContext)
}

/**
 * 运行协程范围
 * */
fun ComponentActivity.launch(
    block: suspend CoroutineScope.() -> Unit,
    coroutineContext: CoroutineContext = EmptyCoroutineContext
): Job {
    return lifecycleScope.launch(coroutineContext, block = block)
}

/**
 * 运行在子线程
 * */
fun ComponentActivity.launchIo(block: suspend CoroutineScope.() -> Unit): Job {
    return launch(block, Dispatchers.IO)
}

/**
 * 运行在主线程
 * */
fun ComponentActivity.launchMain(block: suspend CoroutineScope.() -> Unit): Job {
    return launch(block, Dispatchers.Main)
}

/**
 * 运行在默认线程
 * */
fun ComponentActivity.launchDefault(block: suspend CoroutineScope.() -> Unit): Job {
    return launch(block, Dispatchers.Default)
}

/**
 * 判断当前设备是否存在导航栏
 */
fun ComponentActivity.hasNavigationBar(): Boolean {
    return ProductUiUtil.hasNavigationBar(this)
}

/**
 * 状态栏字体是否是白色
 * */
fun ComponentActivity.isStatusBarWhiteFont(): Boolean {
    return window.isStatusBarWhiteFont()
}

/**
 * 状态栏字体是否是黑色
 * */
fun ComponentActivity.isStatusBarBlackFont(): Boolean {
    return window.isStatusBarBlackFont()
}