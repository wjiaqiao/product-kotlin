package com.example.productkotlin.ui.album

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.android.ktx.context.toast
import com.example.productkotlin.databinding.ActAlbumBinding
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.jiaqiao.product.base.ProductBaseVBAct

class AlbumAct : ProductBaseVBAct<ActAlbumBinding>() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, AlbumAct::class.java))
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        val permissions = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                // Android 13 (API 33)
                listOf(
                    Permission.READ_MEDIA_IMAGES
                )
            }

            else -> {
                // Android 12 (API 32) 及以下版本
                listOf(Permission.READ_EXTERNAL_STORAGE)
            }
        }
        XXPermissions.with(this)
            .permission(permissions)
            .request { _, allGranted ->
                if (allGranted) {
                    loadImageList()
                } else {
                    toast("未授权权限")
                    finish()
                }
            }
    }


    private fun loadImageList() {

    }

}