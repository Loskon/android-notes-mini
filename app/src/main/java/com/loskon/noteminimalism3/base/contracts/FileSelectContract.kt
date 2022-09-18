package com.loskon.noteminimalism3.base.contracts

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

class FileSelectContract(fragment: Fragment) {

    private var handleResult: ((Boolean, Uri?) -> Unit)? = null
    private var handleErrorResult: (() -> Unit)? = null

    private val resultLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val granted = result.resultCode == Activity.RESULT_OK
        handleResult?.invoke(granted, result.data?.data)
    }

    fun launchFileSelect(folderPath: String) {
        val mimetypes = arrayOf(
            "application/x-sqlite3",
            "application/vnd.sqlite3",
            "application/octet-stream"
        )

        try {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.setDataAndType(Uri.parse(folderPath), "resource/folder")
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            val chooserIntent = Intent.createChooser(intent, "Choose a backup file")

            resultLauncher.launch(chooserIntent)
        } catch (exception: Exception) {
            handleErrorResult?.invoke()
        }
    }

    fun setHandleResultListener(handleResult: ((Boolean, Uri?) -> Unit)?) {
        this.handleResult = handleResult
    }

    fun setHandleErrorResultListener(handleErrorResult: (() -> Unit)?) {
        this.handleErrorResult = handleErrorResult
    }
}