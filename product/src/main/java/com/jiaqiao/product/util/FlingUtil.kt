package com.jiaqiao.product.util

import android.content.Context
import android.hardware.SensorManager
import android.view.ViewConfiguration

/**
 * 计算滑动速度的工具
 */
object FlingUtil {


    private const val INFLEXION = 0.35f
    private val mFlingFriction = ViewConfiguration.getScrollFriction()
    private val DECELERATION_RATE = (Math.log(0.78) / Math.log(0.9)).toFloat()


    private fun getSplineDeceleration(context: Context, velocity: Int): Double {
        return Math.log(INFLEXION * Math.abs(velocity) / (mFlingFriction * getPhysicalCoeff(context)))
    }

    private fun getSplineDecelerationByDistance(context: Context, distance: Double): Double {
        val decelMinusOne = DECELERATION_RATE - 1.0
        return decelMinusOne * Math.log(distance / (mFlingFriction * getPhysicalCoeff(context))) / DECELERATION_RATE
    }

    private fun getPhysicalCoeff(context: Context): Double {
        val ppi = context.resources.displayMetrics.density * 160.0f
        return (SensorManager.GRAVITY_EARTH * 39.37f * ppi * 0.84f).toDouble()
    }

    //通过初始速度获取最终滑动距离
    fun getDistanceByVelocity(context: Context, velocity: Int): Double {
        val l = getSplineDeceleration(context, velocity)
        val decelMinusOne = DECELERATION_RATE - 1.0
        return mFlingFriction * getPhysicalCoeff(context) * Math.exp(DECELERATION_RATE / decelMinusOne * l)
    }


    //通过需要滑动的距离获取初始速度
    fun getVelocityByDistance(context: Context, distance: Double): Int {
        val l = getSplineDecelerationByDistance(context, distance)
        val velocity =
            (Math.exp(l) * mFlingFriction * getPhysicalCoeff(context) / INFLEXION).toInt()
        return Math.abs(velocity)
    }


}