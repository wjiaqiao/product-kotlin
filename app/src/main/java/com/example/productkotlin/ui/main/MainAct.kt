package com.example.productkotlin.ui.main

import android.os.Bundle
import com.example.productkotlin.BuildConfig
import com.example.productkotlin.base.BaseVMAct
import com.example.productkotlin.databinding.MainActBinding
import com.jiaqiao.product.ext.launch
import com.jiaqiao.product.ext.launchIo
import com.jiaqiao.product.ext.plog
import kotlinx.coroutines.delay


class MainAct : BaseVMAct<MainActBinding, MainVM>() {

    override fun initView(savedInstanceState: Bundle?) {


        if (BuildConfig.DEBUG) {
//            openActivity<ScrollTestAct>()
        }

//        mViewModel.start()
//
//        mViewBind.root.postDelayed({
//            finish()
//            "finish".plog()
//        }, 1000 * 3)

    }

    override fun onDestroy() {
        super.onDestroy()
        "onDestroy".plog()
    }

}
