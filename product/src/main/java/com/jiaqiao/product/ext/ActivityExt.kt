package com.jiaqiao.product.ext

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.jiaqiao.product.util.ProductUiUtil


/**
 * 设置状态栏字体白色
 * */
fun Activity.statusBarWhiteFont() {
    window.statusBarWhiteFont()
}

/**
 * 设置状态栏字体黑色
 * */
fun Activity.statusBarBlackFont() {
    window.statusBarBlackFont()
}

/**
 * 设置状态栏颜色
 * */
fun Activity.statusBarColor(color: Int) {
    window.statusBarColor(color)
}

/**
 * 设置状态栏颜色
 * */
fun Window.statusBarColor(color: Int) {
    statusBarColor = color
}


/**
 * 设置导航栏字体白色
 * */
fun Activity.navigationBarWhiteFont() {
    window.navigationBarWhiteFont()
}


/**
 * 设置导航栏字体黑色
 * */
fun Activity.navigationBarBlackFont() {
    window.navigationBarBlackFont()
}

/**
 * 设置导航栏颜色
 * */
fun Activity.navigationBarColor(color: Int) {
    window.navigationBarColor(color)
}


/**
 * 隐藏状态栏
 * */
fun Activity.hideStatusBar() {
    window.hideStatusBar()
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
        actionBar?.hide()
    }
}

/**
 * 隐藏导航栏
 * */
fun Activity.hideNavigationBar() {
    window.hideNavigationBar()
}

/**
 * 显示状态栏和导航栏
 * */
fun Activity.showStatusNavigationBar() {
    window.showStatusNavigationBar()
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
        actionBar?.show()
    }
}


/**
 * view显示在状态栏里
 * */
fun Activity.viewIntoStatusBar() {
    window.viewIntoStatusBar()
}

/**
 * view显示在导航栏里
 * */
fun Activity.viewIntoNavigationBar() {
    window.viewIntoNavigationBar()
}


/**
 * view显示在bar里
 * */
fun Activity.viewIntoBar() {
    viewIntoStatusBar()
    viewIntoNavigationBar()
}

/**
 * 判断Activity是否处于可用状态
 * */
fun Activity?.isAvailable(): Boolean {
    return this.notNull() && !this!!.isFinishing && !this.isDestroyed
}

/**
 * 当前activity是否处于横屏状态
 */
fun Activity.isLandscape(): Boolean {
    return resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}
