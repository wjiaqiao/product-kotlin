package com.example.productkotlin.base

/**
 *
 * @param path 文件地址
 * @param date 文件时间戳
 * @param size 文件大小，单位:B
 * */
data class ImagePath(val path: String = "", val date: Long = 0, val size: Long = 0)
