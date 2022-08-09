package com.loskon.noteminimalism3.app

import android.app.Application
import com.loskon.noteminimalism3.BuildConfig
import com.loskon.noteminimalism3.app.presentation.screens.backup.backupModule
import com.loskon.noteminimalism3.app.presentation.screens.backupfilelist.backupFileListModule
import com.loskon.noteminimalism3.app.presentation.screens.notelist.noteListModule
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.utils.ColorUtil
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initializeKoin(this)
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
        ColorUtil.toggleDarkMode(AppPreference.hasDarkMode(this))
    }

    private fun initializeKoin(application: Application) {
        startKoin {
            androidContext(application)
            modules(
                listOf(
                    noteListModule, backupModule, backupFileListModule
                )
            )
        }
    }
}