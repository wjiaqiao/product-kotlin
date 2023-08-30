package com.jiaqiao.product.ui.logfile

import android.graphics.Color
import android.os.Bundle
import com.jiaqiao.product.base.ProductBaseVBFrag
import com.jiaqiao.product.databinding.LogcatFileFragBinding
import com.jiaqiao.product.ext.*
import com.jiaqiao.product.util.ShareUtil
import com.jiaqiao.product.widget.RVDecoration
import com.jiaqiao.product.widget.adapter.LogFileAdapter
import java.io.File

/**
 * 日志文件fragment，用于分享日志文件
 * */
class LogcatFileFrag : ProductBaseVBFrag<LogcatFileFragBinding> {

    //保存日志文件的路径
    var logPath = ""
        private set
    var type = 0
        private set

    companion object {
        fun newInstance(
            logPath: String,
            type: Int = 1
        ): LogcatFileFrag =
            LogcatFileFrag(logPath, type)
    }

    private constructor(logPath: String, type: Int = 1) {
        this.logPath = logPath
        this.type = type
    }

    private val adapter by lazy {
        LogFileAdapter().also {
            it.setOnItemClickListener { _, _, position ->
                ShareUtil.share(
                    requireContext(),
                    it.getItem(position).absolutePath,
                    null,
                    "分享LOG日志"
                )
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.list.vertical().adapter = adapter
        mViewBind.list.addItemDecoration(
            RVDecoration(
                1.dp,
                Color.parseColor("#EFEFEF"))
        )
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }

    /**
     * 刷新日志文件数量
     * */
    private fun refreshData() {
        launch {
            val list = runIo {
                val allFile = File(logPath).listFiles().toMutableList()
                allFile.sortByDescending { it.lastModified() }
                allFile
            }.onErrorReturn {
                mutableListOf<File>()
            }.await()
            val isNull = list.isNullOrEmpty()
            mViewBind.list.visibleOrGone(!isNull)
            mViewBind.nullLay.visibleOrGone(isNull)
            adapter.setList(list)
        }
    }

}