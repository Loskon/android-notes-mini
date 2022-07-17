package com.loskon.noteminimalism3.app.presentation.screens.backup

import com.loskon.noteminimalism3.app.presentation.screens.backup.data.AuthRepositoryImpl
import com.loskon.noteminimalism3.app.presentation.screens.backup.data.CloudRepositoryImpl
import com.loskon.noteminimalism3.app.presentation.screens.backup.domain.AuthInteractor
import com.loskon.noteminimalism3.app.presentation.screens.backup.domain.AuthRepository
import com.loskon.noteminimalism3.app.presentation.screens.backup.domain.CloudInteractor
import com.loskon.noteminimalism3.app.presentation.screens.backup.domain.CloudRepository
import com.loskon.noteminimalism3.app.presentation.screens.backup.presentation.BackupViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val backupModule = module {
    single<AuthRepository> { AuthRepositoryImpl(androidApplication()) }
    single<CloudRepository> { CloudRepositoryImpl(androidApplication()) }
    factory { AuthInteractor(get()) }
    factory { CloudInteractor(get()) }
    viewModel { BackupViewModel(get(), get()) }
}