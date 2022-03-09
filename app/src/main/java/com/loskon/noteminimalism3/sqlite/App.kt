package com.loskon.noteminimalism3.sqlite

import android.app.Application

/**
 * Инициализации базы данных
 */

@Suppress("unused")
internal class App : Application() {

    override fun onCreate() {
        super.onCreate()
        DataBaseAdapter.initDataBase(this)
        DataBaseAdapter.deleteNotesByTime(this)
    }
}