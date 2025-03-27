package com.jiaqiao.product.ext

import android.net.Uri
import com.jiaqiao.product.util.UriUtil
import java.io.File

/**
 * 创建新文件
 * */
fun File.productCreateFile() {
    parentFile?.productCreateDirectory()
    if (exists()) {
        delete()
    }
    createNewFile()
}

/**
 * 创建文件夹
 * */
fun File.productCreateDirectory() {
    if (!exists()) {
        mkdirs()
    }
}

/**
 * 删除文件或文件夹
 * */
fun File.productDeleteAll() {
    if (exists()) {
        if (isDirectory) {
            listFiles()?.forEach { childFile ->
                childFile.productDeleteAll()
            }
            delete()
        } else if (isFile) {
            delete()
        }
    }
}

/**
 * 删除文件夹中的子文件
 * */
fun File.productDeleteChild() {
    if (isDirectory && exists()) {
        listFiles()?.forEach { childFile ->
            childFile.productDeleteAll()
        }
    }
}

/**
 * file文件转换成uri
 * */
fun File?.productToUri(): Uri? {
    return if (isNull()) null else UriUtil.file2Uri(this!!)
}
