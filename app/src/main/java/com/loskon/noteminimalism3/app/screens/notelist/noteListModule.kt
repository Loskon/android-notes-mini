package com.loskon.noteminimalism3.app.screens.notelist

import com.loskon.noteminimalism3.app.screens.notelist.data.NoteListRepositoryImpl
import com.loskon.noteminimalism3.app.screens.notelist.domain.NoteListInteractor
import com.loskon.noteminimalism3.app.screens.notelist.domain.NoteListRepository
import com.loskon.noteminimalism3.app.screens.notelist.presentation.NoteListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val noteListModule = module {
    single<NoteListRepository> { NoteListRepositoryImpl(get()) }
    factory { NoteListInteractor(get()) }
    viewModel { NoteListViewModel(get()) }
}