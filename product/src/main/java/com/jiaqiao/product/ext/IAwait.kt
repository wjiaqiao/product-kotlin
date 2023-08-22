package com.jiaqiao.product.ext

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.CoroutineContext


interface IAwait<T> {
    suspend fun await(): T
}

/**
 * 转换IAwait，将T类型转换成R类型
 * */
inline fun <T, R> IAwait<T>.newAwait(
    crossinline block: suspend IAwait<T>.() -> R
): IAwait<R> = object : IAwait<R> {
    override suspend fun await(): R {
        return this@newAwait.block()
    }
}

/**
 * 重试IAwait操作
 * [times]  重试次数，Int.MAX_VALUE为一直重试不停止
 * [delayTimeInMillis] 重试时的延迟时间
 * [checkRetry]   回调方法返回是否继续重试，默认true
 */
fun <T> IAwait<T>.retry(
    times: Int = Int.MAX_VALUE,
    delayTimeInMillis: Long = 0,
    checkRetry: suspend (Throwable) -> Boolean = { true }
): IAwait<T> = object : IAwait<T> {

    var retryTime = times

    override suspend fun await(): T {
        return try {
            this@retry.await()
        } catch (thr: Throwable) {
            val remaining = retryTime  //Remaining retries
            if (remaining != Int.MAX_VALUE) {
                retryTime = remaining - 1
            }
            val pass = checkRetry(thr)
            if (remaining > 0 && pass) {
                if (delayTimeInMillis > 0) {
                    kotlinx.coroutines.delay(delayTimeInMillis)
                }
                await()
            } else throw thr
        }
    }
}

/**
 * 切换IAwait的运行线程
 * [coroutineContext] 指定运行线程
 * 示例：
 * lifecycleScope.launch {
 *     val t = RxHttp.get("...")
 *         .toClass<T>()
 *         .map { ... }    // Will be executed in IO
 *         .flowOn(Dispatchers.IO)
 *         .filter { ... } // Will be executed in Default
 *         .flowOn(Dispatchers.Default)
 *         .flowOn(Dispatchers.IO)
 *         .filter { ... } // Will be executed in the Main
 *         .await()        // Will be executed in the Main
 * }
 */
fun <T> IAwait<T>.flowOn(
    coroutineContext: CoroutineContext
): IAwait<T> = newAwait {
    withContext(coroutineContext) { await() }
}

/**
 * 将IAwait转换成Flow流处理
 */
fun <T> IAwait<T>.asFlow(): Flow<T> = flow {
    emit(await())
}

/**
 * 在IAwait返回的列表尾部追加数据[element]
 * [element] 添加的数据
 */
fun <T> IAwait<out MutableList<T>>.add(
    element: T
): IAwait<MutableList<T>> = newAwait {
    await().apply { add(element) }
}

/**
 * 在IAwait返回的列表指定位置[position]添加数据[element]
 * [position] 添加位置序号
 * [element] 添加的数据
 */
fun <T> IAwait<out MutableList<T>>.add(
    position: Int,
    element: T
): IAwait<MutableList<T>> = newAwait {
    await().apply { add(position, element) }
}

/**
 * 在IAwait返回的列表尾部添加所有数据[elements]
 * [elements]  T格式数据的列表
 */
fun <T> IAwait<out MutableList<T>>.addAll(
    elements: Collection<T>
): IAwait<MutableList<T>> = newAwait {
    await().apply { addAll(elements) }
}

/**
 * 在IAwait返回的列表指定位置[position]添加所有数据[elements]
 * [position] 添加位置序号
 * [elements]  添加的数据
 */
fun <T> IAwait<out MutableList<T>>.addAll(
    position: Int,
    elements: Collection<T>
): IAwait<MutableList<T>> = newAwait {
    await().apply { addAll(position, elements) }
}

/**
 * 将Iterable的IAwait过滤并转换成ArrayList的IAwait
 * */
inline fun <T> IAwait<out Iterable<T>>.filter(
    crossinline predicate: (T) -> Boolean
): IAwait<ArrayList<T>> = filterTo(ArrayList(), predicate)

/**
 * 将Iterable的IAwait过滤并转换成[C]的IAwait
 */
inline fun <T, C : MutableCollection<in T>> IAwait<out Iterable<T>>.filterTo(
    destination: C,
    crossinline predicate: (T) -> Boolean
): IAwait<C> = newAwait {
    await().filterTo(destination, predicate)
}

/**
 * Returns a IAwait containing a list containing only distinct elements from the given collection.
 *
 * The elements in the resulting list are in the same order as they were in the source collection.
 */
//fun <T> IAwait<out Iterable<T>>.distinct(): IAwait<ArrayList<T>> = distinctTo(ArrayList()) { it }

/**
 * Returns a IAwait containing a list containing only elements from the given collection
 * having distinct keys returned by the given [selector] function.
 *
 * The elements in the resulting list are in the same order as they were in the source collection.
 */
inline fun <T, K> IAwait<out Iterable<T>>.distinctBy(
    crossinline selector: (T) -> K
): IAwait<ArrayList<T>> = distinctTo(ArrayList(), selector)

fun <T, C : MutableList<T>> IAwait<out Iterable<T>>.distinctTo(
    destination: C
): IAwait<C> = distinctTo(destination) { it }

/**
 * Appends all the different elements to the given [destination].
 *
 * The elements in the resulting list are in the same order as they were in the source collection.
 */
inline fun <T, K, C : MutableList<T>> IAwait<out Iterable<T>>.distinctTo(
    destination: C,
    crossinline selector: (T) -> K
): IAwait<C> = newAwait {
    val set = HashSet<K>()
    for (e in destination) {
        val key = selector(e)
        set.add(key)
    }
    for (e in await()) {
        val key = selector(e)
        if (set.add(key))
            destination.add(e)
    }
    destination
}

/**
 * Sorts elements in the list in-place according to their natural sort order.
 *
 * The sort is _stable_. It means that equal elements preserve their order relative to each other after sorting.
 */
fun <T : Comparable<T>> IAwait<out MutableList<T>>.sort(): IAwait<MutableList<T>> = newAwait {
    await().apply { sort() }
}

/**
 * Sorts elements in the list in-place descending according to their natural sort order.
 *
 * The sort is _stable_. It means that equal elements preserve their order relative to each other after sorting.
 */
fun <T : Comparable<T>> IAwait<out MutableList<T>>.sortDescending()
        : IAwait<MutableList<T>> = sortWith(reverseOrder())

fun <T> IAwait<out MutableList<T>>.sortBy(
    vararg selectors: (T) -> Comparable<*>?
): IAwait<MutableList<T>> = sortWith(compareBy(*selectors))

inline fun <T> IAwait<out MutableList<T>>.sortBy(
    crossinline selector: (T) -> Comparable<*>?
): IAwait<MutableList<T>> = sortWith(compareBy(selector))

inline fun <T> IAwait<out MutableList<T>>.sortByDescending(
    crossinline selector: (T) -> Comparable<*>?
): IAwait<MutableList<T>> = sortWith(compareByDescending(selector))

inline fun <T> IAwait<out MutableList<T>>.sortWith(
    crossinline comparator: (T, T) -> Int
): IAwait<MutableList<T>> = sortWith(Comparator { t1, t2 -> comparator(t1, t2) })

/**
 * Sorts the elements in the list in-place, in natural sort order, according to the specified [Comparator].
 *
 * The sort is _stable_. It means that equal elements preserve their order relative to each other after sorting.
 */
fun <T> IAwait<out MutableList<T>>.sortWith(
    comparator: Comparator<in T>
): IAwait<MutableList<T>> = newAwait {
    await().apply { sortWith(comparator) }
}

/**
 * Returns a IAwait containing a new list of all elements sorted according to their natural sort order.
 *
 * The sort is _stable_. It means that equal elements preserve their order relative to each other after sorting.
 */
fun <T : Comparable<T>> IAwait<out Iterable<T>>.sorted(): IAwait<List<T>> = newAwait {
    await().sorted()
}

/**
 * Returns a IAwait containing a new list of all elements sorted descending according to their natural sort order.
 *
 * The sort is _stable_. It means that equal elements preserve their order relative to each other after sorting.
 */
fun <T : Comparable<T>> IAwait<out Iterable<T>>.sortedDescending()
        : IAwait<List<T>> = sortedWith(reverseOrder())

fun <T> IAwait<out Iterable<T>>.sortedBy(
    vararg selectors: (T) -> Comparable<*>?
): IAwait<List<T>> = sortedWith(compareBy(*selectors))

inline fun <T> IAwait<out Iterable<T>>.sortedBy(
    crossinline selector: (T) -> Comparable<*>?
): IAwait<List<T>> = sortedWith(compareBy(selector))

inline fun <T> IAwait<out Iterable<T>>.sortedByDescending(
    crossinline selector: (T) -> Comparable<*>?
): IAwait<List<T>> = sortedWith(compareByDescending(selector))

inline fun <T> IAwait<out Iterable<T>>.sortedWith(
    crossinline comparator: (T, T) -> Int
): IAwait<List<T>> = sortedWith(Comparator { t1, t2 -> comparator(t1, t2) })

/**
 * Returns a IAwait containing a new list of all elements sorted according to the specified [comparator].
 *
 * The sort is _stable_. It means that equal elements preserve their order relative to each other after sorting.
 */
fun <T> IAwait<out Iterable<T>>.sortedWith(
    comparator: Comparator<in T>
): IAwait<List<T>> = newAwait {
    await().sortedWith(comparator)
}


fun <T> IAwait<out List<T>>.subList(
    fromIndex: Int = 0, toIndex: Int
): IAwait<List<T>> = newAwait {
    await().subList(fromIndex, toIndex)
}

fun <T> IAwait<out Iterable<T>>.take(
    count: Int
): IAwait<List<T>> = newAwait {
    await().take(count)
}

fun <T> IAwait<out Iterable<T>>.toMutableList(): IAwait<MutableList<T>> = newAwait {
    await().let {
        if (it is MutableList<T>) it
        else it.toMutableList()
    }
}

/**
 * 设置超时时间
 * [timeInMillis] 超时市场，单位：毫秒
 */
fun <T> IAwait<T>.timeout(
    timeInMillis: Long
): IAwait<T> = newAwait {
    withTimeout(timeInMillis) { await() }
}

/**
 * Returns a IAwait containing the specified object when an error occurs.
 */
fun <T> IAwait<T>.onErrorReturnItem(t: T): IAwait<T> = onErrorReturn { t }

/**
 * Returns a IAwait containing the object specified by the [map] function when an error occurs.
 */
inline fun <T> IAwait<T>.onErrorReturn(
    crossinline map: suspend (Throwable) -> T
): IAwait<T> = newAwait {
    try {
        await()
    } catch (thr: Throwable) {
        map(thr)
    }
}

/**
 * Returns a IAwait containing the results of applying the given [map] function
 */
inline fun <T, R> IAwait<T>.map(
    crossinline map: suspend (T) -> R
): IAwait<R> = newAwait {
    map(await())
}

/**
 * 运行结束后延迟 [timeInMillis] 时间
 * [timeInMillis] 延迟时间，单位：毫秒
 */
fun <T> IAwait<T>.endDelay(timeInMillis: Long): IAwait<T> = newAwait {
    val t = await()
    kotlinx.coroutines.delay(timeInMillis)
    t
}

/**
 * 运行前延迟 [timeInMillis] 时间
 * [timeInMillis] 延迟时间，单位：毫秒
 */
fun <T> IAwait<T>.delay(timeInMillis: Long): IAwait<T> = startDelay(timeInMillis)

/**
 * 运行前延迟 [timeInMillis] 时间
 * [timeInMillis] 延迟时间，单位：毫秒
 */
fun <T> IAwait<T>.startDelay(timeInMillis: Long): IAwait<T> = newAwait {
    kotlinx.coroutines.delay(timeInMillis)
    await()
}

/**
 * 等待同步执行方法
 */
suspend fun <T> IAwait<T>.async(
    scope: CoroutineScope,
    context: CoroutineContext = SupervisorJob(),
    start: CoroutineStart = CoroutineStart.DEFAULT
): Deferred<T> = scope.async(context, start) {
    await()
}

/**
 * 方法报错后依然回值，返回值需要做空判断
 */
suspend fun <T> Deferred<T>.tryAwait(): T? = tryAwait { await() }

/**
 * 方法报错后依然回值，返回值需要做空判断
 */
suspend fun <T> IAwait<T>.tryAwait(): T? = tryAwait { await() }

/**
 * 方法报错后依然回值，返回值需要做空判断
 */
private inline fun <T> tryAwait(block: () -> T): T? {
    return try {
        block()
    } catch (thr: Throwable) {
        null
    }
}

/**
 * 重复执行方法
 * [delayTimeInMillis] 延迟时间，单位：毫秒
 * [numTimes] 重复次数，Long.MAX_VALUE，永不停止
 * [stop] 判断终止函数
 */
fun <T> IAwait<T>.repeat(
    delayTimeInMillis: Long = 0,
    numTimes: Long = Long.MAX_VALUE,
    stop: suspend (T) -> Boolean = { false }
): IAwait<T> = object : IAwait<T> {
    var remaining = if (numTimes == Long.MAX_VALUE) Long.MAX_VALUE else numTimes - 1
    override suspend fun await(): T {
        while (remaining > 0) {
            if (remaining != Long.MAX_VALUE) {
                remaining--
            }
            val t = this@repeat.await()
            if (stop(t)) {
                return t
            }
            kotlinx.coroutines.delay(delayTimeInMillis)
        }
        return this@repeat.await()
    }
}

/**
 * 报错后依然返回值的重复执行方法
 * [delayTimeInMillis]  延迟时间，单位：毫秒
 * [numTimes] 重复次数，Long.MAX_VALUE，永不停止
 * [stop] 判断终止函数
 */
fun <T> IAwait<T>.tryRepeat(
    delayTimeInMillis: Long = 0,
    numTimes: Long = Long.MAX_VALUE,
    stop: suspend (T?) -> Boolean = { false }
): IAwait<T> = object : IAwait<T> {
    var remaining = if (numTimes == Long.MAX_VALUE) Long.MAX_VALUE else numTimes - 1
    override suspend fun await(): T {
        while (remaining > 0) {
            if (remaining != Long.MAX_VALUE) {
                remaining--
            }
            val t = try {
                this@tryRepeat.await()
            } catch (thr: Throwable) {
                null
            }
            if (stop(t)) {
                return t!!
            }
            kotlinx.coroutines.delay(delayTimeInMillis)
        }
        return this@tryRepeat.await()
    }
}

