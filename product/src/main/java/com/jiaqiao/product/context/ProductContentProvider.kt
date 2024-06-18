package com.jiaqiao.product.context

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import com.jiaqiao.product.base.ProductConstants
import com.jiaqiao.product.helper.CrashHandler
import java.io.File

/**
 *
 * 自定义ContentProvider的对象，获取应用的context对象，并进行必要的初始化
 *
 * */
class ProductContentProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        productContext = context?.applicationContext
        initLib(ProductContentProvider.getContext())
        return false
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }

    /**
     * 初始化lib
     * */
    private fun initLib(context: Context) {

        ProductConstants.appFilePath = context.filesDir.absolutePath
        ProductConstants.sdAppFilePath = context.getExternalFilesDir(null)?.absolutePath ?: ""
        ProductConstants.sdCacheFilePath = context.externalCacheDir?.absolutePath ?: ""
        ProductConstants.sdLogcatFilePath =
            ProductConstants.sdAppFilePath + File.separator + "product_logcat"
        ProductConstants.sdLogFilePath = ProductConstants.sdLogcatFilePath + File.separator + "log"
        ProductConstants.sdExpFilePath = ProductConstants.sdLogcatFilePath + File.separator + "exp"
        ProductConstants.sdRootFilePath = Environment.getExternalStorageDirectory().absolutePath

        if (context is Application) {
            context.registerActivityLifecycleCallbacks(ProductLifecycle())
        }

        CrashHandler.init()

    }

    companion object {
        private var productContext: Context? = null

        /**
         * 获取应用的context对象
         * */
        fun getContext(): Context {
            return productContext!!
        }

    }

}