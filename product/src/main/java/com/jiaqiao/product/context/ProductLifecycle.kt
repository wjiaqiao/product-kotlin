package com.jiaqiao.product.context

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.jiaqiao.product.config.ProductActivityCallback
import com.jiaqiao.product.ext.runPCatch
import com.jiaqiao.product.util.ProductApp
import com.jiaqiao.product.util.ProductThreadPool
import com.jiaqiao.product.util.ProductViewUtil

class ProductLifecycle : Application.ActivityLifecycleCallbacks {

    //过滤重复开启activity
    private var state = 0

    //已添加的activity数量
    private var startCount = 0

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        ProductApp.addActivity(activity)
    }

    override fun onActivityStarted(activity: Activity) {
        startCount++
        if (startCount > 0) {
            if (state != 2) {
                state = 2
                appResume()
            }
        }
    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {
        startCount--
        if (startCount == 0) {
            state = 1
            appPaused()
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        ProductApp.removeActivity(activity)
        if (ProductApp.activitySize <= 0) {
            ProductThreadPool.close()
            ProductViewUtil.cleanClickMap()
            runPCatch { ProductActivityCallback.onAppDestroyedAction?.invoke() }
        }
    }


    private fun appResume() {
        ProductApp.isAppResume = true
        runPCatch { ProductActivityCallback.onAppResumeAction?.invoke() }
//        "回到app".libPlog()
    }

    private fun appPaused() {
        ProductApp.isAppResume = false
        runPCatch { ProductActivityCallback.onAppPausedAction?.invoke() }
//        "退到后台".libPlog()
    }

}