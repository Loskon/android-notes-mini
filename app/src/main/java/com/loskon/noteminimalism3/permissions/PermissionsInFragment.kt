package com.loskon.noteminimalism3.permissions

import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

/**
 *
 */

class PermissionsInFragment(fragment: Fragment, permissionsInterface: PermissionsInterface) {

    companion object {
        val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    private var isGranted: Boolean = false

    val requestMultiplePermissions =
        fragment.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val readPermissions = permissions[Manifest.permission.READ_EXTERNAL_STORAGE]
            val writePermissions = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE]

            isGranted = (readPermissions == true && writePermissions == true)

            permissionsInterface.onRequestPermissionsStorageResult(isGranted)
        }

    fun startPermissionsRequest() {
        requestMultiplePermissions.launch(PERMISSIONS_STORAGE)
    }

    val isGrantedPermissions: Boolean
        get() {
            return isGranted
        }
}