package com.loskon.noteminimalism3.sqlite

import android.app.Application
import com.loskon.noteminimalism3.sharedpref.AppPreference

/**
 * Инициализации базы данных
 */

@Suppress("unused")
internal class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val rangeInDays: Int = AppPreference.getRetentionRange(this)
        DataBaseAdapter.initDataBase(this, rangeInDays)
        //DataBaseAdapter.deleteNotesByTime(this)
    }
}