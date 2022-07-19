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
import com.loskon.noteminimalism3.app.presentation.screens.backup.domain.GoogleOneTapSignInRepository
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Actions with the user and
 */
class GoogleOneTapSignInRepositoryImpl(context: Context) : GoogleOneTapSignInRepository {

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

    override suspend fun getBeginSignInResult(activity: Activity): BeginSignInResult {
        return suspendCoroutine { cont ->
            signInClient.beginSignIn(signInRequest)
                .addOnSuccessListener {
                    Timber.d(it.toString())
                    cont.resume(it)
                }
                .addOnFailureListener {
                    Timber.e(it)
                    cont.resumeWithException(it)
                }
        }
    }

    override suspend fun signIn(data: Intent?): Boolean {
        val credential = signInClient.getSignInCredentialFromIntent(data)
        val firebaseCredential = GoogleAuthProvider.getCredential(credential.googleIdToken, null)

        return suspendCoroutine { cont ->
            auth.signInWithCredential(firebaseCredential)
                .addOnSuccessListener {
                    Timber.d(it.toString())
                    cont.resume(true)
                }
                .addOnFailureListener {
                    Timber.e(it)
                    cont.resume(false)
                }
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun deleteAccount(): Boolean {
        return suspendCoroutine { cont ->
            val user = auth.currentUser

            if (user != null) {
                auth.currentUser?.delete()
                    ?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            Timber.d(it.toString())
                            cont.resume(true)
                        } else {
                            Timber.d(it.toString())
                            cont.resume(false)
                        }
                    }
                    ?.addOnFailureListener {
                        Timber.e(it)
                        cont.resume(false)
                    }
            } else {
                cont.resume(false)
            }
        }
    }

    override suspend fun reauthenticate(data: Intent?): Boolean {
        val credential = signInClient.getSignInCredentialFromIntent(data)
        val firebaseCredential = GoogleAuthProvider.getCredential(credential.googleIdToken, null)

        return suspendCoroutine { cont ->
            auth.currentUser?.reauthenticate(firebaseCredential)
                ?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        Timber.d(it.toString())
                        cont.resume(true)
                    } else {
                        Timber.d(it.toString())
                        cont.resume(false)
                    }
                }
                ?.addOnFailureListener {
                    Timber.e(it)
                    cont.resume(false)
                }
        }
    }
}