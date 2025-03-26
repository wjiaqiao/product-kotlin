package com.jiaqiao.product.ext

import android.net.Uri
import com.jiaqiao.product.util.UriUtil
import java.io.File

/**
 * 创建新文件
 * */
fun File.pCreateFile() {
    parentFile?.pCreateDirectory()
    if (exists()) {
        delete()
    }
    createNewFile()
}

/**
 * 创建文件夹
 * */
fun File.pCreateDirectory() {
    if (!exists()) {
        mkdirs()
    }
}

/**
 * 删除文件或文件夹
 * */
fun File.pDeleteAll() {
    if (exists()) {
        if (isDirectory) {
            listFiles()?.forEach { childFile ->
                childFile.pDeleteAll()
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
fun File.pDeleteChild() {
    if (isDirectory && exists()) {
        listFiles()?.forEach { childFile ->
            childFile.pDeleteAll()
        }
    }
}

/**
 * file文件转换成uri
 * */
fun File?.toProductUri(): Uri? {
    return if (isNull()) null else UriUtil.file2Uri(this!!)
}
