package com.loskon.noteminimalism3.app.screens.backupfilelist.presentation

import com.loskon.noteminimalism3.app.screens.backupfilelist.domain.BackupFileListInteractor
import com.loskon.noteminimalism3.base.presentation.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import java.io.File

class BackupFileListViewModel(
    private val backupFileListInteractor: BackupFileListInteractor
) : BaseViewModel() {

    private val backupFileList = MutableStateFlow<List<File>?>(null)
    val getBackupFileList get() = backupFileList.asStateFlow()

    fun getBackupFileList() {
        launchErrorJob {
            backupFileListInteractor.getBackupListFilesAsFlow().collectLatest { backupFileList.emit(it) }
        }
    }

    fun deleteFile(file: File) {
        launchErrorJob {
            backupFileListInteractor.deleteFile(file)
        }
    }

    fun deleteAllFiles(files: List<File>) {

    }
}