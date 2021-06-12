package com.loskon.noteminimalism3.auxiliary.permissions

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION
import android.os.Environment
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.loskon.noteminimalism3.auxiliary.other.RequestCode.REQUEST_CODE_PERMISSIONS
import com.loskon.noteminimalism3.auxiliary.permissions.PermissionsValues.Companion.PERMISSIONS_STORAGE
import com.loskon.noteminimalism3.auxiliary.permissions.PermissionsValues.Companion.REQUEST_CODE_STORAGE
import com.loskon.noteminimalism3.auxiliary.permissions.PermissionsValues.Companion.GRANTED

/**
 * Проверка разрешений для взаимодействия с внутренней памятью
 */


class PermissionsInActivity {

    fun isAccess(activity: Activity): Boolean {
        return if (VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            checkPermissionsForAndroidR(activity)
        } else {
            checkPermissions(activity)
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun checkPermissionsForAndroidR(activity: Activity): Boolean {
        val externalPermission: Boolean = Environment.isExternalStorageManager()

        return if (externalPermission) {
            true
        } else {
            try {
                intentAppFiles(activity)
            } catch (exception: Exception) {
                exception.stackTrace
                intentAllFiles(activity)
            }
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun intentAppFiles(activity: Activity) {
        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
        intent.addCategory("android.intent.category.DEFAULT")
        intent.data =
            Uri.parse(String.format("package:%s", activity.applicationContext.packageName))
        activity.startActivityForResult(intent, REQUEST_CODE_STORAGE)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun intentAllFiles(activity: Activity) {
        val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
        activity.startActivityForResult(intent, REQUEST_CODE_STORAGE)
    }

    private fun checkPermissions(activity: Activity): Boolean {
        val writePermission: Int = getPermission(activity, WRITE_EXTERNAL_STORAGE)
        val readPermission: Int = getPermission(activity, READ_EXTERNAL_STORAGE)

        return if (writePermission != GRANTED || readPermission != GRANTED) {
            requestPermissions(activity)
            false
        } else {
            true
        }
    }

    private fun getPermission(activity: Activity, externalString: String): Int {
        return ActivityCompat.checkSelfPermission(
            activity,
            externalString
        )
    }

    private fun requestPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            PERMISSIONS_STORAGE,
            REQUEST_CODE_PERMISSIONS
        )
    }
}