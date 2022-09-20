package com.loskon.noteminimalism3.app.screens.rootsettings.domain

interface RootSettingsRepository {
    fun deleteExtraFiles(backupFolderPath: String, maxFilesCount: Int)
}