package com.loskon.noteminimalism3.app.presentation.screens.createbackup

import com.loskon.noteminimalism3.app.presentation.screens.createbackup.data.CreateBackupRepositoryImpl
import com.loskon.noteminimalism3.app.presentation.screens.createbackup.domain.CreateBackupInteractor
import com.loskon.noteminimalism3.app.presentation.screens.createbackup.domain.CreateBackupRepository
import com.loskon.noteminimalism3.app.presentation.screens.createbackup.presentation.CreateBackupViewModel
import com.loskon.noteminimalism3.app.presentation.screens.note.presentation.NoteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val createBackupModule = module {
    single<CreateBackupRepository> { CreateBackupRepositoryImpl(get()) }
    factory { CreateBackupInteractor(get()) }
    viewModel { CreateBackupViewModel(get()) }
}