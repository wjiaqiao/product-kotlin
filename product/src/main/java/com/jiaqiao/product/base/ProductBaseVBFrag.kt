package com.jiaqiao.product.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.jiaqiao.product.ext.createViewBinding

/**
 * 带viewbinging基础fragment类
 * */
abstract class ProductBaseVBFrag<VB : ViewBinding> : ProductBaseFrag() {

    lateinit var mViewBind: VB
    private var mVbRoot: View? = null

    /**
     * 初始化view
     */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 添加控件点击事件或添加监听器
     * */
    open fun onBindViewClick() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mViewBind = createViewBinding(inflater, container, false)
        mVbRoot = mViewBind.root
        return mVbRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mVbRoot?.post {
            initView(savedInstanceState)
            onBindViewClick()
        }
    }

}