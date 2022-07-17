package com.loskon.noteminimalism3.app.presentation.screens.backup.presentation

import android.app.Activity
import android.content.Intent
import com.loskon.noteminimalism3.app.base.presentation.viewmodel.BaseViewModel
import com.loskon.noteminimalism3.app.presentation.screens.backup.domain.AuthInteractor
import com.loskon.noteminimalism3.app.presentation.screens.backup.domain.CloudInteractor
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class BackupViewModel(
    private val authInteractor: AuthInteractor,
    private val cloudInteractor: CloudInteractor
) : BaseViewModel() {

    private val backupState = MutableStateFlow(BackupState())
    private val authAction = MutableSharedFlow<AuthAction>()
    val getAuthState get() = backupState.asStateFlow()
    val getAuthAction get() = authAction.asSharedFlow()

    fun hasAuthorizedUser() {
        launchErrorJob {
            val hasAuthorizedUser = authInteractor.hasAuthorizedUser()
            backupState.emit(BackupState(hasAuthorizedUser))
        }
    }

    fun signInToGoogle(activity: Activity, data: Intent?) {
        launchErrorJob {
            val signInSuccess = authInteractor.signInToGoogle(activity, data)
            if (signInSuccess) cloudInteractor.uploadFile()
            backupState.emit(BackupState(signInSuccess))
        }
    }

    fun getGoogleAuthIntentSender(activity: Activity) {
        launchErrorJob {
            if (backupState.value.showAccountMenuItem) {
                cloudInteractor.uploadFile()
                /* val fileExists = cloudInteractor.fileExists()
                 if (fileExists) cloudInteractor.uploadFile()*/
            } else {
                val intentSender = authInteractor.getGoogleAuthIntentSender(activity)
                val launchAuthContractAction = AuthAction.LaunchAuthContract(intentSender)
                authAction.emit(launchAuthContractAction)
            }
        }
    }

    fun signOut() {
        launchErrorJob {
            authInteractor.signOut()
        }
    }
}