package com.loskon.noteminimalism3.app.presentation.screens.backup.domain

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.identity.BeginSignInResult

interface AuthRepository {

    suspend fun hasAuthorizedUser(): Boolean

    suspend fun getGoogleAuthBeginSignInResult(activity: Activity): BeginSignInResult

    suspend fun signInToGoogle(activity: Activity, data: Intent?): Boolean

    suspend fun signOut()
}