package com.jiaqiao.product.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import com.jiaqiao.product.ext.isNull
import com.jiaqiao.product.ext.notNull

/**
 *
 * 后台启动activity辅助类
 *
 * 需要悬浮窗权限，api小于等于23不需要悬浮窗权限
 *
 *
 * */
class BackgroundOpen(val activity: Activity) {

    private var view: View? = null
    private var layoutParams: WindowManager.LayoutParams? = null
    private val mOnViewVisibleListener by lazy {
        object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                view?.let {
                    it.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    it.callOnClick()
                }
            }
        }
    }

    //检查是否有悬浮窗权限
    fun canDrawOverlays(): Boolean {
        return (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(activity))
    }

    //开始后台跳转activity
    @SuppressLint("SuspiciousIndentation")
    fun open(openListener: () -> Unit) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openListener.invoke()
            return
        } else if (!Settings.canDrawOverlays(activity)) {//检查授权悬浮窗权限
            return
        }
        var width = 0
        var height = 0
        val wm = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (wm.isNull()) {
            val dm = activity.resources.displayMetrics
            width = dm.widthPixels
            height = dm.heightPixels
        } else {
            val point = Point()
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                wm.defaultDisplay.getRealSize(point)
//            } else {
                wm.defaultDisplay.getSize(point)
//            }
            width = point.x
            height = point.y
        }
        /**
         *
         * api>=23
         *
         * 使用悬浮窗绕过后台启动界面权限，启动页面
         *
         * 华为需要悬浮窗权限
         * 小米需要悬浮窗、后台启动界面权限
         * VIVO需要悬浮窗、后台启动界面权限
         *
         */
        //获取WindowManager服务
        val applicationContext = activity.applicationContext
        val windowManager =
            applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        //新建悬浮窗控件
        if (view.isNull()) {
            view = View(applicationContext)
        }
        view!!.setOnClickListener {
            openListener.invoke()
            if (view!!.parent.notNull()) {
                windowManager.removeView(view)
            }
        }
        view!!.setBackgroundColor(Color.TRANSPARENT)
        if (layoutParams.isNull()) {
            layoutParams = WindowManager.LayoutParams()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                layoutParams!!.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                layoutParams!!.type = WindowManager.LayoutParams.TYPE_PHONE
            }
            layoutParams!!.format = PixelFormat.RGBA_8888
            layoutParams!!.flags =
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            layoutParams!!.width = 1
            layoutParams!!.height = 1
            layoutParams!!.x = -width / 2
            layoutParams!!.y = height / 2
        }
        if (view!!.parent.notNull()) {
            windowManager.removeView(view)
        }
        val viewTreeObserver = view!!.viewTreeObserver
        viewTreeObserver.removeOnGlobalLayoutListener(mOnViewVisibleListener)
        viewTreeObserver.addOnGlobalLayoutListener(mOnViewVisibleListener)
        windowManager.addView(view, layoutParams)
    }


}