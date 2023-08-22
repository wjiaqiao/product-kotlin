package com.jiaqiao.product.ext

import androidx.lifecycle.*

/**
 * 添加始终活动的观察者，并添加主动销毁操作
 * */
inline fun <reified T> LiveData<T>?.observeAlways(
    lifecycleOwner: LifecycleOwner,
    lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    crossinline observer: (T) -> Unit
) {
    this?.also { liveData ->
        val obser = Observer<T> {
            observer.invoke(it)
        }
        liveData.observeForever(obser)
        lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (lifeEvent == event) liveData.removeObserver(obser)
            }
        })
    }
}