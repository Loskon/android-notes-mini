package com.loskon.noteminimalism3.app.screens.rootsettings

import com.loskon.noteminimalism3.app.screens.rootsettings.data.RootSettingsRepositoryImpl
import com.loskon.noteminimalism3.app.screens.rootsettings.domain.RootSettingsInteractor
import com.loskon.noteminimalism3.app.screens.rootsettings.domain.RootSettingsRepository
import com.loskon.noteminimalism3.app.screens.rootsettings.presentation.RootSettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val rootSettingsModule = module {
    single<RootSettingsRepository> { RootSettingsRepositoryImpl(get()) }
    factory { RootSettingsInteractor(get()) }
    viewModel { RootSettingsViewModel(get()) }
}