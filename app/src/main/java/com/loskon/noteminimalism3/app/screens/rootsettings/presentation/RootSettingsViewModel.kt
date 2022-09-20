package com.loskon.noteminimalism3.app.screens.rootsettings.presentation

import com.loskon.noteminimalism3.app.screens.rootsettings.domain.RootSettingsInteractor
import com.loskon.noteminimalism3.base.presentation.viewmodel.BaseViewModel

class RootSettingsViewModel(
    private val rootSettingsInteractor: RootSettingsInteractor
): BaseViewModel() {

    fun deleteExtraFiles(backupFolderPath: String, maxFilesCount: Int) {
        rootSettingsInteractor.deleteExtraFiles(backupFolderPath, maxFilesCount)
    }
}