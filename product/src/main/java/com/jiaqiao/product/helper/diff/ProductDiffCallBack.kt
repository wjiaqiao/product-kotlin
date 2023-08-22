package com.jiaqiao.product.helper.diff

import androidx.recyclerview.widget.DiffUtil
import com.jiaqiao.product.ext.hasPosition

open class ProductDiffCallBack<T>(private val old: List<T>, private val new: List<T>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return old.size
    }

    override fun getNewListSize(): Int {
        return new.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old.hasPosition(oldItemPosition) && new.hasPosition(newItemPosition) && old[oldItemPosition] == new[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old.hasPosition(oldItemPosition) && new.hasPosition(newItemPosition) && old[oldItemPosition] == new[newItemPosition]
    }

}