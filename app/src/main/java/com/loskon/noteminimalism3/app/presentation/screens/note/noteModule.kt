package com.loskon.noteminimalism3.app.presentation.screens.note

import com.loskon.noteminimalism3.app.presentation.screens.note.data.AutoBackupRepositoryImpl
import com.loskon.noteminimalism3.app.presentation.screens.note.data.NoteRepositoryImpl
import com.loskon.noteminimalism3.app.presentation.screens.note.domain.AutoBackupInteractor
import com.loskon.noteminimalism3.app.presentation.screens.note.domain.AutoBackupRepository
import com.loskon.noteminimalism3.app.presentation.screens.note.domain.NoteInteractor
import com.loskon.noteminimalism3.app.presentation.screens.note.domain.NoteRepository
import com.loskon.noteminimalism3.app.presentation.screens.note.presentation.NoteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val noteModule = module {
    single<NoteRepository> { NoteRepositoryImpl(get()) }
    single<AutoBackupRepository> { AutoBackupRepositoryImpl(get()) }
    factory { NoteInteractor(get()) }
    factory { AutoBackupInteractor(get()) }
    viewModel { NoteViewModel(get(), get()) }
}