package com.loskon.noteminimalism3.app

import android.app.Application
import com.loskon.noteminimalism3.BuildConfig
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.sqlite.DataBaseAdapter
import timber.log.Timber

@Suppress("unused")
internal class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val rangeInDays: Int = AppPreference.getRetentionRange(this)
        DataBaseAdapter.initDataBase(this, rangeInDays)
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}