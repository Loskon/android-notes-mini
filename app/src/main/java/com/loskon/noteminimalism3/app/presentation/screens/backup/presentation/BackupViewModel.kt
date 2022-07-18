package com.loskon.noteminimalism3.app.presentation.screens.backup.presentation

import android.app.Activity
import android.content.Intent
import com.loskon.noteminimalism3.app.base.presentation.viewmodel.BaseViewModel
import com.loskon.noteminimalism3.app.presentation.screens.backup.domain.CloudStorageInteractor
import com.loskon.noteminimalism3.app.presentation.screens.backup.domain.GoogleOneTapSignInInteractor
import com.loskon.noteminimalism3.utils.NetworkUtil
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class BackupViewModel(
    private val googleOneTapSignInInteractor: GoogleOneTapSignInInteractor,
    private val cloudStorageInteractor: CloudStorageInteractor,
    private val networkUtil: NetworkUtil
) : BaseViewModel() {

    private val backupState = MutableStateFlow(BackupState())
    private val backupAction = MutableSharedFlow<AuthAction>()
    val getBackupState get() = backupState.asStateFlow()
    val getBackupAction get() = backupAction.asSharedFlow()

    fun hasAuthorizedUser() {
        launchWithCheckInternetErrorJob {
            val hasAuthorizedUser = googleOneTapSignInInteractor.hasAuthorizedUser()
            backupState.emit(BackupState(hasAuthorizedUser = hasAuthorizedUser))
        }
    }

    fun signInToGoogle(activity: Activity, data: Intent?, isBackup: Boolean) {
        launchErrorJob {
            val signInSuccess = googleOneTapSignInInteractor.signIn(activity, data)
            backupState.emit(BackupState(hasAuthorizedUser = signInSuccess))

            if (signInSuccess) {
                if (isBackup) {
                    uploadDatabaseFile()
                } else {
                    downloadDatabaseFile()
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

    fun getIntentSenderForAuthContract(activity: Activity) {
        launchWithCheckInternetErrorJob {
            val intentSender = googleOneTapSignInInteractor.getIntentSender(activity)
            backupAction.emit(AuthAction.LaunchAuthContract(intentSender))
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
        backupAction.emit(AuthAction.ShowSnackbar(messageType))
    }
}