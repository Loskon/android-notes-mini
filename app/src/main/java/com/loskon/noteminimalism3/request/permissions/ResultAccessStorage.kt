package com.loskon.noteminimalism3.request.permissions

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

/**
 * Запроса доступа к внутренней памяти
 */

class ResultAccessStorage {
    companion object {
        private const val readPermissions = Manifest.permission.READ_EXTERNAL_STORAGE
        private const val writePermissions = Manifest.permission.WRITE_EXTERNAL_STORAGE
        private const val GRANTED = PackageManager.PERMISSION_GRANTED

        private val PERMISSIONS_STORAGE = arrayOf(readPermissions, writePermissions)

        private var resultLauncherAndroidR: ActivityResultLauncher<Intent>? = null
        private var resultLauncher: ActivityResultLauncher<Array<out String>>? = null

        @JvmStatic
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

        @JvmStatic
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
                    resultAccessStorageInterface?.onRequestPermissionsStorageResult(isGrantedAndroidR)
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
                    resultAccessStorageInterface?.onRequestPermissionsStorageResult(isGrantedAndroidR)
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
            val read = permissions[readPermissions]
            val write = permissions[writePermissions]

            val isGranted: Boolean = (read == true && write == true)
            resultAccessStorageInterface?.onRequestPermissionsStorageResult(isGranted)
        }

        @JvmStatic
        fun hasAccessStorageRequest(context: Context): Boolean {
            var isGrantedPermissions = false

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    isGrantedPermissions = true
                } else {
                    requestPermissionsAndroidR(context)
                }
            } else {
                val read = ActivityCompat.checkSelfPermission(context, readPermissions)
                val write = ActivityCompat.checkSelfPermission(context, writePermissions)

                if (read == GRANTED && write == GRANTED) {
                    isGrantedPermissions = true
                } else {
                    requestPermissions()
                }
            }

            return isGrantedPermissions
        }

        @JvmStatic
        fun hasAccessStorage(context: Context): Boolean {
            var isGrantedPermissions = false

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) isGrantedPermissions = true
            } else {
                val read = ActivityCompat.checkSelfPermission(context, readPermissions)
                val write = ActivityCompat.checkSelfPermission(context, writePermissions)

                if (read == GRANTED && write == GRANTED) isGrantedPermissions = true
            }

            return isGrantedPermissions
        }

        @RequiresApi(Build.VERSION_CODES.R)
        private fun requestPermissionsAndroidR(context: Context) {
            val packageApp: String = context.applicationContext?.packageName.toString()
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.addCategory("android.intent.category.DEFAULT")
            intent.data = Uri.parse(String.format("package:%s", packageApp))
            resultLauncherAndroidR?.launch(intent)
        }

        private fun requestPermissions() {
            resultLauncher?.launch(PERMISSIONS_STORAGE)
        }
    }
}