package com.example.productkotlin.ui.test

import android.os.Bundle
import com.example.productkotlin.R
import com.example.productkotlin.base.BaseVAct
import com.example.productkotlin.databinding.TestAcBinding
import com.example.productkotlin.ui.activity.TestFrag
import com.jiaqiao.product.ext.click

class TestAc : BaseVAct<TestAcBinding>() {

    private val frag by lazy { TestFrag() }

    override fun initView(savedInstanceState: Bundle?) {
        supportFragmentManager.beginTransaction().add(R.id.root_lay, frag).commitAllowingStateLoss()


        mViewBind.hide.click {
            supportFragmentManager.beginTransaction().remove(frag).commitAllowingStateLoss()
        }

    }

}