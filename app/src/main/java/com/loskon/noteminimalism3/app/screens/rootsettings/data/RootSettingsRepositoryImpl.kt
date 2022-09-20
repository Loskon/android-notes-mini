package com.loskon.noteminimalism3.app.screens.rootsettings.data

import com.loskon.noteminimalism3.app.screens.note.data.LocaleFileSource
import com.loskon.noteminimalism3.app.screens.rootsettings.domain.RootSettingsRepository
import java.io.File

class RootSettingsRepositoryImpl(
    private val localeFileSource: LocaleFileSource
) : RootSettingsRepository {

    override fun deleteExtraFiles(backupFolderPath: String, maxFilesCount: Int) {
        localeFileSource.deleteExtraFiles(File(backupFolderPath), maxFilesCount)
    }
}