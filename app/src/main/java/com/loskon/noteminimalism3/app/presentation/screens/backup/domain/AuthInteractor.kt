package com.loskon.noteminimalism3.app.presentation.screens.backup.domain

import android.app.Activity
import android.content.Intent
import android.content.IntentSender

class AuthInteractor(private val authRepository: AuthRepository) {

    suspend fun hasAuthorizedUser(): Boolean {
        return authRepository.hasAuthorizedUser()
    }

    suspend fun getGoogleAuthIntentSender(activity: Activity): IntentSender {
        return authRepository.getGoogleAuthBeginSignInResult(activity).pendingIntent.intentSender
    }

    suspend fun signInToGoogle(activity: Activity, data: Intent?): Boolean {
        return authRepository.signInToGoogle(activity, data)
    }

    suspend fun signOut() {
        authRepository.signOut()
    }
}