package com.jiaqiao.product.ext

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jiaqiao.product.config.ViewConfig
import com.jiaqiao.product.util.ProductViewUtil

/**
 * BaseQuickAdapter设置过滤重复点击事件的监听器
 * */
fun BaseQuickAdapter<*, *>.setOnItemRepeatClickListener(
    shortInterval: Int = 0,
    listener: OnItemClickListener
) {
    this.setOnItemClickListener { ada, view, position ->
        val relInterval = if (shortInterval <= 0) ViewConfig.clickShortInterval else shortInterval
        if (relInterval <= 0) {
            listener.onItemClick(ada, view, position)
        } else {
            val keyId = ProductViewUtil.getViewKey(view)
            val lastTime = ProductViewUtil.getClickTime(keyId)
            val nowTime = System.currentTimeMillis()
            if (lastTime.isNull() || (nowTime - lastTime!!) >= relInterval) {
                listener.onItemClick(ada, view, position)
                ProductViewUtil.putClickTime(keyId, nowTime)
            }
        }
    }
}

/**
 * BaseQuickAdapter设置过滤重复点击子控件事件的监听器
 * */
fun BaseQuickAdapter<*, *>.setOnItemChildRepeatClickListener(
    shortInterval: Int = 0,
    listener: OnItemChildClickListener
) {
    this.setOnItemChildClickListener { ada, view, position ->
        val relInterval = if (shortInterval <= 0) ViewConfig.clickShortInterval else shortInterval
        if (relInterval <= 0) {
            listener.onItemChildClick(ada, view, position)
        } else {
            val keyId = ProductViewUtil.getViewKey(view)
            val lastTime = ProductViewUtil.getClickTime(keyId)
            val nowTime = System.currentTimeMillis()
            if (lastTime.isNull() || (nowTime - lastTime!!) >= relInterval) {
                listener.onItemChildClick(ada, view, position)
                ProductViewUtil.putClickTime(keyId, nowTime)
            }
        }
    }
}

/**
 * BaseQuickAdapter刷新单个[t]数据
 * setData刷新时会闪动
 * notifyItemChanged刷新时不会将数据添加进data中，导致item重用时数据加载错误
 * */
fun <T> BaseQuickAdapter<T, *>.notifyItemChangedData(index: Int, t: T) {
    if (index >= this.data.size) {
        return
    }
    this.data[index] = t
    notifyItemChanged(index + headerLayoutCount, t)
}
