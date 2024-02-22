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
 * 设置状态栏字体白色
 * */
fun Window.statusBarWhiteFont() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        ViewCompat.getWindowInsetsController(decorView)?.isAppearanceLightStatusBars = false
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        decorView.systemUiVisibility =
            decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }
}

/**
 * 设置状态栏字体黑色
 * */
fun Activity.statusBarBlackFont() {
    window.statusBarBlackFont()
}

/**
 * 设置状态栏字体黑色
 * */
fun Window.statusBarBlackFont() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        ViewCompat.getWindowInsetsController(decorView)?.isAppearanceLightStatusBars = true
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        decorView.systemUiVisibility =
            decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
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
 * 设置导航栏字体白色
 * */
fun Window.navigationBarWhiteFont() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        return
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && isNavigationBarContrastEnforced) {
        isNavigationBarContrastEnforced = false
    }
    val befNavColor = navigationBarColor
    navigationBarColor = Color.TRANSPARENT
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        decorView.findViewById<ViewGroup>(android.R.id.content).windowInsetsController?.setSystemBarsAppearance(
            0,
            WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
        )
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        decorView.systemUiVisibility =
            decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
    }
    if (befNavColor != -1 && befNavColor != Color.TRANSPARENT) {
        navigationBarColor = befNavColor
    }
}


/**
 * 设置导航栏字体黑色
 * */
fun Activity.navigationBarBlackFont() {
    window.navigationBarBlackFont()
}

/**
 * 设置导航栏字体黑色
 * */
fun Window.navigationBarBlackFont() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        return
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && isNavigationBarContrastEnforced) {
        isNavigationBarContrastEnforced = false
    }
    val befNavColor = navigationBarColor
    navigationBarColor = Color.TRANSPARENT
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        decorView.findViewById<ViewGroup>(android.R.id.content).windowInsetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
        )
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        decorView.systemUiVisibility =
            decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    }
    if (befNavColor != -1 && befNavColor != Color.TRANSPARENT) {
        navigationBarColor = befNavColor
    }
}


/**
 * 设置导航栏颜色
 * */
fun Activity.navigationBarColor(color: Int) {
    window.navigationBarColor(color)
}

/**
 * 设置导航栏颜色
 * */
fun Window.navigationBarColor(color: Int) {
    navigationBarColor = color
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
 * 隐藏状态栏
 * */
fun Window.hideStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        decorView.windowInsetsController?.hide(WindowInsets.Type.statusBars())
        decorView.windowInsetsController?.systemBarsBehavior =
            WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    } else {
        decorView.systemUiVisibility =
            decorView.systemUiVisibility or (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

    }
}

/**
 * 隐藏导航栏
 * */
fun Activity.hideNavigationBar() {
    window.hideNavigationBar()
}

/**
 * 隐藏导航栏
 * */
fun Window.hideNavigationBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        decorView.windowInsetsController?.hide(WindowInsets.Type.navigationBars())
        decorView.windowInsetsController?.systemBarsBehavior =
            WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    } else {
        decorView.systemUiVisibility =
            decorView.systemUiVisibility or (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }
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
 * 显示状态栏和导航栏
 * */
fun Window.showStatusNavigationBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        decorView.windowInsetsController?.show(WindowInsets.Type.statusBars())
        decorView.windowInsetsController?.show(WindowInsets.Type.navigationBars())
    } else {
        decorView.systemUiVisibility =
            decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }
}

/**
 * view显示在状态栏里
 * */
fun Activity.viewIntoStatusBar() {
    window.viewIntoStatusBar()
}

/**
 * view显示在状态栏里
 * */
fun Window.viewIntoStatusBar() {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//        setDecorFitsSystemWindows(false)
//    } else {
    decorView.systemUiVisibility =
        decorView.systemUiVisibility or (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
//    }
    statusBarColor = Color.TRANSPARENT
}

/**
 * view显示在导航栏里
 * */
fun Activity.viewIntoNavigationBar() {
    window.viewIntoNavigationBar()
}

/**
 * view显示在导航栏里
 * */
fun Window.viewIntoNavigationBar() {
    decorView.systemUiVisibility =
        decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    navigationBarColor = Color.TRANSPARENT
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
 * 判断当前设备是否存在导航栏
 */
fun ComponentActivity.hasNavigationBar(): Boolean {
    return ProductUiUtil.hasNavigationBar(this)
}

/**
 * 状态栏字体是否是白色
 * */
fun ComponentActivity.isStatusBarWhiteFont(): Boolean {
    return window.isStatusBarWhiteFont()
}

/**
 * 状态栏字体是否是白色
 * */
fun Window.isStatusBarWhiteFont(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        var isWhite = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            isWhite =
                ViewCompat.getWindowInsetsController(decorView)?.isAppearanceLightStatusBars != true
        }
        if (!isWhite && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val state = decorView.systemUiVisibility
            isWhite = state == (state and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv())
        }
        isWhite
    } else {
        false
    }
}

/**
 * 状态栏字体是否是黑色
 * */
fun ComponentActivity.isStatusBarBlackFont(): Boolean {
    return window.isStatusBarBlackFont()
}

/**
 * 状态栏字体是否是黑色
 * */
fun Window.isStatusBarBlackFont(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        var isBlack = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            isBlack =
                ViewCompat.getWindowInsetsController(decorView)?.isAppearanceLightStatusBars == true
        }
        if (!isBlack && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val state = decorView.systemUiVisibility
            isBlack =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
                    && state != (state and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv())
                    && state == (state or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                ) {
                    false
                } else {
                    state == (state or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                }
        }
        isBlack
    } else {
        false
    }
}

///**
// * 导航栏字体是否是白色
// * */
//fun ComponentActivity.isNavigationBarWhiteFont(): Boolean {
//    return window.isNavigationBarWhiteFont()
//}
//
///**
// * 导航栏字体是否是白色
// * */
//fun Window.isNavigationBarWhiteFont(): Boolean {
//    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        var isWhite = false
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            decorView.findViewById<ViewGroup>(android.R.id.content).windowInsetsController?.systemBarsAppearance?.let { state ->
//                isWhite = state != (state or WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS)
//            }
//            "1.w".libPlog()
//        }
//        if (!isWhite && Build.VERSION.SDK_INT < Build.VERSION_CODES.R && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val state = decorView.systemUiVisibility
//            isWhite = !(state == (state and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv())
//                    || state == (state or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR))
//            state.libPlog("state")
//            (state or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR).libPlog("or")
//            (state or (state or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)).libPlog("or2")
//            (state and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()).libPlog("and")
//            (state and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR).libPlog("and2")
//            "2.w".libPlog()
//        }
//        isWhite
//    } else {
//        false
//    }
//}
//
///**
// * 导航栏字体是否是黑色
// * */
//fun ComponentActivity.isNavigationBarBlackFont(): Boolean {
//    return window.isNavigationBarBlackFont()
//}
//
///**
// * 导航栏字体是否是黑色
// * */
//fun Window.isNavigationBarBlackFont(): Boolean {
//    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        var isBlack = false
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            decorView.findViewById<ViewGroup>(android.R.id.content).windowInsetsController?.systemBarsAppearance?.let { state ->
//                isBlack =
//                    state == (state or WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS)
//            }
//            "1.b".libPlog()
//        }
//        if (!isBlack && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val state = decorView.systemUiVisibility
//            state.libPlog("state")
//            isBlack = state != (state and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv())
//            (state or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR).libPlog("or")
//            (state or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()).libPlog("or2")
//            (state and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR).libPlog("and")
//            (state and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()).libPlog("and2")
//            "2.b".libPlog()
//        }
//        isBlack
//    } else {
//        false
//    }
//}

/**
 * 当前activity是否处于横屏状态
 */
fun Activity.isLandscape(): Boolean {
    return resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}
