package com.jiaqiao.product.ext

import androidx.fragment.app.Fragment
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


/**
 * 添加异常捕获，防止崩溃
 * 异常信息：Can't access the Fragment View's LifecycleOwner for UserFragment{55876be} (301459f2-457b-4270-b208-2b1aee1190a4) when getView() is null i.e., before onCreateView() or after onDestroyView()
 *
 * */
fun <T> LiveData<T>.observeFragment(fragment: Fragment, observer: Observer<T>) {
    runPlogCatch {
        this.observe(fragment.viewLifecycleOwner, observer)
    }
}


/**
 * 添加异常捕获，防止崩溃
 * 异常信息：Can't access the Fragment View's LifecycleOwner for UserFragment{55876be} (301459f2-457b-4270-b208-2b1aee1190a4) when getView() is null i.e., before onCreateView() or after onDestroyView()
 *
 * */
fun <T> LiveData<T>.observeAlwaysFragment(fragment: Fragment, observer: (T) -> Unit) {
    runPlogCatch {
        this.observeAlways(fragment.viewLifecycleOwner, observer = observer)
    }
}