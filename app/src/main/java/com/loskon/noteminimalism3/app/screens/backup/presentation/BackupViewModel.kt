package com.loskon.noteminimalism3.app.screens.backup.presentation

import android.app.Activity
import android.content.Intent
import com.loskon.noteminimalism3.app.screens.backup.domain.CloudStorageInteractor
import com.loskon.noteminimalism3.app.screens.backup.domain.GoogleOneTapSignInInteractor
import com.loskon.noteminimalism3.app.screens.backup.domain.LocalFileInteractor
import com.loskon.noteminimalism3.app.screens.backup.presentation.state.BackupAction
import com.loskon.noteminimalism3.app.screens.backup.presentation.state.BackupAuthWay
import com.loskon.noteminimalism3.app.screens.backup.presentation.state.BackupMessageType
import com.loskon.noteminimalism3.app.screens.backup.presentation.state.BackupUiState
import com.loskon.noteminimalism3.base.presentation.viewmodel.BaseViewModel
import com.loskon.noteminimalism3.utils.NetworkUtil
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.io.FileDescriptor

class BackupViewModel(
    private val googleOneTapSignInInteractor: GoogleOneTapSignInInteractor,
    private val cloudStorageInteractor: CloudStorageInteractor,
    private val localFileInteractor: LocalFileInteractor,
    private val networkUtil: NetworkUtil
) : BaseViewModel() {

    private val backupUiState = MutableStateFlow(BackupUiState())
    private val backupAction = MutableSharedFlow<BackupAction>()
    val getBackupUiState get() = backupUiState.asStateFlow()
    val getBackupAction get() = backupAction.asSharedFlow()

    private var backupAuthWay: BackupAuthWay? = null

    fun hasAuthorizedUser() {
        launchErrorJob {
            val hasAuthorizedUser = googleOneTapSignInInteractor.hasAuthorizedUser()
            emitBackupState(hasAuthorizedUser)
        }
    }

    fun authenticationWithSelectWay(data: Intent?) {
        launchErrorJob {
            if (backupAuthWay == BackupAuthWay.BACKUP) {
                val signInSuccess = googleOneTapSignInInteractor.signIn(data)
                emitBackupState(hasAuthorizedUser = signInSuccess)
                if (signInSuccess) uploadDatabaseFile()
            } else if (backupAuthWay == BackupAuthWay.RESTORE) {
                val signInSuccess = googleOneTapSignInInteractor.signIn(data)
                emitBackupState(hasAuthorizedUser = signInSuccess)
                if (signInSuccess) checkExistsDatabaseFile()
            } else if (backupAuthWay == BackupAuthWay.DELETE_ACCOUNT) {
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

    fun setBackupAuthWay(backupAuthWay: BackupAuthWay) {
        this.backupAuthWay = backupAuthWay
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

    fun copyFileInCacheDir(fileDescriptor: FileDescriptor, fileName: String, cacheDir: File): Boolean {
        return localFileInteractor.copyFileInCacheDir(fileDescriptor, fileName, cacheDir)
    }

    fun validSQLiteFile(backupFile: String): Boolean {
        return localFileInteractor.validSQLiteFile(backupFile)
    }

    fun performRestore(backupFile: String, databasePath: String): Boolean {
        return localFileInteractor.restore(backupFile, databasePath)
    }

    fun deleteFile(file: String) {
        localFileInteractor.deleteFile(file)
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
        backupUiState.emit(BackupUiState(hasAuthorizedUser))
    }

    private suspend fun emitShowSnackbarAction(messageType: BackupMessageType) {
        backupAction.emit(BackupAction.ShowSnackbar(messageType))
    }
}