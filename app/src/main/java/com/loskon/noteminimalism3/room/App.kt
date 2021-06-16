package com.loskon.noteminimalism3.room

import android.app.Application

/**
 * Инициализации репозитория.
 */

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        AppRepository.initRepository(this)
    }
}