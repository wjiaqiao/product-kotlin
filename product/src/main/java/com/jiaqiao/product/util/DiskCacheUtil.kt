package com.jiaqiao.product.util

import android.content.Context
import android.os.Environment
import com.jiaqiao.product.ext.pCreateDirectory
import java.io.File


object DiskCacheUtil {

    /**
     * 获取磁盘缓存的文件地址
     */
    fun getDiskCachePath(context: Context, cachePath: String, cacheName: String): File {
        val file = File(
            if (cachePath.isNullOrEmpty()) {
                if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                    context.externalCacheDir!!.path
                } else {
                    context.cacheDir.path
                }
            } else {
                cachePath
            } + File.separator + cacheName
        )
        file.pCreateDirectory()
        return file

    }

}