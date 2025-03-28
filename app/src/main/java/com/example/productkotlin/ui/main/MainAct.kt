package com.example.productkotlin.ui.main

import android.os.Bundle
import com.example.productkotlin.base.BaseVMAct
import com.example.productkotlin.databinding.MainActBinding
import com.example.productkotlin.ui.album.AlbumAct
import com.jiaqiao.product.ext.click
import com.jiaqiao.product.ext.plog
import com.jiaqiao.product.ui.logfile.LogFileAct


class MainAct : BaseVMAct<MainActBinding, MainVM>() {

    override fun initView(savedInstanceState: Bundle?) {

        mViewBind.butLog.click {
            LogFileAct.start(this)
        }

        mViewBind.butAlbum.click {
            AlbumAct.start(this)
        }

//        mViewModel.start()
//
//        mViewBind.root.postDelayed({
//            finish()
//            "finish".plog()
//        }, 1000 * 3)
        mViewBind.butAlbum.callOnClick()
    }

    override fun onDestroy() {
        super.onDestroy()
        "onDestroy".plog()
    }

}
