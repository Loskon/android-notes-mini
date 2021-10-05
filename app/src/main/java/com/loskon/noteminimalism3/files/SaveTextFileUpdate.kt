package com.loskon.noteminimalism3.files

import android.content.Context
import com.loskon.noteminimalism3.backup.second.BackupPath
import com.loskon.noteminimalism3.ui.fragments.update.NoteFragmentUpdate
import com.loskon.noteminimalism3.ui.snackbars.update.SnackbarApp.Companion.MSG_SAVE_TXT_COMPLETED
import com.loskon.noteminimalism3.ui.snackbars.update.SnackbarApp.Companion.MSG_SAVE_TXT_FAILED
import com.loskon.noteminimalism3.ui.snackbars.update.SnackbarApp.Companion.MSG_UNABLE_CREATE_FILE
import com.loskon.noteminimalism3.utils.createFolder
import com.loskon.noteminimalism3.utils.replaceForbiddenCharacters
import java.io.File
import java.io.FileWriter

/**
 * Создание и сохранение текстового файла заметки
 */

class SaveTextFileUpdate(
    private val context: Context,
    private val noteFragment: NoteFragmentUpdate
) {

    fun createTextFile(text: String) {
        val folderHome = BackupPath.getFolder(context)
        val folderTextFiles = File(folderHome, "Text Files")

        val hasCreatedFolder = folderTextFiles.createFolder()

        if (hasCreatedFolder) {
            mainMethodCreateTextFile(folderTextFiles, text)
        } else {
            showSnackbar(MSG_UNABLE_CREATE_FILE, false)
        }
    }

    private fun mainMethodCreateTextFile(file: File, text: String) {
        try {
            val fileText = File(file, getTitleFileName(text))
            val writer = FileWriter(fileText)

            writer.append(text)
            writer.flush()
            writer.close()

            showSnackbar(MSG_SAVE_TXT_COMPLETED, true)
        } catch (exception: Exception) {
            exception.printStackTrace()
            showSnackbar(MSG_SAVE_TXT_FAILED, false)
        }
    }

    private fun getTitleFileName(text: String): String {
        var name: String = text

        name = name.substring(0, 20.coerceAtMost(name.length))

        name = name.replaceForbiddenCharacters()

        return name.trim()
    }

    private fun showSnackbar(typeMessage: String, isSuccess: Boolean) {
        noteFragment.showSnackbar(typeMessage, isSuccess)
    }
}