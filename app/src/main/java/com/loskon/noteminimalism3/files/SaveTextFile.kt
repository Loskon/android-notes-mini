package com.loskon.noteminimalism3.files

import android.content.Context
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.backup.BackupPathManager
import com.loskon.noteminimalism3.ui.fragments.NoteFragment
import com.loskon.noteminimalism3.ui.snackbars.SnackbarManager
import com.loskon.noteminimalism3.utils.DateManager
import com.loskon.noteminimalism3.utils.StringUtil
import java.io.File
import java.io.FileWriter

/**
 * Создание и сохранение текстового файла заметки
 */

class SaveTextFile(
    private val context: Context,
    private val fragment: NoteFragment
) {

    fun creationFolderTextFiles(text: String) {
        val folderHome: File = BackupPathManager.getBackupFolder(context)
        val folderTextFiles =
            File(folderHome, context.getString(R.string.app_name_folder_text_files))

        val hasCreatedFolder: Boolean = CheckCreatedFiles.checkCreatedFolder(folderTextFiles)

        if (hasCreatedFolder) {
            performCreationTextFile(folderTextFiles, text)
        } else {
            showSnackbar(SnackbarManager.MSG_UNABLE_CREATE_TEXT_FILE)
        }
    }

    private fun performCreationTextFile(file: File, text: String) {
        try {
            val fileText = File(file, getTitleTextFile(text))
            val writer = FileWriter(fileText)

            writer.append(text)
            writer.flush()
            writer.close()

            showSnackbar(SnackbarManager.MSG_SAVE_TXT_COMPLETED)
        } catch (exception: Exception) {
            showSnackbar(SnackbarManager.MSG_SAVE_TXT_FAILED)
        }
    }

    private fun getTitleTextFile(text: String): String {
        var title: String = text
        title = title.substring(0, 14.coerceAtMost(title.length)) + " " + DateManager.getTimeNow()
        title = StringUtil.replaceForbiddenCharacters(title)
        return title.trim()
    }

    private fun showSnackbar(typeMessage: String) {
        fragment.showSnackbar(typeMessage)
    }
}