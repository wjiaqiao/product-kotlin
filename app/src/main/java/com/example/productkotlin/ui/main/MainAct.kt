package com.example.productkotlin.ui.main

import android.content.Intent
import android.os.Bundle
import com.example.productkotlin.base.BaseVMAct
import com.example.productkotlin.databinding.MainActBinding
import com.example.productkotlin.ui.album.AlbumAct
import com.example.productkotlin.ui.album.ClipImageAct
import com.jiaqiao.product.ext.click
import com.jiaqiao.product.ext.intentResult
import com.jiaqiao.product.ext.load
import com.jiaqiao.product.ext.notNullAndEmpty
import com.jiaqiao.product.ext.plog
import com.jiaqiao.product.ui.logfile.LogFileAct


class MainAct : BaseVMAct<MainActBinding, MainVM>() {

    override fun initView(savedInstanceState: Bundle?) {

        mViewBind.butLog.click {
            LogFileAct.start(this)
        }

        mViewBind.butAlbum.click {
            albumAndClip(1)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        "onDestroy".plog()
    }

    private fun albumAndClip(type: Int) {
        intentResult(Intent(this, AlbumAct::class.java)) { resultCode, data ->
            if (resultCode == RESULT_OK) {
                val selectPath = data?.getStringExtra("path")
                if (selectPath.notNullAndEmpty()) {
                    intentResult(
                        ClipImageAct.intent(
                            this,
                            selectPath!!,
                            type
                        )
                    ) { resultCode, data ->
                        if (resultCode == RESULT_OK) {
                            val clipPath = data?.getStringExtra("clipPath")
                            if (clipPath.notNullAndEmpty()) {
                                mViewBind.ivClip.load(clipPath!!)
                            }
                        }
                    }
                }
            }
        }
    }

}
