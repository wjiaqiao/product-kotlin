package com.jiaqiao.product.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Looper
import android.os.Process
import androidx.core.content.ContextCompat
import com.jiaqiao.product.ext.notNull
import com.jiaqiao.product.ext.plogE
import com.jiaqiao.product.ext.processName
import com.jiaqiao.product.ext.runPlogCatch
import com.jiaqiao.product.ext.toDiv
import com.jiaqiao.product.throwable.ProductException
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest
import java.util.*
import kotlin.system.exitProcess

object ProductUtil {

    val isAndroid6 by lazy { Build.VERSION.SDK_INT >= Build.VERSION_CODES.M }
    val isAndroid8 by lazy { Build.VERSION.SDK_INT >= Build.VERSION_CODES.O }
    val isAndroid9 by lazy { Build.VERSION.SDK_INT >= Build.VERSION_CODES.P }
    val isAndroid10 by lazy { Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q }
    val isAndroid11 by lazy { Build.VERSION.SDK_INT >= Build.VERSION_CODES.R }
    val isAndroid12 by lazy { Build.VERSION.SDK_INT >= Build.VERSION_CODES.S }


    private val messageDigestCharArray by lazy {
        charArrayOf(
            48.toChar(),
            49.toChar(),
            50.toChar(),
            51.toChar(),
            52.toChar(),
            53.toChar(),
            54.toChar(),
            55.toChar(),
            56.toChar(),
            57.toChar(),
            97.toChar(),
            98.toChar(),
            99.toChar(),
            100.toChar(),
            101.toChar(),
            102.toChar()
        )
    }

    private val hexDigits by lazy { "0123456789ABCDEF".toCharArray() }

    /**
     *
     * 获取线性渐变中的某一个比例点的颜色
     *
     * [startColor] 开始颜色
     * [endColor]  结束颜色
     * [radio]  比例值，0.0~1.0
     * */
    fun radioColor(startColor: Int, endColor: Int, radio: Float): Int {
        val alphaStart: Int = Color.alpha(startColor)
        val redStart: Int = Color.red(startColor)
        val blueStart: Int = Color.blue(startColor)
        val greenStart: Int = Color.green(startColor)
        val alphaEnd: Int = Color.alpha(endColor)
        val redEnd: Int = Color.red(endColor)
        val blueEnd: Int = Color.blue(endColor)
        val greenEnd: Int = Color.green(endColor)
        val alpha = (alphaStart + ((alphaEnd - alphaStart) * radio + 0.5)).toInt()
        val red = (redStart + ((redEnd - redStart) * radio + 0.5)).toInt()
        val greed = (greenStart + ((greenEnd - greenStart) * radio + 0.5)).toInt()
        val blue = (blueStart + ((blueEnd - blueStart) * radio + 0.5)).toInt()
        return Color.argb(alpha, red, greed, blue)
    }

    /**
     * 判断当前线程是否是主线程
     *
     * */
    fun isMainThread(): Boolean {
        return Looper.getMainLooper() == Looper.myLooper()
    }


    /**
     * 判断当前Application是否是App的主进程
     *
     */
    fun isMainProcess(context: Context): Boolean {
        val currentName: String = context.processName()
        return currentName.isNotEmpty() && currentName == context.packageName
    }

    /**
     *
     * 判断权限名[permission]是否授权成功
     * [permission] 权限名
     * @return true 授权成功, false 授权失败
     * */
    fun hasPermission(context: Context, permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
            context,
            permission
        )
    }

    /**
     * 获取manifast清单中的权限列表
     * @return 返回权限列表
     * */
    fun getManifestPermissions(context: Context): MutableList<String> {
        var packageInfo: PackageInfo? = null
        runPlogCatch {
            packageInfo =
                context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_PERMISSIONS
                )
        }
        return packageInfo?.requestedPermissions?.toMutableList() ?: mutableListOf()
    }

    /**
     * 判断manifast清单中是否添加权限
     * [permission] 权限名
     * */
    fun manifestHasPermission(context: Context, permission: String): Boolean {
        return getManifestPermissions(context).any { it == permission }
    }

    /**
     * 判断包名应用是否安装
     * [context]  context对象
     * [packageName] 应用包名
     */
    fun isInstalled(context: Context, packageName: String): Boolean {
        return runPlogCatch {
            if (isAndroid12 && !manifestHasPermission(
                    context,
                    Manifest.permission.REQUEST_INSTALL_PACKAGES
                )
            ) {
                throw ProductException("Android12及以上需要在‘AndroidManifest.xml’添加‘REQUEST_INSTALL_PACKAGES’才能正常获取应用列表")
            }
            context.packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
                .notNull()
        }.getOrDefault(false)
    }

    /**
     * 获取对应包名信息
     * [paramContext]  context对象
     * [paramString] 应用包名
     */
    private fun getRawSignature(paramContext: Context, paramString: String): Array<Signature> {
        if (paramString.isEmpty()) {
            return arrayOf()
        }
        val packageManager = paramContext.packageManager
        return runPlogCatch {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val packageInfo = packageManager.getPackageInfo(
                    paramString,
                    PackageManager.GET_SIGNING_CERTIFICATES
                )
                packageInfo.signingInfo?.apkContentsSigners
            } else {
                val packageInfo =
                    packageManager.getPackageInfo(paramString, PackageManager.GET_SIGNATURES)
                packageInfo.signatures
            }
        }.getOrNull() ?: arrayOf()
    }


    /**
     * 解析应用信息
     * */
    private fun getMessageDigest(paramArrayOfByte: ByteArray): String {
        runPlogCatch {
            val localObject: Any = MessageDigest.getInstance("MD5")
            (localObject as MessageDigest).update(paramArrayOfByte)
            val paramArrayOfByteTemp = localObject.digest()
            val k = paramArrayOfByteTemp.size
            val localObjectTemp = CharArray(k * 2)
            var i = 0
            var j = 0
            while (true) {
                if (i >= k) {
                    return String(localObjectTemp)
                }
                val m = paramArrayOfByteTemp[i].toInt()
                val n = j + 1
                localObjectTemp[j] = messageDigestCharArray[m ushr 4 and 0xF]
                j = n + 1
                localObjectTemp[n] = messageDigestCharArray[m and 0xF]
                i += 1
            }
        }
        return ""
    }

    /**
     * 获取对应包名的sign
     * [context]  context对象
     * [packageName] 应用包名
     */
    fun getSign(context: Context, packageName: String): String {
        if (!isInstalled(context, packageName)) {
            return ""
        }
        val rawSignature: Array<Signature> = getRawSignature(context, packageName)
        if (rawSignature.isEmpty()) {
            return ""
        }
        val strBuf = StringBuffer()
        for (i in rawSignature.indices) {
            strBuf.append(getMessageDigest(rawSignature[i].toByteArray()))
        }
        return strBuf.toString()
    }

    /**
     * 判断position是否可用
     *
     * [count] 总数数量
     * [position] 序号
     */
    fun hasPosition(count: Int, position: Int): Boolean {
        return position in 0 until count
    }


    /**
     * 设备是否root，不一定能够准确。
     * 原理：通过判断存储root文件的路径是否存在判断设备有无root
     * 受到系统影响可能会没有读写权限
     */
    fun isDeviceRoot(): Boolean {
        val su = "su"
        val locations = arrayOf(
            "/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
            "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/",
            "/system/sbin/", "/usr/bin/", "/vendor/bin/"
        )
        for (location in locations) {
            runPlogCatch {
                if (File(location + su).exists()) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * activity是否可用
     * 当activity为空或者处于销毁状态时不可用
     */
    fun isActive(activity: Activity?): Boolean {
        return activity != null && !activity.isDestroyed && !activity.isFinishing
    }

    //获取宽高比
    fun getWidthScale(width: Int, height: Int): Double {
        return 1.0 * width.toDiv(height.toDouble())
    }

    /**
     * 获取最大尺寸下的等比例最大分辨率
     * */
    fun getScaleMaxPiex(
        maxWidthSize: Int,
        maxHeight: Int,
        widthScale: Int,
        heightScale: Int
    ): IntArray {
        return if (getWidthScale(widthScale, heightScale) == getWidthScale(
                maxWidthSize,
                maxHeight
            )
        ) {
            intArrayOf(maxWidthSize, maxHeight)
        } else {
            var realWidth = 0
            var realHeight = 0
            if (widthScale > heightScale) {
                realWidth = maxWidthSize
                realHeight = (heightScale.toDiv(widthScale.toDouble()) * maxHeight).toInt()
            } else {
                realWidth = (widthScale.toDiv(heightScale.toDouble()) * maxWidthSize).toInt()
                realHeight = maxHeight
            }
            intArrayOf(realWidth, realHeight)
        }
    }

    /**
     * 获取包名对应应用的启动主页路径
     * [packageName] 应用包名
     * */
    fun getLauncherActivity(context: Context, packageName: String): String {
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.setPackage(packageName)
        val pm: PackageManager = context.packageManager
        val info = pm.queryIntentActivities(intent, 0)
        return if (info.isEmpty()) "" else info[0].activityInfo.name
    }

    /**
     * 退出并杀死APP
     * */
    fun exitApp() {
        Process.killProcess(Process.myPid())
        exitProcess(0)
    }

    /**
     * 获取图片的宽高但是不加载bitmap到内存中
     * [filepath] 本地文件路径
     * @return 以int数组返回图片宽高，0 宽，1 高
     * */
    fun getImageWidthHeight(filepath: String): IntArray {
        var imageWidth = 0
        var imageHeight = 0
        var inputStream: FileInputStream? = null
        try {
            val bitmapOptions = BitmapFactory.Options()
            bitmapOptions.inJustDecodeBounds = true
            inputStream = FileInputStream(File(filepath))
            BitmapFactory.decodeStream(inputStream, null, bitmapOptions)
            imageWidth = bitmapOptions.outWidth
            imageHeight = bitmapOptions.outHeight
        } catch (e: Throwable) {
            e.plogE()
        } finally {
            runPlogCatch {
                inputStream?.close()
            }
        }
        return intArrayOf(imageWidth, imageHeight)
    }

    /**
     * 对value数据做MD5
     * [value] 原始数据
     * @return MD5后的数据
     * */
    fun md5(value: String): String {
        return runPlogCatch {
            val md = MessageDigest.getInstance("MD5")
            val bytes = md.digest(value.toByteArray(charset("utf-8")))
            toHex(bytes)
        }.getOrDefault("")
    }

    /**
     * 将byte转换成对应的string
     * */
    private fun toHex(bytes: ByteArray): String {
        val ret = StringBuilder(bytes.size * 2)
        for (i in bytes.indices) {
            ret.append(hexDigits[(bytes[i].toInt() shr 4) and 0x0F])
            ret.append(hexDigits[bytes[i].toInt() and 0x0F])
        }
        return ret.toString()
    }

    /**
     * 获取随机UUID，字母大写且不带中划线
     * */
    fun getUUID(): String {
        return UUID.randomUUID().toString().replace("-", "").uppercase()
    }

    /**
     * 反射调用SystemPropertie获取string值
     * [key] 系统配置文件中的key值
     * [def] 获取失败时返回的默认值
     */
    fun getSystemPropertieString(key: String, def: String = ""): String {
        return runPlogCatch {
            val systemProperties = Class.forName("android.os.SystemProperties")
            (systemProperties.getMethod(
                "get", *arrayOf<Class<*>>(
                    String::class.java,
                    String::class.java
                )
            ).invoke(systemProperties, key, def) as String)
        }.getOrDefault(def)
    }

    /**
     * 是否是平板设备
     */
    fun isTabletDevice(): Boolean {
        return runPlogCatch {
            getSystemPropertieString("ro.build.characteristics").contains("tablet")
        }.getOrDefault(false)
    }

    /**
     * 是否开启平板模式
     */
    fun isTabletModel(context: Context): Boolean {
        return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

}