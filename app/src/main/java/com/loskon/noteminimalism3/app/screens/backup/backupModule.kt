package com.loskon.noteminimalism3.app.screens.backup

import com.loskon.noteminimalism3.app.screens.backup.data.CloudStorageRepositoryImpl
import com.loskon.noteminimalism3.app.screens.backup.data.GoogleOneTapSignInRepositoryImpl
import com.loskon.noteminimalism3.app.screens.backup.data.LocalFileRepositoryImpl
import com.loskon.noteminimalism3.app.screens.backup.domain.CloudStorageInteractor
import com.loskon.noteminimalism3.app.screens.backup.domain.CloudStorageRepository
import com.loskon.noteminimalism3.app.screens.backup.domain.GoogleOneTapSignInInteractor
import com.loskon.noteminimalism3.app.screens.backup.domain.GoogleOneTapSignInRepository
import com.loskon.noteminimalism3.app.screens.backup.domain.LocalFileInteractor
import com.loskon.noteminimalism3.app.screens.backup.domain.LocalFileRepository
import com.loskon.noteminimalism3.app.screens.backup.presentation.BackupViewModel
import com.loskon.noteminimalism3.utils.NetworkUtil
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val backupModule = module {
    single { NetworkUtil(androidApplication()) }
    single<GoogleOneTapSignInRepository> { GoogleOneTapSignInRepositoryImpl(androidApplication()) }
    single<CloudStorageRepository> { CloudStorageRepositoryImpl(androidApplication()) }
    single<LocalFileRepository> { LocalFileRepositoryImpl(get()) }
    factory { GoogleOneTapSignInInteractor(get()) }
    factory { CloudStorageInteractor(get()) }
    factory { LocalFileInteractor(get()) }
    viewModel { BackupViewModel(get(), get(), get(), get()) }
}