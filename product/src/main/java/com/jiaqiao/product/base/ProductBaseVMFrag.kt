package com.jiaqiao.product.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.jiaqiao.product.ext.createViewBinding
import java.lang.reflect.ParameterizedType

/**
 * 带viewbinging和viewmodel基础fragment类
 * */
abstract class ProductBaseVMFrag<VB : ViewBinding, VM : ProductViewModel> : ProductBaseFrag() {

    lateinit var mViewBind: VB
    lateinit var mViewModel: VM

    private var vbRoot: View? = null

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
        vbRoot = mViewBind.root
        mViewModel = createViewModel()
        return vbRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vbRoot?.post {
            initView(savedInstanceState)
            onBindViewClick()
        }
    }

    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }

    private fun <VM> getVmClazz(obj: Any): VM {
        return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as VM
    }

}