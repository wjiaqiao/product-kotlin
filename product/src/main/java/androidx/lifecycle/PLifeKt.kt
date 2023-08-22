package androidx.lifecycle

import com.jiaqiao.product.ext.isNull
import com.jiaqiao.product.ext.notNull
import com.jiaqiao.product.ext.runPCatch
import com.jiaqiao.product.util.PLifeScope
import java.io.Closeable
import java.util.concurrent.ConcurrentHashMap

private const val JOB_KEY = "androidx.lifecycle.PLifeScope.JOB_KEY"
private const val JOB_KEY_CLOSEABLE = "androidx.lifecycle.PLifeScope.JOB_KEY_CLOSEABLE"

/**
 * 辅助缓存lifescope的map
 * */
private val cacheMap by lazy { ConcurrentHashMap<Int, PLifeScope>() }


/**
 * 创建LifeScope
 * */
val ViewModel.pLifeScope: PLifeScope
    get() {
        val scope: PLifeScope? = this.getTag(JOB_KEY)
        if (scope != null) {
            return scope
        }
        if (getTag<Closeable?>(JOB_KEY_CLOSEABLE).isNull()) {
            setTagIfAbsent(JOB_KEY_CLOSEABLE, object : Closeable {
                override fun close() {
                    runPCatch {
                        this@pLifeScope.getTag<PLifeScope?>(JOB_KEY)?.close()
                    }
                }
            })
        }
        return setTagIfAbsent(JOB_KEY, PLifeScope())
    }

/**
 * 创建LifeScope
 * */
val LifecycleOwner.pLifeScope: PLifeScope
    get() = lifecycle.pLifeScope

/**
 * 创建LifeScope
 * */
val Lifecycle.pLifeScope: PLifeScope
    get() {
        val hashCodeKey = hashCode()
        if (cacheMap.containsKey(hashCodeKey) && cacheMap[hashCodeKey].notNull()) {
            return cacheMap[hashCodeKey]!!
        }
        val newScope = PLifeScope(this, key = hashCodeKey)
        cacheMap[hashCodeKey] = newScope
        return newScope
    }

/**
 * 将lifescope从缓存map中移除
 * */
fun PLifeScope.remove(hashCodeKey: Int) {
    if (cacheMap.containsKey(hashCodeKey)) {
        cacheMap.remove(hashCodeKey)
    }
}

/**
 * 关闭所有LifeScope并清空cacheMap
 */
fun closeAllLifeScopeRemove() {
    cacheMap.forEach {
        runPCatch {
            it.value.close()
        }
    }
    cacheMap.clear()
}
