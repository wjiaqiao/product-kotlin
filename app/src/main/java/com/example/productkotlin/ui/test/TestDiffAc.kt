package com.example.productkotlin.ui.test

import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.pLifeScope
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.productkotlin.base.BaseVAct
import com.example.productkotlin.databinding.ActivityTestDiffBinding
import com.example.productkotlin.databinding.ItemDiffBinding
import com.jiaqiao.product.ext.*
import com.jiaqiao.product.util.PLifeScope
import com.jiaqiao.product.view.adapter.ProductBaseBindingAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class TestDiffAc : BaseVAct<ActivityTestDiffBinding>() {

    private val adapter by lazy { DiffAda() }
    private val list1 = mutableListOf<String>()
    private val list2 = mutableListOf<String>()

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.rv.adapter = adapter
        launch {
            runIo {
                list1.clear()
                list2.clear()
                for (i in 0..1000) {
                    list1.add(i.toString())
                    list2.add((i * 2).toString())
                }
            }.await()
            adapter.setList(list1)
        }
        mViewBind.test1.click {
            adapter.diffSetList(list1)
        }
        mViewBind.test2.click {
            adapter.diffSetList(list2)
        }
    }

}

class DiffAda : ProductBaseBindingAdapter<String, ItemDiffBinding>() {

    override fun convert(bind: ItemDiffBinding, item: String, holder: BaseViewHolder) {
        bind.testStart.text = item
        bind.root.setBackgroundColor(
            if (holder.bindingAdapterPosition % 2 == 0) Color.parseColor("#AAAAAA") else Color.parseColor(
                "#EEEEEE"
            )
        )
    }

}