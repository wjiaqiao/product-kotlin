package com.jiaqiao.product.ext

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

//方便区分不同状态的观察者事件
fun Lifecycle.runLifecycleEvent(block: (Lifecycle.Event) -> Unit) {
    addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            block(event)
            if (event == Lifecycle.Event.ON_DESTROY) {
                //自动移除观察者
                this@runLifecycleEvent.removeObserver(this)
            }
        }
    })
}

//运行在 targetEvent 状态时
fun Lifecycle.runOnEvent(targetEvent: Lifecycle.Event, block: () -> Unit) {
    addObserver(object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == targetEvent) {
                block()
            }
            if (event == Lifecycle.Event.ON_DESTROY) {
                //自动移除观察者
                this@runOnEvent.removeObserver(this)
            }
        }
    })
}

//运行在Create时
fun Lifecycle.runOnCreate(block: () -> Unit) {
    return this.runOnEvent(Lifecycle.Event.ON_CREATE, block)
}

//运行在Start时
fun Lifecycle.runOnStart(block: () -> Unit) {
    return this.runOnEvent(Lifecycle.Event.ON_START, block)
}

//运行在Resume时
fun Lifecycle.runOnResume(block: () -> Unit) {
    return this.runOnEvent(Lifecycle.Event.ON_RESUME, block)
}

//运行在Pause时
fun Lifecycle.runOnPause(block: () -> Unit) {
    return this.runOnEvent(Lifecycle.Event.ON_PAUSE, block)
}

//运行在Stop时
fun Lifecycle.runOnStop(block: () -> Unit) {
    return this.runOnEvent(Lifecycle.Event.ON_STOP, block)
}

//运行在Destroy时
fun Lifecycle.runOnDestroy(block: () -> Unit) {
    return this.runOnEvent(Lifecycle.Event.ON_DESTROY, block)
}
