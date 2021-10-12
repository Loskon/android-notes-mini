package com.loskon.noteminimalism3.files

import android.content.Context
import com.loskon.noteminimalism3.backup.second.BackupPath
import com.loskon.noteminimalism3.backup.update.FolderUtil
import com.loskon.noteminimalism3.ui.fragments.update.NoteFragmentUpdate
import com.loskon.noteminimalism3.ui.snackbars.update.SnackbarApp
import com.loskon.noteminimalism3.utils.StringUtil
import java.io.File
import java.io.FileWriter

/**
 * Создание и сохранение текстового файла заметки
 */

class SaveTextFileUpdate(
    private val context: Context,
    private val fragment: NoteFragmentUpdate
) {

    fun creationFolderTextFiles(text: String) {
        val folderHome: File = BackupPath.getFolderBackup(context)
        val folderTextFiles = File(folderHome, "Text Files")

        val hasCreatedFolder: Boolean = FolderUtil.checkCreatedTextFilesFolder(folderTextFiles)

        if (hasCreatedFolder) {
            performCreationTextFile(folderTextFiles, text)
        } else {
            showSnackbar(SnackbarApp.MSG_UNABLE_CREATE_FILE)
        }
    }

    private fun performCreationTextFile(file: File, text: String) {
        try {
            val fileText = File(file, getTitleTextFile(text))
            val writer = FileWriter(fileText)

            writer.append(text)
            writer.flush()
            writer.close()

            showSnackbar(SnackbarApp.MSG_SAVE_TXT_COMPLETED)
        } catch (exception: Exception) {
            showSnackbar(SnackbarApp.MSG_SAVE_TXT_FAILED)
        }
    }

    private fun getTitleTextFile(text: String): String {
        var title: String = text
        title = title.substring(0, 20.coerceAtMost(title.length))
        title = StringUtil.replaceForbiddenCharacters(title)
        return title.trim()
    }

    private fun showSnackbar(typeMessage: String) {
        fragment.showSnackbar(typeMessage)
    }
}