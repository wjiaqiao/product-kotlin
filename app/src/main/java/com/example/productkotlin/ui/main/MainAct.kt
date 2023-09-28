package com.example.productkotlin.ui.main

import android.os.Bundle
import android.util.Xml
import android.view.InflateException
import com.drake.serialize.intent.bundle
import com.example.productkotlin.R
import com.example.productkotlin.base.BaseVMAct
import com.example.productkotlin.databinding.DiaTestBinding
import com.example.productkotlin.databinding.MainActBinding
import com.example.productkotlin.ui.activity.TestRVContract
import com.example.productkotlin.view.dialog.TestDia
import com.jiaqiao.product.ext.*
import com.jiaqiao.product.util.ProductUtil
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException


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
        mViewBind.btn2.click { }

        TestDia(this).show()

    }


}
