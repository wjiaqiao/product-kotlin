package com.example.productkotlin.ui.main

import android.graphics.Color
import android.os.Bundle
import com.drake.serialize.intent.bundle
import com.drake.serialize.intent.openActivity
import com.example.productkotlin.base.BaseVMAct
import com.example.productkotlin.databinding.MainActBinding
import com.example.productkotlin.ui.activity.TestRVAc
import com.example.productkotlin.ui.activity.TestRVContract
import com.example.productkotlin.ui.test.TestAc
import com.jiaqiao.product.ext.*
import com.jiaqiao.product.util.ProductUtil


class MainAct : BaseVMAct<MainActBinding, MainVM>() {

    val ins: String by bundle("")

    private val register = registerForActivityResult(TestRVContract()) {
        it.plog("register")
    }

    override fun initView(savedInstanceState: Bundle?) {
        launchIo {
            ProductUtil.isTabletDevice().plog("isTabletDevice")
            ProductUtil.isTabletModel(this@MainAct).plog("isTabletModel")
            screenPixel().plog("screenPixel")
            windowsPixel().plog("windowsPixel")
        }
        mViewBind.btn1.click {
//            mViewBind.btn1.defBgColor = Color.BLUE
        }
        mViewBind.btn2.click {  }

    }

}
