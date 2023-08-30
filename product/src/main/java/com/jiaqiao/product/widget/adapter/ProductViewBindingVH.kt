package com.jiaqiao.product.widget.adapter

import android.view.View
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.viewholder.BaseViewHolder

open class ProductViewBindingVH<VB : ViewBinding> constructor(view: View, val binding: VB) :
    BaseViewHolder(view)