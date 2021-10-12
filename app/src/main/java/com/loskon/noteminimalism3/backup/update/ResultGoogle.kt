package com.loskon.noteminimalism3.backup.update

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.IdpResponse

/**
 *
 */

class ResultGoogle {
    companion object {

        private var signInLauncher: ActivityResultLauncher<Intent>? = null

        fun installing(
            fragment: Fragment?,
            resultInterface: GoogleResultInterface?
        ) {
            signInLauncher = fragment?.registerForActivityResult(
                FirebaseAuthUIActivityResultContract()
            ) { result ->
                val isGranted: Boolean = result.resultCode == Activity.RESULT_OK
                val response: IdpResponse? = result.idpResponse
                resultInterface?.onRequestGoogleResult(isGranted, response)
            }
        }

        fun launcher(signInIntent: Intent) {
            signInLauncher?.launch(signInIntent)
        }
    }
}