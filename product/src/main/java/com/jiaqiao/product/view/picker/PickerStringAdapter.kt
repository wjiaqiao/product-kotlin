package com.jiaqiao.product.view.picker

import android.graphics.Color
import android.view.ViewGroup
import com.jiaqiao.product.databinding.PickerStringBinding
import com.jiaqiao.product.ext.textSizePx
import com.jiaqiao.product.util.ProductUtil
import com.jiaqiao.product.util.ViewBindingUtil

open class PickerStringAdapter<T> : PickerBaseAdapter<T, PickerStringAdapter.ViewHolder>() {

    var unselectTextColor = Color.BLACK //未选中时的文本颜色
    var selectTextColor = Color.BLACK //选中时的文本颜色
    var unselectTextSize = 0 //未选中时的文本字体
    var selectTextSize = 0 //选中时的文本字体

    class ViewHolder(val vb: PickerStringBinding) : PickerBaseAdapter.ViewHolder(vb.root) {

    }

    override fun creViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ViewBindingUtil.create(PickerStringBinding::class.java, parent.context))
    }


    //渲染未选中的UI
    override fun onBindUnselectUi(holder: ViewHolder, data: T, position: Int) {
        holder.vb.pickerStringText.text = data.toString()
        holder.vb.pickerStringText.textSizePx(unselectTextSize)
        holder.vb.pickerStringText.setTextColor(unselectTextColor)
    }

    //渲染滑动过程中的UI
    override fun onBindScrollYUi(holder: ViewHolder, data: T, position: Int, scrollYProgress: Float) {
        holder.vb.pickerStringText.setTextColor(
            ProductUtil.radioColor(
                unselectTextColor,
                selectTextColor,
                scrollYProgress
            )
        )
        holder.vb.pickerStringText.textSizePx(unselectTextSize + (selectTextSize - unselectTextSize) * scrollYProgress)
    }


    //渲染选中的UI
    override fun onBindSelectUi(holder: ViewHolder, data: T, position: Int) {

        holder.vb.pickerStringText.textSizePx(selectTextSize)
        holder.vb.pickerStringText.setTextColor(selectTextColor)

    }

}