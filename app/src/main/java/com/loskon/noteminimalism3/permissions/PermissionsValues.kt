package com.loskon.noteminimalism3.permissions

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager

/**
 *
 */

class PermissionsValues {
    companion object {
        val PERMISSIONS_STORAGE = arrayOf(
            READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE
        )

        const val GRANTED: Int = PackageManager.PERMISSION_GRANTED
        const val REQUEST_CODE_STORAGE: Int = 451
    }
}