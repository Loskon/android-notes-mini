package com.loskon.noteminimalism3.app.presentation.screens.backup.domain

import android.app.Activity
import android.content.Intent
import android.content.IntentSender

class GoogleOneTapSignInInteractor(
    private val googleOneTapSignInRepository: GoogleOneTapSignInRepository
) {

    suspend fun hasAuthorizedUser(): Boolean {
        return googleOneTapSignInRepository.hasAuthorizedUser()
    }

    suspend fun getIntentSender(activity: Activity): IntentSender {
        return googleOneTapSignInRepository.getBeginSignInResult(activity).pendingIntent.intentSender
    }

    suspend fun signIn(activity: Activity, data: Intent?): Boolean {
        return googleOneTapSignInRepository.signIn(activity, data)
    }

    suspend fun signOut() {
        googleOneTapSignInRepository.signOut()
    }
}