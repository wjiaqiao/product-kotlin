package com.jiaqiao.product.ext

import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.webkit.WebView
import android.widget.AbsListView
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.jiaqiao.product.util.ProductViewUtil


/**
 * 设置view的PaddingStart值
 * [paddingStart] PaddingStart值
 * @return 返回view对象
 * */
fun View.setPaddingStart(paddingStart: Int): View {
    setPaddingRelative(paddingStart, paddingTop, paddingEnd, paddingBottom)
    return this
}

/**
 * 设置view的paddingTop值
 * [paddingTop] paddingTop值
 * @return 返回view对象
 * */
fun View.setPaddingTop(paddingTop: Int): View {
    setPaddingRelative(paddingStart, paddingTop, paddingEnd, paddingBottom)
    return this
}

/**
 * 设置view的paddingEnd值
 * [paddingEnd] paddingEnd值
 * @return 返回view对象
 * */
fun View.setPaddingEnd(paddingEnd: Int): View {
    setPaddingRelative(paddingStart, paddingTop, paddingEnd, paddingBottom)
    return this
}

/**
 * 设置view的paddingBottom值
 * [paddingBottom] paddingBottom值
 * @return 返回view对象
 * */
fun View.setPaddingBottom(paddingBottom: Int): View {
    setPaddingRelative(paddingStart, paddingTop, paddingEnd, paddingBottom)
    return this
}


/**
 * 设置view的paddingStart、paddingEnd值
 * [paddingHorizontal] padding值
 * @return 返回view对象
 * */
fun View.setPaddingHorizontal(paddingHorizontal: Int): View {
    setPaddingRelative(paddingHorizontal, paddingTop, paddingHorizontal, paddingBottom)
    return this
}

/**
 * 设置view的paddingStart、paddingEnd值
 * [paddingStart] paddingStart值
 * [paddingEnd] paddingEnd值
 * @return 返回view对象
 * */
fun View.setPaddingHorizontal(paddingStart: Int, paddingEnd: Int): View {
    setPaddingRelative(paddingStart, paddingTop, paddingEnd, paddingBottom)
    return this
}

/**
 * 设置view的paddingTop、paddingBottom值
 * [paddingVertical] padding值
 * @return 返回view对象
 * */
fun View.setPaddingVertical(paddingVertical: Int): View {
    setPaddingRelative(paddingStart, paddingVertical, paddingEnd, paddingVertical)
    return this
}

/**
 * 设置view的paddingTop、paddingBottom值
 * [paddingTop] paddingTop值
 * [paddingBottom] paddingBottom值
 * @return 返回view对象
 * */
fun View.setPaddingVertical(paddingTop: Int, paddingBottom: Int): View {
    setPaddingRelative(paddingStart, paddingTop, paddingEnd, paddingBottom)
    return this
}


/**
 * 设置view的四边padding值
 * [padding] padding值
 * @return 返回view对象
 * */
fun View.setPaddingRelative(padding: Int): View {
    setPaddingRelative(padding, padding, padding, padding)
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