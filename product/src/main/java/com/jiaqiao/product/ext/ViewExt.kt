package com.jiaqiao.product.ext

import android.content.Context
import android.graphics.Paint
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.widget.AbsListView
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.jiaqiao.product.util.ProductViewUtil


/**
 * 设置view的paddingleft值
 * [paddingLeft] paddingleft值
 * @return 返回view对象
 * */
fun View.setPaddingLeft(paddingLeft: Int): View {
    setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
    return this
}

/**
 * 设置view的paddingTop值
 * [paddingTop] paddingTop值
 * @return 返回view对象
 * */
fun View.setPaddingTop(paddingTop: Int): View {
    setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
    return this
}

/**
 * 设置view的paddingRight值
 * [paddingRight] paddingRight值
 * @return 返回view对象
 * */
fun View.setPaddingRight(paddingRight: Int): View {
    setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
    return this
}

/**
 * 设置view的paddingBottom值
 * [paddingBottom] paddingBottom值
 * @return 返回view对象
 * */
fun View.setPaddingBottom(paddingBottom: Int): View {
    setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
    return this
}


/**
 * 设置view的paddingLeft、paddingRight值
 * [paddingHorizontal] padding值
 * @return 返回view对象
 * */
fun View.setPaddingHorizontal(paddingHorizontal: Int): View {
    setPadding(paddingHorizontal, paddingTop, paddingHorizontal, paddingBottom)
    return this
}

/**
 * 设置view的paddingLeft、paddingRight值
 * [paddingLeft] paddingLeft值
 * [paddingRight] paddingRight值
 * @return 返回view对象
 * */
fun View.setPaddingHorizontal(paddingLeft: Int, paddingRight: Int): View {
    setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
    return this
}

/**
 * 设置view的paddingTop、paddingBottom值
 * [paddingVertical] padding值
 * @return 返回view对象
 * */
fun View.setPaddingVertical(paddingVertical: Int): View {
    setPadding(paddingLeft, paddingVertical, paddingRight, paddingVertical)
    return this
}

/**
 * 设置view的paddingTop、paddingBottom值
 * [paddingTop] paddingTop值
 * [paddingBottom] paddingBottom值
 * @return 返回view对象
 * */
fun View.setPaddingVertical(paddingTop: Int, paddingBottom: Int): View {
    setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
    return this
}


/**
 * 设置view的四边padding值
 * [padding] padding值
 * @return 返回view对象
 * */
fun View.setPadding(padding: Int): View {
    setPadding(padding, padding, padding, padding)
    return this
}


/**
 * 设置view的宽度
 * [width] width值
 * @return 返回view对象
 * */
fun View.setWidth(width: Int): View {
    var layoutPar = layoutParams
    if (layoutPar.isNull()) {
        layoutPar = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
    if (layoutPar.width != width) {
        layoutPar.width = width
        layoutParams = layoutPar
    }
    return this
}


/**
 * 设置view的高度
 * [height] height值
 * @return 返回view对象
 * */
fun View.setHeight(height: Int): View {
    var layoutPar = layoutParams
    if (layoutPar.isNull()) {
        layoutPar = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
    if (layoutPar.height != height) {
        layoutPar.height = height
        layoutParams = layoutPar
    }
    return this
}


/**
 * 设置view的宽度高度
 * [width] width值
 * [height] height值
 * @return 返回view对象
 * */
fun View.setWidthHeight(width: Int, height: Int): View {
    var layoutPar = layoutParams
    if (layoutPar.isNull()) {
        layoutPar = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
    layoutPar.width = width
    layoutPar.height = height
    layoutParams = layoutPar
    return this
}

/**
 * 设置view的宽度填充父容器
 * @return 返回view对象
 * */
fun View.setWidthMatchParent(): View {
    return setWidth(ViewGroup.LayoutParams.MATCH_PARENT)
}

/**
 * 设置view的高度填充父容器
 * @return 返回view对象
 * */
fun View.setHeightMatchParent(): View {
    return setHeight(ViewGroup.LayoutParams.MATCH_PARENT)
}

/**
 * 设置view的宽度内容填充
 * @return 返回view对象
 * */
fun View.setWidthWrapContent(): View {
    return setWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
}

/**
 * 设置view的高度内容填充
 * @return 返回view对象
 * */
fun View.setHeightWrapContent(): View {
    return setHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
}

/**
 * 设置view的不可见但占用位置
 * @return 返回view对象
 * */
fun View.invisible(): View {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
    return this
}

/**
 * 设置view的可见并占用位置
 * @return 返回view对象
 * */
fun View.visible(): View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}

/**
 * 设置view的可见性
 * @return 返回view对象
 * */
fun View.visibleOrGone(isVisible: Boolean): View {
    if (isVisible) {
        visible()
    } else {
        gone()
    }
    return this
}

/**
 * 设置view的可见性
 * @return 返回view对象
 * */
fun View.visibleOrInvisible(isVisible: Boolean): View {
    if (isVisible) {
        visible()
    } else {
        invisible()
    }
    return this
}

/**
 * 设置view的不可见不占用位置
 * @return 返回view对象
 * */
fun View.gone(): View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}

/**
 * view的x轴中心值
 * @return view的x轴中心值
 */
fun View.centerX(): Int {
    return (right - left) / 2
}

/**
 * view的y轴中心值
 * @return 返回view的y轴中心值
 */
fun View.centerY(): Int {
    return (bottom - top) / 2
}

/**
 * 设置view的margin
 * [margin] margin值
 * @return 返回view对象
 */
fun View.setMargin(margin: Int): View {
    return setMargin(margin, margin, margin, margin)
}

/**
 * 设置view的margin
 * [left] left值
 * [top] top值
 * [right] right值
 * [bottom] bottom值
 * @return 返回view对象
 */
fun View.setMargin(left: Int, top: Int, right: Int, bottom: Int): View {
    try {
        var layoutPar = layoutParams as ViewGroup.MarginLayoutParams
        if (layoutPar.isNull()) {
            layoutPar = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        layoutPar.setMargins(left, top, right, bottom)
        layoutParams = layoutPar
    } catch (thr: Throwable) {
        thr.plogE()
    }
    return this
}


/**
 * 设置view的left
 * [left] left值
 * @return 返回view对象
 */
fun View.setMarginLeft(left: Int): View {
    try {
        var layoutPar = layoutParams as ViewGroup.MarginLayoutParams
        if (layoutPar.isNull()) {
            layoutPar = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        if (layoutPar.leftMargin != left) {
            layoutPar.setMargins(
                left,
                layoutPar.topMargin,
                layoutPar.rightMargin,
                layoutPar.bottomMargin
            )
            layoutParams = layoutPar
        }
    } catch (thr: Throwable) {
        thr.plogE()
    }
    return this
}

/**
 * 设置view的MarginStart
 * [start] start值
 * @return 返回view对象
 */
fun View.setMarginStart(start: Int): View {
    try {
        var layoutPar = layoutParams as ViewGroup.MarginLayoutParams
        if (layoutPar.isNull()) {
            layoutPar = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        if (layoutPar.marginStart != start) {
            layoutPar.setMargins(
                start,
                layoutPar.topMargin,
                layoutPar.rightMargin,
                layoutPar.bottomMargin
            )
            layoutPar.marginStart = start
            layoutParams = layoutPar
        }
    } catch (thr: Throwable) {
        thr.plogE()
    }
    return this
}

/**
 * 设置view的top
 * [top] top值
 * @return 返回view对象
 */
fun View.setMarginTop(top: Int): View {

    try {
        var layoutPar = layoutParams as ViewGroup.MarginLayoutParams
        if (layoutPar.isNull()) {
            layoutPar = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        if (layoutPar.topMargin != top) {
            layoutPar.setMargins(
                layoutPar.leftMargin,
                top,
                layoutPar.rightMargin,
                layoutPar.bottomMargin
            )
            layoutParams = layoutPar
        }
    } catch (thr: Throwable) {
        thr.plogE()
    }
    return this
}

/**
 * 设置view的right
 * [right] right值
 * @return 返回view对象
 */
fun View.setMarginRight(right: Int): View {
    try {
        var layoutPar = layoutParams as ViewGroup.MarginLayoutParams
        if (layoutPar.isNull()) {
            layoutPar = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        if (layoutPar.rightMargin != right) {
            layoutPar.setMargins(
                layoutPar.leftMargin,
                layoutPar.topMargin,
                right,
                layoutPar.bottomMargin
            )
            layoutParams = layoutPar
        }
    } catch (thr: Throwable) {
        thr.plogE()
    }
    return this
}

/**
 * 设置view的MarginEnd
 * [end] end值
 * @return 返回view对象
 */
fun View.setMarginEnd(end: Int): View {
    try {
        var layoutPar = layoutParams as ViewGroup.MarginLayoutParams
        if (layoutPar.isNull()) {
            layoutPar = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        if (layoutPar.marginEnd != end) {
            layoutPar.setMargins(
                layoutPar.leftMargin,
                layoutPar.topMargin,
                end,
                layoutPar.bottomMargin
            )
            layoutPar.marginEnd = end
            layoutParams = layoutPar
        }
    } catch (thr: Throwable) {
        thr.plogE()
    }
    return this
}

/**
 * 设置view的bottom
 * [bottom] bottom值
 * @return 返回view对象
 */
fun View.setMarginBottom(bottom: Int): View {
    try {
        var layoutPar = layoutParams as ViewGroup.MarginLayoutParams
        if (layoutPar.isNull()) {
            layoutPar = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        if (layoutPar.bottomMargin != bottom) {
            layoutPar.setMargins(
                layoutPar.leftMargin,
                layoutPar.topMargin,
                layoutPar.rightMargin,
                bottom
            )
            layoutParams = layoutPar
        }
    } catch (thr: Throwable) {
        thr.plogE()
    }
    return this
}


/**
 * 设置view的水平margin
 * [marginHorizontal] margin值
 * @return 返回view对象
 */
fun View.setMarginHorizontal(marginHorizontal: Int): View {
    return setMarginHorizontal(marginHorizontal, marginHorizontal)
}

/**
 * 设置view的水平margin
 * [start] start值
 * [end] end值
 * @return 返回view对象
 */
fun View.setMarginHorizontal(start: Int, end: Int): View {
    try {
        var layoutPar = layoutParams as ViewGroup.MarginLayoutParams
        if (layoutPar.isNull()) {
            layoutPar = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        layoutPar.setMargins(start, layoutPar.topMargin, end, layoutPar.bottomMargin)
        layoutPar.marginStart = start
        layoutPar.marginEnd = end
        layoutParams = layoutPar
    } catch (thr: Throwable) {
        thr.plogE()
    }
    return this
}

/**
 * 设置view的垂直margin
 * [marginVertical] margin值
 * @return 返回view对象
 */
fun View.setMarginVertical(marginVertical: Int): View {
    return setMarginVertical(marginVertical, marginVertical)
}

/**
 * 设置view的垂直margin
 * [top] top值
 * [bottom] bottom值
 * @return 返回view对象
 */
fun View.setMarginVertical(top: Int, bottom: Int): View {
    try {
        var layoutPar = layoutParams as ViewGroup.MarginLayoutParams
        if (layoutPar.isNull()) {
            layoutPar = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        layoutPar.setMargins(layoutPar.leftMargin, top, layoutPar.rightMargin, bottom)
        layoutParams = layoutPar
    } catch (thr: Throwable) {
        thr.plogE()
    }
    return this
}


/**
 * 设置字体加粗
 * [isBold]  字体是否加粗，默认true
 * @return 返回TextView对象
 */
fun TextView.bold(isBold: Boolean = true): TextView {
    paint.isFakeBoldText = isBold
    return this
}

/**
 * 设置字体显示下划线
 * @return 返回TextView对象
 */
fun TextView.underline(): TextView {
    paint.flags = Paint.UNDERLINE_TEXT_FLAG //下划线
    paint.isAntiAlias = true //抗锯齿
    return this
}


/**
 * 在EditText上展示或隐藏键盘
 * [isShow]  展示或隐藏键盘，默认true
 * @return 返回EditText对象
 */
fun EditText.softInput(isShow: Boolean = true): EditText {
    if (isShow) {
        requestFocus()
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
        inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        if (text.notNullAndEmpty()) {
            setSelection(text.length)
        }
    } else {
        clearFocus()
        (context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            windowToken,
            0
        )
    }
    return this
}

/**
 * 获取输入框的内容长度
 */
fun EditText.calculateLength(): Int {
    var varLength = 0
    text.toString().toCharArray().forEach {
        /**
         * 增加中文标点范围 ，标点范围有待详细化
         * 中文字符范围0x4e00 0x9fbb
         * */
        varLength += if (it.code in 0x2E80..0xFE4F || it.code in 0xA13F..0xAA40 || it.code >= 0x80) {
            2
        } else {
            1
        }
    }
    return varLength
}

/**
 * view是否处于顶部
 * */
fun View?.isTop(): Boolean {
    return when {
        this.isNull() -> {
            false
        }
        this is RecyclerView -> {
            (this as RecyclerView).isTop()
        }
        this is AbsListView -> {
            (this as AbsListView).isTop()
        }
        this is ScrollView -> {
            (this as ScrollView).isTop()
        }
        this is NestedScrollView -> {
            (this as NestedScrollView).isTop()
        }
        this is WebView -> {
            (this as WebView).isTop()
        }
        else -> {
            false
        }
    }
}

fun RecyclerView.isTop(): Boolean {
    return !canScrollVertically(-1)
}

fun AbsListView.isTop(): Boolean {
    return !canScrollVertically(-1)
}

fun ScrollView.isTop(): Boolean {
    return scrollY == 0
}

fun NestedScrollView.isTop(): Boolean {
    return scrollY == 0
}

fun WebView.isTop(): Boolean {
    return scrollY == 0
}

/**
 * view是否处于底部
 * */
fun View?.isBottom(): Boolean {
    return when {
        this.isNull() -> {
            false
        }
        this is RecyclerView -> {
            (this as RecyclerView).isBottom()
        }
        this is AbsListView -> {
            (this as AbsListView).isBottom()
        }
        this is ScrollView -> {
            (this as ScrollView).isBottom()
        }
        this is NestedScrollView -> {
            (this as NestedScrollView).isBottom()
        }
        this is WebView -> {
            (this as WebView).isBottom()
        }
        else -> {
            false
        }
    }
}

fun RecyclerView.isBottom(): Boolean {
    return !canScrollVertically(1)
}

fun AbsListView.isBottom(): Boolean {
    return !canScrollVertically(1)
}

fun ScrollView.isBottom(): Boolean {
    val contentView = getChildAt(0)
    return contentView.notNull() && contentView.measuredHeight == scrollY + height
}

fun NestedScrollView.isBottom(): Boolean {
    val contentView = getChildAt(0)
    return contentView.notNull() && contentView.measuredHeight == scrollY + height
}

fun WebView.isBottom(): Boolean {
    return !canScrollVertically(1)
}

/**
 * view点击事件
 * */
fun View.click(shortInterval: Int = 0, clickInvoke: () -> Unit): View {
    ProductViewUtil.click(this, shortInterval, clickInvoke)
    return this
}

/**
 * TextView的onTextChanged回调
 * */
fun TextView.onTextChanged(otcInterval: (CharSequence, Int, Int, Int) -> Unit): TextView {
    val tw = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s.notNull()) {
                otcInterval.invoke(s!!, start, before, count)
            }
        }

        override fun afterTextChanged(s: Editable?) {

        }
    }
    addTextChangedListener(tw)
    return this
}

/**
 * TextView的beforeTextChanged回调
 * */
fun TextView.beforeTextChanged(btcInterval: (CharSequence, Int, Int, Int) -> Unit): TextView {
    val tw = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            if (s.notNull()) {
                btcInterval.invoke(s!!, start, count, after)
            }
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {

        }

    }
    addTextChangedListener(tw)
    return this
}

/**
 * TextView的beforeTextChanged回调
 * */
fun TextView.afterTextChanged(atcInterval: (Editable) -> Unit): TextView {
    val tw = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            if (s.notNull()) {
                atcInterval.invoke(s!!)
            }
        }

    }
    addTextChangedListener(tw)
    return this
}

/**
 * 控件可见时执行回调[action]
 * */
fun View.visibleCallback(action: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            action.invoke()
        }
    })
}

/**
 * 添加InputFilter过滤器
 * */
fun EditText.addFilter(inputFilter: InputFilter): EditText {
    val size = filters.size
    if (size > 0) {
        val newFil = arrayOfNulls<InputFilter>(size + 1)
        for (i in 0 until size) {
            newFil[i] = filters[i]
        }
        newFil[size] = inputFilter
        filters = newFil
    } else {
        filters = arrayOf<InputFilter>(inputFilter)
    }
    return this
}

/**
 * 添加InputFilter过滤器
 * */
fun EditText.addFilters(inputFilters: Array<InputFilter>): EditText {
    val size = filters.size
    if (size > 0) {
        val newFil = arrayOfNulls<InputFilter>(size + inputFilters.size)
        for (i in 0 until size) {
            newFil[i] = filters[i]
        }
        for (i in size until size + inputFilters.size) {
            newFil[i] = inputFilters[i - size]
        }
        filters = newFil
    } else {
        filters = inputFilters
    }
    return this
}

/**
 * view长按事件
 * */
fun View.longClick(longClickInvoke: () -> Unit): View {
    setOnLongClickListener {
        longClickInvoke.invoke()
        true
    }
    return this
}

/**
 * 是否是RTL布局
 * */
fun View.isRTL(): Boolean {
    return context.resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL
}