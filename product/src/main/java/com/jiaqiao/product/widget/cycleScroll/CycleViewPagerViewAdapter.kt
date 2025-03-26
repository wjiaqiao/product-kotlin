package com.jiaqiao.product.widget.cycleScroll

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.jiaqiao.product.ext.notNullAndEmpty
import java.util.*


open abstract class CycleViewPagerViewAdapter<T, VH : CycleViewPagerViewAdapter.ViewHolder?>(
    private val context: Context, list: List<T>
) :
    PagerAdapter() {
    var isCycleScroll = true
    private var viewHolderList: MutableMap<Int, VH?>? = HashMap()
    private var data: List<T>? = ArrayList()

    init {
        update(list, false)
    }

    private fun update(list: List<T>, isNotify: Boolean = true) {
        if (viewHolderList == null) {
            viewHolderList = HashMap()
        }
        data = list
        if (isNotify) {
            notifyDataSetChanged()
        }
    }

    // getItemPosition的返回值将决定item是否更新
    override fun getItemPosition(obj: Any): Int {
        return POSITION_NONE
    }

    override fun notifyDataSetChanged() {
        if (viewHolderList == null) {
            viewHolderList = HashMap()
        }
        if (viewHolderList!!.notNullAndEmpty()) {
            for (intPosi in viewHolderList!!.keys) {
                val item = viewHolderList!![intPosi]
                if (item != null) {
                    item.isNeedUpdate = true
                }
            }
        }
        super.notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return if (isCycleScroll) {
            if (data != null) data!!.size + 2 else 0
        } else {
            if (data != null) data!!.size else 0
        }
    }

    val realCount: Int
        get() = if (data != null) data!!.size else 0

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        if (viewHolderList!!.containsKey(position) && viewHolderList!![position] != null) {
            container.removeView(viewHolderList!![position]!!.itemView)
        }
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var item: View? = null
        var itemHolder: VH? = null
        if (viewHolderList!!.containsKey(position)) {
            itemHolder = viewHolderList!![position]
            item = itemHolder!!.itemView
        }
        val realPosition: Int
        val realCount = data!!.size
        realPosition = if (isCycleScroll) {
            if (position == 0) {
                realCount - 1
            } else if (position == realCount + 1) {
                0
            } else {
                position - 1
            }
        } else {
            position
        }
        if (item == null) {
            itemHolder = createViewHolder(context, realPosition)
            if (itemHolder != null) {
                itemHolder.adapterPosition = realPosition
                bindViewHolder(data!![realPosition], itemHolder, realPosition)
                viewHolderList!![position] = itemHolder
                item = itemHolder.itemView
            }
        }
        if (itemHolder != null && itemHolder.isNeedUpdate) {
            itemHolder.isNeedUpdate = false
            bindViewHolder(data!![realPosition], itemHolder, realPosition)
        }
        container.addView(item)
        return item!!
    }

    protected abstract fun createViewHolder(context: Context?, position: Int): VH
    protected abstract fun bindViewHolder(t: T, holder: VH, position: Int)
    abstract class ViewHolder(view: View) {
        var isNeedUpdate = false
        var adapterPosition = -1
        var itemView: View? = null

        init {
            this.itemView = view
            isNeedUpdate = false
            itemView?.setOnClickListener {  }
            itemView?.setOnLongClickListener {
                false
            }
        }
    }

}