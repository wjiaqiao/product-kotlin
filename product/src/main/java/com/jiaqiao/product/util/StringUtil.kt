package com.jiaqiao.product.util

object StringUtil {

    /**
     * 计算两个字符串之间的相似度（0 和 1 之间的数字）。
     */
    fun similarity(s1: String, s2: String): Double {
        var longer = s1
        var shorter = s2
        if (s1.length < s2.length) { // longer should always have greater length
            longer = s2
            shorter = s1
        }
        val longerLength = longer.length
        return if (longerLength == 0) {
            0.0
        } else (longerLength - editDistance(
            longer,
            shorter
        )) / longerLength.toDouble()
    }

    private fun editDistance(s1: String, s2: String): Int {
        var s11 = s1
        var s21 = s2
        s11 = s11.lowercase()
        s21 = s21.lowercase()
        val costs = IntArray(s21.length + 1)
        for (i in 0..s11.length) {
            var lastValue = i
            for (j in 0..s21.length) {
                if (i == 0) costs[j] = j else {
                    if (j > 0) {
                        var newValue = costs[j - 1]
                        if (s11[i - 1] != s21[j - 1]) newValue = Math.min(
                            Math.min(newValue, lastValue),
                            costs[j]
                        ) + 1
                        costs[j - 1] = lastValue
                        lastValue = newValue
                    }
                }
            }
            if (i > 0) costs[s21.length] = lastValue
        }
        return costs[s21.length]
    }


}