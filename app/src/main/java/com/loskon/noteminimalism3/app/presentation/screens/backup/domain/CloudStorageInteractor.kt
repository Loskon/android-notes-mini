package com.loskon.noteminimalism3.app.presentation.screens.backup.domain

class CloudStorageInteractor(
    private val cloudStorageRepository: CloudStorageRepository
) {

    suspend fun uploadFile() = cloudStorageRepository.uploadDatabaseFile()

    suspend fun fileExists() = cloudStorageRepository.databaseFileExists()

    suspend fun downloadFile() = cloudStorageRepository.downloadDatabaseFile()
}