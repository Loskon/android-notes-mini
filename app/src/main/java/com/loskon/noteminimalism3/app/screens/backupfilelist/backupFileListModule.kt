package com.loskon.noteminimalism3.app.screens.backupfilelist

import com.loskon.noteminimalism3.app.screens.backupfilelist.data.BackupFileListRepositoryImpl
import com.loskon.noteminimalism3.app.screens.backupfilelist.domain.BackupFileListInteractor
import com.loskon.noteminimalism3.app.screens.backupfilelist.domain.BackupFileListRepository
import com.loskon.noteminimalism3.app.screens.backupfilelist.presentation.BackupFileListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val backupFileListModule = module {
    single<BackupFileListRepository> { BackupFileListRepositoryImpl(androidApplication()) }
    factory { BackupFileListInteractor(get()) }
    viewModel { BackupFileListViewModel(get()) }
}