package com.jiaqiao.product.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * 限制控件，限制子控件完全显示在父容器中，前提子控件不超出父容器的宽高
 * */
open class LimitView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {

    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val relaLeft = 0
        val relaTop = 0
        val relaRight = relaLeft + right - left
        val relaBottom = relaTop + bottom - top

        for (i in 0 until childCount) {
            getChildAt(i)?.let {
                val itW = it.right - it.left
                val itH = it.bottom - it.top
                if (itW <= relaRight && itH <= relaBottom) {
                    var itL = it.left
                    var itT = it.top
                    var itR = it.right
                    var itB = it.bottom
                    if (itL < relaLeft) {
                        itL = relaLeft
                        itR = itL + itW
                    }
                    if (itR > relaRight) {
                        itR = relaRight
                        itL = itR - itW
                    }
                    if (itT < relaTop) {
                        itT = relaTop
                        itB = itT + itH
                    }
                    if (itB > relaBottom) {
                        itB = relaBottom
                        itT = itB - itH
                    }
                    if (itL != it.left
                        || itT != it.top
                        || itR != it.right
                        || itB != it.bottom
                    ) {
                        it.layout(itL, itT, itR, itB)
                    }
                }
            }
        }
    }

}