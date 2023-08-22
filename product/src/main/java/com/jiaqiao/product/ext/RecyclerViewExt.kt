package com.jiaqiao.product.ext

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.JustifyContent
import com.jiaqiao.product.view.ProductFlexboxLayoutManager


/**
 *设置垂直方向的recyclerview
 * */
fun RecyclerView.vertical(): RecyclerView {
    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    return this
}

/**
 *设置水平方向的recyclerview
 * */
fun RecyclerView.horizontal(): RecyclerView {
    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    return this
}


/**
 *设置网格布局垂直方向的recyclerview
 * */
fun RecyclerView.gridVertical(spanCount: Int): RecyclerView {
    layoutManager = GridLayoutManager(context, spanCount, RecyclerView.VERTICAL, false)
    return this
}

/**
 *设置网格布局水平方向的recyclerview
 * */
fun RecyclerView.gridHorizontal(spanCount: Int): RecyclerView {
    layoutManager = GridLayoutManager(context, spanCount, RecyclerView.HORIZONTAL, false)
    return this
}

/**
 *设置瀑布流布局水平方向的recyclerview
 * */
fun RecyclerView.staggeredHorizontal(spanCount: Int): RecyclerView {
    layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.HORIZONTAL)
    return this
}

/**
 *设置瀑布流布局垂直方向的recyclerview
 * */
fun RecyclerView.staggeredVertical(spanCount: Int): RecyclerView {
    layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
    return this
}


/**
 *设置Flexbox布局垂直方向的recyclerview
 *
 * [justifyContent]子空间排序方式，默认排序方式：[JustifyContent.FLEX_START]
 * */
fun RecyclerView.flexbox(
    @FlexDirection flexDirection: Int = FlexDirection.ROW,
    @JustifyContent justifyContent: Int = JustifyContent.FLEX_START
): RecyclerView {
    layoutManager = ProductFlexboxLayoutManager(context, flexDirection).also {
        it.justifyContent = justifyContent
    }
    return this
}
