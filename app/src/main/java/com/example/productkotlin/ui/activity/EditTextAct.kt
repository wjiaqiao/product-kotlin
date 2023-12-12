package com.example.productkotlin.ui.activity

import android.os.Bundle
import com.example.productkotlin.base.BaseVAct
import com.example.productkotlin.databinding.ActivityEditBinding
import com.jiaqiao.product.ext.plog
import com.jiaqiao.product.util.SoftInputPanelHelper


class EditTextAct : BaseVAct<ActivityEditBinding>() {

    private val helper by lazy { SoftInputPanelHelper(this, true) }

    override fun initView(savedInstanceState: Bundle?) {
        helper.bind()
        helper.changeSoftInputHeightAction = { rootView, height ->
            height.plog("SoftInputHeight")
            mViewBind.editText.bottom.plog("bottom")
        }
    }

}
