package com.loskon.noteminimalism3.app

import android.app.Application
import com.loskon.noteminimalism3.BuildConfig
import com.loskon.noteminimalism3.app.presentation.screens.backup.backupModule
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.sqlite.DataBaseAdapter
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

@Suppress("unused")
internal class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val rangeInDays: Int = AppPreference.getRetentionRange(this)
        DataBaseAdapter.initDataBase(this, rangeInDays)
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        initializeKoin(this)
    }

    private fun initializeKoin(application: Application) {
        startKoin {
            androidContext(application)
            modules(
                listOf(
                    backupModule
                )
            )
        }
    }
}