package com.loskon.noteminimalism3.app.screens.createbackup

import com.loskon.noteminimalism3.app.screens.createbackup.data.CreateBackupRepositoryImpl
import com.loskon.noteminimalism3.app.screens.createbackup.domain.CreateBackupInteractor
import com.loskon.noteminimalism3.app.screens.createbackup.domain.CreateBackupRepository
import com.loskon.noteminimalism3.app.screens.createbackup.presentation.CreateBackupViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val createBackupModule = module {
    single<CreateBackupRepository> { CreateBackupRepositoryImpl(get()) }
    factory { CreateBackupInteractor(get()) }
    viewModel { CreateBackupViewModel(get()) }
}