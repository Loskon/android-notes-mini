package com.loskon.noteminimalism3.files

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.other.RequestCode
import com.loskon.noteminimalism3.utils.showToast

/**
 *
 */

class Result {
    companion object {

        private var resultLauncher: ActivityResultLauncher<Intent>? = null

        private var request_code = 0

        fun installing(
            activity: ComponentActivity?,
            activityResultInterface: ActivityResultInterface?
        ) {
            resultLauncher = activity?.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                val isGranted: Boolean = result.resultCode == Activity.RESULT_OK

                activityResultInterface?.onRequestActivityResult(
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
                context.showToast(R.string.sb_settings_file_manager_not_found)
            }

        }
    }
}