package com.example.productkotlin.view.dialog

import android.content.Context
import com.example.productkotlin.databinding.DiaTestBinding
import com.jiaqiao.product.ext.dp
import com.jiaqiao.product.ext.isNull
import com.jiaqiao.product.ext.plog
import com.jiaqiao.product.widget.ProductBaseDia

class TestDia(context: Context) : ProductBaseDia<DiaTestBinding>(context) {

    override fun width() = 100.dp
    override fun height() = 100.dp

    override fun initView() {
        "initView".plog()
    }

    override fun loadView() {
        "loadView".plog()
        (mViewBind.bg.isNull()).plog("bg.view")
    }

}