package com.example.productkotlin.ui.activity

import android.content.Intent
import android.os.Bundle
import com.example.productkotlin.databinding.TestFragBinding
import com.jiaqiao.product.base.ProductBaseVBFrag
import com.jiaqiao.product.ext.intentResult
import com.jiaqiao.product.ext.plog

class TestFrag : ProductBaseVBFrag<TestFragBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        intentResult(Intent(requireContext(), TestRVAc::class.java)) { code, intent ->
            "result".plog("TestFrag")
        }
    }

}