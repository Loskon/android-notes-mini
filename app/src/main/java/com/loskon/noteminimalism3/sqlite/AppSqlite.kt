package com.loskon.noteminimalism3.sqlite

import android.app.Application
import com.loskon.noteminimalism3.auxiliary.sharedpref.AppPref

/**
 * Инициализации базы данных
 */

internal class AppSqlite : Application() {

    override fun onCreate() {
        super.onCreate()
        DateBaseAdapter.initDateBase(this)
        val rangeInDays: Int = AppPref.getRangeInDays(this)
        DateBaseAdapter.deleteNotesByTime(rangeInDays)
    }
}