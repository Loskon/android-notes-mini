package com.loskon.noteminimalism3.app.presentation.screens.backup.presentation

import android.content.IntentSender

sealed class AuthAction {
    class LaunchAuthContract(val intentSender: IntentSender) : AuthAction()
    class VerificationResult(val result: Boolean) : AuthAction()
}