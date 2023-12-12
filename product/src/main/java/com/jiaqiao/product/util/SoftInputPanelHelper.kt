package com.jiaqiao.product.util

import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.jiaqiao.product.ext.setPaddingBottom

/**
 * 软键盘的辅助类，用于监听键盘高度变化
 * [activity] 绑定的activity
 * [setPadBot] 是否将键盘高度设置为rootview的paddingbottom
 */
class SoftInputPanelHelper(val activity: AppCompatActivity, val setPadBot: Boolean = false) {

    //手机软键盘高度
    var softInputHeight = 0
        private set

    /**
     * 软键盘高度变化回调事件
     * {View} rootview
     * {Int} 键盘高度，为0键盘关闭
     */
    var changeSoftInputHeightAction: ((View, Int) -> Unit)? = null

    //手机导航栏高度
    private var navigationBarHeight = 0

    //判断手机是否显示导航栏
    private var hasNavigationBar = false

    //activity的rootview
    private var rootView: View? = null

    //获取rootview大小
    private val rect by lazy { Rect() }

    //rootview宽高变化时监听器
    private val onPreDrawListener by lazy {
        ViewTreeObserver.OnPreDrawListener {
            rootView?.let {
                it.getWindowVisibleDisplayFrame(rect)
                val screenHeight = it.height
                val keypadHeight =
                    if (hasNavigationBar) screenHeight - rect.bottom - navigationBarHeight else screenHeight - rect.bottom
                if (keypadHeight >= 0) {
                    if (softInputHeight == keypadHeight) return@let
                    softInputHeight = keypadHeight
                    if (setPadBot) it.setPaddingBottom(softInputHeight)
                    changeSoftInputHeightAction?.invoke(it, softInputHeight)
                }
            }
            true
        }
    }

    //自动销毁的lifecycle生命周期
    private val lifecycleDestory by lazy {
        object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (Lifecycle.Event.ON_DESTROY == event) {
                    activity.lifecycle.removeObserver(this)
                    rootView?.viewTreeObserver?.removeOnPreDrawListener(onPreDrawListener)
                }
            }
        }
    }

    //绑定activity
    fun bind() {
        rootView = activity.window.decorView
        if (rootView?.measuredWidth ?: 0 > 0) {
            setRootViewListener()
        } else {
            rootView?.post { setRootViewListener() }
        }
    }

    //设置rootview的监听器
    private fun setRootViewListener() {
        rootView?.viewTreeObserver?.removeOnPreDrawListener(onPreDrawListener)
        rootView!!.viewTreeObserver.addOnPreDrawListener(onPreDrawListener)
        activity.lifecycle.removeObserver(lifecycleDestory)
        activity.lifecycle.addObserver(lifecycleDestory)
    }

}