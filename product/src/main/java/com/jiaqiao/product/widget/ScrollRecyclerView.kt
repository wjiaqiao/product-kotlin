package com.jiaqiao.product.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jiaqiao.product.ext.isNull
import kotlin.math.abs


/**
 * 带滑动过程的recyclerview
 */
open class ScrollRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    private val SCROLL_TO_START = 1
    private val SCROLL_TO_CENTER = 2
    private val SCROLL_TO_END = 3
    private var scrollPosiType = 0
    private val MIN_SCROLL_HEIGHT = 50 //滑动小于的距离时进行偏移滑动
    private var mShouldScroll = false //目标项是否在最后一个可见项之后
    private var nextPosition = -1 //记录目标项位置
    private var fixScroll = 0 //滑动偏移量
    private var isSmooth = false //滑动时是否带滚动过程

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> if (mShouldScroll) {
                mShouldScroll = false
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
        if (mShouldScroll) {
            if (abs(if (isVertical) dy else dx) <= MIN_SCROLL_HEIGHT) {
                smoothMoveToPositionRel(nextPosition)
            }
        }
    }

    private val topItemPosition: Int
        get() {
            if (layoutManager is LinearLayoutManager) {
                val linearLayoutManager: LinearLayoutManager =
                    layoutManager as LinearLayoutManager
                return linearLayoutManager.findFirstVisibleItemPosition()
            }
            return -1
        }

    /**
     * 将子项滑动到中间
     *
     * [position]  子项序号
     * [fixScroll]子项距离中间偏移，单位：像素
     */
    fun movePositionCenter(position: Int, fixScroll: Int = 0) {
        if (layoutManager.isNull()) {
            return
        }
        scrollPosiType = SCROLL_TO_CENTER
        this.fixScroll = fixScroll
        isSmooth = false
        smoothMoveToPositionRel(position)
    }

    /**
     * 将子项滑动到底部（垂直布局）或右边（水平布局）
     *
     * [position]  子项序号
     * [fixScroll]子项距离底部（垂直布局）或右边（水平布局）偏移，单位：像素
     */
    fun movePositionEnd(position: Int, fixScroll: Int = 0) {
        if (layoutManager.isNull()) {
            return
        }
        scrollPosiType = SCROLL_TO_END
        this.fixScroll = fixScroll
        isSmooth = false
        smoothMoveToPositionRel(position)
    }

    /**
     * 将子项滑动到顶部（垂直布局）或左边（水平布局）
     *
     * [position]  子项序号
     * [fixScroll]子项距离顶部（垂直布局）或左边（水平布局）偏移，单位：像素
     */
    fun movePositionStart(position: Int, fixScroll: Int = 0) {
        if (layoutManager.isNull()) {
            return
        }
        scrollPosiType = SCROLL_TO_START
        this.fixScroll = fixScroll
        isSmooth = false
        smoothMoveToPositionRel(position)
    }

    /**
     * 将子项滑动到顶部（垂直布局）或左边（水平布局）
     *
     * [position] 子项序号
     */
    fun moveToPosition(position: Int) {
        movePositionStart(position, 0)
    }

    /**
     * 将子项滑动到顶部（垂直布局）或左边（水平布局）
     *
     * [position]  子项序号
     * [fixScroll]子项距离顶部（垂直布局）或左边（水平布局）偏移，单位：像素
     */
    fun moveToPosition(position: Int, fixScroll: Int) {
        movePositionStart(position, fixScroll)
    }

    /**
     * 将子项滑动到中间
     *
     * [position]  子项序号
     * [fixScroll]子项距离中间偏移，单位：像素
     */
    fun smoothMovePositionCenter(position: Int, fixScroll: Int = 0) {
        if (layoutManager.isNull()) {
            return
        }
        scrollPosiType = SCROLL_TO_CENTER
        this.fixScroll = fixScroll
        isSmooth = true
        smoothMoveToPositionRel(position)
    }

    /**
     * 将子项滑动到底部（垂直布局）或右边（水平布局）
     *
     * [position]  子项序号
     * [fixScroll]子项距离底部（垂直布局）或右边（水平布局）偏移，单位：像素
     */
    fun smoothMovePositionEnd(position: Int, fixScroll: Int = 0) {
        if (layoutManager.isNull()) {
            return
        }
        scrollPosiType = SCROLL_TO_END
        this.fixScroll = fixScroll
        isSmooth = true
        smoothMoveToPositionRel(position)
    }

    /**
     * 将子项滑动到顶部（垂直布局）或左边（水平布局）
     *
     * [position]  子项序号
     * [fixScroll]子项距离顶部（垂直布局）或左边（水平布局）偏移，单位：像素
     */
    fun smoothMovePositionStart(position: Int, fixScroll: Int = 0) {
        if (layoutManager.isNull()) {
            return
        }
        scrollPosiType = SCROLL_TO_START
        this.fixScroll = fixScroll
        isSmooth = true
        smoothMoveToPositionRel(position)
    }

    /**
     * 将子项滑动到顶部（垂直布局）或左边（水平布局）
     *
     * [position] 子项序号
     */
    fun smoothMoveToPosition(position: Int) {
        smoothMovePositionStart(position, 0)
    }

    /**
     * 将子项滑动到顶部（垂直布局）或左边（水平布局）
     *
     * [position]  子项序号
     * [fixScroll]子项距离顶部（垂直布局）或左边（水平布局）偏移，单位：像素
     */
    fun smoothMoveToPosition(position: Int, fixScroll: Int) {
        smoothMovePositionStart(position, fixScroll)
    }

    /**
     * 滑动到指定位置
     */
    private fun smoothMoveToPositionRel(position: Int) {
        mShouldScroll = false
        val firstItem: Int = getChildLayoutPosition(getChildAt(0))
        val movePosition = position - firstItem
        if (movePosition >= 0 && movePosition < getChildCount()) {
            stopScroll()
            val view: View = getChildAt(movePosition)
            val viewLeft: Int
            val viewTop: Int
            val viewRight: Int
            val viewBottom: Int
            if (view.layoutParams is MarginLayoutParams) {
                val marginLayoutParams = view.layoutParams as MarginLayoutParams
                viewLeft = view.left - marginLayoutParams.leftMargin
                viewTop = view.top - marginLayoutParams.topMargin
                viewRight = view.right + marginLayoutParams.rightMargin
                viewBottom = view.bottom + marginLayoutParams.bottomMargin
            } else {
                viewLeft = view.left
                viewTop = view.top
                viewRight = view.right
                viewBottom = view.bottom
            }
            val isVerticalModel = isVertical
            val paddingLeft: Int = getPaddingLeft()
            val paddingTop: Int = getPaddingTop()
            val paddingRight: Int = getPaddingRight()
            val paddingBottom: Int = getPaddingBottom()
            val parentWidth: Int = getMeasuredWidth()
            val parentHeight: Int = getMeasuredHeight()
            var scrollBenNum = 0
            var benchmarkNum = 0
            val scrollType = scrollPosiType
            when (scrollType) {
                SCROLL_TO_START -> {
                    scrollBenNum = if (isVerticalModel) viewTop else viewLeft
                    benchmarkNum = if (isVerticalModel) paddingTop else paddingLeft
                }

                SCROLL_TO_CENTER -> {
                    scrollBenNum =
                        if (isVerticalModel) (viewBottom + viewTop) / 2 else (viewRight + viewLeft) / 2
                    benchmarkNum = if (isVerticalModel) parentHeight / 2 else parentWidth / 2
                }

                SCROLL_TO_END -> {
                    scrollBenNum = if (isVerticalModel) viewBottom else viewRight
                    benchmarkNum =
                        if (isVerticalModel) parentHeight - paddingBottom else parentWidth - paddingRight
                }

                else -> return
            }
            val scrollNum = scrollBenNum - benchmarkNum - fixScroll
            if (scrollNum != 0) {
                if (isVerticalModel) {
                    if (isSmooth) {
                        post { smoothScrollBy(0, scrollNum) }
                    } else {
                        post { scrollBy(0, scrollNum) }
                    }
                } else {
                    if (isSmooth) {
                        post { smoothScrollBy(scrollNum, 0) }
                    } else {
                        post { scrollBy(scrollNum, 0) }
                    }
                }
            }
        } else {
            if (isSmooth) {
                smoothScrollToPosition(position)
            } else {
                scrollToPosition(position)
            }
            nextPosition = position
            mShouldScroll = true
        }
    }

    private val isVertical: Boolean
        get() = layoutManager?.canScrollVertically() ?: false
}
