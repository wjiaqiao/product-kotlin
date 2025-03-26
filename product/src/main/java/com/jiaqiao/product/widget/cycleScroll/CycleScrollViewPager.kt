package com.jiaqiao.product.widget.cycleScroll

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.jiaqiao.product.R


open class CycleScrollViewPager @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) :
    ViewPager(context, attrs) {
    //设置无限滑动
    //是否可以无限滑动
    var isCycleScroll = true //是否循环滑动
    private var mOnPageChangeListener: SimpleOnPageChangeListener? = null
    private var cycleAdapter: CycleViewPagerViewAdapter<*, *>? = null
    private var pageChangeListener: OnPageChangeListener? = null
    private var lastChangeBack = -1
    private var isAutoScroll = false //是否自动滑动
    var intervalTime = 3000 //滑动间隔时间，单位：毫秒
    private var scrollTime = 500 //滑动时间，单位：毫秒
    private var autoScrollRun: Runnable? = null //滑动的事件
    private var isAutoScrollRun = false //滑动事件是否进行中
    private var pagerScroller: ViewPagerScroller? = null //滑动工具


    init {
        isCycleScroll = true
        isAutoScroll = false
        intervalTime = 3000
        autoScrollRun = null
        scrollTime = 500

        // 读取配置
        if (attrs != null) {
            val array: TypedArray =
                context.obtainStyledAttributes(attrs, R.styleable.CycleScrollViewPager)
            isCycleScroll =
                array.getBoolean(R.styleable.CycleScrollViewPager_csvp_cycle_scroll, isCycleScroll)
            isAutoScroll =
                array.getBoolean(R.styleable.CycleScrollViewPager_csvp_auto_scroll, isAutoScroll)
            intervalTime =
                array.getInteger(R.styleable.CycleScrollViewPager_csvp_interval_time, intervalTime)
            scrollTime =
                array.getInteger(R.styleable.CycleScrollViewPager_csvp_scroll_time, scrollTime)
            array.recycle()
        }
        if (intervalTime <= 0) {
            intervalTime = 3000
        }
        if (scrollTime <= 0) {
            scrollTime = 2000
        }
        if (mOnPageChangeListener == null) {
            mOnPageChangeListener = object : SimpleOnPageChangeListener() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    onStateChanged(state)
                }

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    changeBack(realCurrentItem)
                }
            }
        }
        removeOnPageChangeListener(mOnPageChangeListener!!)
        addOnPageChangeListener(mOnPageChangeListener!!)
        pagerScroller = ViewPagerScroller(context)
        pagerScroller!!.setScrollDuration(scrollTime) //设置时间，时间越长，速度越慢
        pagerScroller!!.initViewPagerScroll(this)
    }


    //viewpager滑动状态变化
    private fun onStateChanged(state: Int) {
        if (!isCanCycleScroll) {
            return
        }
        val currentItem = currentItem
        val count = adapterRealCount
        if (count <= 0) {
            return
        }
        when (state) {
            0 -> {
                if (currentItem == 0) {
                    setCurrentItem(count, false)
                } else if (currentItem == count + 1) {
                    setCurrentItem(1, false)
                }
                if (isAutoScrollRun) {
                    startAutoScroll()
                }
            }
            1 -> if (currentItem == count + 1) {
                setCurrentItem(1, false)
            } else if (currentItem == 0) {
                setCurrentItem(count, false)
            }
            2 -> {
            }
        }
    }

    override fun setAdapter(adapter: PagerAdapter?) {
        super.setAdapter(adapter)
        if (adapter is CycleViewPagerViewAdapter<*, *>) {
            cycleAdapter = adapter
            cycleAdapter!!.isCycleScroll = isCycleScroll
            setRealCurrentItem(0, false)
        } else {
            cycleAdapter = null
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        checkAutoScroll()
    }

    fun setRealCurrentItem(item: Int, smoothScroll: Boolean) {
        var item1 = item
        if (isCanCycleScroll) {
            item1 += 1
        }
        super.setCurrentItem(item1, smoothScroll)
    }

    var realCurrentItem: Int
        get() = getRealCurrentItem(currentItem)
        set(item) {
            var item = item
            if (isCanCycleScroll) {
                item = item + 1
            }
            super.setCurrentItem(item)
        }

    fun getRealCurrentItem(currentItem: Int): Int {
        return if (isCanCycleScroll) {
            val count = adapterRealCount
            if (currentItem == 0) {
                count - 1
            } else if (currentItem == count + 1) {
                0
            } else {
                currentItem - 1
            }
        } else {
            getCurrentItem()
        }
    }

    //adapter是否可用
    fun isCycleAdapter(): Boolean {
        return cycleAdapter != null && cycleAdapter is CycleViewPagerViewAdapter<*, *>
    }

    //获取adapter总数包含头尾两个占位view
    val adapterCount: Int
        get() = if (isCycleAdapter()) {
            cycleAdapter!!.count
        } else {
            0
        }

    //获取真实总数
    val adapterRealCount: Int
        get() {
            return if (isCycleAdapter()) {
                cycleAdapter!!.realCount
            } else {
                0
            }
        }

    //是否可以使用无限滑动
    val isCanCycleScroll: Boolean
        get() = (isCycleScroll
                && isCycleAdapter()
                && (adapterRealCount > 1))

    fun setPageChangeListener(pageChangeListener: OnPageChangeListener?) {
        this.pageChangeListener = pageChangeListener
        lastChangeBack = -1
    }

    private fun changeBack(realPosition: Int) {
        if (lastChangeBack == realPosition) {
            return
        }
        lastChangeBack = realPosition
        if (pageChangeListener != null) {
            pageChangeListener!!.changePosition(realPosition)
        }
    }

    interface OnPageChangeListener {
        fun changePosition(position: Int)
    }

    private fun isAutoScrollRun(): Boolean {
        return (isCanCycleScroll
                && isAutoScroll
                && isShown)
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        checkAutoScroll()
    }

    override fun onDetachedFromWindow() {
        distoryAutoScroll()
        super.onDetachedFromWindow()
    }

    private fun checkAutoScroll() {
        if (isAutoScrollRun) {
            return
        }
        startAutoScroll()
    }

    private fun startAutoScroll() {
        if (!isAutoScrollRun()) {
            distoryAutoScroll()
            return
        }
        if (autoScrollRun == null) {
            autoScrollRun = Runnable {
                if (!isAutoScrollRun()) {
                    distoryAutoScroll()
                    return@Runnable
                }
                val currentItem = currentItem
                val count = adapterCount
                if (count < 0) {
                    distoryAutoScroll()
                    return@Runnable
                }
                val nextPosi = (currentItem + 1) % count
                setCurrentItem(nextPosi, true)
            }
        }
        postDelayed(autoScrollRun, intervalTime.toLong())
        isAutoScrollRun = true
    }

    //注销滑动事件的定时器
    private fun distoryAutoScroll() {
        if (autoScrollRun != null) {
            removeCallbacks(autoScrollRun)
        }
        isAutoScrollRun = false
    }

    fun isAutoScroll(): Boolean {
        return isAutoScroll
    }

    fun setAutoScroll(autoScroll: Boolean) {
        if (isAutoScroll == autoScroll) {
            return
        }
        isAutoScroll = autoScroll
        if (isAutoScroll) {
            checkAutoScroll()
        } else {
            distoryAutoScroll()
        }
    }

    fun getScrollTime(): Int {
        return scrollTime
    }

    fun setScrollTime(scrollTime: Int) {
        this.scrollTime = scrollTime
        if (pagerScroller != null) {
            pagerScroller!!.setScrollDuration(this.scrollTime)
        }
    }
}
