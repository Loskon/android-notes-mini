package com.loskon.noteminimalism3.app.presentation.screens.backup.data

import android.content.Context
import android.net.Uri
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.app.presentation.screens.backup.domain.CloudRepository
import com.loskon.noteminimalism3.sqlite.NoteDatebaseSchema
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 *
 */
class CloudRepositoryImpl(
    private val context: Context
) : CloudRepository {

    private val user = Firebase.auth.currentUser
    private var storage: FirebaseStorage = FirebaseStorage.getInstance()
    private var storageRef: StorageReference = storage.reference

    private val datebasePath = context.getDatabasePath(NoteDatebaseSchema.DATABASE_NAME).toString()

    override suspend fun uploadFile(): Boolean {
        val cloudPath = getCloudPath()
        val dateBaseUri = Uri.fromFile(File(datebasePath))

        return suspendCoroutine { cont ->
            storageRef.child(cloudPath).putFile(dateBaseUri)
                .addOnSuccessListener { cont.resume(true) }
                .addOnFailureListener { cont.resumeWithException(it) }
        }
    }

    private fun getCloudPath(): String {
        val currentUser = user

        if (currentUser != null) {
            return context.getString(R.string.folder_backups_name) +
                File.separator + currentUser.uid + File.separator + NoteDatebaseSchema.DATABASE_NAME
        } else {
            throw NullPointerException("Null user")
        }
    }

    override suspend fun fileExists(): Boolean {
        val cloudPath = getCloudPath()

        return suspendCoroutine { cont ->
            storageRef.child(cloudPath).metadata
                .addOnSuccessListener { cont.resume(true) }
                .addOnFailureListener { cont.resume(false) }
        }
    }

    override suspend fun downloadFile(): Boolean {
        val cloudPath = getCloudPath()
        val downloadTask = storageRef.child(cloudPath).getFile(File(datebasePath))

        return suspendCoroutine { cont ->
            downloadTask
                .addOnSuccessListener { cont.resume(true) }
                .addOnFailureListener { cont.resumeWithException(it) }
        }
    }
}