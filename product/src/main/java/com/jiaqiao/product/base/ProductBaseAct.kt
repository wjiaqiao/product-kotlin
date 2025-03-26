package com.jiaqiao.product.base

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jiaqiao.product.ext.notNullAndEmpty
import com.jiaqiao.product.ext.statusBarBlackFont
import com.jiaqiao.product.ext.viewIntoStatusBar
import java.util.ArrayList

/**
 * 基础activity类
 * */
open class ProductBaseAct : AppCompatActivity() {

    //dialog列表
    private val dialogList by lazy { mutableListOf<Dialog>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        statusBarBlackFont()
        viewIntoStatusBar()
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        dismissDialog()
        super.onDestroy()
    }

    //添加dialog到列表中
    open fun add(dialog: Dialog) {
        if (!dialogList.contains(dialog)) {
            dialogList.add(dialog)
        }
    }

    //隐藏dialog列表中的dialog，防止出现空异常
    open fun dismissDialog() {
        if (dialogList.notNullAndEmpty()) {
            dialogList.forEach { dia ->
                if (dia.isShowing) {
                    dia.dismiss()
                }
            }
        }
    }

}