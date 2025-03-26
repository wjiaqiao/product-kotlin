package com.jiaqiao.product.util

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.jakewharton.disklrucache.DiskLruCache
import com.jiaqiao.product.config.PlogConfig
import com.jiaqiao.product.context.ProductContentProvider
import com.jiaqiao.product.ext.*
import java.io.*


/**
 * 创建磁盘缓存
 * [cacheName] 缓存名称
 * [cacheVersion] 缓存版本号，初始值为1，当版本号发生改变时，会将之前的缓存信息清空
 * [cachePath] 缓存路径
 * [cacheMaxSize] 缓存的总大小（单位：B，默认200MB），当缓存大小超过这个设定值后，DiskLruCache会清除一些缓存，从而保证不会超过这个设定值
 */
open class ProductDiskCache(
    val cacheName: String,
    val cacheVersion: Int,
    val cachePath: String = "",
    val cacheMaxSize: Long = 200 * 1024 * 1024
) {

    val dlc = DiskLruCache.open(
        DiskCacheUtil.getDiskCachePath(
            ProductContentProvider.getContext(),
            cachePath,
            cacheName
        ), cacheVersion, 1, if (cacheMaxSize < 0) {
            200 * 1024 * 1024
        } else {
            cacheMaxSize
        }
    )

    /**
     * 对key名做签名
     * */
    private fun getKeyName(name: String): String {
        return ProductUtil.md5(name + cacheName).lowercase()
    }


    /**
     * 将value转化成bytes进行存储
     * */
    @SuppressLint("WrongConstant")
    fun valueToBytes(value: Any?): ByteArray? {
        return try {
            if (value.isNull()) {
                return null
            }
            if (PlogConfig.debug && value.notNull()) {
                value!!::class.java.libPlog("valueToBytes")
            }
            var bytes: ByteArray? = null
            when (value) {
                is Bitmap -> {
                    val baos = ByteArrayOutputStream()
                    value.compress(Bitmap.CompressFormat.PNG, 100, baos)
                    bytes = baos.toByteArray()
                }
                is Parcelable -> {
                    val source = Parcel.obtain()
                    value.writeToParcel(source, value.describeContents())
                    bytes = source.marshall()
                }
                else -> {
                    ByteArrayOutputStream().use { byteOutput ->
                        ObjectOutputStream(byteOutput).use { objOutput ->
                            objOutput.writeObject(
                                if (value is Serializable) {
                                    value
                                } else {
                                    value.toFastJsonString()
                                }
                            )
                            bytes = byteOutput.toByteArray()
                        }
                    }
                }
            }
            bytes
        } catch (thr: Throwable) {
            thr.plogE()
            null
        }
    }

    /**
     * 将bytes数据强转成T类型
     * */
    inline fun <reified T> bytesToValue(bytes: ByteArray?): T? {
        if (bytes.isNull() || bytes?.isEmpty().isTrue()) {
            return null
        }
        val clazz = T::class.java
        return try {
            when {
                clazz == Bitmap::class.java -> {
                    BitmapFactory.decodeByteArray(bytes, 0, bytes!!.size) as? T
                }
                Parcelable::class.java.isAssignableFrom(clazz) -> {
                    val source = Parcel.obtain()
                    source.unmarshall(bytes!!, 0, bytes!!.size)
                    source.setDataPosition(0)
                    val f = clazz.getField("CREATOR")
                    val creator = f[null as Any?] as Creator<*>
                    creator.createFromParcel(source) as? T
                }
                else -> {
                    var obj: Any? = null
                    ByteArrayInputStream(bytes).use { byteInput ->
                        ObjectInputStream(byteInput).use { objInput ->
                            obj = objInput.readObject()
                        }
                    }
                    if (!Serializable::class.java.isAssignableFrom(clazz)) {
                        runPCatch {
                            obj = obj.toString().toFastJsonParse()
                        }
                    }
                    obj as? T
                }
            }
        } catch (thr: Throwable) {
            thr.plogE()
            null
        } ?: null as? T
    }

    /**
     * 存入数据
     * [key] 键值
     * [value] 数据内容
     * @return true 存入成功，false 存入失败
     * */
    fun put(key: String, value: Any): Boolean {
        var os: OutputStream? = null
        return try {
            val editor = dlc.edit(getKeyName(key))
            os = editor.newOutputStream(0)
            val bytes = valueToBytes(value)
            if (bytes.notNull()) {
                os.write(bytes)
                editor.commit()
                true
            } else {
                false
            }
        } catch (thr: Throwable) {
            thr.plogE()
            false
        } finally {
            os?.close()
        }
    }

    /**
     * 获取已存入的数据
     * [key] 键值
     * @return 返回数据不为null，说明获取成功，反之获取失败、未存入数据
     * */
    inline fun <reified T> get(key: String): T? {
        if (dlc.size() <= 0) {
            return null
        }
        var isz: InputStream? = null
        var ed: DiskLruCache.Editor? = null
        return try {
            ed = dlc.edit(ProductUtil.md5(key + cacheName).lowercase())
            if (ed.isNull()) {
                null
            } else {
                isz = ed.newInputStream(0)
                bytesToValue<T>(isz.readBytes())
            }
        } catch (thr: Throwable) {
            thr.plogE()
            null
        } finally {
            isz?.close()
            ed?.commit()
        }
    }

    /**
     * 获取已存入的数据，获取数据为null时使用默认值
     * [key] 键值
     * [defValue] 默认值
     * @return 获取成功返回已存入的数据，反之返回[defValue]
     * */
    inline fun <reified T> getDef(key: String, defValue: T?): T? {
        return get(key) ?: defValue
    }

    /**
     * 根据key移除缓存数据
     * [key] 键值
     * @return true 移除成功，false 移除失败
     * */
    fun remove(key: String): Boolean {
        return try {
            if (dlc.size() <= 0) {
                return true
            }
            val ed = dlc.edit(getKeyName(key))
            if (ed.isNull()) {
                true
            } else {
                ed.commit()
                dlc.remove(key)
            }
        } catch (thr: Throwable) {
            thr.plogE()
            false
        }
    }
}