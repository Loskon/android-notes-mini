package com.loskon.noteminimalism3.requests.storage

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * Регистрация, получение и обработка результатов контракта
 */

private const val READ = Manifest.permission.READ_EXTERNAL_STORAGE
private const val WRITE = Manifest.permission.WRITE_EXTERNAL_STORAGE
private const val GRANTED = PackageManager.PERMISSION_GRANTED
private val PERMISSIONS_STORAGE = arrayOf(READ, WRITE)

class ResultStorageAccess(
    private val context: Context,
    private val fragment: Fragment,
    private val storageInterface: ResultAccessStorageInterface
) {

    private lateinit var resultLauncherAndroidR: ActivityResultLauncher<Intent>
    private lateinit var resultLauncher: ActivityResultLauncher<Array<out String>>

    fun installingContracts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            checkPermissionsAndroidR()
        } else {
            checkPermissions()
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun checkPermissionsAndroidR() {
        resultLauncherAndroidR =
            fragment.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) {
                val isGranted: Boolean = Environment.isExternalStorageManager()
                storageInterface.onRequestPermissionsStorageResult(isGranted)
            }
    }

    private fun checkPermissions(    ) {
        resultLauncher = fragment.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions -> getResultPermissions(permissions) }
    }

    private fun getResultPermissions(permissions: Map<String, Boolean>) {
        val read: Boolean? = permissions[READ]
        val write: Boolean? = permissions[WRITE]

        val isGranted: Boolean = (read == true && write == true)
        storageInterface.onRequestPermissionsStorageResult(isGranted)
    }

    fun hasAccessStorageRequest(): Boolean {
        var isGrantedPermissions = false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            isGrantedPermissions = true
        } else {
            val read = ContextCompat.checkSelfPermission(context, READ)
            val write = ContextCompat.checkSelfPermission(context, WRITE)

            if (read == GRANTED && write == GRANTED) {
                isGrantedPermissions = true
            } else {
                requestPermissions()
            }
        }

        return isGrantedPermissions
    }

    fun hasAccessStorage(): Boolean {
        var isGrantedPermissions = false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            isGrantedPermissions = true
        } else {
            val read = ContextCompat.checkSelfPermission(context, READ)
            val write = ContextCompat.checkSelfPermission(context, WRITE)

            if (read == GRANTED && write == GRANTED) isGrantedPermissions = true
        }

        return isGrantedPermissions
    }

    private fun requestPermissions() {
        resultLauncher.launch(PERMISSIONS_STORAGE)
    }
}