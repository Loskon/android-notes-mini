package com.loskon.noteminimalism3.request.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.loskon.noteminimalism3.backup.BackupPathManager
import com.loskon.noteminimalism3.request.RequestCode
import com.loskon.noteminimalism3.toast.ToastManager




/**
 * Различные запросы
 */

class ResultActivity {
    companion object {

        private var resultLauncher: ActivityResultLauncher<Intent>? = null

        private var requestCode = 0

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
                    requestCode,
                    result.data?.data
                )
            }
        }

        fun installing(
            fragment: Fragment?,
            resultActivityInterface: ResultActivityInterface?
        ) {
            resultLauncher = fragment?.registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                val isGranted: Boolean = result.resultCode == Activity.RESULT_OK

                resultActivityInterface?.onRequestActivityResult(
                    isGranted,
                    requestCode,
                    result.data?.data
                )
            }
        }

        fun launcherSelectingFolder(context: Context) {
            requestCode = RequestCode.REQUEST_CODE_GET_FOLDER

            try {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                    addCategory(Intent.CATEGORY_DEFAULT)
                    putExtra(Intent.EXTRA_LOCAL_ONLY, true)
                }

                val intentChooser = Intent.createChooser(intent, "Choose a file")
                resultLauncher?.launch(intentChooser)

            } catch (exception: Exception) {
                ToastManager.show(context, ToastManager.MSG_TOAST_FILE_MANAGER_NOT_FOUND)
            }
        }

        fun launcherSelectingDateBaseFile(context: Context) {
            requestCode = RequestCode.REQUEST_CODE_GET_BACKUP_FILE
            val backupFolderUri: Uri = Uri.parse(BackupPathManager.getBackupFolder(context).path)
            val mimetypes: Array<String> = arrayOf(
                "application/x-sqlite3",
                "application/vnd.sqlite3",
                "application/octet-stream"
            )

            try {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    setDataAndType(backupFolderUri, "resource/folder")
                    addCategory(Intent.CATEGORY_OPENABLE)
                    putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
                    putExtra(Intent.EXTRA_LOCAL_ONLY, true)
                }

                val intentChooser = Intent.createChooser(intent, "Choose a backup file")
                resultLauncher?.launch(intentChooser)

            } catch (exception: Exception) {
                ToastManager.show(context, ToastManager.MSG_TOAST_FILE_MANAGER_NOT_FOUND)
            }
        }
    }
}