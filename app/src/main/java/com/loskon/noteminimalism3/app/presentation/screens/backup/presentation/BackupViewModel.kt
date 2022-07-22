package com.loskon.noteminimalism3.app.presentation.screens.backup.presentation

import android.app.Activity
import android.content.Intent
import com.loskon.noteminimalism3.app.base.presentation.viewmodel.BaseViewModel
import com.loskon.noteminimalism3.app.presentation.screens.backup.domain.CloudStorageInteractor
import com.loskon.noteminimalism3.app.presentation.screens.backup.domain.GoogleOneTapSignInInteractor
import com.loskon.noteminimalism3.app.presentation.screens.backup.presentation.state.AuthIntent
import com.loskon.noteminimalism3.app.presentation.screens.backup.presentation.state.BackupAction
import com.loskon.noteminimalism3.app.presentation.screens.backup.presentation.state.BackupMessageType
import com.loskon.noteminimalism3.app.presentation.screens.backup.presentation.state.BackupState
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
    private val backupAction = MutableSharedFlow<BackupAction>()
    val getBackupState get() = backupState.asStateFlow()
    val getBackupAction get() = backupAction.asSharedFlow()

    private var authIntent: AuthIntent? = null

    fun hasAuthorizedUser() {
        launchErrorJob {
            val hasAuthorizedUser = googleOneTapSignInInteractor.hasAuthorizedUser()
            emitBackupState(hasAuthorizedUser)
        }
    }

    fun authenticationWithSelectWay(data: Intent?) {
        launchErrorJob {
            if (authIntent == AuthIntent.BACKUP) {
                val signInSuccess = googleOneTapSignInInteractor.signIn(data)
                emitBackupState(hasAuthorizedUser = signInSuccess)
                if (signInSuccess) uploadDatabaseFile()
            } else if (authIntent == AuthIntent.RESTORE) {
                val signInSuccess = googleOneTapSignInInteractor.signIn(data)
                emitBackupState(hasAuthorizedUser = signInSuccess)
                if (signInSuccess) checkExistsDatabaseFile()
            } else if (authIntent == AuthIntent.DELETE_ACCOUNT) {
                val reauthenticateSuccess = googleOneTapSignInInteractor.reAuthenticate(data)
                if (reauthenticateSuccess) deleteAccount()
            }
        }
    }

    private suspend fun deleteAccount() {
        val deleteAccountSuccess = googleOneTapSignInInteractor.deleteAccount()

        if (deleteAccountSuccess) {
            emitBackupState(hasAuthorizedUser = false)
            emitShowSnackbarAction(BackupMessageType.DELETED_ACCOUNT)
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

    private suspend fun checkExistsDatabaseFile() {
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

    fun checkInternetBeforeShowConfirmSheetDialog(isBackup: Boolean) {
        launchWithCheckInternetErrorJob {
            backupAction.emit(BackupAction.ShowConfirmSheetDialog(isBackup))
        }
    }

    fun getIntentSenderForAuthContract(activity: Activity) {
        launchWithCheckInternetErrorJob {
            val intentSender = googleOneTapSignInInteractor.getIntentSender(activity)
            backupAction.emit(BackupAction.LaunchAuthContract(intentSender))
        }
    }

    fun setAuthIntent(authIntent: AuthIntent) {
        this.authIntent = authIntent
    }

    fun backupDatebaseFile() {
        launchWithCheckInternetErrorJob {
            uploadDatabaseFile()
        }
    }

    fun restoreDatabaseFile() {
        launchWithCheckInternetErrorJob {
            checkExistsDatabaseFile()
        }
    }

    fun deleteDatabaseFile() {
        launchWithCheckInternetErrorJob {
            cloudStorageInteractor.deleteDatabaseFile()
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
            emitBackupState(hasAuthorizedUser = false)
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

    private suspend fun emitBackupState(hasAuthorizedUser: Boolean) {
        backupState.emit(BackupState(hasAuthorizedUser))
    }

    private suspend fun emitShowSnackbarAction(messageType: BackupMessageType) {
        backupAction.emit(BackupAction.ShowSnackbar(messageType))
    }
}