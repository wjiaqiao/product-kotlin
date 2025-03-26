package com.jiaqiao.product.widget

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager

/**
 *
 * 解决floxbox在转换layoutmanager时发生的错误
 *
 * */
open class ProductFlexboxLayoutManager : FlexboxLayoutManager {


    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, flexDirection: Int) : super(context, flexDirection) {

    }

    constructor(context: Context, flexDirection: Int, flexWrap: Int) : super(
        context,
        flexDirection,
        flexWrap
    ) {

    }


    /**
     * 将LayoutParams转换成新的FlexboxLayoutManager.LayoutParams
     */
    override fun generateLayoutParams(lp: ViewGroup.LayoutParams?): RecyclerView.LayoutParams? {
        return when (lp) {
            is RecyclerView.LayoutParams -> {
                LayoutParams(lp as RecyclerView.LayoutParams?)
            }

            is ViewGroup.MarginLayoutParams -> {
                LayoutParams(lp as ViewGroup.MarginLayoutParams?)
            }

            else -> {
                LayoutParams(lp)
            }
        }
    }


}