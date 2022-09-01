package com.loskon.noteminimalism3.app.base.contracts

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

    private var handleResultData2: ((Intent?) -> Unit)? = null

    private val authLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        val granted: Boolean = (result.resultCode == Activity.RESULT_OK)
        if (granted) handleResultData2?.invoke(result.data)
    }

    fun launch(intentSender: IntentSender) {
        try {
            val intentSenderRequest = IntentSenderRequest.Builder(intentSender).build()
            authLauncher.launch(intentSenderRequest, null)
        } catch (e: IntentSender.SendIntentException) {
            Timber.e("Couldn't start One Tap UI: " + e.localizedMessage)
        }
    }

    fun setHandleResultDataListener(handleData2: ((Intent?) -> Unit)?) {
        this.handleResultData2 = handleData2
    }
}