package com.loskon.noteminimalism3.app.presentation.screens.notelist

import com.loskon.noteminimalism3.app.presentation.screens.notelist.data.NoteListRepositoryImpl
import com.loskon.noteminimalism3.app.presentation.screens.notelist.domain.NoteListInteractor
import com.loskon.noteminimalism3.app.presentation.screens.notelist.domain.NoteListRepository
import com.loskon.noteminimalism3.app.presentation.screens.notelist.presentation.NoteListViewModel
import com.loskon.noteminimalism3.sqlite.DatabaseAdapterNew
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val noteListModule = module {
    single { DatabaseAdapterNew(androidApplication()) }

    single<NoteListRepository> { NoteListRepositoryImpl(get()) }
    factory { NoteListInteractor(get()) }
    viewModel { NoteListViewModel(get()) }
}