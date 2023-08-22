package com.jiaqiao.product.ext

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.jiaqiao.product.view.adapter.VP2FragAdapter


/**
 * viewpager2去除滑动阴影效果
 * */
fun ViewPager2.cleanShadowEffect(): ViewPager2 {
    if (childCount > 0) {
        getChildAt(0).overScrollMode = View.OVER_SCROLL_NEVER
    }
    return this
}

/**
 * 设置viewpager2垂直滑动
 * */
fun ViewPager2.vertical(): ViewPager2 {
    orientation = ViewPager2.ORIENTATION_VERTICAL
    return this
}

/**
 * 设置viewpager2水平滑动
 * */
fun ViewPager2.horizontal(): ViewPager2 {
    orientation = ViewPager2.ORIENTATION_HORIZONTAL
    return this
}

/**
 * 设置viewpager2的数据源
 * */
fun ViewPager2.setList(fragment: Fragment, fragList: MutableList<out Fragment>): ViewPager2 {
    if (adapter.notNull() && adapter is VP2FragAdapter) {
        (adapter as VP2FragAdapter).setList(fragList)
    } else {
        adapter = VP2FragAdapter(fragment, fragList)
    }
    return this
}

/**
 * 设置viewpager2的数据源
 * */
fun ViewPager2.setList(
    fragmentActivity: FragmentActivity,
    fragList: MutableList<out Fragment>
): ViewPager2 {
    if (adapter.notNull() && adapter is VP2FragAdapter) {
        (adapter as VP2FragAdapter).setList(fragList)
    } else {
        adapter = VP2FragAdapter(fragmentActivity, fragList)
    }
    return this
}

/**
 * 更新viewpager2的数据源
 * */
fun ViewPager2.update(fragList: MutableList<Fragment>): ViewPager2 {
    if (adapter.notNull() && adapter is VP2FragAdapter) {
        (adapter as VP2FragAdapter).setList(fragList)
    }
    return this
}