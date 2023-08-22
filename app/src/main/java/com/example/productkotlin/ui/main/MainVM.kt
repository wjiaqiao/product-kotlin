package com.example.productkotlin.ui.main

import com.jiaqiao.product.base.ProductViewModel
import com.jiaqiao.product.ext.flowOnIo
import com.jiaqiao.product.ext.launch
import com.jiaqiao.product.ext.libPlog
import com.jiaqiao.product.ext.plog
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow

class MainVM : ProductViewModel() {

    fun start(){
        launch {

        }
    }

    fun loadFlow() = flow<Int> {
        "loadFlow".plog()
        for(i in 0..5){
            i.plog("loadFlow.emit")
            delay(10)
        }
        emit(1)
    }

}