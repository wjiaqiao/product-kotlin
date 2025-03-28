package com.example.productkotlin.util

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.example.productkotlin.view.dialog.LoadDia

/**
 * 加载框管理工具
 * */
object LoadDialogUtil {

    private val diaList by lazy { mutableMapOf<Int, LoadDia>() }

    //显示加载框
    fun showLoadDialog(activity: FragmentActivity) {
        val key = activity.hashCode()
        if (!diaList.containsKey(key)) {
            activity.lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        removeLoadDialog(activity)
                    }
                }
            })
            diaList[key] = LoadDia(activity)
        }
        diaList[key]?.also {
            if (!it.isShowing) it.show()
        }
    }

    //隐藏加载框
    fun hideLoadDialog(activity: FragmentActivity) {
        val key = activity.hashCode()
        if (diaList.containsKey(key)) {
            diaList[key]?.also {
                if (it.isShowing) it.dismiss()
            }
        }
    }

    //移除加载框
    private fun removeLoadDialog(activity: FragmentActivity) {
        hideLoadDialog(activity)
        diaList.remove(activity.hashCode())
    }
}