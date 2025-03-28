package com.example.productkotlin.view.dialog

import android.content.Context
import com.example.productkotlin.databinding.DiaLoadBinding
import com.example.productkotlin.databinding.DiaTestBinding
import com.jiaqiao.product.ext.dp
import com.jiaqiao.product.ext.isNull
import com.jiaqiao.product.ext.plog
import com.jiaqiao.product.widget.ProductBaseDia

class LoadDia(context: Context) : ProductBaseDia<DiaLoadBinding>(context) {

    override fun initView() {
        setCancelable(false)
    }

}