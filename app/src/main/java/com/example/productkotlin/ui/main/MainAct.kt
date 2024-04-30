package com.example.productkotlin.ui.main

import android.os.Bundle
import com.drake.serialize.intent.openActivity
import com.example.productkotlin.BuildConfig
import com.example.productkotlin.base.BaseVMAct
import com.example.productkotlin.databinding.MainActBinding
import com.example.productkotlin.ui.activity.ScrollTestAct


class MainAct : BaseVMAct<MainActBinding, MainVM>() {

    override fun initView(savedInstanceState: Bundle?) {


        if (BuildConfig.DEBUG) {
//            openActivity<ScrollTestAct>()
        }

    }

}
