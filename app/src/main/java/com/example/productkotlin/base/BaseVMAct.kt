package com.example.productkotlin.base

import androidx.viewbinding.ViewBinding
import com.jiaqiao.product.base.ProductBaseVMAct
import com.jiaqiao.product.base.ProductViewModel

open abstract class BaseVMAct<VB : ViewBinding, VM : ProductViewModel> :
    ProductBaseVMAct<VB, VM>() {


}