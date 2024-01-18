package com.jiaqiao.product.widget.picker

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jiaqiao.product.R
import com.jiaqiao.product.ext.textSizePx
import com.jiaqiao.product.util.ProductUtil

open class PickerStringAdapter :
    PickerBaseAdapter<Pair<String, Int>, PickerStringAdapter.ViewHolder>() {

    var unselectTextColor = Color.BLACK //未选中时的文本颜色
    var selectTextColor = Color.BLACK //选中时的文本颜色
    var unselectTextSize = 0 //未选中时的文本字体
    var selectTextSize = 0 //选中时的文本字体

    class ViewHolder(view: View) : PickerBaseAdapter.ViewHolder(view) {

    }

    override fun creViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.picker_string, parent, false)
        )
    }


    //渲染未选中的UI
    override fun onBindUnselectUi(holder: ViewHolder, data: Pair<String, Int>, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.picker_string_text)?.let {
            it.text = data.first
            it.textSizePx(unselectTextSize)
            it.setTextColor(unselectTextColor)
        }
    }

    //渲染滑动过程中的UI
    override fun onBindScrollYUi(
        holder: ViewHolder,
        data: Pair<String, Int>,
        position: Int,
        scrollYProgress: Float
    ) {
        holder.itemView.findViewById<TextView>(R.id.picker_string_text)?.let {
            it.setTextColor(
                ProductUtil.radioColor(
                    unselectTextColor,
                    selectTextColor,
                    scrollYProgress
                )
            )
            val fontSize =
                (unselectTextSize + (selectTextSize - unselectTextSize) * scrollYProgress).toInt()
            val befFontSize = it.textSize.toInt()
            if (fontSize != befFontSize) {
                it.textSizePx(fontSize)
            }
        }
    }


    //渲染选中的UI
    override fun onBindSelectUi(holder: ViewHolder, data: Pair<String, Int>, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.picker_string_text)?.let {
            it.text = data.first
            it.textSizePx(selectTextSize)
            it.setTextColor(selectTextColor)
        }
    }

}