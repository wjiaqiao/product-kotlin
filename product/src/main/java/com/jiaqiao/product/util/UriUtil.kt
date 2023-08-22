package com.jiaqiao.product.util

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import androidx.core.content.FileProvider
import com.jiaqiao.product.context.ProductContentProvider
import com.jiaqiao.product.ext.isNull
import com.jiaqiao.product.ext.notNullAndEmpty
import com.jiaqiao.product.ext.plogE
import java.io.File

object UriUtil {


    /**
     * Resource to uri.
     *
     * res2Uri([res type]/[res name]) -> res2Uri(drawable/icon), res2Uri(raw/icon)
     *
     * res2Uri([resource_id]) -> res2Uri(R.drawable.icon)
     *
     * [resPath] The path of res.
     * @return uri
     */
    fun res2Uri(resPath: String): Uri? {
        return Uri.parse(
            "android.resource://" + ProductContentProvider.getContext().packageName.toString() + "/" + resPath
        )
    }

    /**
     * File to uri.
     *
     * [file] The file.
     * @return uri
     */
    fun file2Uri(file: File, uriPerPkg: String? = null): Uri? {
        if (!file.exists()) return null
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val authority: String =
                ProductContentProvider.getContext().packageName.toString() + ".ProductFileProvider"
            FileProvider.getUriForFile(ProductContentProvider.getContext(), authority, file!!)
                .apply {
                    if (uriPerPkg.notNullAndEmpty()) {
                        ProductContentProvider.getContext().grantUriPermission(
                            uriPerPkg,//填包名
                            this, Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                    }
                }
        } else {
            Uri.fromFile(file)
        }
    }


    /**
     * Uri to file.
     *
     * [uri] The uri.
     * @return file
     */
    fun uri2File(uri: Uri): File? {
        val authority = uri.authority
        val scheme = uri.scheme
        val path = uri.path
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && path != null) {
            val externals = arrayOf("/external/", "/external_path/")
            var file: File? = null
            for (external: String in externals) {
                if (path.startsWith(external)) {
                    file = File(
                        Environment.getExternalStorageDirectory().absolutePath
                                + path.replace(external, "/")
                    )
                    if (file.exists()) {
                        return file
                    }
                }
            }
            file = null
            when {
                path.startsWith("/files_path/") -> {
                    file = File(
                        ProductContentProvider.getContext().filesDir.absolutePath
                            .toString() + path.replace("/files_path/", "/")
                    )
                }
                path.startsWith("/cache_path/") -> {
                    file = File(
                        ProductContentProvider.getContext().cacheDir.absolutePath
                            .toString() + path.replace("/cache_path/", "/")
                    )
                }
                path.startsWith("/external_files_path/") -> {
                    file = File(
                        ProductContentProvider.getContext().getExternalFilesDir(null)?.absolutePath
                            .toString() + path.replace("/external_files_path/", "/")
                    )
                }
                path.startsWith("/external_cache_path/") -> {
                    file = File(
                        ProductContentProvider.getContext().externalCacheDir?.absolutePath
                            .toString() + path.replace("/external_cache_path/", "/")
                    )
                }
            }
            if (file != null && file.exists()) {
                return file
            }
        }
        if ((ContentResolver.SCHEME_FILE == scheme)) {
            if (path != null) return File(path)
            return null
        } // end 0
        else if (DocumentsContract.isDocumentUri(ProductContentProvider.getContext(), uri)) {
            if (("com.android.externalstorage.documents" == authority)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return File(
                        Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    )
                } else {
                    // Below logic is how External Storage provider build URI for documents
                    // http://stackoverflow.com/questions/28605278/android-5-sd-card-label
                    val mStorageManager =
                        ProductContentProvider.getContext()
                            .getSystemService(Context.STORAGE_SERVICE) as StorageManager
                    try {
                        val storageVolumeClazz = Class.forName("android.os.storage.StorageVolume")
                        val getVolumeList = mStorageManager.javaClass.getMethod("getVolumeList")
                        val getUuid = storageVolumeClazz.getMethod("getUuid")
                        val getState = storageVolumeClazz.getMethod("getState")
                        val getPath = storageVolumeClazz.getMethod("getPath")
                        val isPrimary = storageVolumeClazz.getMethod("isPrimary")
                        val isEmulated = storageVolumeClazz.getMethod("isEmulated")
                        val result = getVolumeList.invoke(mStorageManager)
                        val length = java.lang.reflect.Array.getLength(result)
                        for (i in 0 until length) {
                            val storageVolumeElement = java.lang.reflect.Array.get(result, i)
                            //String uuid = (String) getUuid.invoke(storageVolumeElement);
                            val mounted =
                                ((Environment.MEDIA_MOUNTED == getState.invoke(storageVolumeElement)) || (Environment.MEDIA_MOUNTED_READ_ONLY == getState.invoke(
                                    storageVolumeElement
                                )))

                            //if the media is not mounted, we need not get the volume details
                            if (!mounted) continue

                            //Primary storage is already handled.
                            if ((isPrimary.invoke(storageVolumeElement) as Boolean
                                        && isEmulated.invoke(storageVolumeElement) as Boolean)
                            ) {
                                continue
                            }
                            val uuid: String? = getUuid.invoke(storageVolumeElement) as String
                            if (uuid != null && (uuid == type)) {
                                return File(
                                    getPath.invoke(storageVolumeElement).toString() + "/" + split[1]
                                )
                            }
                        }
                    } catch (ex: Exception) {
                        ex.plogE()
                    }
                }
                return null
            } else if (("com.android.providers.downloads.documents" == authority)) {
                var id = DocumentsContract.getDocumentId(uri)
                if (TextUtils.isEmpty(id)) {
                    return null
                }
                if (id.startsWith("raw:")) {
                    return File(id.substring(4))
                } else if (id.startsWith("msf:")) {
                    id = id.split(":").toTypedArray()[1]
                }
                var availableId: Long = 0
                try {
                    availableId = id.toLong()
                } catch (thr: Throwable) {
                    return null
                }
                val contentUriPrefixesToTry = arrayOf(
                    "content://downloads/public_downloads",
                    "content://downloads/all_downloads",
                    "content://downloads/my_downloads"
                )
                for (contentUriPrefix: String in contentUriPrefixesToTry) {
                    val contentUri =
                        ContentUris.withAppendedId(Uri.parse(contentUriPrefix), availableId)
                    try {
                        val file = getFileFromUri(contentUri, "1_1")
                        if (file != null) {
                            return file
                        }
                    } catch (ignorthr: Throwable) {
                    }
                }
                return null
            } else if (("com.android.providers.media.documents" == authority)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                val contentUri: Uri
                if (("image" == type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if (("video" == type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if (("audio" == type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                } else {
                    return null
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getFileFromUri(contentUri, selection, selectionArgs, "1_2")
            } else if ((ContentResolver.SCHEME_CONTENT == scheme)) {
                return getFileFromUri(uri, "1_3")
            } else {
                return null
            }
        } else if ((ContentResolver.SCHEME_CONTENT == scheme)) {
            return getFileFromUri(uri, "2")
        } else {
            return null
        }
    }

    private fun getFileFromUri(uri: Uri, code: String): File? {
        return getFileFromUri(uri, null, null, code)
    }

    private fun getFileFromUri(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?,
        code: String
    ): File? {
        if (("com.google.android.apps.photos.content" == uri.authority)) {
            if (!TextUtils.isEmpty(uri.lastPathSegment)) {
                return File(uri.lastPathSegment)
            }
        } else if (("com.tencent.mtt.fileprovider" == uri.authority)) {
            val path = uri.path
            if (!TextUtils.isEmpty(path)) {
                val fileDir = Environment.getExternalStorageDirectory()
                return File(fileDir, path!!.substring("/QQBrowser".length, path.length))
            }
        } else if (("com.huawei.hidisk.fileprovider" == uri.authority)) {
            val path = uri.path
            if (!TextUtils.isEmpty(path)) {
                return File(path!!.replace("/root", ""))
            }
        }
        val cursor = ProductContentProvider.getContext().contentResolver.query(
            uri, arrayOf("_data"), selection, selectionArgs, null
        )
        if (cursor.isNull()) {
            return null
        }
        try {
            return if (cursor!!.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex("_data")
                if (columnIndex > -1) {
                    File(cursor.getString(columnIndex))
                } else {
                    null
                }
            } else {
                null
            }
        } catch (thr: Throwable) {
            thr.plogE()
            return null
        } finally {
            cursor?.close()
        }
    }


}