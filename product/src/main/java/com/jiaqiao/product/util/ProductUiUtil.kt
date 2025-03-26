package com.jiaqiao.product.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.jiaqiao.product.ext.isNull
import com.jiaqiao.product.ext.navigationBarHeight
import com.jiaqiao.product.ext.plogE
import com.jiaqiao.product.ext.runPlogCatch

object ProductUiUtil {

    private var toast: Toast? = null

    /**
     * 获取状态栏高度(单位：像素)
     */
    fun getStatusBarHeight(context: Context): Int {
        return runPlogCatch {
            var result = 0
            val resourceId =
                context.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = context.resources.getDimensionPixelSize(resourceId)
            }
            result
        }.getOrDefault(0)
    }

    /**
     * 获取导航栏高度(单位：像素)
     */
    fun getNavigationBarHeight(context: Context): Int {
        return runPlogCatch {
            var result = 0
            val resourceId =
                context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = context.resources.getDimensionPixelSize(resourceId)
            }
            result
        }.getOrDefault(0)
    }

    /**
     * 判断导航栏是否存在
     */
    fun hasNavigationBar(activity: ComponentActivity): Boolean {
        return runPlogCatch {
            val realDisplayMetrics = DisplayMetrics()
            (activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
                .defaultDisplay.getRealMetrics(realDisplayMetrics)
            val realHeight = realDisplayMetrics.heightPixels
            val navigationBarHeight = activity.navigationBarHeight()
            val conView =
                activity.window.decorView.findViewById<ViewGroup>(android.R.id.content)?.getChildAt(0)
            val viewHeight = if (conView.isNull()) {
                0
            } else if (conView?.measuredHeight!! > 0) {
                conView.measuredHeight
            } else if (conView.height > 0) {
                conView.height
            } else {
                0
            }
            (viewHeight + navigationBarHeight) == realHeight
        }.getOrDefault(false)
    }

    /**
     * 显示toast提醒
     * [string]提醒内容
     * */
    fun toast(context: Context?, string: String) {
        if (context.isNull()) return
        toast?.cancel()
        toast = Toast.makeText(context, string, Toast.LENGTH_SHORT)
        toast?.show()
    }

    /**
     * 长时间显示toast提醒
     * [string]提醒内容
     * */
    fun toastLong(context: Context?, string: String) {
        if (context.isNull()) return
        toast?.cancel()
        toast = Toast.makeText(context, string, Toast.LENGTH_LONG)
        toast?.show()
    }


    /**
     * 显示toast提醒
     * [resId] string的资源ID
     * */
    fun toast(context: Context?, resId: Int) {
        if (context.isNull()) return
        toast?.cancel()
        toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT)
        toast?.show()
    }

    /**
     * 长时间显示toast提醒
     * [resId] string的资源ID
     * */
    fun toastLong(context: Context?, resId: Int) {
        if (context.isNull()) return
        toast?.cancel()
        toast = Toast.makeText(context, resId, Toast.LENGTH_LONG)
        toast?.show()
    }
}