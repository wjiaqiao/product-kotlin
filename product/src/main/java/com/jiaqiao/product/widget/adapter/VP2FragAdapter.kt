package com.jiaqiao.product.widget.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * viewpager2的fragment适配器
 * */
open class VP2FragAdapter : FragmentStateAdapter {
    private val fragList by lazy { mutableListOf<Fragment>() }
    private val fragKeyList by lazy { mutableListOf<Long>() }

    constructor(fragment: Fragment) : super(fragment)

    constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)

    constructor(fragment: Fragment, list: MutableList<out Fragment>) : super(fragment) {
        setDataOnly(list)
    }

    constructor(fragmentActivity: FragmentActivity, list: MutableList<out Fragment>) : super(
        fragmentActivity
    ) {
        setDataOnly(list)
    }

    /**
     * 重写放回item的key，以防止无法刷新fragemnt的问题
     * */
    override fun getItemId(position: Int): Long {
        return fragKeyList[position]
    }

    /**
     * 重写放回item的key，以防止无法刷新fragemnt的问题
     * */
    override fun containsItem(itemId: Long): Boolean {
        return fragKeyList.contains(itemId)
    }

    override fun getItemCount(): Int {
        return fragList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragList[position]
    }

    /**
     * 刷新fragment列表
     * */
    fun setList(list: MutableList<out Fragment>) {
        setDataOnly(list)
        notifyDataSetChanged()
    }

    private fun setDataOnly(list: MutableList<out Fragment>) {
        fragList.clear()
        if (list !== fragList) {
            if (list.isNotEmpty()) {
                fragList.addAll(list.toMutableList())
            }
        } else {
            if (list.isNotEmpty()) {
                fragList.addAll(ArrayList(list))
            }
        }
        refreshKey()
    }

    /**
     * 刷新fragment的key
     * */
    private fun refreshKey() {
        fragKeyList.clear()
        if (fragList.isNotEmpty()) {
            for (i in fragList.indices) {
                fragKeyList.add(fragList[i].hashCode().toLong())
            }
        }
    }

}