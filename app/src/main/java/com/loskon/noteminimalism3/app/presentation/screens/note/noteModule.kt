package com.loskon.noteminimalism3.app.presentation.screens.note

import com.loskon.noteminimalism3.app.presentation.screens.note.data.NoteRepositoryImpl
import com.loskon.noteminimalism3.app.presentation.screens.note.domain.NoteInteractor
import com.loskon.noteminimalism3.app.presentation.screens.note.domain.NoteRepository
import com.loskon.noteminimalism3.app.presentation.screens.note.presentation.NoteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val noteModule = module {
    single<NoteRepository> { NoteRepositoryImpl(get()) }
    factory { NoteInteractor(get()) }
    viewModel { NoteViewModel(get()) }
}