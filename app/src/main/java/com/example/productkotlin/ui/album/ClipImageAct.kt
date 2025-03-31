package com.example.productkotlin.ui.album

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.example.productkotlin.databinding.ActClipImageBinding
import com.example.productkotlin.ext.hideLoad
import com.example.productkotlin.ext.showLoad
import com.jiaqiao.product.base.ProductBaseVBAct
import com.jiaqiao.product.base.ProductConstants
import com.jiaqiao.product.ext.click
import com.jiaqiao.product.ext.dp
import com.jiaqiao.product.ext.isNull
import com.jiaqiao.product.ext.launchMain
import com.jiaqiao.product.ext.load
import com.jiaqiao.product.ext.navigationBarColor
import com.jiaqiao.product.ext.plog
import com.jiaqiao.product.ext.productCreateFile
import com.jiaqiao.product.ext.runIo
import com.jiaqiao.product.ext.runPlogCatch
import com.jiaqiao.product.ext.savePng
import com.jiaqiao.product.widget.ClipImageView
import kotlinx.coroutines.delay
import java.io.File

class ClipImageAct : ProductBaseVBAct<ActClipImageBinding>() {

    companion object {

        fun start(context: Context, imagePath: String, clipType: Int = 1) {
            context.startActivity(intent(context, imagePath, clipType))
        }

        fun intent(context: Context, imagePath: String, clipType: Int = 1): Intent {
            return Intent(context, ClipImageAct::class.java).also {
                it.putExtra("imagePath", imagePath)
                it.putExtra("clipType", clipType)
            }
        }

    }

    private var imagePath = ""
    private var clipType = 0

    override fun initView(savedInstanceState: Bundle?) {
        imagePath = intent.getStringExtra("imagePath") ?: ""
        clipType = intent.getIntExtra("clipType", 0)
        if (imagePath.isEmpty() || clipType !in 1..3) {
            finish()
            return
        }
        navigationBarColor(Color.BLACK)
        when (clipType) {
            1 -> {
                mViewBind.civ.setClipBoxType(ClipImageView.CIRCLE_CLIP_BOX)
                mViewBind.civ.setCircleRadii(280.dp)
            }

            2 -> {
                mViewBind.civ.setClipBoxType(ClipImageView.RECT_CLIP_BOX)
                mViewBind.civ.setRectWidth(240.dp)
                mViewBind.civ.setRectHeight(280.dp)
            }

            3 -> {
                mViewBind.civ.setClipBoxType(ClipImageView.RECT_CLIP_BOX_ROUND)
                mViewBind.civ.setRectWidth(260.dp)
                mViewBind.civ.setRectHeight(260.dp)
                mViewBind.civ.setRectRadius(20.dp)
            }
        }
        mViewBind.civ.load(imagePath)
        mViewBind.tvConfirm.click { clip() }
    }

    private fun clip() {
        launchMain {
            showLoad()
            val parentFile = File(ProductConstants.sdAppFilePath + File.separator + "clip")
            val file = File(parentFile, "clip_${System.currentTimeMillis()}.png")
            val isSuccess = runIo {
                val bitmap = runPlogCatch {
                    var count = 0
                    while (!mViewBind.civ.isCanClip && count <= 30) {
                        delay(20)
                        count++
                    }
                    if (mViewBind.civ.isCanClip) {
                        mViewBind.civ.clip()
                    } else null
                }.getOrNull()
                if (bitmap.isNull()) {
                    return@runIo false
                } else {
                    file.productCreateFile()
                    return@runIo bitmap?.savePng(file) ?: false
                }
            }.await()
            hideLoad()
            if (isSuccess) {
                setResult(RESULT_OK, Intent().also {
                    it.putExtra("clipPath", file.absolutePath)
                })
                finish()
            } else {
                "裁剪失败".plog()
            }
        }
    }

}