package com.loskon.noteminimalism3.app.screens.rootsettings.domain

class RootSettingsInteractor(
    private val rootSettingsRepository: RootSettingsRepository
) {

    fun deleteExtraFiles(backupFolderPath: String, maxFilesCount: Int) {
        rootSettingsRepository.deleteExtraFiles(backupFolderPath, maxFilesCount)
    }
}