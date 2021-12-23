package com.loskon.noteminimalism3.requests.google

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract

/**
 * Регистрация, получение и обработка результатов контракта
 */

class ResultGoogle {
    companion object {

        private var resultLauncher: ActivityResultLauncher<Intent>? = null

        fun installing(
            fragment: Fragment?,
            resultGoogleInterface: ResultGoogleInterface?
        ) {
            resultLauncher = fragment?.registerForActivityResult(
                FirebaseAuthUIActivityResultContract()
            ) { result ->
                val isGranted: Boolean = result.resultCode == Activity.RESULT_OK
                resultGoogleInterface?.onRequestGoogleResult(isGranted)
            }
        }

        fun launcher(signInIntent: Intent) {
            resultLauncher?.launch(signInIntent)
        }
    }
}