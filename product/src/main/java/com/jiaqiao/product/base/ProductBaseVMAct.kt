package com.jiaqiao.product.base

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.jiaqiao.product.ext.createViewBinding
import java.lang.reflect.ParameterizedType

/**
 * 带viewbinging和viewmodel基础activity类
 * */
abstract class ProductBaseVMAct<VB : ViewBinding, VM : ProductViewModel> : ProductBaseAct() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewBind()
        setContentView(vbRoot)
        mViewModel = createViewModel()
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