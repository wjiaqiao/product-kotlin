package com.jiaqiao.product.util

import android.graphics.Bitmap
import android.util.LruCache

/**
 * 存储在内存中的bitmap
 * 用于暂时缓存bitmap，应用被杀死后清空
 * */
object BitmapCache {

    /**
     * 获取应用程序最大可用内存
     * 设置图片缓存大小为程序最大可用内存的1/8
     * */
    private val maxSize by lazy {
        val maxMemory = Runtime.getRuntime().maxMemory().toInt()
        maxMemory / 8
    }

    private val mLruCache by lazy {
        object : LruCache<String, Bitmap>(maxSize) {
            override fun sizeOf(key: String?, bitmap: Bitmap): Int {
                return bitmap.byteCount
            }
        }
    }

    /**
     * 将bitmap存入缓存中
     *
     * [key] bitmap对应的key值
     * [value] bitmap数据
     * */
    fun put(key: String, value: Bitmap) {
        mLruCache.put(key, value)
    }

    /**
     * 将key对应的bitmap从缓存中移除
     *
     * [key] bitmap对应的key值
     * */
    fun remove(key: String) {
        mLruCache.remove(key)
    }

    /**
     * 获取key对应的bitmap
     *
     * [key] bitmap对应的key值
     * @return key存在返回bitmap，不存在则返回null
     * */
    fun get(key: String): Bitmap? {
        return mLruCache.get(key)
    }

    /**
     * 清空缓存中所有bitmap数据
     * */
    fun clean() {
        mLruCache.evictAll()
        mLruCache.trimToSize(maxSize)
    }

    /**
     * 缓存中所有bitmap的字节总数
     * */
    fun size(): Int {
        return mLruCache.size()
    }

    /**
     * 缓存可使用的最大字节总数
     * */
    fun maxSize(): Int {
        return mLruCache.maxSize()
    }
}