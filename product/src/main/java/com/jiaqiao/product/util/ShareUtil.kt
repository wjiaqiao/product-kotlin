package com.jiaqiao.product.util

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.jiaqiao.product.ext.notNullAndEmpty
import com.jiaqiao.product.ext.runPlogCatch
import java.io.File

object ShareUtil {


    /**
     *调用系统分享框分享
     *
     **/
    fun share(
        content: Context,
        filePath: String,
        shareType: String? = null,
        shareTitle: String = "",
        compNamePkg: String? = null,
        compNameCls: String? = null
    ) {
        runPlogCatch {
            val uri = UriUtil.file2Uri(File(filePath))
            val intent = Intent(Intent.ACTION_SEND)
            if (compNamePkg.notNullAndEmpty() && compNameCls.notNullAndEmpty()) {
                //过滤分享目标应用
                intent.component = ComponentName(compNamePkg!!, compNameCls!!)
            }
            if (shareType.isNullOrEmpty()) {
                val mediaFileType = MediaFileUtil.getFileType(filePath)
                intent.type =
                    if (mediaFileType.fileType == MediaFileUtil.FILE_TYPE_UNKNOWN) "*/*" else
                        mediaFileType.mimeType
            } else {
                intent.type = shareType //设置MIME类型
            }
            intent.putExtra(Intent.EXTRA_STREAM, uri)//需要分享的文件URI
            content.startActivity(Intent.createChooser(intent, shareTitle))
        }
    }


}
