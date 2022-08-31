package com.loskon.noteminimalism3.app.presentation.screens.note.presentation

import android.content.Context
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.files.BackupFileHelper
import com.loskon.noteminimalism3.files.BackupPath
import com.loskon.noteminimalism3.utils.DateUtil
import com.loskon.noteminimalism3.utils.StringUtil
import java.io.File
import java.io.FileWriter

class SaveTextFileNew(
    private val context: Context,
    private val saveSuccess: (Int)-> Unit,
    private val saveFailure: (Int)-> Unit
) {

    fun creationFolderTextFiles(text: String) {
        val folderBackup = BackupPath.getBackupFolder(context)
        val folderTextFiles = File(folderBackup, context.getString(R.string.folder_text_files_name))
        val hasCreatedFolder = BackupFileHelper.hasCreated(folderTextFiles)

        if (hasCreatedFolder) {
            performCreationTextFile(folderTextFiles, text)
        } else {
            saveFailure(R.string.sb_bp_unable_created_folder)
        }
    }

    private fun performCreationTextFile(file: File, text: String) {
        try {
            val fileText = File(file, getTitleTextFile(text))
            val writer = FileWriter(fileText)

            writer.append(text)
            writer.flush()
            writer.close()

            saveSuccess(R.string.sb_note_create_text_files_completed)
        } catch (exception: Exception) {
            saveFailure(R.string.sb_note_create_text_file_failed)
        }
    }

    private fun getTitleTextFile(text: String): String {
        val primaryTitle = text.substring(0, 14.coerceAtMost(text.length))
        val secondTitle = DateUtil.getTimeNowWithBrackets()
        val commonTitle = "$primaryTitle$secondTitle".trim()
        val finalTitle = StringUtil.replaceForbiddenCharacters(commonTitle)

        return "$finalTitle.txt"
    }
}