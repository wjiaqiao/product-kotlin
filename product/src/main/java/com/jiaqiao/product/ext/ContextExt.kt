package com.jiaqiao.product.ext

import android.app.ActivityManager
import android.app.NotificationManager
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.location.LocationManager
import android.media.AudioManager
import android.media.session.MediaSessionManager
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.jiaqiao.product.util.ProductUiUtil
import com.jiaqiao.product.util.ProductUtil


/**
 *
 * 判断权限名[permission]是否授权成功
 * [permission] 权限名
 * @return true 授权成功, false 授权失败
 * */
fun Context.hasPermission(permission: String): Boolean {
    return ProductUtil.hasPermission(this, permission)
}


/**
 *
 * 判断权限组[permissions]是否授权成功
 * [permissions]  权限组
 * @return true 授权成功, false 授权失败
 * */
fun Context.hasPermission(permissions: Array<String>): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        return true
    }
    permissions.forEach {
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, it)) {
            return false
        }
    }
    return true
}

/**
 * 获取当前context的线程名
 *
 * @return 返回进程名
 */
fun Context.processName(): String {
    val pid = Process.myPid()
    val mActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (appProcess in mActivityManager.runningAppProcesses) {
        if (appProcess.pid == pid) {
            return appProcess.processName
        }
    }
    return ""
}


/**
 * 判断当前Application是否是App的主进程
 *
 */
fun Context.isAppProcess(): Boolean {
    return ProductUtil.isMainProcess(this)
}

/**
 * 判断包名应用是否安装
 * [packageName] 应用包名
 */
fun Context.isInstalled(packageName: String): Boolean {
    return ProductUtil.isInstalled(this, packageName)
}

/**
 * 获取自身app的sign
 * [packageName] 应用包名
 */
fun Context.getSign(packageName: String = ""): String {
    return ProductUtil.getSign(
        this, if (packageName.isEmpty()) {
            this.packageName
        } else packageName
    )
}

/**
 * 判断应用是否拥有安装apk的权限。Android8.0以上才有
 * 需要在 AndroidManifest 中添加权限 "android.permission.REQUEST_INSTALL_PACKAGES"
 *
 */
fun Context.hasInstallAppPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        try {
            packageManager.canRequestPackageInstalls()
        } catch (thr: Throwable) {
            thr.plogE()
            false
        }
    } else {
        //安卓8.0以下
        true
    }
}

/**
 * 跳转到系统安装应用权限页面
 * 需要在 AndroidManifest 中添加权限 "android.permission.REQUEST_INSTALL_PACKAGES"
 */
fun Context.toInstallPermissionSet() {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //注意这个是8.0新API
            startActivity(
                Intent(
                    Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                    Uri.parse("package:$packageName")
                )
            )
        }
    } catch (thr: Throwable) {
        thr.plogE()
    }
}


/**
 * 判断应用是否拥有通知权限
 */
fun Context.hasNotificationPermission(): Boolean {
    return NotificationManagerCompat.from(this).areNotificationsEnabled()
}

/**
 * 跳转到通知设置页面
 */
fun Context.toNotificationSet() {
    startActivity(Intent().also {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                // android 8.0引导
                it.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                it.putExtra("android.provider.extra.APP_PACKAGE", packageName)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                // android 5.0-7.0
                it.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                it.putExtra("app_package", packageName)
                it.putExtra("app_uid", applicationInfo.uid)
            }
            else -> {
                // 其他
                it.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                it.data = Uri.fromParts("package", packageName, null)
            }
        }
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    })
}

/**
 * 获取当前设备的状态高度，单位：像素
 */
fun Context.statusBarHeight(): Int {
    return ProductUiUtil.getStatusBarHeight(this)
}

/**
 * 获取当前设备的导航栏高度，单位：像素
 */
fun Context.navigationBarHeight(): Int {
    return ProductUiUtil.getNavigationBarHeight(this)
}

/**
 * 跳转到应用的系统设置信息页面
 * */
fun Context.toAppInfo() {
    val mIntent = Intent()
    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    mIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
    mIntent.data = Uri.fromParts("package", packageName, null)
    startActivity(mIntent)
}

/**
 * 获取assets的图片bitmap
 * [assetsPath] assets的文件路径
 * */
fun Context.getAssetsBitmap(assetsPath: String): Bitmap {
    val ins = resources.assets.open(assetsPath)
    val bitmap = BitmapFactory.decodeStream(ins)
    ins.close()
    return bitmap
}

/**
 * 跳转到应用市场并对应[packageName]应用信息
 * [packageName] 应用包名
 * */
fun Context.toAppMarket(packageName: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse("market://details?id=$packageName")
    startActivity(intent)
}

/**
 * 重启至应用主界面
 * [isKillProcess] 是否杀死当前进程
 * */
fun Context.relunchApp(isKillProcess: Boolean = false) {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    intent.setClassName(packageName, ProductUtil.getLauncherActivity(this, packageName))
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.addFlags(
        Intent.FLAG_ACTIVITY_NEW_TASK
                or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
    )
    startActivity(intent)
    if (isKillProcess) {
        ProductUtil.exitApp()
    }
}

/**
 * 获取BluetoothManager服务
 * */
fun Context.bluetoothManager(): BluetoothManager {
    return (getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager)
}

/**
 * 获取BluetoothAdapter蓝牙控制器
 * */
fun Context.bluetoothAdapter(): BluetoothAdapter {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        bluetoothManager().adapter
    } else {
        BluetoothAdapter.getDefaultAdapter()
    }
}

/**
 * 获取CameraManager服务
 * */
fun Context.cameraManager(): CameraManager {
    return (getSystemService(Context.CAMERA_SERVICE) as CameraManager)
}

/**
 * 获取AudioManager服务
 * */
fun Context.audioManager(): AudioManager {
    return (getSystemService(Context.AUDIO_SERVICE) as AudioManager)
}

/**
 * 获取TelephonyManager服务
 * */
fun Context.telephonyManager(): TelephonyManager {
    return (getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager)
}

/**
 * 获取MediaSessionManager服务
 * */
fun Context.mediaSessionManager(): MediaSessionManager {
    return (getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager)
}

/**
 * 获取NotificationManager服务
 * */
fun Context.notificationManager(): NotificationManager {
    return (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
}

/**
 * 获取ActivityManager服务
 * */
fun Context.activityManager(): ActivityManager {
    return (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
}

/**
 * 获取LocationManager服务
 * */
fun Context.locationManager(): LocationManager {
    return (getSystemService(Context.LOCATION_SERVICE) as LocationManager)
}

/**
 * 获取SensorManager服务
 * */
fun Context.sensorManager(): SensorManager {
    return (getSystemService(Context.SENSOR_SERVICE) as SensorManager)
}

/**
 * 获取WindowManager服务
 * */
fun Context.windowManager(): WindowManager {
    return (getSystemService(Context.WINDOW_SERVICE) as WindowManager)
}

/**
 * 获取LayoutInflater服务
 */
fun Context.layoutInflater(): LayoutInflater {
    return getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
}

/**
 * 获取屏幕分辨率（包含状态栏、导航栏高度），返回Pair:1宽度、2高度
 */
fun Context.screenPixel(): Pair<Int, Int> {
    val realDisplayMetrics = DisplayMetrics()
    (this.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
        .defaultDisplay.getRealMetrics(realDisplayMetrics)
    return Pair(realDisplayMetrics.widthPixels, realDisplayMetrics.heightPixels)
}

/**
 * 获取Window可用分辨率，返回Pair:1宽度、2高度
 */
fun Context.windowsPixel(): Pair<Int, Int> {
    val po = Point()
    (this.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
        .defaultDisplay.getSize(po)
    return Pair(po.x, po.y)
}