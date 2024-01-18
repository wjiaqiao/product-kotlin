package com.jiaqiao.product.widget.picker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jiaqiao.product.R
import com.jiaqiao.product.ext.centerY
import com.jiaqiao.product.ext.dp
import com.jiaqiao.product.ext.notNull
import com.jiaqiao.product.helper.SoundPool
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.math.abs

open class PickerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs), CoroutineScope {

    private var itemHeight = 0 //子项高度
    private var itemHeightHalf = 0 //子项高度一半
    private var visibleItemNum = 2 //上下可见子项的数量
    private var isLoop = false //无限循环开关
    private var unselectTextColor = Color.BLACK //未选中时的文本颜色
    private var selectTextColor = Color.BLACK //选中时的文本颜色
    private var unselectTextSize = 0 //未选中时的文本字体
    private var selectTextSize = 0 //选中时的文本字体
    private var isVoice = false //播放选中时的声音
    private var isSplitLine = false //是否绘制分割线
    private var splitLineHeight = 1 //分割线高度
    private var splitLineColor = Color.BLACK //分割线颜色
    private var isMasking = false //是否绘制蒙层
    private var maskingColor = Color.BLACK //蒙层颜色

    private var thisWidth = 0 //控件的宽度
    private var thisHeight = 0 //控件的高度
    private var centerY = 0 //当前控件的y轴中心位置
    private var centerYPosi = -1 //处于y轴中心位置的序号
    private var centerYRealPosi = -1 //处于y轴中心位置的真实数据序号
    private var lastScrollY = -1 //Y轴上一次滑动距离
    private var scrollYDistance = 0 //Y轴相对上传的滑动距离
    private var lastScrollTime: Long = 0 //上次滑动时间
    private val baseRade = 1.08f //选中后滑动最大音乐速率
    private val changeBase = 0.28f //选中后滑动可变音乐速率
    private var isFirstLoad = false //第一次加载无限滚动选择器
    private var lastLoopCenterPosi = -1 //无限滚动时的序号
    private var scrollChange = false //滑动方向
    private val paint by lazy { Paint() }

    private val pickerStringAdapter by lazy { PickerStringAdapter() } //recyclerview的适配器
    private val layoutManager by lazy { LinearLayoutManager(context) } //强制使用垂直布局
    private var viewConfigMap = mutableMapOf<Int, Int>() //存储滑动时viewholder的状态

    private val soundPoolHelper by lazy { SoundPool(context, R.raw.ring1) }
    private val superlaunch by lazy { SupervisorJob() }
    private var pickerAdapter: PickerBaseAdapter<*, *>? = null


    //选中项的回调
    var positionChange: (position: Int) -> Unit = {}

    override val coroutineContext: CoroutineContext
        get() = superlaunch + Dispatchers.Main


    init {

        //初始化数据
        unselectTextSize = 12.dp
        selectTextSize = 16.dp

        //获取XML的参数
        attrs?.let {
            val array = context.obtainStyledAttributes(it, R.styleable.PickerView)

            visibleItemNum =
                array.getInt(R.styleable.PickerView_pv_visible_item_number, visibleItemNum)
            itemHeight =
                array.getDimensionPixelOffset(R.styleable.PickerView_pv_item_height, itemHeight)
            isLoop = array.getBoolean(R.styleable.PickerView_pv_is_loop, isLoop)
            unselectTextColor =
                array.getColor(R.styleable.PickerView_pv_unselect_text_color, unselectTextColor)
            selectTextColor =
                array.getColor(R.styleable.PickerView_pv_select_text_color, selectTextColor)
            unselectTextSize =
                array.getDimensionPixelOffset(
                    R.styleable.PickerView_pv_unselect_text_size,
                    unselectTextSize
                )
            selectTextSize =
                array.getDimensionPixelOffset(
                    R.styleable.PickerView_pv_select_text_size,
                    selectTextSize
                )
            isVoice = array.getBoolean(R.styleable.PickerView_pv_is_voice, isVoice)
            isSplitLine = array.getBoolean(R.styleable.PickerView_pv_is_split_line, isSplitLine)
            splitLineHeight = array.getDimensionPixelOffset(
                R.styleable.PickerView_pv_split_line_height,
                splitLineHeight
            )
            splitLineColor =
                array.getColor(R.styleable.PickerView_pv_split_line_color, splitLineColor)
            isMasking = array.getBoolean(R.styleable.PickerView_pv_is_masking, isMasking)
            maskingColor = array.getColor(R.styleable.PickerView_pv_masking_color, maskingColor)

            array.recycle()
        }

        itemHeightHalf = itemHeight / 2


        //检查数据合规
        if (visibleItemNum < 0) {
            visibleItemNum = 0
        }

        //设置recyclerview基础参数
        overScrollMode = View.OVER_SCROLL_NEVER
        setLayoutManager(layoutManager)

        //设置adapter的基础参数
        pickerStringAdapter.unselectTextColor = unselectTextColor
        pickerStringAdapter.selectTextColor = selectTextColor
        pickerStringAdapter.unselectTextSize = unselectTextSize
        pickerStringAdapter.selectTextSize = selectTextSize
        pickerStringAdapter.visibleItemNum = visibleItemNum

        paint.isAntiAlias = true

    }

    //view附加进页面
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        soundPoolHelper.load()
    }

    //view从页面中销毁
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        soundPoolHelper.release()
        coroutineContext.cancelChildren()
    }

    ///取消滑动到顶部或底部的动效
    override fun setOverScrollMode(overScrollMode: Int) {
        super.setOverScrollMode(View.OVER_SCROLL_NEVER)
    }

    //设置高度内容填充
    override fun setLayoutParams(params: ViewGroup.LayoutParams?) {
        params?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        super.setLayoutParams(params)
    }

    //设置垂直布局
    override fun setLayoutManager(layout: LayoutManager?) {
        super.setLayoutManager(layoutManager)
    }

    //控件大小发送变化
    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(
            widthSpec,
            MeasureSpec.makeMeasureSpec(
                itemHeight * getVisibleItemNumber(),
                MeasureSpec.EXACTLY
            )
        )
    }

    //布局位置发送变化
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        centerY = centerY()
        thisWidth = r - l
        thisHeight = b - t
    }


    //滑动状态变化的回调
    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        when (state) {
            SCROLL_STATE_IDLE -> {

                //获取最后一个可见view的位置
                val lastItemPosition = layoutManager.findLastVisibleItemPosition()
                //获取第一个可见view的位置
                val firstItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (centerY <= 0) {
                    return
                }
                var scrollNextY = 0
                var centerPosi = -1
                for (i in firstItemPosition..lastItemPosition) {
                    val item = layoutManager.findViewByPosition(i)
                    if (item != null && item.top <= centerY && centerY <= item.bottom) {
                        centerPosi = i
                        scrollNextY = (item.bottom - item.top) / 2 + item.top - centerY
                        break
                    }
                }
                if (scrollNextY != 0 && abs(scrollNextY) > 1) {
                    smoothScrollBy(0, scrollNextY)
                } else {
                    centerPosiCallBack(centerPosi)
                    pickerAdapter?.selectPosi(centerPosi)

                    pickerAdapter?.let {
                        //无限滚动时校准位置
                        if (isLoop && centerPosi >= 0 && lastLoopCenterPosi != centerPosi) {
                            val itemCount = it.itemCount
                            if (firstItemPosition <= itemCount * 0.3f
                                || lastItemPosition >= itemCount * (1 - 0.3f)
                            ) {
                                scrollToPosition(it.getRealDataPosition(centerPosi))
                            }
                            lastLoopCenterPosi = centerPosi
                        }
                    }
                }

            }

            else -> {
                lastLoopCenterPosi = -1
            }
        }
    }


    override fun scrollToPosition(position: Int) {
        pickerAdapter?.let {
            centerYRealPosi = position
            var scrollPosi = it.getLoopScrollCenterPosition(position)
            layoutManager.scrollToPositionWithOffset(scrollPosi, itemHeight * visibleItemNum)
        }
    }

    //滑动的回调
    override fun onScrolled(dx: Int, dy: Int) {
        super.onScrolled(dx, dy)
        scrollChange = dy < 0
        if (lastScrollY == -1) {
            lastScrollY = dy
        } else {
            scrollYDistance = dy - lastScrollY
        }
        updateItemScrollState()
    }


    private fun updateItemScrollState() {
        if (centerY <= 0) return
        var centerPosi = -1
        val lastItemPosition = layoutManager.findLastVisibleItemPosition()
        val firstItemPosition = layoutManager.findFirstVisibleItemPosition()
        for (i in firstItemPosition..lastItemPosition) {
            val item = layoutManager.findViewByPosition(i)
            if (item != null) {
                val itemCenterY = item.centerY() + item.top
                if (itemCenterY == centerY) {
                    //处于Y轴中心位置
                    centerPosi = i
                }
                if (item.top <= centerY && centerY <= item.bottom) {
                    //位于中心区域，不一定在中心点上
                    centerPosi = i
                    var scrolly = when {
                        itemCenterY > centerY -> {
                            abs(itemCenterY - (centerY + itemHeight))
                        }

                        itemCenterY < centerY -> {
                            abs(itemCenterY - (centerY - itemHeight))
                        }

                        else -> {
                            0
                        }
                    }
                    if (scrolly != 0) {
                        scrollYPosition(i, scrolly)
                    } else {
                        centerPosiCallBack(i)
                    }
                    viewConfigMap[i] = 2
                } else if (itemCenterY > centerY - itemHeight && centerY - itemHeightHalf > itemCenterY) {
                    //位于中心区域上一项
                    scrollYPosition(i, abs(itemCenterY - (centerY - itemHeight)))
                    viewConfigMap[i] = 1
                } else if (itemCenterY < centerY + itemHeight && centerY + itemHeightHalf < itemCenterY) {
                    //位于中心区域下一项
                    scrollYPosition(i, abs(itemCenterY - (centerY + itemHeight)))
                    viewConfigMap[i] = 3
                } else {
                    if (viewConfigMap[i] != 0) {
                        pickerAdapter?.unselectPosi(i)
                    }
                    viewConfigMap[i] = 0
                }
            }
        }
        if (centerPosi != -1) {
            centerPosiCallBack(centerPosi)
        }
    }

    //中心项选中回调
    private fun centerPosiCallBack(position: Int) {
        if (centerYPosi == position) {
            return
        }
        centerYPosi = position
        if (scrollState == SCROLL_STATE_IDLE) {
            pickerAdapter?.selectPosi(centerYPosi)
        }

        if (isVoice && scrollYDistance != 0 && (System.currentTimeMillis() - lastScrollTime) > 40) {
            lastScrollTime = System.currentTimeMillis()
            launch(Dispatchers.IO) {
                var dis = abs(scrollYDistance)
                var rade = when {
                    dis <= 100 -> (changeBase * baseRade * dis / 100) + (1.0f - changeBase) * baseRade
                    else -> baseRade
                }
                soundPoolHelper.play(rade)
            }
        }

        if (pickerAdapter.notNull()) {
            var realPosi = pickerAdapter!!.getRealDataPosition(centerYPosi)
            if (centerYRealPosi != realPosi) {
                centerYRealPosi = realPosi
                positionChange.invoke(centerYRealPosi)
            }
        }
    }


    /**
     * 获取recyclerview处于中间位置的position
     */
    fun getCenterPosition() = centerYRealPosi

    //获取最大可见数量
    private fun getVisibleItemNumber() = visibleItemNum * 2 + 1


    //设置string类型的数据源
    fun setTextList(list: MutableList<Pair<String, Int>>) {
        pickerStringAdapter.setList(list)
        if (adapter == pickerStringAdapter) {
            pickerStringAdapter.notifyDataSetChanged()
        } else {
            adapter = pickerStringAdapter
        }
        if (!isFirstLoad) {
            isFirstLoad = true
        }
        if (pickerStringAdapter.getRealItemCount() > 0) {
            post {
                val scrollToPosi =
                    if (0 <= centerYRealPosi && centerYRealPosi < pickerStringAdapter.getRealItemCount()) {
                        centerYRealPosi
                    } else if (centerYRealPosi >= pickerStringAdapter.getRealItemCount()) {
                        pickerStringAdapter.getRealItemCount() - 1
                    } else 0
                if (centerYRealPosi == scrollToPosi) {
                    centerYPosi = -1
                }
                scrollToPosition(scrollToPosi)
                updateItemScrollState()
            }
        }
    }

    fun getTextItemPosition(position: Int): Pair<String, Int> {
        return pickerStringAdapter.getItemPosition(position)
    }

    fun getCenterItem(): Pair<String, Int>? {
        return if (getCenterPosition() < 0) {
            null
        } else getTextItemPosition(getCenterPosition())
    }


    fun getDataCount() = pickerStringAdapter.getRealItemCount()

    //滑动过程的比例
    private fun scrollYPosition(position: Int, scrollY: Int) {
        var progress = (1.0f * scrollY / itemHeight * 100).toInt() / 100f
        if (progress > 1) {
            progress = 1f
        }
        if (progress < 0) {
            progress = 0f
        }
        pickerAdapter?.scrollPosi(position, progress)
    }

    //设置adapter
    override fun setAdapter(adapter: Adapter<*>?) {
        if (adapter is PickerBaseAdapter<*, *>) {
            pickerAdapter = adapter
            pickerAdapter?.let {
                it.visibleItemNum = visibleItemNum
                it.itemHeight = itemHeight
                it.isLoop = isLoop
            }
            super.setAdapter(adapter)
        }
    }

    //绘制分割线和遮蔽蒙层
    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)

        if (isMasking) {
            paint.color = maskingColor
            canvas.drawRect(Rect(0, 0, thisWidth, itemHeight * visibleItemNum), paint)
            canvas.drawRect(
                Rect(
                    0,
                    thisHeight - itemHeight * visibleItemNum,
                    thisWidth,
                    thisHeight
                ), paint
            )
        }

        if (isSplitLine) {
            paint.color = splitLineColor
            paint.strokeWidth = splitLineHeight.toFloat()
            canvas.drawLine(
                0f,
                (itemHeight * visibleItemNum).toFloat(),
                thisWidth.toFloat(), (itemHeight * visibleItemNum).toFloat(), paint
            )
            canvas.drawLine(
                0f,
                (thisHeight - itemHeight * visibleItemNum).toFloat(),
                thisWidth.toFloat(), (thisHeight - itemHeight * visibleItemNum).toFloat(), paint
            )
        }

    }

    //设置子项高度
    fun setItemHeight(itemHeight: Int) {
        this.itemHeight = itemHeight
        itemHeightHalf = this.itemHeight / 2

        invalidate()
        pickerAdapter?.let {
            it.itemHeight = this.itemHeight
            it.notifyDataSetChanged()
        }
    }

    //设置上下可见子项的数量
    fun setVisibleItemNum(visibleItemNum: Int) {
        this.visibleItemNum = visibleItemNum

        invalidate()
        pickerAdapter?.let {
            it.visibleItemNum = this.visibleItemNum
            it.updateUiData()
            it.notifyDataSetChanged()
        }
    }

    fun setVoice(isVoice: Boolean) {
        this.isVoice = isVoice
    }

    fun setSplitLine(isSplitLine: Boolean) {
        this.isSplitLine = isSplitLine
        invalidate()
    }

    fun setSplitLineHeight(splitLineHeight: Int) {
        this.splitLineHeight = splitLineHeight
        invalidate()
    }

    fun setSplitLineColor(splitLineColor: Int) {
        this.splitLineColor = splitLineColor
        invalidate()
    }

    fun setMasking(isMasking: Boolean) {
        this.isMasking = isMasking
        invalidate()
    }

    fun setMaskingColor(maskingColor: Int) {
        this.maskingColor = maskingColor
        invalidate()
    }

    fun setSelectTextColor(selectTextColor: Int) {
        this.selectTextColor = selectTextColor
        pickerStringAdapter.selectTextColor = this.selectTextColor
        if (adapter == pickerStringAdapter && pickerStringAdapter.itemCount > 0) {
            pickerStringAdapter.notifyDataSetChanged()
        }
    }

}