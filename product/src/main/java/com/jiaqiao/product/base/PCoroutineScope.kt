package com.jiaqiao.product.base

import com.jiaqiao.product.ext.notNullAndEmpty
import com.jiaqiao.product.ext.toCancle
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * 自定义的协程实现类
 * */
class PCoroutineScope : CoroutineScope {

    private val jobList by lazy { mutableListOf<Job>() }

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Main.immediate

    /**
     * 取消作用域中的子任务
     * */
    fun cancleChildren() {
        coroutineContext.cancelChildren()
    }

    /**
     * 取消作用域创建的所有协程JOB，并清空list
     * */
    fun close() {
        if(jobList.notNullAndEmpty()){
            jobList.forEach {
                it.toCancle()
            }
            jobList.clear()
        }
    }

    /**
     * 添加job对象，close时取消
     * */
    fun addJob(job: Job) {
        if(!jobList.contains(job)){
            jobList.add(job)
        }
    }

}