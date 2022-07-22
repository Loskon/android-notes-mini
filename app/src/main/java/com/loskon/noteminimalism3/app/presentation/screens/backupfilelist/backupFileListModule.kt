package com.loskon.noteminimalism3.app.presentation.screens.backupfilelist

import com.loskon.noteminimalism3.app.presentation.screens.backupfilelist.data.BackupFileListRepositoryImpl
import com.loskon.noteminimalism3.app.presentation.screens.backupfilelist.domain.BackupFileListInteractor
import com.loskon.noteminimalism3.app.presentation.screens.backupfilelist.domain.BackupFileListRepository
import com.loskon.noteminimalism3.app.presentation.screens.backupfilelist.presentation.BackupFileListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val backupFileListModule = module {
    single<BackupFileListRepository> { BackupFileListRepositoryImpl(androidApplication()) }
    factory { BackupFileListInteractor(get()) }
    viewModel { BackupFileListViewModel(get()) }
}