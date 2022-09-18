package com.loskon.noteminimalism3.base.contracts

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class StorageContract(fragment: Fragment) {

    private val read = Manifest.permission.READ_EXTERNAL_STORAGE
    private val write = Manifest.permission.WRITE_EXTERNAL_STORAGE

    private var handleGranted: ((Boolean) -> Unit)? = null

    private val resultLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val read = permissions[Manifest.permission.READ_EXTERNAL_STORAGE]
        val write = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE]

        val granted = (read == true && write == true)
        handleGranted?.invoke(granted)
    }

    fun storageAccess(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            true
        } else {
            val checkedReadPermission = ContextCompat.checkSelfPermission(context, read)
            val checkedWritePermission = ContextCompat.checkSelfPermission(context, write)

            (checkedReadPermission == PackageManager.PERMISSION_GRANTED &&
                checkedWritePermission == PackageManager.PERMISSION_GRANTED)
        }
    }

    fun launchAccessRequest() {
        val permissions = arrayOf(read, write)
        resultLauncher.launch(permissions)
    }

    fun setHandleGrantedListener(handleGranted2: ((Boolean) -> Unit)?) {
        this.handleGranted = handleGranted2
    }
}