package com.loskon.noteminimalism3.request.google

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract

/**
 * Запрос на доступ к Google-аккаунту
 */

class ResultGoogle {
    companion object {

        private var signInLauncher: ActivityResultLauncher<Intent>? = null

        fun installing(
            fragment: Fragment?,
            resultGoogleInterface: ResultGoogleInterface?
        ) {
            signInLauncher = fragment?.registerForActivityResult(
                FirebaseAuthUIActivityResultContract()
            ) { result ->
                val isGranted: Boolean = result.resultCode == Activity.RESULT_OK
                resultGoogleInterface?.onRequestGoogleResult(isGranted)
            }
        }

        fun launcher(signInIntent: Intent) {
            signInLauncher?.launch(signInIntent)
        }
    }
}