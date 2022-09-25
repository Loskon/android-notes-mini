package com.loskon.noteminimalism3.app.screens.notetrash

import com.loskon.noteminimalism3.app.screens.notetrash.data.NoteTrashRepositoryImpl
import com.loskon.noteminimalism3.app.screens.notetrash.domain.NoteTrashInteractor
import com.loskon.noteminimalism3.app.screens.notetrash.domain.NoteTrashRepository
import com.loskon.noteminimalism3.app.screens.notetrash.presentation.NoteTrashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val noteTrashModule = module {
    single<NoteTrashRepository> { NoteTrashRepositoryImpl(get()) }
    factory { NoteTrashInteractor(get()) }
    viewModel { NoteTrashViewModel(get()) }
}