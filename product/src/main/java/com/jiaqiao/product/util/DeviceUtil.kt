package com.jiaqiao.product.util

import android.os.Build
import android.text.TextUtils

object DeviceUtil {

    /**
     * 返回设备的系统sdk版本号
     */
    fun getSDKVersionCode(): Int {
        return Build.VERSION.SDK_INT
    }


    /**
     * 返回设备名称
     */
    fun getModel(): String {
        var model = Build.MODEL
        model = model.trim { it <= ' ' }?.replace("\\s*".toRegex(), "")
        return model
    }

    /**
     * 获取设别支持的abi类型
     */
    fun getABIs(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Build.SUPPORTED_ABIS
        } else {
            if (!TextUtils.isEmpty(Build.CPU_ABI2)) {
                arrayOf(Build.CPU_ABI, Build.CPU_ABI2)
            } else arrayOf(Build.CPU_ABI)
        }
    }

}
