package com.jiaqiao.product.ext

import android.net.Uri
import com.jiaqiao.product.util.UriUtil
import java.io.File

/**
 * Uri转化成File文件
 * */
fun Uri?.toFile(): File? {
    return if (isNull()) null else UriUtil.uri2File(this!!)
}