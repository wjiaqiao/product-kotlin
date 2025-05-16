package com.jiaqiao.product.ext

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer


/**
 * 添加始终活动的观察者，并添加主动销毁操作
 * */
fun <T> LiveData<T>?.observeAlways(
    lifecycleOwner: LifecycleOwner,
    lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    observer: Observer<T>
) {
    this?.also { liveData ->
        liveData.observeForever(observer)
        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (lifeEvent == event) liveData.removeObserver(observer)
            }
        })
    }
}