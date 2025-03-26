package com.jiaqiao.product.config

object ProductActivityCallback {


    //app从后台回到前台
    var onAppResumeAction: (() -> Unit)? = null

    //app退到后台
    var onAppPausedAction: (() -> Unit)? = null

    //app的activity全部销毁
    var onAppDestroyedAction: (() -> Unit)? = null


}