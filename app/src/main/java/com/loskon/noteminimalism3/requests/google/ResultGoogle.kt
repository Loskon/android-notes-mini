package com.loskon.noteminimalism3.requests.google

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.loskon.noteminimalism3.R

/**
 * Регистрация, получение и обработка результатов контракта
 */

class ResultGoogle(
    private val fragment: Fragment,
    private val googleInterface: ResultGoogleInterface
) {

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    fun installingContracts() {
        resultLauncher = fragment.registerForActivityResult(
            FirebaseAuthUIActivityResultContract()
        ) { result ->
            val isGranted: Boolean = result.resultCode == Activity.RESULT_OK
            googleInterface.onRequestGoogleResult(isGranted)
        }
    }

    fun launcher() {
        val providers: List<AuthUI.IdpConfig> = listOf(AuthUI.IdpConfig.GoogleBuilder().build())

        val signInIntent: Intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.Authentication)
            .build()

        resultLauncher.launch(signInIntent)
    }
}
