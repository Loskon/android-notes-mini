package com.loskon.noteminimalism3.app.presentation.screens.backup.domain

import android.app.Activity
import android.content.Intent
import android.content.IntentSender

class GoogleOneTapSignInInteractor(
    private val googleOneTapSignInRepository: GoogleOneTapSignInRepository
) {

    suspend fun hasAuthorizedUser() = googleOneTapSignInRepository.hasAuthorizedUser()

    suspend fun getIntentSender(activity: Activity): IntentSender {
        return googleOneTapSignInRepository.getBeginSignInResult(activity).pendingIntent.intentSender
    }

    suspend fun signIn(data: Intent?) = googleOneTapSignInRepository.signIn(data)

    suspend fun signOut() = googleOneTapSignInRepository.signOut()

    suspend fun deleteAccount() = googleOneTapSignInRepository.deleteAccount()

    suspend fun reAuthenticate(data: Intent?) = googleOneTapSignInRepository.reauthenticate(data)
}