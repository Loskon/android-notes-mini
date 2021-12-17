package com.loskon.noteminimalism3.sqlite

import android.app.Application

/**
 * Инициализации базы данных
 */

internal class App : Application() {

    override fun onCreate() {
        super.onCreate()
        DataBaseAdapter.initDateBase(this)
        DataBaseAdapter.deleteNotesByTime(this)
    }
}