package com.loskon.noteminimalism3.app.presentation.screens.note.domain

interface LocaleFileRepository {
    fun performBackup(dbFilePath: String, backupFilePath: String)
    fun performRestore(backupFilePath: String, dbFilePath: String)
}