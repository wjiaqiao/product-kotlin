package com.jiaqiao.product.util

import android.view.View
import com.jiaqiao.product.config.ViewConfig

/**
 * view工具类
 * */
object ProductViewUtil {

    private val viewClickAttachChange by lazy {
        object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {

            }

            override fun onViewDetachedFromWindow(v: View) {
                removeClickTime(getViewKey(v))
            }

        }
    }

    /**
     * 存储点击时间戳的map
     * */
    private val clickMap by lazy { mutableMapOf<String, Long>() }

    /**
     * 带点击事件过滤的点击事件
     * [view]  view控件
     * [shortInterval] 最短点击间隔
     * [clickInvoke] 点击事件回调
     * */
    fun click(view: View, shortInterval: Int = 0, clickInvoke: () -> Unit) {
        view.removeOnAttachStateChangeListener(viewClickAttachChange)
        view.addOnAttachStateChangeListener(viewClickAttachChange)
        view.setOnClickListener {
            val relInterval =
                if (shortInterval <= 0) ViewConfig.clickShortInterval else shortInterval
            if (relInterval <= 0) {
                clickInvoke.invoke()
            } else {
                val keyId = getViewKey(view)
                val lastTime = getClickTime(keyId)
                val nowTime = System.currentTimeMillis()
                if ((nowTime - lastTime) >= relInterval) {
                    clickInvoke.invoke()
                    putClickTime(keyId, nowTime)
                }
            }
        }
    }

    /**
     * 根据keyID获取对应view控件的点击时间
     * */
    fun getClickTime(id: String): Long {
        return clickMap[id] ?: -1
    }

    /**
     * 根据keyID获取对应view控件的点击时间
     * */
    fun putClickTime(id: String, time: Long) {
        clickMap[id] = time
    }

    /**
     * 根据keyID移除对应view控件的点击参数
     * */
    fun removeClickTime(id: String) {
        if (clickMap.containsKey(id)) {
            clickMap.remove(id)
        }
    }

    /**
     * 获取view对应的keyID
     * */
    fun getViewKey(view: View): String {
        return "${view.context.hashCode()}_${view.id}_${view.hashCode()}"
    }

    /**
     * 清空所有按钮的点击时间
     */
    fun cleanClickMap() {
        clickMap.clear()
    }


}