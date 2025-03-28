package com.example.productkotlin.ui.album

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore.MediaColumns
import androidx.core.content.ContextCompat
import com.android.ktx.context.toast
import com.example.productkotlin.base.ImagePath
import com.example.productkotlin.databinding.ActAlbumBinding
import com.example.productkotlin.ext.hideLoad
import com.example.productkotlin.ext.showLoad
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.jiaqiao.product.base.ProductBaseVBAct
import com.jiaqiao.product.ext.gridVertical
import com.jiaqiao.product.ext.launchMain
import com.jiaqiao.product.ext.notNullAndEmpty
import com.jiaqiao.product.ext.plog
import com.jiaqiao.product.ext.runIo
import com.jiaqiao.product.ext.runPlogCatch
import kotlinx.coroutines.delay

class AlbumAct : ProductBaseVBAct<ActAlbumBinding>() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, AlbumAct::class.java))
        }
    }

    private var loader: PhotoDirectoryLoader? = null

    private val ada by lazy { AlbumAdapter() }

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.rv.gridVertical(4).adapter = ada
        loader = PhotoDirectoryLoader(this, false)
        val permissions = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
                // Android 14 (API 34) 及以上版本
                listOf(
                    Permission.READ_MEDIA_IMAGES, Permission.READ_MEDIA_VISUAL_USER_SELECTED
                )
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                // Android 13 (API 33)
                listOf(
                    Permission.READ_MEDIA_IMAGES
                )
            }

            else -> {
                // Android 12 (API 32) 及以下版本
                listOf(Permission.MANAGE_EXTERNAL_STORAGE)
            }
        }
        XXPermissions.with(this).permission(permissions).request { _, allGranted ->
            if (allGranted) {
                loadImageList()
            } else {
                toast("未授权权限")
                finish()
            }
        }
    }

    private fun loadImageList() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                "授权了所有图片".plog()
            } else if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                "授权了部分图片".plog()
            } else {
                "未授权".plog()
            }
        }
        launchMain {
            val images = mutableListOf<ImagePath>()
            showLoad()
            runIo {
                delay(500)
                runPlogCatch {
                    loader?.loadInBackground()?.let {
                        val pathPosi = it.getColumnIndexOrThrow(MediaColumns.DATA)
                        val sizePosi = it.getColumnIndexOrThrow(MediaColumns.SIZE)
                        val datePosi = it.getColumnIndexOrThrow(MediaColumns.DATE_ADDED)
                        while (it.moveToNext()) {
                            val path = it.getString(pathPosi)
                            val size = it.getLong(sizePosi)
                            val date = it.getLong(datePosi) * 1000
                            if (size >= 1024) {
                                images.add(ImagePath(path, date, size))
                            }
                        }
                    }
                }
            }.await()
            hideLoad()
            images.size.plog("images.size")
            if (images.notNullAndEmpty()) {
                images.first().path.plog("images.first()")
                ada.setList(images)
            } else {
                toast("未找到图片")
                finish()
            }
        }
    }

}