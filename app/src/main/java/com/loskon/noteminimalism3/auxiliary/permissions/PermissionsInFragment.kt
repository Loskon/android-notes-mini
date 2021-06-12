package com.loskon.noteminimalism3.auxiliary.permissions

import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.loskon.noteminimalism3.auxiliary.permissions.PermissionsValues.Companion.PERMISSIONS_STORAGE

/**
 *
 */

class PermissionsInFragment(fragment: Fragment, permissionsInterface: PermissionsInterface) {

    val requestMultiplePermissions = fragment.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted: Boolean =
            (permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true &&
                    permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true)
        permissionsInterface.onRequestPermissionsStorageResult(isGranted)
    }

    fun startPermissionsRequest() {
        requestMultiplePermissions.launch(PERMISSIONS_STORAGE)
    }
}