package com.loskon.noteminimalism3.sqlite

import android.app.Application

/**
 * Инициализации базы данных
 */

internal class AppSqlite : Application() {

    override fun onCreate() {
        super.onCreate()
        //TypefaceUtil.overrideFont(this, "fonts/oswald_light.ttf", "fonts/oswald_light.ttf")
        DateBaseAdapter.initDateBase(this)
        DateBaseAdapter.deleteNotesByTime(this)
    }
}