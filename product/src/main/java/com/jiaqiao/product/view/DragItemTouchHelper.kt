package com.jiaqiao.product.view

import androidx.recyclerview.widget.*
import com.google.android.flexbox.FlexboxLayoutManager
import java.util.*

/**
 *
 * 长按拖拽触摸的辅助类
 *
 * GridLayoutManager 支持上下左右
 * StaggeredGridLayoutManager 支持上下左右
 * FlexboxLayoutManager 支持上下左右
 * LinearLayoutManager 根据显示方向固定推拽的方向
 *
 *
 * [list] 存储adapter中数据类型为 T 的列表
 *
 * **/
open class DragItemTouchHelper<T> : ItemTouchHelper.Callback {

    private var list: List<T> = listOf()

    constructor() {

    }

    constructor(list: List<T>) {
        this.list = list
    }

    /**
     * 绑定adapter中的数据列表，用于松手后的数据交换位置
     * */
    fun bindList(list: List<T>) {
        this.list = list
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags: Int
        val swipFlags: Int
        if (recyclerView.layoutManager is GridLayoutManager
            || recyclerView.layoutManager is StaggeredGridLayoutManager
            || recyclerView.layoutManager is FlexboxLayoutManager
        ) {
            dragFlags =
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            swipFlags = 0
        } else if (recyclerView.layoutManager is LinearLayoutManager) {
            dragFlags =
                if ((recyclerView.layoutManager as LinearLayoutManager).orientation == RecyclerView.VERTICAL) {
                    ItemTouchHelper.UP or ItemTouchHelper.DOWN
                } else {
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                }
            swipFlags = 0
        } else {
            dragFlags = 0
            swipFlags = 0
        }
        return makeMovementFlags(dragFlags, swipFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        recyclerView.adapter?.let {
            val fromPosition = viewHolder.bindingAdapterPosition //得到拖动ViewHolder的position
            val toPosition = target.bindingAdapterPosition //得到目标ViewHolder的position
            if (fromPosition < toPosition) {
                for (i in fromPosition until toPosition) {
                    Collections.swap(list, i, i + 1)
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    Collections.swap(list, i, i - 1)
                }
            }
            it.notifyItemMoved(fromPosition, toPosition)
        }
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }


}
