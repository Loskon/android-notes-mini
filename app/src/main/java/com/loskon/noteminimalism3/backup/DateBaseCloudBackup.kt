package com.loskon.noteminimalism3.backup

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.CountDownTimer
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig.GoogleBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.*
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.request.google.ResultGoogle
import com.loskon.noteminimalism3.request.google.ResultGoogleInterface
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable
import com.loskon.noteminimalism3.ui.dialogs.DialogProgress
import com.loskon.noteminimalism3.ui.fragments.BackupFragment
import com.loskon.noteminimalism3.ui.snackbars.SnackbarControl
import java.io.File

/**
 * Работа с FirebaseStorage и Google аккаунтом
 */

class DateBaseCloudBackup(
    private val context: Context,
    private val fragment: BackupFragment
) :
    ResultGoogleInterface {

    private val dialogProgress: DialogProgress = DialogProgress(context)

    private var firebaseAuth: FirebaseAuth = Firebase.auth

    private var storage: FirebaseStorage = FirebaseStorage.getInstance()
    private var storageRef: StorageReference = storage.reference

    private var uploadTask: UploadTask? = null
    private var downloadTask: StorageTask<FileDownloadTask.TaskSnapshot>? = null

    private var countDownTimer: CountDownTimer? = null

    private var isBackup: Boolean = false

    init {
        ResultGoogle.installing(fragment, this)
        changeVisibilityMenuItemAccount()
    }

    fun performBackupCloud() {
        isBackup = true

        if (user == null) {
            signInToGoogle()
        } else {
            uploadCopyDateBaseToCloud()
        }
    }

    private val user: FirebaseUser?
        get() {
            return firebaseAuth.currentUser
        }

    private fun signInToGoogle() {
        val providers: List<AuthUI.IdpConfig> = listOf(GoogleBuilder().build())

        val signInIntent: Intent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.AuthenticationTheme)
            .build()

        ResultGoogle.launcher(signInIntent)
    }

    private fun changeVisibilityMenuItemAccount() {
        fragment.visibilityMenuItemAccount(user != null)
    }

    private fun uploadCopyDateBaseToCloud() {
        showDialog()

        val dateBaseUri: Uri = Uri.fromFile(File(dateBasePath))
        uploadTask = storageRef.child(cloudPath).putFile(dateBaseUri)

        uploadTask?.addOnSuccessListener {
            showSnackbar(SnackbarControl.MSG_BACKUP_COMPLETED)
        }?.addOnFailureListener {
            showSnackbar(SnackbarControl.MSG_BACKUP_FAILED)
        }
    }

    private fun showDialog() {
        dialogProgress.show()
        startCountDownTimer()
    }

    private fun startCountDownTimer() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                onFinishTimer()
            }
        }.start()
    }

    private fun onFinishTimer() {
        uploadTask?.cancel()
        downloadTask?.cancel()
        showSnackbar(SnackbarControl.MSG_INTERNET_PROBLEM)
    }

    private fun showSnackbar(typeMessage: String) {
        countDownTimer?.cancel()
        dialogProgress.close()
        fragment.showSnackbar(typeMessage)
    }

    private val cloudPath: String
        get() {
            return context.getString(R.string.folder_backups_name) + File.separator +
                    user?.uid + File.separator + NoteTable.DATABASE_NAME
        }

    private val dateBasePath: String
        get() {
            return context.getDatabasePath(NoteTable.DATABASE_NAME).toString()
        }

    fun performRestoreCloud() {
        isBackup = false

        if (user == null) {
            signInToGoogle()
        } else {
            checkingPresenceFile()
        }
    }

    private fun checkingPresenceFile() {
        showDialog()

        storageRef.child(cloudPath).metadata.addOnSuccessListener {
            downloadCopyDateBaseFromCloud()
        }.addOnFailureListener {
            showSnackbar(SnackbarControl.MSG_BACKUP_NOT_FOUND)
        }
    }

    private fun downloadCopyDateBaseFromCloud() {
        downloadTask = storageRef.child(cloudPath).getFile(File(dateBasePath))

        (downloadTask as FileDownloadTask).addOnSuccessListener {
            showSnackbar(SnackbarControl.MSG_RESTORE_COMPLETED)
            callback?.onRestoreNotes()
        }.addOnFailureListener {
            showSnackbar(SnackbarControl.MSG_RESTORE_FAILED)
        }
    }

    override fun onRequestGoogleResult(isGranted: Boolean) {
        if (isGranted) {
            if (isBackup) {
                uploadCopyDateBaseToCloud()
            } else {
                checkingPresenceFile()
            }
        } else {
            fragment.showSnackbar(SnackbarControl.MSG_TEXT_SIGN_IN_FAILED)
        }

        changeVisibilityMenuItemAccount()
    }

    fun signOutFromGoogle() {
        startCountDownTimer()

        AuthUI.getInstance()
            .signOut(context)
            .addOnCompleteListener {
                countDownTimer?.cancel()
                changeVisibilityMenuItemAccount()
                fragment.showSnackbar(SnackbarControl.MSG_TEXT_OUT)
            }
    }

    fun deleteUserAccount() {
        startCountDownTimer()

        storageRef.child(cloudPath).delete()
        AuthUI.getInstance().signOut(context)

        AuthUI.getInstance()
            .delete(context)
            .addOnCompleteListener {
                countDownTimer?.cancel()
                changeVisibilityMenuItemAccount()
                fragment.showSnackbar(SnackbarControl.MSG_DEL_DATA)
            }
    }

    interface CallbackRestoreNoteCloud {
        fun onRestoreNotes()
    }

    companion object {
        private var callback: CallbackRestoreNoteCloud? = null

        fun listenerCallback(callback: CallbackRestoreNoteCloud) {
            this.callback = callback
        }
    }
}