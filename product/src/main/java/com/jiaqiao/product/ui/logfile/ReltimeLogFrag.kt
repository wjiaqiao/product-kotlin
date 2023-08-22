package com.jiaqiao.product.ui.logfile

import android.os.Bundle
import com.jiaqiao.product.base.ProductBaseVBFrag
import com.jiaqiao.product.databinding.ReltimeLogFragBinding

/**
 * 实时日志fragment，用于显示app中的实时日志
 * */
class ReltimeLogFrag: ProductBaseVBFrag<ReltimeLogFragBinding> {

    companion object{
        fun newInstance():ReltimeLogFrag = ReltimeLogFrag()
    }

    private constructor()

    override fun initView(savedInstanceState: Bundle?) {

    }

}