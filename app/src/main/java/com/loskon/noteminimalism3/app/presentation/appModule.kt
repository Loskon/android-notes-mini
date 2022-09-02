package com.loskon.noteminimalism3.app.presentation

import com.loskon.noteminimalism3.app.presentation.screens.note.data.LocaleFileSource
import com.loskon.noteminimalism3.sqlite.DatabaseAdapterNew
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single { DatabaseAdapterNew(androidApplication()) }
    single { LocaleFileSource() }
}