package com.loskon.noteminimalism3.app

import android.app.Application
import com.loskon.noteminimalism3.BuildConfig
import com.loskon.noteminimalism3.app.screens.backup.backupModule
import com.loskon.noteminimalism3.app.screens.backupfilelist.backupFileListModule
import com.loskon.noteminimalism3.app.screens.createbackup.createBackupModule
import com.loskon.noteminimalism3.app.screens.note.noteModule
import com.loskon.noteminimalism3.app.screens.notelist.noteListModule
import com.loskon.noteminimalism3.app.screens.notetrash.noteTrashModule
import com.loskon.noteminimalism3.app.screens.rootsettings.rootSettingsModule
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
                    appModule, noteListModule,
                    noteModule, backupModule,
                    backupFileListModule, createBackupModule,
                    rootSettingsModule, noteTrashModule
                )
            )
        }
    }
}