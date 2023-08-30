package com.jiaqiao.product.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.jiaqiao.product.ext.statusBarHeight

/**
 * 高度与手机状态栏相同的view，用于占用状态栏位置
 * */
open class StatusBarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(context.statusBarHeight(), MeasureSpec.EXACTLY)
        )
    }

}