package com.jiaqiao.product.util

import android.app.Activity
import android.content.Context
import com.jiaqiao.product.context.ProductContentProvider
import com.jiaqiao.product.ext.isAvailable
import com.jiaqiao.product.ext.notNull
import com.jiaqiao.product.ext.notNullAndEmpty
import java.util.*

object ProductApp {

    //存活的activity列表
    private val activityList = LinkedList<Activity>()

    //app前台状态
    var isAppResume = false

    //activity数量，最后的activity是最顶部的activity
    val activitySize: Int get() = activityList.size

    //栈顶Activity
    val currentActivity: Activity? get() = if (activityList.isNullOrEmpty()) null else activityList.last

    //获取当前活动的activity
    val availableActivity: Activity?
        get() {
            if (activityList.notNullAndEmpty()) {
                for (i in activityList.size - 1 downTo 0) {
                    val activity = activityList[i]
                    if (activity.isAvailable()) {
                        return activity
                    }
                }
            }
            return null
        }

    //获取当前可用且处于活动状态的context，有可能获取到application
    val context: Context
        get() {
            return when {
                availableActivity.notNull() -> availableActivity!!
                currentActivity.notNull() -> currentActivity!!
                else -> ProductContentProvider.getContext()
            }
        }


    /**
     * 入栈Activity
     */
    fun addActivity(activity: Activity) {
        activityList.add(activity)
    }

    /**
     * finish并移除Activity
     */
    fun removeActivity(activity: Activity) {
        if (!activity.isFinishing) {
            activity.finish()
        }
        activityList.remove(activity)
    }

    /**
     * finish并移除对应class的Activity
     */
    fun removeActivity(cls: Class<*>) {
        if (activityList.isNullOrEmpty()) return
        val index = activityList.indexOfFirst { it.javaClass == cls }
        if (index == -1) return
        if (!activityList[index].isFinishing) {
            activityList[index].finish()
        }
        activityList.removeAt(index)
    }

    /**
     * Activity全部finish并移除
     */
    fun removeAllActivity() {
        activityList.forEach {
            if (!it.isFinishing) {
                it.finish()
            }
        }
        activityList.clear()
    }

    /**
     *
     *除了顶部的activity，其他activity全部finish并移除
     **/
    fun removeAllExitTop() {
        if (activityList.size <= 1) return
        for (i in (0 until activityList.size - 1)) {
            val act = activityList[i]
            if (!act.isFinishing) {
                act.finish()
            }
        }
        val top = activityList.last()
        activityList.clear()
        activityList.add(top)
    }


}