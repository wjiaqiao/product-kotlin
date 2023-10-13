package com.example.productkotlin.ui.main

import android.os.Bundle
import com.drake.serialize.intent.bundle
import com.example.productkotlin.base.BaseVMAct
import com.example.productkotlin.databinding.MainActBinding
import com.example.productkotlin.ui.activity.TestRVContract
import com.jiaqiao.product.ext.*
import com.jiaqiao.product.util.ProductThreadPool
import com.jiaqiao.product.util.ProductUtil


class MainAct : BaseVMAct<MainActBinding, MainVM>() {

    val ins: String by bundle("")

    private val register = registerForActivityResult(TestRVContract()) {
        it.plog("register")
    }

    override fun initView(savedInstanceState: Bundle?) {
//        launchIo {
//            ProductUtil.isTabletDevice().plog("isTabletDevice")
//            ProductUtil.isTabletModel(this@MainAct).plog("isTabletModel")
//            screenPixel().plog("screenPixel")
//            windowsPixel().plog("windowsPixel")
//        }
        mViewBind.btn1.click {
//            mViewBind.btn1.defBgColor = Color.BLUE
        }
        mViewBind.btn2.click { }


//        launchIo {
//
//            val list = mutableListOf<Runnable>()
//            for (i in 0..10) {
//                list.add {
//                    i.plog("posi")
//                    Thread.sleep(1000 * 3)
//                }
//            }
//            "start pool".plog()
//            ProductThreadPool.runAndWait(list)
//            "end pool".plog()
//
//        }

    }


}
