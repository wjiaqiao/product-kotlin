package com.jiaqiao.product.helper.diff

import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView

open class ProductDiffUpdateCallback<VH : RecyclerView.ViewHolder>(
    val adapter: RecyclerView.Adapter<VH>,
    var isMoveAndChange: Boolean = false
) :
    ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {
        adapter.notifyItemRangeInserted(position, count)
    }

    override fun onRemoved(position: Int, count: Int) {
        adapter.notifyItemRangeRemoved(position, count)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
        adapter.notifyItemMoved(fromPosition, toPosition)
        if (isMoveAndChange) {
            adapter.notifyItemChanged(toPosition)
        }
    }

    override fun onChanged(position: Int, count: Int, payload: Any?) {
        adapter.notifyItemRangeChanged(position, count, payload)
    }
}