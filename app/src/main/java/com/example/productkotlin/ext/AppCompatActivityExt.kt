package com.example.productkotlin.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.productkotlin.util.LoadDialogUtil

//显示应用的加载框
fun AppCompatActivity.showLoad() {
    LoadDialogUtil.showLoadDialog(this)
}

//隐藏应用的加载框
fun AppCompatActivity.hideLoad() {
    LoadDialogUtil.hideLoadDialog(this)
}


//显示应用的加载框
fun Fragment.showLoad() {
    activity?.also { LoadDialogUtil.showLoadDialog(it) }
}

//隐藏应用的加载框
fun Fragment.hideLoad() {
    activity?.also { LoadDialogUtil.hideLoadDialog(it) }
}
