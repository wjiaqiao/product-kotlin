package com.example.productkotlin.ui.main

import android.os.Bundle
import androidx.lifecycle.pLifeScope
import com.drake.serialize.intent.bundle
import com.example.productkotlin.base.BaseVMAct
import com.example.productkotlin.databinding.MainActBinding
import com.example.productkotlin.run.RunRSA
import com.example.productkotlin.ui.activity.TestRVContract
import com.jiaqiao.product.ext.*
import com.jiaqiao.product.util.ProductThreadPool
import com.jiaqiao.product.util.ProductUtil
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


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


//        pLifeScope.launchIo {
//
//            "start".plog()
//            val run1 = async {
//                "run1".plog()
//                delay(1000)
//                "run1.end".plog()
//            }
//            val run2 = async {
//                "run2".plog()
//                delay(2000)
//                "run2.end".plog()
//            }
//            run1.await()
//            run2.await()
//            "end".plog()
//
//        }
//        Thread{
//            Thread.sleep(1200)
//            pLifeScope.close()
//            "close".plog()
//        }.start()

//        Thread{
//            launch {
//                ProductUtil.isMainThread().plog("main")
//            }
//        }.start()

        launchIo {
            RunRSA.launch()
        }

    }


}
