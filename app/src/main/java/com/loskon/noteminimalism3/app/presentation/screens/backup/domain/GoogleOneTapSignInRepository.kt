package com.loskon.noteminimalism3.app.presentation.screens.backup.domain

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.identity.BeginSignInResult

interface GoogleOneTapSignInRepository {

    suspend fun hasAuthorizedUser(): Boolean

    suspend fun getBeginSignInResult(activity: Activity): BeginSignInResult

    suspend fun signIn(activity: Activity, data: Intent?): Boolean

    suspend fun signOut()
}