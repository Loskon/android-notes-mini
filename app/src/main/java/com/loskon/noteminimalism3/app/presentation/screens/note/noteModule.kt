package com.loskon.noteminimalism3.app.presentation.screens.note

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val noteModule = module {
    single<NoteRepository> { NoteRepositoryImpl(get()) }
    factory { NoteInteractor(get()) }
    viewModel { NoteViewModel(get()) }
}