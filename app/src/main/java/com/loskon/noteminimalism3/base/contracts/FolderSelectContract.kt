package com.loskon.noteminimalism3.base.contracts

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import timber.log.Timber

class FolderSelectContract(fragment: Fragment) {

    private var handleResult: ((Boolean, Uri?) -> Unit)? = null
    private var handleErrorResult: (() -> Unit)? = null

    private val resultLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val granted = result.resultCode == Activity.RESULT_OK
        handleResult?.invoke(granted, result.data?.data)
    }

    fun launchFolderSelect() {
        try {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            val intentChooser = Intent.createChooser(intent, "Choose a file")

            resultLauncher.launch(intentChooser)
        } catch (exception: Exception) {
            Timber.e(exception)
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