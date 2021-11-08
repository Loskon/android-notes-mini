package com.loskon.noteminimalism3.request.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.loskon.noteminimalism3.request.RequestCode
import com.loskon.noteminimalism3.toast.ToastManager

/**
 * Различные запросы
 */

class ResultActivity {
    companion object {

        private var resultLauncher: ActivityResultLauncher<Intent>? = null

        private var request_code = 0

        fun installing(
            activity: ComponentActivity?,
            resultActivityInterface: ResultActivityInterface?
        ) {
            resultLauncher = activity?.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                val isGranted: Boolean = result.resultCode == Activity.RESULT_OK

                resultActivityInterface?.onRequestActivityResult(
                    isGranted,
                    request_code,
                    result.data?.data
                )
            }
        }

        fun launcherSelectingFolder(context: Context) {
            try {
                request_code = RequestCode.REQUEST_CODE_READ

                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)

                val intentChooser = Intent.createChooser(intent, "Choose a file")
                resultLauncher?.launch(intentChooser)

            } catch (exception: Exception) {
                ToastManager.show(context, ToastManager.MSG_TOAST_FILE_MANAGER_NOT_FOUND)
            }

        }
    }
}