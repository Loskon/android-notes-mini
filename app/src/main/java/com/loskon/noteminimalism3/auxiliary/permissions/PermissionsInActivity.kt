package com.loskon.noteminimalism3.auxiliary.permissions

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.os.Environment
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.loskon.noteminimalism3.auxiliary.other.RequestCode.REQUEST_CODE_PERMISSIONS
import com.loskon.noteminimalism3.auxiliary.permissions.PermissionsValues.Companion.GRANTED
import com.loskon.noteminimalism3.auxiliary.permissions.PermissionsValues.Companion.PERMISSIONS_STORAGE
import com.loskon.noteminimalism3.auxiliary.permissions.PermissionsValues.Companion.REQUEST_CODE_STORAGE

/**
 * Проверка разрешений для взаимодействия с внутренней памятью
 */


class PermissionsInActivity {

    fun isAccess(context: Context): Boolean {
        return if (VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            checkPermissionsForAndroidR(context)
        } else {
            checkPermissions(context)
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun checkPermissionsForAndroidR(context: Context): Boolean {
        val externalPermission: Boolean = Environment.isExternalStorageManager()

        return if (externalPermission) {
            true
        } else {
            try {
                intentAppFiles(context)
            } catch (exception: Exception) {
                exception.stackTrace
                intentAllFiles(context)
            }
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun intentAppFiles(context: Context) {
        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
        intent.addCategory("android.intent.category.DEFAULT")
        intent.data =
            Uri.parse(String.format("package:%s", context.applicationContext.packageName))
        (context as Activity).startActivityForResult(intent, REQUEST_CODE_STORAGE)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun intentAllFiles(context: Context) {
        val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
        (context as Activity).startActivityForResult(intent, REQUEST_CODE_STORAGE)
    }

    private fun checkPermissions(context: Context): Boolean {
        val writePermission: Int = getPermission(context, WRITE_EXTERNAL_STORAGE)
        val readPermission: Int = getPermission(context, READ_EXTERNAL_STORAGE)

        return if (writePermission != GRANTED || readPermission != GRANTED) {
            requestPermissions(context)
            false
        } else {
            true
        }
    }

    private fun getPermission(context: Context, externalString: String): Int {
        return ActivityCompat.checkSelfPermission(
            context,
            externalString
        )
    }

    private fun requestPermissions(context: Context) {
        ActivityCompat.requestPermissions(
            (context as Activity),
            PERMISSIONS_STORAGE,
            REQUEST_CODE_PERMISSIONS
        )
    }
}