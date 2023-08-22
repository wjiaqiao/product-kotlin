package com.example.productkotlin.ui.activity

import android.os.Bundle
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.productkotlin.base.BaseVAct
import com.example.productkotlin.databinding.TestRvAcBinding
import com.example.productkotlin.databinding.TestRvItemBinding
import com.jiaqiao.product.ext.*
import com.jiaqiao.product.view.adapter.ProductBaseBindingAdapter
import kotlinx.coroutines.delay

class TestRVAc : BaseVAct<TestRvAcBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.rv.adapter = TestAdapter().apply {
            setList(mutableListOf<String>().also {
                for (i in 1..20) {
                    it.add(i.toString())
                }
            })
        }
        mViewBind.hideBut.click {

            registerResult(TestRVContract()){
                it.plog("TestRVAc")
            }.launch(null)

        }

    }

    private class TestAdapter : ProductBaseBindingAdapter<String, TestRvItemBinding>() {
        override fun convert(bind: TestRvItemBinding, item: String, holder: BaseViewHolder) {
            bind.string.text = item
//            item.plog()
        }

    }
}