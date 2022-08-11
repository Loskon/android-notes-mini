package com.loskon.noteminimalism3.app.presentation.screens.backup.data

import android.content.Context
import android.net.Uri
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.presentation.screens.backup.domain.CloudStorageRepository
import com.loskon.noteminimalism3.sqlite.NoteDatabaseSchema
import timber.log.Timber
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 *
 */
class CloudStorageRepositoryImpl(
    private val context: Context
) : CloudStorageRepository {

    private var storage: FirebaseStorage = FirebaseStorage.getInstance()
    private var storageRef: StorageReference = storage.reference

    private val datebasePath = context.getDatabasePath(NoteDatabaseSchema.DATABASE_NAME).toString()

    override suspend fun uploadDatabaseFile(): Boolean {
        val cloudPath = getCloudPath()
        val dateBaseUri = Uri.fromFile(File(datebasePath))

        return suspendCoroutine { cont ->
            storageRef.child(cloudPath).putFile(dateBaseUri)
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

    private fun getCloudPath(): String {
        val currentUser = Firebase.auth.currentUser

        if (currentUser != null) {
            return context.getString(R.string.folder_backups_name) +
                File.separator + currentUser.uid + File.separator + NoteDatabaseSchema.DATABASE_NAME
        } else {
            throw NullPointerException("Null user")
        }
    }

    override suspend fun databaseFileExists(): Boolean {
        val cloudPath = getCloudPath()

        return suspendCoroutine { cont ->
            storageRef.child(cloudPath).metadata
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

    override suspend fun downloadDatabaseFile(): Boolean {
        val cloudPath = getCloudPath()
        val downloadTask = storageRef.child(cloudPath).getFile(File(datebasePath))

        return suspendCoroutine { cont ->
            downloadTask
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

    override suspend fun deleteDatabaseFile() {
        val cloudPath = getCloudPath()
        storageRef.child(cloudPath).delete()
    }
}