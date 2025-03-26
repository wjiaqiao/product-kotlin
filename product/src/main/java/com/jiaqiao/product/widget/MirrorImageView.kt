package com.jiaqiao.product.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.ViewCompat
import com.jiaqiao.product.ext.isRTL

/**
 * 镜像ImageView
 * 用于解决在RTL布局中，ImageView的图片显示方向错误的问题
 * */
class MirrorImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    init {
        // 在初始化时设置布局方向变化的监听器
        ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
            updateScaleX()
            insets
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        updateScaleX()
    }

    private fun updateScaleX() {
        scaleX = if (isRTL()) -1f else 1f
    }
}