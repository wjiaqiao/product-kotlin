package com.jiaqiao.product.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.jiaqiao.product.R
import com.jiaqiao.product.ext.notNull
import com.jiaqiao.product.ext.plogE
import com.jiaqiao.product.ext.runPlogCatch
import com.jiaqiao.product.ext.setHeight
import com.jiaqiao.product.util.FlingUtil

/**
 * 解决NestedScrollView嵌套recyclerview引发的 无法复用item、无限高度、嵌套滑动异常 等问题
 */
open class ProductNestedScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {


    //recyclerview顶部的间距，可以达到滑动后显示tab的效果
    private var rvMarginBar = 0

    //需要绑定的recyclerview
    private var rv: RecyclerView? = null
    private var rvId = 0
    private var heightRun: Runnable? = null

    //recyclerview顶部控件隐藏区域的高度，减去rvMarginBar
    private var topContentHideHeight = 0

    //判断是否绑定recyclerview
    private val isBindRV get() = rv.notNull()

    init {
        attrs?.let {
            context.obtainStyledAttributes(it, R.styleable.ProductNestedScrollView).let { array ->

                rvMarginBar = array.getDimensionPixelOffset(
                    R.styleable.ProductNestedScrollView_pns_rv_margin_bar,
                    0
                )
                rvId = array.getResourceId(
                    R.styleable.ProductNestedScrollView_pns_rv_id,
                    0
                )

                array.recycle()
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (rvId != 0) {
            runPlogCatch {
                val view: View = findViewById(rvId)
                if (view is RecyclerView) {
                    rv = view
                }
            }.onFailure {
                rv = null
            }
            rv?.let {
                it.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                    refreshRVHeight()
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        refreshRVHeight()
    }

    /**
     * 注意这里嵌套滑动的主导者是RecyclerView（所有嵌套滑动的主导者都是子view询问父view是否需要优先滚动）
     * 当NestedScrollView消耗不完的事件才交给RecyclerView来处理
     * */
    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        if (isBindRV) {
            if (scrollY < topContentHideHeight) {
                if (scrollY + dy <= topContentHideHeight) {//NestedScrollView能处理所有的滚动事件（内容区域已经还在显示），所以没有剩余的滑动给RecyclerView
                    scrollBy(0, dy)
                    consumed[1] = dy
                } else if (scrollY + dy > topContentHideHeight) {//内容区域已经全部隐藏，所以剩余滑动事件交给RecyclerView
                    val scrollViewNeedScrollY = topContentHideHeight - scrollY
                    scrollBy(0, scrollViewNeedScrollY)
                    consumed[1] = scrollViewNeedScrollY
                }
            } else {
                super.onNestedPreScroll(target, dx, dy, consumed, type)
            }
        } else {
            super.onNestedPreScroll(target, dx, dy, consumed, type)
        }
    }

    /**
     * 注意这里的Fling不是属于传统意义的嵌套滑动的范畴，所以没有重写onNestedFling，这里的主导view是NestedScrollView
     * 当其没有消耗的事件才交给recyclerView来处理
     * */
    override fun fling(velocityY: Int) {
        if (isBindRV) {
            val dy = FlingUtil.getDistanceByVelocity(context, velocityY)
            if (scrollY < topContentHideHeight) {
                if (scrollY + dy <= topContentHideHeight) {
                    super.fling(velocityY)
                } else if (scrollY + dy > topContentHideHeight) {
                    val scrollViewNeedScrollY = topContentHideHeight - scrollY
                    //让NestedScrollView先处理所有的滚动事件
                    val scrollViewNeedVelocity =
                        FlingUtil.getVelocityByDistance(context, scrollViewNeedScrollY.toDouble())
                    if (velocityY > 0) {
                        super.fling(scrollViewNeedVelocity)
                    } else {
                        super.fling(-scrollViewNeedVelocity)
                    }
                    //把剩余的滚动事件交给RecyclerView处理
                    val recyclerViewScrollY = dy - scrollViewNeedScrollY
                    val recyclerViewNeedVelocity =
                        FlingUtil.getVelocityByDistance(context, recyclerViewScrollY)
                    if (velocityY > 0) {
                        rv?.fling(0, recyclerViewNeedVelocity)
                    } else {
                        rv?.fling(0, -recyclerViewNeedVelocity)
                    }
                }
            } else {
                super.fling(velocityY)
            }
        } else {
            super.fling(velocityY)
        }
    }

    private fun refreshRVHeight() {
        if (isBindRV) {
            topContentHideHeight = rv!!.top - top - rvMarginBar
            if (topContentHideHeight <= 0) {
                topContentHideHeight = 0
                setRvHeight(measuredHeight)
            } else {
                setRvHeight(measuredHeight - rvMarginBar)
            }
        }
    }

    //设置recyclerview的宽度，在父容器调整控制宽高时需要使用post延时设置rv的高度
    private fun setRvHeight(height: Int) {
        if (heightRun.notNull()) {
            rv?.removeCallbacks(heightRun)
        }
        heightRun = Runnable {
            rv?.setHeight(height)
        }
        rv?.post(heightRun)
    }

}