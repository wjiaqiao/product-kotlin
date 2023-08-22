package com.jiaqiao.product.helper.contract

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class IntentContract : ActivityResultContract<Intent, Pair<Int, Intent?>>() {

    override fun createIntent(context: Context, input: Intent): Intent {
        return input
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Pair<Int, Intent?> {
        return Pair(resultCode, intent)
    }

}