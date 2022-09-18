package com.loskon.noteminimalism3.base.contracts

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import timber.log.Timber

/**
 * Contract for authentication request
 */
class AuthContract(fragment: Fragment) {

    private var handleResultData: ((Intent?) -> Unit)? = null

    private val authLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        val granted = (result.resultCode == Activity.RESULT_OK)
        if (granted) handleResultData?.invoke(result.data)
    }

    fun launch(intentSender: IntentSender) {
        try {
            val intentSenderRequest = IntentSenderRequest.Builder(intentSender).build()
            authLauncher.launch(intentSenderRequest, null)
        } catch (e: IntentSender.SendIntentException) {
            Timber.e("Couldn't start One Tap UI: " + e.localizedMessage)
        }
    }

    fun setHandleResultDataListener(handleResultData: ((Intent?) -> Unit)?) {
        this.handleResultData = handleResultData
    }
}