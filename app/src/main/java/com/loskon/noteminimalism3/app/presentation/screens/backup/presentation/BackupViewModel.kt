package com.loskon.noteminimalism3.app.presentation.screens.backup.presentation

import android.app.Activity
import android.content.Intent
import com.loskon.noteminimalism3.app.base.presentation.viewmodel.BaseViewModel
import com.loskon.noteminimalism3.app.presentation.screens.backup.domain.CloudStorageInteractor
import com.loskon.noteminimalism3.app.presentation.screens.backup.domain.GoogleOneTapSignInInteractor
import com.loskon.noteminimalism3.app.presentation.screens.backup.presentation.state.BackupAction
import com.loskon.noteminimalism3.app.presentation.screens.backup.presentation.state.BackupMessageType
import com.loskon.noteminimalism3.app.presentation.screens.backup.presentation.state.BackupState
import com.loskon.noteminimalism3.utils.NetworkUtil
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AuthIntent {
    BACKUP,
    RESTORE,
    REAUTHENTICATE
}

class BackupViewModel(
    private val googleOneTapSignInInteractor: GoogleOneTapSignInInteractor,
    private val cloudStorageInteractor: CloudStorageInteractor,
    private val networkUtil: NetworkUtil
) : BaseViewModel() {

    private val backupState = MutableStateFlow(BackupState())
    private val backupAction = MutableSharedFlow<BackupAction>()
    val getBackupState get() = backupState.asStateFlow()
    val getBackupAction get() = backupAction.asSharedFlow()

    private var authIntent: AuthIntent? = null

    fun hasAuthorizedUser() {
        launchErrorJob {
            val hasAuthorizedUser = googleOneTapSignInInteractor.hasAuthorizedUser()
            backupState.emit(BackupState(hasAuthorizedUser = hasAuthorizedUser))
        }
    }

    fun signInToGoogle(data: Intent?) {
        launchErrorJob {
            if (authIntent == AuthIntent.REAUTHENTICATE) {
                val reauthenticateSuccess = googleOneTapSignInInteractor.reAuthenticate(data)

                if (reauthenticateSuccess) {
                    val deleteAccountSuccess = googleOneTapSignInInteractor.deleteAccount()

                    if (deleteAccountSuccess) {
                        backupState.emit(BackupState(hasAuthorizedUser = false))
                        emitShowSnackbarAction(BackupMessageType.DELETE_ACCOUNT)
                    }
                }
            } else {
                val signInSuccess = googleOneTapSignInInteractor.signIn(data)
                backupState.emit(BackupState(hasAuthorizedUser = signInSuccess))

                if (signInSuccess) {
                    if (authIntent == AuthIntent.BACKUP) {
                        uploadDatabaseFile()
                    } else if (authIntent == AuthIntent.RESTORE) {
                        downloadDatabaseFile()
                    }
                }
            }
        }
    }

    private suspend fun uploadDatabaseFile() {
        val uploadFileSuccess = cloudStorageInteractor.uploadFile()

        if (uploadFileSuccess) {
            emitShowSnackbarAction(BackupMessageType.BACKUP_SUCCESS)
        } else {
            emitShowSnackbarAction(BackupMessageType.BACKUP_FAILURE)
        }
    }

    private suspend fun downloadDatabaseFile() {
        val fileExists = cloudStorageInteractor.fileExists()

        if (fileExists) {
            val downloadFileSuccess = cloudStorageInteractor.downloadFile()

            if (downloadFileSuccess) {
                emitShowSnackbarAction(BackupMessageType.RESTORE_SUCCESS)
            } else {
                emitShowSnackbarAction(BackupMessageType.BACKUP_FAILURE)
            }
        } else {
            emitShowSnackbarAction(BackupMessageType.FILE_NO_EXISTS)
        }
    }

    fun checkInternetBeforeShowConfirmDialog(isBackup: Boolean) {
        launchWithCheckInternetErrorJob {
            backupAction.emit(BackupAction.ShowConfirmDialog(isBackup))
        }
    }

    fun getIntentSenderForAuthContract(activity: Activity, authIntent: AuthIntent) {
        launchWithCheckInternetErrorJob {
            this.authIntent = authIntent
            if (authIntent == AuthIntent.REAUTHENTICATE) cloudStorageInteractor.deleteDatabaseFile()
            val intentSender = googleOneTapSignInInteractor.getIntentSender(activity)
            backupAction.emit(BackupAction.LaunchAuthContract(intentSender))
        }
    }

    fun backupDatebaseFile() {
        launchWithCheckInternetErrorJob {
            uploadDatabaseFile()
        }
    }

    fun restoreDatabaseFile() {
        launchWithCheckInternetErrorJob {
            downloadDatabaseFile()
        }
    }

    fun checkInternetBeforeShowAccountDialog() {
        launchWithCheckInternetErrorJob {
            backupAction.emit(BackupAction.ShowAccountDialog)
        }
    }

    fun signOut() {
        launchWithCheckInternetErrorJob {
            googleOneTapSignInInteractor.signOut()
            backupState.emit(BackupState(hasAuthorizedUser = false))
            emitShowSnackbarAction(BackupMessageType.SIGN_OUT)
        }
    }

    private fun launchWithCheckInternetErrorJob(block: suspend () -> Unit): Job {
        return launchErrorJob {
            if (networkUtil.hasConnected()) {
                block()
            } else {
                emitShowSnackbarAction(BackupMessageType.NO_INTERNET)
            }
        }
    }

    private suspend fun emitShowSnackbarAction(messageType: BackupMessageType) {
        backupAction.emit(BackupAction.ShowSnackbar(messageType))
    }
}