package com.jiaqiao.product.helper

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.jiaqiao.product.ext.isNull

/**
 *
 * SoundPool 播放铃声的辅助类
 *
 * [context]  context对象
 * [rawId]音频文件的rawid
 * */
data class SoundPool(val context: Context, val rawId: Int) {

    private var soundID = -1 //加载后的资源id
    private var streamID = -1 //播放后的流id
    private var soundPool: SoundPool? = null //播放器对象

    /**
     * 开始加载资源
     * */
    fun load() {
        if (soundPool.isNull()) {
            var attr = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
            soundPool = SoundPool.Builder()
                .setAudioAttributes(attr)
                .build()
        }
        soundID = soundPool?.load(context, rawId, 1) ?: -1
    }

    /**
     * 开始播放
     * */
    fun play() {
        play(1.0f)
    }

    /**
     * 开始播放
     * [rate]播放速率
     * */
    fun play(rate: Float) {
        stop()
        if (soundID > 0) {
            streamID = soundPool?.play(soundID, 1.0f, 1.0f, 1, 0, rate) ?: -1
        }
    }

    /**
     * 停止播放
     * */
    fun stop() {
        if (streamID > 0) {
            soundPool?.stop(streamID)
        }
    }

    /**
     * 重置并销毁SoundPool
     * */
    fun release() {
        stop()
        if (soundID > 0) {
            soundPool?.unload(soundID)
        }
        soundID = -1
        streamID = -1
        soundPool?.release()
        soundPool = null
    }


}