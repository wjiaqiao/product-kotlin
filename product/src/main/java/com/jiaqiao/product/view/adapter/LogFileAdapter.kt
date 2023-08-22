package com.jiaqiao.product.view.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiaqiao.product.R
import java.io.File

/**
 * 日志文件的适配器
 * */
class LogFileAdapter : BaseQuickAdapter<File, BaseViewHolder>(R.layout.log_file_item) {
    override fun convert(holder: BaseViewHolder, item: File) {
        holder.setText(R.id.log_file_name, item.name)
    }
}