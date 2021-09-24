package com.loskon.noteminimalism3.sqlite

import android.app.Application

/**
 * Инициализации базы данных
 */

internal class AppSqlite: Application() {

    override fun onCreate() {
        super.onCreate()
        DateBaseAdapter.initDateBase(this)
    }
}