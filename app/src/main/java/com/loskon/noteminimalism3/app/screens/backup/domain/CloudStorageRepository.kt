package com.loskon.noteminimalism3.app.screens.backup.domain

interface CloudStorageRepository {

    suspend fun uploadDatabaseFile(): Boolean

    suspend fun databaseFileExists(): Boolean

    suspend fun downloadDatabaseFile(): Boolean

    suspend fun deleteDatabaseFile()
}