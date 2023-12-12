package com.example.productkotlin.ui.main

import android.os.Bundle
import androidx.lifecycle.pLifeScope
import com.drake.serialize.intent.bundle
import com.drake.serialize.intent.openActivity
import com.example.productkotlin.base.BaseVMAct
import com.example.productkotlin.databinding.MainActBinding
import com.example.productkotlin.run.RunRSA
import com.example.productkotlin.ui.activity.EditTextAct
import com.example.productkotlin.ui.activity.TestRVContract
import com.jiaqiao.product.ext.*
import com.jiaqiao.product.util.ProductThreadPool
import com.jiaqiao.product.util.ProductUtil
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class MainAct : BaseVMAct<MainActBinding, MainVM>() {

    override fun initView(savedInstanceState: Bundle?) {
        openActivity<EditTextAct>()
    }

}
