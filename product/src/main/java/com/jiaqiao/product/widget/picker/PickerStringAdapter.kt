package com.jiaqiao.product.widget.picker

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jiaqiao.product.R
import com.jiaqiao.product.ext.bold
import com.jiaqiao.product.ext.textSizePx
import com.jiaqiao.product.util.ProductUtil

open class PickerStringAdapter :
    PickerBaseAdapter<Pair<String, Int>, PickerStringAdapter.ViewHolder>() {

    var unselectTextColor = Color.BLACK //未选中时的文本颜色
    var selectTextColor = Color.BLACK //选中时的文本颜色
    var unselectTextSize = 0 //未选中时的文本字体
    var selectTextSize = 0 //选中时的文本字体

    private var targetScale = 1f

    fun updateConfig() {
        targetScale = 1f * selectTextSize / unselectTextSize
    }

    class ViewHolder(view: View) : PickerBaseAdapter.ViewHolder(view) {
        val textView = view.findViewById<TextView>(R.id.picker_string_text)
    }

    override fun creViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.picker_string, parent, false)
        )
    }


    //渲染未选中的UI
    override fun onBindUnselectUi(holder: ViewHolder, data: Pair<String, Int>, position: Int) {
        holder.textView.text = data.first
        holder.textView.textSizePx(unselectTextSize)
        holder.textView.setTextColor(unselectTextColor)
        holder.textView.bold(isTextBold)
        holder.textView.scaleX = 1f
        holder.textView.scaleY = 1f
    }

    override fun scrollPosi(position: Int, scrollYProgress: Float) {
        if (unselectTextColor != selectTextColor || unselectTextSize != selectTextSize) {
            super.scrollPosi(position, scrollYProgress)
        }
    }

    //渲染滑动过程中的UI
    override fun onBindScrollYUi(
        holder: ViewHolder,
        data: Pair<String, Int>,
        position: Int,
        scrollYProgress: Float
    ) {
        if (unselectTextColor != selectTextColor) {
            holder.textView.setTextColor(
                ProductUtil.radioColor(
                    unselectTextColor,
                    selectTextColor,
                    scrollYProgress
                )
            )
        }
        if (unselectTextSize != selectTextSize) {
            if (unselectTextSize != holder.textView.textSize.toInt()) {
                holder.textView.textSizePx(unselectTextSize)
            }
            val target = 1 + scrollYProgress * (targetScale - 1)
            if (holder.textView.scaleX != target) {
                holder.textView.scaleX = target
                holder.textView.scaleY = target
            }
        }
    }


    //渲染选中的UI
    override fun onBindSelectUi(holder: ViewHolder, data: Pair<String, Int>, position: Int) {
        holder.textView.text = data.first
        holder.textView.textSizePx(selectTextSize)
        holder.textView.setTextColor(selectTextColor)
        holder.textView.bold(isTextBold)
        holder.textView.scaleX = 1f
        holder.textView.scaleY = 1f
    }

}