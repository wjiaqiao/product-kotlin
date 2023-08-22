package com.jiaqiao.product.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jiaqiao.product.ext.*
import com.jiaqiao.product.helper.diff.ProductDiffCallBack
import com.jiaqiao.product.helper.diff.ProductDiffUpdateCallback


open abstract class ProductBaseBindingAdapter<T, VB : ViewBinding> :
    BaseQuickAdapter<T, ProductViewBindingVH<VB>>(0) {

    private val diffUpdateCallback by lazy { ProductDiffUpdateCallback(this) }

    override fun convert(holder: ProductViewBindingVH<VB>, item: T, payloads: List<Any>) {
        super.convert(holder, item, payloads)
        payloads?.forEach { payload ->
            (try {
                payload as T
            } catch (thr: Throwable) {
                thr.plogE()
                null
            })?.let {
                convert(holder, it)
            }
        }
    }

    override fun convert(holder: ProductViewBindingVH<VB>, item: T) {
        convert(holder.binding, item, holder)
    }

    abstract fun convert(bind: VB, item: T, holder: BaseViewHolder)

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): ProductViewBindingVH<VB> {
        val binding = viewBindingClass<VB>(this, 1) { clazz ->
            clazz.getMethod(
                "inflate",
                LayoutInflater::class.java,
                ViewGroup::class.java,
                Boolean::class.java
            ).invoke(null, LayoutInflater.from(parent.context), parent, false) as VB
        }
        return ProductViewBindingVH(binding.root, binding)
    }

    /**
     * 使用DiffUtil对比列表变化并使用动画刷新列表
     */
    fun diffSetList(newList: MutableList<T>, isMoveAndChange: Boolean = false) {
        if (newList.size >= 300 || data.size >= 300) {
            "列表数量过多不建议使用动画刷新".libPlog()
        }
        if (context is AppCompatActivity) {
            (context as AppCompatActivity).launchIo {
                if (recyclerView.isAttachedToWindow) {
                    val diff = DiffUtil.calculateDiff(ProductDiffCallBack(data, newList), true)
                    if (recyclerView.isAttachedToWindow) {
                        runMain {
                            diffUpdateCallback.isMoveAndChange = isMoveAndChange
                            diff.dispatchUpdatesTo(diffUpdateCallback)
                            setOnlyList(newList)
                        }.await()
                    }
                }
            }
        } else {
            setList(newList)
        }
    }

    /**
     * 检查列表数量判断使用哪种刷新方式
     */
    fun diffSetListAuto(newList: MutableList<T>) {
        if (newList.size >= 300 || data.size >= 300) {
            setList(newList)
        } else {
            diffSetList(newList)
        }
    }

    /**
     * 只设置数据源不刷新adapter
     */
    private fun setOnlyList(list: MutableList<T>) {
        if (list !== this.data) {
            this.data.clear()
            if (!list.isNullOrEmpty()) {
                this.data.addAll(list)
            }
        } else {
            if (!list.isNullOrEmpty()) {
                val newList = ArrayList(list)
                this.data.clear()
                this.data.addAll(newList)
            } else {
                this.data.clear()
            }
        }
    }

}