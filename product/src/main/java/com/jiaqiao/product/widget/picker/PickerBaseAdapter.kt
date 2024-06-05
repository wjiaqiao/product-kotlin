package com.jiaqiao.product.widget.picker

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jiaqiao.product.ext.invisible
import com.jiaqiao.product.ext.isNull
import com.jiaqiao.product.ext.notNull
import com.jiaqiao.product.ext.setWidthHeight
import com.jiaqiao.product.ext.visible

/**
 *
 * 选择器的adapter对象
 *
 *
 * [T] adapter的数据源类型
 * [VH] adapter的ViewHolder对象
 *
 * */
open abstract class PickerBaseAdapter<T, VH : PickerBaseAdapter.ViewHolder> :
    RecyclerView.Adapter<VH>() {

    var itemHeight = 0 //子项高度
    var visibleItemNum = 2 //上下可见子项的数量
    var isLoop = false //无限循环开关
        set(value) {
            if (field != value) {
                field = value
                updateUiData()
            }
        }
    var resetSize = false

    private var realDataList = mutableListOf<T>() //外部设置的数据源
    private var uiDataList = mutableListOf<Any?>() //对内UI显示的数据源

    private var attrRecyclerView: RecyclerView? = null

    private var selectRelPosi = -1

    private var recyclerViewWidth = 0
        set(value) {
            if (field <= 0 && value > 0) {
                field = value
                notifyDataSetChanged()
            }
        }

    //创建adapte的viewholder
    abstract fun creViewHolder(parent: ViewGroup, viewType: Int): VH

    //渲染未选中的UI
    abstract fun onBindUnselectUi(holder: VH, data: T, position: Int)

    //渲染滑动过程中的UI
    abstract fun onBindScrollYUi(holder: VH, data: T, position: Int, scrollYProgress: Float)

    //渲染选中的UI
    abstract fun onBindSelectUi(holder: VH, data: T, position: Int)

    open class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    //创建viewholder并设置itemview的宽高
    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        //        vh.itemView.setWidthMatchParent().setHeight(itemHeight)
        return creViewHolder(parent, viewType)
    }

    //绑定数据源渲染UI
    final override fun onBindViewHolder(holder: VH, position: Int) {
        if (recyclerViewWidth <= 0 && attrRecyclerView.notNull()) {
            recyclerViewWidth = attrRecyclerView?.measuredWidth ?: 0
        }
        holder.itemView.setWidthHeight(
            if (recyclerViewWidth > 0) recyclerViewWidth else ViewGroup.LayoutParams.MATCH_PARENT,
            itemHeight
        )
        var userPosi = getUserPosition(position)
        var realData = uiDataList[userPosi]
        var realPosi = getRealPosition(userPosi)
        if (realData.isNull() || realPosi < 0) {
            holder.itemView.invisible()
        } else {
            holder.itemView.visible()
            if (selectRelPosi == realPosi) {
                onBindSelectUi(holder, realData as T, selectRelPosi)
            } else {
                onBindUnselectUi(holder, realData as T, realPosi)
            }
        }
    }

    //设置数据源
    fun setList(list: MutableList<T>) {
        realDataList = list
        updateUiData()
    }

    //刷新UI数据源
    fun updateUiData() {
        uiDataList.clear()
        if (isLoop) {
            uiDataList.addAll(realDataList)
        } else {
            uiDataList.addAll(realDataList)
            for (i in 0 until visibleItemNum) {
                uiDataList.add(0, null)
                uiDataList.add(uiDataList.size, null)
            }
        }
    }


    //recyclerview附加到adapter上
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        attrRecyclerView = recyclerView
        recyclerViewWidth = attrRecyclerView?.measuredWidth ?: 0
        if (recyclerViewWidth <= 0 && attrRecyclerView.notNull()) {
            attrRecyclerView?.post {
                recyclerViewWidth = attrRecyclerView?.measuredWidth ?: 0
            }
        }
    }

    //recyclerview从adapter中移除
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        attrRecyclerView = null
        recyclerViewWidth = 0
    }

    //获取position对应的viewholder，用于刷新UI
    fun getViewHolder(position: Int): VH? {
        return if (attrRecyclerView.isNull()) {
            null
        } else {
            attrRecyclerView?.layoutManager?.findViewByPosition(position)?.let {
                getViewHolder(it)
            }
        }
    }

    //获取view对应的viewholder，用于刷新UI
    private fun getViewHolder(view: View): VH? {
        return if (attrRecyclerView.isNull()) {
            null
        } else {
            attrRecyclerView?.getChildViewHolder(view) as VH
        }
    }

    //获取子项数量
    override fun getItemCount(): Int {
        return if (isLoop) (Int.MAX_VALUE / realDataList.size * realDataList.size) else uiDataList.size
    }


    //获取对外数据源真实序号，-1代表无效值
    private fun getRealPosition(uiPosition: Int): Int {
        return if (isLoop) {
            uiPosition
        } else {
            var backPosi = uiPosition - visibleItemNum
            if (backPosi < 0 || backPosi >= realDataList.size) -1 else backPosi
        }
    }


    //触发刷新选中项的UI
    fun selectPosi(position: Int) {
        getViewHolder(position)?.let {
            var userPosi = getUserPosition(position)
            var realData = uiDataList[userPosi]
            var realPosi = getRealPosition(userPosi)
            if (realData != null && realPosi >= 0) {
                selectRelPosi = realPosi
                onBindSelectUi(it, realData as T, selectRelPosi)
            }
        }
    }

    //触发刷新未选中项的UI
    fun unselectPosi(position: Int) {
        getViewHolder(position)?.let {
            var userPosi = getUserPosition(position)
            var realData = uiDataList[userPosi]
            var realPosi = getRealPosition(userPosi)
            if (realData != null && realPosi >= 0) {
                onBindUnselectUi(it, realData as T, realPosi)
            }
        }
    }

    //触发刷新滑动项的UI
    fun scrollPosi(position: Int, scrollYProgress: Float) {
        getViewHolder(position)?.let {
            var userPosi = getUserPosition(position)
            var realData = uiDataList[userPosi]
            var realPosi = getRealPosition(userPosi)
            if (realData != null && realPosi >= 0) {
                onBindScrollYUi(it, realData as T, realPosi, scrollYProgress)
            }
        }
    }

    //根据真实数据源序号获取对应中心点的列表position
    fun getLoopScrollCenterPosition(position: Int): Int {
        return if (isLoop) {
            itemCount / (2 * realDataList.size) * realDataList.size + position
        } else {
            position + visibleItemNum
        }
    }

    //获取真实数据源的position
    fun getRealDataPosition(position: Int): Int {
        return if (isLoop) (position % realDataList.size) else (position - visibleItemNum)
    }

    //获取渲染UI时使用的position
    private fun getUserPosition(position: Int): Int {
        return if (isLoop) {
            position % realDataList.size
        } else {
            position
        }
    }

    //获取position对应的数据
    fun getItemPosition(position: Int): T {
        return realDataList[position]
    }

    fun getRealItemCount() = realDataList.size

}