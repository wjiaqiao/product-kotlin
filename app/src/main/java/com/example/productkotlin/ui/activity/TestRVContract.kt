package com.example.productkotlin.ui.activity

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class TestRVContract:ActivityResultContract<String?,String>() {

    override fun createIntent(context: Context, input: String?): Intent {
        return Intent(context,TestRVAc::class.java).apply {

        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String {
        return "string"
    }

}