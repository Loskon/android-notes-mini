package com.loskon.noteminimalism3.backup.update

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig.GoogleBuilder
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.*
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable
import com.loskon.noteminimalism3.ui.dialogs.DialogProgress
import com.loskon.noteminimalism3.ui.fragments.update.BackupFragment
import com.loskon.noteminimalism3.ui.snackbars.update.SnackbarApp
import java.io.File

/**
 * Работа с FirebaseStorage и Google аккаунтом
 */

class DateBaseCloudBackup(
    private val context: Context,
    private val fragment: BackupFragment
) :
    GoogleResultInterface {

    private val dialogProgress: DialogProgress = DialogProgress(context)

    private var firebaseAuth: FirebaseAuth = Firebase.auth

    private var storage: FirebaseStorage = FirebaseStorage.getInstance()
    private var storageRef: StorageReference = storage.reference

    private var uploadTask: UploadTask? = null
    private var downloadTask: StorageTask<FileDownloadTask.TaskSnapshot>? = null

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
            .build()

        ResultGoogle.launcher(signInIntent)
    }

    private fun changeVisibilityMenuItemAccount() {
        fragment.visibilityMenuItemAccount(user != null)
    }

    private fun uploadCopyDateBaseToCloud() {
        dialogProgress.show()

        val dateBaseUri: Uri = Uri.fromFile(File(dateBasePath))
        uploadTask = storageRef.child(cloudPath).putFile(dateBaseUri)

        uploadTask?.addOnSuccessListener {
            dialogProgress.close()
            fragment.showSnackbar(SnackbarApp.MSG_BACKUP_COMPLETED)
        }?.addOnFailureListener {
            dialogProgress.close()
            fragment.showSnackbar(SnackbarApp.MSG_BACKUP_FAILED)
        }
    }

    private val cloudPath: String
        get() {
            return context.getString(R.string.app_name_backup) + File.separator +
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
        dialogProgress.show()

        storageRef.child(cloudPath).metadata.addOnSuccessListener {
            downloadCopyDateBaseFromCloud()
        }.addOnFailureListener {
            dialogProgress.close()
            fragment.showSnackbar(SnackbarApp.MSG_BACKUP_NOT_FOUND)
        }
    }

    private fun downloadCopyDateBaseFromCloud() {
        downloadTask = storageRef.child(cloudPath).getFile(File(dateBasePath))

        (downloadTask as FileDownloadTask).addOnSuccessListener {
            dialogProgress.close()
            fragment.showSnackbar(SnackbarApp.MSG_RESTORE_COMPLETED)
            callback?.onRestoreNotes()
        }.addOnFailureListener {
            dialogProgress.close()
            fragment.showSnackbar(SnackbarApp.MSG_RESTORE_FAILED)
        }
    }

    override fun onRequestGoogleResult(isGranted: Boolean, response: IdpResponse?) {
        if (isGranted) {
            if (isBackup) {
                uploadCopyDateBaseToCloud()
            } else {
                checkingPresenceFile()
            }
        } else {
            fragment.showSnackbar(SnackbarApp.MSG_TEXT_SIGN_IN_FAILED)
        }

        changeVisibilityMenuItemAccount()
    }

    fun signOutFromGoogle() {
        AuthUI.getInstance()
            .signOut(context)
            .addOnCompleteListener {
                changeVisibilityMenuItemAccount()
                fragment.showSnackbar(SnackbarApp.MSG_TEXT_OUT)
            }
    }

    fun deleteUserAccount() {
        storageRef.child(cloudPath).delete()
        AuthUI.getInstance().signOut(context)

        AuthUI.getInstance()
            .delete(context)
            .addOnCompleteListener {
                changeVisibilityMenuItemAccount()
                fragment.showSnackbar(SnackbarApp.MSG_DEL_DATA)
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