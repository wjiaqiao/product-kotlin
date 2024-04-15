package com.jiaqiao.product.base

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.jiaqiao.product.ext.createViewBinding

/**
 * 带viewbinging基础activity类
 * */
open abstract class ProductBaseVBAct<VB : ViewBinding> : ProductBaseAct() {

    lateinit var mViewBind: VB
    private var vbRoot: View? = null

    /**
     * 初始化view
     */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 添加控件点击事件或添加监听器
     * */
    open fun onBindViewClick() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBind()
        setContentView(vbRoot)
        vbRoot?.post {
            initView(savedInstanceState)
            onBindViewClick()
        }
    }

    /**
     * 创建ViewBinding
     * 利用反射 根据泛型得到 ViewBinding
     */
    private fun initViewBind() {
        mViewBind = createViewBinding()
        vbRoot = mViewBind.root
    }

}