package com.loskon.noteminimalism3.requests.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.loskon.noteminimalism3.files.BackupPath
import com.loskon.noteminimalism3.ui.toast.WarningToast

/**
 * Регистрация, получение и обработка результатов контракта
 */

const val REQUEST_CODE_FOLDER_FOR_BACKUP = 451
const val REQUEST_CODE_BACKUP_FILE = 452

class ResultActivity(
    private val context: Context,
    private val fragment: Fragment,
    private val resultInterface: ResultActivityInterface
) {

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private var requestCode: Int = 0

    fun installingContracts() {
        resultLauncher = fragment.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val isGranted: Boolean = result.resultCode == Activity.RESULT_OK

            resultInterface.onRequestActivityResult(
                isGranted,
                requestCode,
                result.data?.data
            )
        }
    }

    //--- Android Old ------------------------------------------------------------------------------
    fun launchFolderSelect() {
        requestCode = REQUEST_CODE_FOLDER_FOR_BACKUP

        try {
            val intent: Intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
                addCategory(Intent.CATEGORY_DEFAULT)
                putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            }

            val intentChooser: Intent = Intent.createChooser(intent, "Choose a file")
            resultLauncher.launch(intentChooser)

        } catch (exception: Exception) {
            WarningToast.show(context, WarningToast.MSG_TOAST_FILE_MANAGER_NOT_FOUND)
        }
    }

    //--- Android 11 -------------------------------------------------------------------------------
    fun launchDateBaseFileSelect() {
        requestCode = REQUEST_CODE_BACKUP_FILE

        val backupFolderUri: Uri = Uri.parse(BackupPath.getPathBackupFolder(context))
        val mimetypes: Array<String> = arrayOf(
            "application/x-sqlite3",
            "application/vnd.sqlite3",
            "application/octet-stream"
        )

        try {
            val intent: Intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                setDataAndType(backupFolderUri, "resource/folder")
                addCategory(Intent.CATEGORY_OPENABLE)
                putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
                putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            }

            val intentChooser: Intent = Intent.createChooser(intent, "Choose a backup file")
            resultLauncher.launch(intentChooser)

        } catch (exception: Exception) {
            WarningToast.show(context, WarningToast.MSG_TOAST_FILE_MANAGER_NOT_FOUND)
        }
    }
}
