package com.loskon.noteminimalism3.app.presentation.screens.backup

import com.loskon.noteminimalism3.app.presentation.screens.backup.data.CloudStorageRepositoryImpl
import com.loskon.noteminimalism3.app.presentation.screens.backup.data.GoogleOneTapSignInRepositoryImpl
import com.loskon.noteminimalism3.app.presentation.screens.backup.domain.CloudStorageInteractor
import com.loskon.noteminimalism3.app.presentation.screens.backup.domain.CloudStorageRepository
import com.loskon.noteminimalism3.app.presentation.screens.backup.domain.GoogleOneTapSignInInteractor
import com.loskon.noteminimalism3.app.presentation.screens.backup.domain.GoogleOneTapSignInRepository
import com.loskon.noteminimalism3.app.presentation.screens.backup.presentation.BackupViewModel
import com.loskon.noteminimalism3.utils.NetworkUtil
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val backupModule = module {
    single { NetworkUtil(androidApplication()) }
    single<GoogleOneTapSignInRepository> { GoogleOneTapSignInRepositoryImpl(androidApplication()) }
    single<CloudStorageRepository> { CloudStorageRepositoryImpl(androidApplication()) }
    factory { GoogleOneTapSignInInteractor(get()) }
    factory { CloudStorageInteractor(get()) }
    viewModel { BackupViewModel(get(), get(), get()) }
}