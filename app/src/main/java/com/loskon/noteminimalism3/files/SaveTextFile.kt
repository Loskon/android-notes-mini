package com.loskon.noteminimalism3.files

import android.content.Context
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.ui.fragments.NoteFragment
import com.loskon.noteminimalism3.ui.snackbars.WarningSnackbar
import com.loskon.noteminimalism3.utils.DateUtil
import com.loskon.noteminimalism3.utils.StringUtil
import java.io.File
import java.io.FileWriter

/**
 * Создание и сохранение текстового файла
 */

class SaveTextFile(
    private val context: Context,
    private val fragment: NoteFragment
) {

    fun creationFolderTextFiles(text: String) {
        val folderBackup: File = BackupPath.getBackupFolder(context)
        val folderTextFiles = File(folderBackup, context.getString(R.string.folder_text_files_name))

        val hasCreatedFolder: Boolean = CheckCreatedFile.hasCreated(folderTextFiles)

        if (hasCreatedFolder) {
            performCreationTextFile(folderTextFiles, text)
        } else {
            showSnackbar(WarningSnackbar.MSG_UNABLE_CREATE_FOLDER)
        }
    }

    private fun performCreationTextFile(file: File, text: String) {
        try {
            val fileText = File(file, getTitleTextFile(text))
            val writer = FileWriter(fileText)

            writer.append(text)
            writer.flush()
            writer.close()

            showSnackbar(WarningSnackbar.MSG_SAVE_TXT_COMPLETED)
        } catch (exception: Exception) {
            showSnackbar(WarningSnackbar.MSG_SAVE_TXT_FAILED)
        }
    }

    private fun getTitleTextFile(text: String): String {
        val primaryTitle: String = text.substring(0, 14.coerceAtMost(text.length))
        val secondTitle: String = DateUtil.getTimeNowWithBrackets()
        val commonTitle = "$primaryTitle$secondTitle".trim()
        val finalTitle = StringUtil.replaceForbiddenCharacters(commonTitle)
        return "$finalTitle.txt"
    }

    private fun showSnackbar(messageType: String) = fragment.showSnackbar(messageType)
}