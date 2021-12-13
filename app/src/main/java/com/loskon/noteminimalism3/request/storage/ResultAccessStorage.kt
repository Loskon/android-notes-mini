package com.loskon.noteminimalism3.request.storage

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

/**
 * Регистрация, получение и обработка результатов контракта
 */

class ResultAccessStorage {
    companion object {

        private const val read = Manifest.permission.READ_EXTERNAL_STORAGE
        private const val write = Manifest.permission.WRITE_EXTERNAL_STORAGE
        private const val GRANTED = PackageManager.PERMISSION_GRANTED

        private val PERMISSIONS_STORAGE = arrayOf(read, write)

        private var resultLauncherAndroidR: ActivityResultLauncher<Intent>? = null
        private var resultLauncher: ActivityResultLauncher<Array<out String>>? = null

        fun installingVerification(
            activity: ComponentActivity?,
            resultAccessStorageInterface: ResultAccessStorageInterface?
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                checkPermissionsAndroidR(activity, resultAccessStorageInterface)
            } else {
                checkPermissions(activity, resultAccessStorageInterface)
            }
        }

        fun installingVerification(
            fragment: Fragment?,
            resultAccessStorageInterface: ResultAccessStorageInterface?
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                checkPermissionsAndroidR(fragment, resultAccessStorageInterface)
            } else {
                checkPermissions(fragment, resultAccessStorageInterface)
            }
        }

        @RequiresApi(Build.VERSION_CODES.R)
        private fun checkPermissionsAndroidR(
            activity: ComponentActivity?,
            resultAccessStorageInterface: ResultAccessStorageInterface?
        ) {
            resultLauncherAndroidR =
                activity?.registerForActivityResult(
                    ActivityResultContracts.StartActivityForResult()
                ) {
                    val isGrantedAndroidR: Boolean = Environment.isExternalStorageManager()
                    resultAccessStorageInterface?.onRequestPermissionsStorageResult(
                        isGrantedAndroidR
                    )
                }
        }

        @RequiresApi(Build.VERSION_CODES.R)
        private fun checkPermissionsAndroidR(
            fragment: Fragment?,
            resultAccessStorageInterface: ResultAccessStorageInterface?
        ) {
            resultLauncherAndroidR =
                fragment?.registerForActivityResult(
                    ActivityResultContracts.StartActivityForResult()
                ) {
                    val isGrantedAndroidR: Boolean = Environment.isExternalStorageManager()
                    resultAccessStorageInterface?.onRequestPermissionsStorageResult(
                        isGrantedAndroidR
                    )
                }
        }

        private fun checkPermissions(
            activity: ComponentActivity?,
            resultAccessStorageInterface: ResultAccessStorageInterface?
        ) {
            resultLauncher = activity?.registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions -> getResultPermissions(permissions, resultAccessStorageInterface) }
        }

        private fun checkPermissions(
            fragment: Fragment?,
            resultAccessStorageInterface: ResultAccessStorageInterface?
        ) {
            resultLauncher = fragment?.registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions -> getResultPermissions(permissions, resultAccessStorageInterface) }
        }

        private fun getResultPermissions(
            permissions: Map<String, Boolean>,
            resultAccessStorageInterface: ResultAccessStorageInterface?
        ) {
            val read = permissions[read]
            val write = permissions[write]

            val isGranted: Boolean = (read == true && write == true)
            resultAccessStorageInterface?.onRequestPermissionsStorageResult(isGranted)
        }

        fun hasAccessStorageRequest(context: Context): Boolean {
            var isGrantedPermissions = false

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                isGrantedPermissions = true
            } else {
                val read = ActivityCompat.checkSelfPermission(context, read)
                val write = ActivityCompat.checkSelfPermission(context, write)

                if (read == GRANTED && write == GRANTED) {
                    isGrantedPermissions = true
                } else {
                    requestPermissions()
                }
            }

            return isGrantedPermissions
        }

        fun hasAccessStorage(context: Context): Boolean {
            var isGrantedPermissions = false

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                //if (Environment.isExternalStorageManager())
                isGrantedPermissions = true
            } else {
                val read = ActivityCompat.checkSelfPermission(context, read)
                val write = ActivityCompat.checkSelfPermission(context, write)

                if (read == GRANTED && write == GRANTED) isGrantedPermissions = true
            }

            return isGrantedPermissions
        }

        private fun requestPermissions() {
            resultLauncher?.launch(PERMISSIONS_STORAGE)
        }
    }
}