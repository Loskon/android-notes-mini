package com.loskon.noteminimalism3.app.presentation.screens.backup.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.presentation.screens.backup.domain.AuthRepository
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Actions with the user and
 */
class AuthRepositoryImpl(context: Context) : AuthRepository {

    private val signInClient: SignInClient = Identity.getSignInClient(context)
    private val auth: FirebaseAuth = Firebase.auth

    private val requestOptions = BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
        .setSupported(true)
        .setServerClientId(context.getString(R.string.web_client_id))
        .setFilterByAuthorizedAccounts(false)
        .build()

    private val signInRequest: BeginSignInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(requestOptions)
        .build()

    override suspend fun hasAuthorizedUser(): Boolean {
        return auth.currentUser != null
    }

    override suspend fun getGoogleAuthBeginSignInResult(activity: Activity): BeginSignInResult {
        return suspendCoroutine { cont ->
            signInClient.beginSignIn(signInRequest)
                .addOnSuccessListener { cont.resume(it) }
                .addOnFailureListener { cont.resumeWithException(it) }
        }
    }

    override suspend fun signInToGoogle(activity: Activity, data: Intent?): Boolean {
        val credential = signInClient.getSignInCredentialFromIntent(data)
        val firebaseCredential = GoogleAuthProvider.getCredential(credential.googleIdToken, null)

        return suspendCoroutine { cont ->
            auth.signInWithCredential(firebaseCredential)
                .addOnSuccessListener { cont.resume(true) }
                .addOnFailureListener { cont.resumeWithException(it) }
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }
}