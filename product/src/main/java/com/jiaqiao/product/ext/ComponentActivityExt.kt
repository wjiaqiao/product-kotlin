package com.jiaqiao.product.ext

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.jiaqiao.product.helper.contract.IntentContract

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