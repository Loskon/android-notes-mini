package com.loskon.noteminimalism3.sqlite

import android.app.Application

/**
 * Инициализации базы данных и очистка корзины
 */

internal class App : Application() {

    override fun onCreate() {
        super.onCreate()
        DateBaseAdapter.initDateBase(this)
        DateBaseAdapter.deleteNotesByTime(this)
    }
}