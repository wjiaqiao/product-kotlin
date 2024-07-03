package com.jiaqiao.product.ui.logfile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.jiaqiao.product.R
import com.jiaqiao.product.base.ProductBaseVBAct
import com.jiaqiao.product.base.ProductConstants
import com.jiaqiao.product.databinding.LogFileActBinding
import com.jiaqiao.product.ext.cleanShadowEffect
import com.jiaqiao.product.ext.horizontal
import com.jiaqiao.product.ext.setList

class LogFileAct : ProductBaseVBAct<LogFileActBinding>() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, LogFileAct::class.java))
        }
    }

    private val logcatFileFrag =
        LogcatFileFrag.newInstance(ProductConstants.sdLogFilePath, 1)
    private val expLogFileFrag =
        LogcatFileFrag.newInstance(ProductConstants.sdExpFilePath, 2)

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.viewPager.cleanShadowEffect().horizontal()
            .setList(this, mutableListOf<Fragment>().also {
                it.add(logcatFileFrag)
                it.add(expLogFileFrag)
            })
        mViewBind.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                mViewBind.appTitle.setText(
                    when (position) {
                        0 -> getString(R.string.app_logcat)
                        1 -> getString(R.string.exp_log)
                        else -> ""
                    }
                )
            }
        })
    }

}