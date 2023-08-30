package com.jiaqiao.product.widget

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.jiaqiao.product.config.RVDecorationConfig
import com.jiaqiao.product.ext.isNull


/**
 * recyclerview 的分割线
 * */
open class RVDecoration : RecyclerView.ItemDecoration {

    var decorationSize = 1 //分割线大小，单位：px
    private var decorationColor = Color.TRANSPARENT //分割线颜色
    var isDrawAll = false //是否在item周围绘制分割线
    private val paint by lazy {
        Paint().also {
            it.isAntiAlias = true
            it.color = decorationColor
        }
    }

    constructor() {
        defConfig()
        loadConfig()
    }

    constructor(decorationSize: Int) {
        defConfig()
        this.decorationSize = decorationSize
        loadConfig()
    }

    constructor(decorationSize: Int, decorationColor: Int) {
        defConfig()
        this.decorationSize = decorationSize
        this.decorationColor = decorationColor
        loadConfig()
    }

    constructor(decorationSize: Int, isDrawAll: Boolean) {
        defConfig()
        this.decorationSize = decorationSize
        this.isDrawAll = isDrawAll
        loadConfig()
    }

    constructor(decorationSize: Int, decorationColor: Int, isDrawAll: Boolean) {
        defConfig()
        this.decorationSize = decorationSize
        this.decorationColor = decorationColor
        this.isDrawAll = isDrawAll
        loadConfig()
    }


    /**
     * 加载默认全局配置
     */
    private fun defConfig() {
        decorationSize = RVDecorationConfig.decorationSize
        decorationColor = RVDecorationConfig.decorationColor
        isDrawAll = RVDecorationConfig.isDrawAll
    }

    private fun loadConfig() {
        paint.isAntiAlias = true
        paint.color = decorationColor
    }

    fun getDecorationColor(): Int {
        return decorationColor
    }

    fun setDecorationColor(decorationColor: Int) {
        this.decorationColor = decorationColor
        paint.color = decorationColor
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        /**
         * 设置 item 四周的边距大小，
         * 可以设置 recyclerview 背景色和 item 四周的边距，
         * 使得 item 四周的边距透出 recyclerview 背景色来达到分割线的目的。
         *
         * item其实就是一个矩形区域，set方法就是设置了这个矩形区域每个边框线的粗细
         * 参数依次是左、上、右、下
         */
        if (parent?.adapter.isNull() || parent?.layoutManager.isNull()) {
            return
        }
        val layoutManager: RecyclerView.LayoutManager = parent.layoutManager!!
        if (layoutManager !is LinearLayoutManager
            && layoutManager !is GridLayoutManager
            && layoutManager !is StaggeredGridLayoutManager
        ) {
            return
        }
        val childPosi = parent.getChildAdapterPosition(view)
        val count = state.itemCount
        when (layoutManager) {
            is GridLayoutManager -> {
                val gridLayoutManager = layoutManager as GridLayoutManager
                var spanCount = gridLayoutManager.spanCount //每行个数
                val spanIndex =
                    gridLayoutManager.spanSizeLookup.getSpanIndex(
                        childPosi,
                        gridLayoutManager.spanCount
                    )
                val spanSize = gridLayoutManager.spanSizeLookup.getSpanSize(childPosi)
                when (gridLayoutManager.orientation) {
                    RecyclerView.HORIZONTAL -> {
                        //如果出现小数的话，可能会出现微小的偏差
                        val realVerticalSize =
                            if (decorationSize % spanCount == 0) decorationSize else decorationSize / spanCount * spanCount
                        val eachItemHorDividerSize =
                            if (spanSize == spanCount) (if (isDrawAll) 2 * realVerticalSize else 0)
                            else ((spanCount + if (isDrawAll) 1 else -1) * realVerticalSize / spanCount)
                        if (isDrawAll) {
                            outRect.top =
                                (spanIndex + 1) * realVerticalSize - spanIndex * eachItemHorDividerSize
                            outRect.bottom = eachItemHorDividerSize - outRect.top
                            outRect.left = if (spanIndex == childPosi) decorationSize else 0
                            if (spanSize == spanCount) {
                                outRect.right = 0
                            } else {
                                outRect.right = decorationSize
                            }
                        } else {
                            outRect.top = spanIndex * (realVerticalSize - eachItemHorDividerSize)
                            outRect.bottom = eachItemHorDividerSize - outRect.top
                            outRect.left = 0
                            if (spanSize == spanCount) {
                                outRect.right = 0
                            } else {
                                outRect.right =
                                    if (isInLastRowOrColumn(
                                            childPosi,
                                            count,
                                            spanCount,
                                            layoutManager
                                        )
                                    )
                                        0 else decorationSize
                            }
                        }
                    }
                    else -> {
                        val realHorizontalSize =
                            if (decorationSize % spanCount == 0) decorationSize else decorationSize / spanCount * spanCount
                        val eachItemHorDividerSize =
                            if (spanSize == spanCount) (if (isDrawAll) 2 * realHorizontalSize else 0)
                            else ((spanCount + if (isDrawAll) 1 else -1) * realHorizontalSize / spanCount)
                        if (isDrawAll) {
                            outRect.left =
                                (spanIndex + 1) * realHorizontalSize - spanIndex * eachItemHorDividerSize
                            outRect.right = eachItemHorDividerSize - outRect.left
                            outRect.top = if (spanIndex == childPosi) decorationSize else 0
                            if (spanSize == spanCount) {
                                outRect.bottom = 0
                            } else {
                                outRect.bottom = decorationSize
                            }
                        } else {
                            outRect.left = spanIndex * (realHorizontalSize - eachItemHorDividerSize)
                            outRect.right = eachItemHorDividerSize - outRect.left
                            outRect.top = 0
                            if (spanSize == spanCount) {
                                outRect.bottom = 0
                            } else {
                                outRect.bottom =
                                    if (isInLastRowOrColumn(
                                            childPosi,
                                            count,
                                            spanCount,
                                            layoutManager
                                        )
                                    )
                                        0 else decorationSize
                            }
                        }
                    }
                }
            }
            is LinearLayoutManager -> {
                val linearLayoutManager = layoutManager as LinearLayoutManager
                when (linearLayoutManager.orientation) {
                    RecyclerView.HORIZONTAL -> {
                        outRect.right = decorationSize
                        if (childPosi == 0) {
                            outRect.left = if (isDrawAll) decorationSize else 0
                        } else if (childPosi == count - 1) {
                            outRect.right = if (isDrawAll) decorationSize else 0
                        }
                        outRect.bottom = if (isDrawAll) decorationSize else 0
                        outRect.top = if (isDrawAll) decorationSize else 0
                    }
                    else -> {
                        outRect.bottom = decorationSize
                        if (childPosi == 0) {
                            outRect.top = if (isDrawAll) decorationSize else 0
                        } else if (childPosi == count - 1) {
                            outRect.bottom = if (isDrawAll) decorationSize else 0
                        }
                        outRect.left = if (isDrawAll) decorationSize else 0
                        outRect.right = if (isDrawAll) decorationSize else 0
                    }
                }
            }
            is StaggeredGridLayoutManager -> {
                val staggeredGridLayoutManager = layoutManager as StaggeredGridLayoutManager
                var spanCount = staggeredGridLayoutManager.spanCount //每行个数
                val spanIndex = childPosi % spanCount

            }
        }
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(canvas, parent, state)
        if (parent?.adapter.isNull() || parent?.layoutManager.isNull()) {
            return
        }
        val layoutManager: RecyclerView.LayoutManager = parent.layoutManager!!
        if (layoutManager !is LinearLayoutManager
            && layoutManager !is GridLayoutManager
            && layoutManager !is StaggeredGridLayoutManager
        ) {
            return
        }
        if (parent.clipToPadding) {
            parent.clipToPadding = false
        }
        for (i in 0 until parent.childCount) {
            val childView = parent.getChildAt(i)
            val leftDecWidth = layoutManager.getLeftDecorationWidth(childView)
            val topDecHeight = layoutManager.getTopDecorationHeight(childView)
            val rightDecWidth = layoutManager.getRightDecorationWidth(childView)
            val bottomDecHeight = layoutManager.getBottomDecorationHeight(childView)
            if (leftDecWidth > 0) {
                canvas.drawRect(
                    (
                            childView.left - leftDecWidth).toFloat(),
                    childView.top.toFloat(),
                    childView.left.toFloat(),
                    childView.bottom
                        .toFloat(), paint!!
                )
            }
            if (topDecHeight > 0) {
                canvas.drawRect(
                    (
                            childView.left - leftDecWidth).toFloat(), (
                            childView.top - topDecHeight).toFloat(), (
                            childView.right + rightDecWidth).toFloat(),
                    childView.top
                        .toFloat(), paint!!
                )
            }
            if (rightDecWidth > 0) {
                canvas.drawRect(
                    childView.right.toFloat(),
                    childView.top.toFloat(), (
                            childView.right + rightDecWidth).toFloat(),
                    childView.bottom
                        .toFloat(), paint!!
                )
            }
            if (bottomDecHeight > 0) {
                canvas.drawRect(
                    (
                            childView.left - leftDecWidth).toFloat(),
                    childView.bottom.toFloat(), (
                            childView.right + rightDecWidth).toFloat(), (
                            childView.bottom + bottomDecHeight
                            ).toFloat(), paint!!
                )
            }
        }
    }

    /**
     * [itemPosition] is in last row for Vertical or last column for Horizontal
     */
    private fun isInLastRowOrColumn(
        itemPosition: Int, itemCount: Int, spanCount: Int,
        layoutManager: GridLayoutManager
    ): Boolean {
        val lastPosition = itemCount - 1
        if (itemPosition == lastPosition) {
            //最后一个 item 肯定在最后一行（列）
            return true
        }
        if (lastPosition - itemPosition < spanCount) {
            //有可能是最后一行的下标，候选
            //上一行（列）的最后一个 position
            var beforeLastItemPosition = -1
            for (i in lastPosition - 1 downTo lastPosition - spanCount) {
                //从倒数第二个开始到候选 item 的最后一个，
                // 如果这中间有上一行（列）的最后一个，则这个 item 包括它之前的 item 都不在最后一行
                val spanIndex =
                    layoutManager.spanSizeLookup.getSpanIndex(i, layoutManager.spanCount)
                val spanSize = layoutManager.spanSizeLookup.getSpanSize(i)
                if (spanIndex + spanSize == spanCount) {
                    beforeLastItemPosition = i
                    break
                }
            }
            return itemPosition > beforeLastItemPosition
        }
        return false
    }


}
