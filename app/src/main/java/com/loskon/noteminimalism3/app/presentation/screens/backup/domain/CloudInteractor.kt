package com.loskon.noteminimalism3.app.presentation.screens.backup.domain

class CloudInteractor(
    private val cloudRepository: CloudRepository
) {

    suspend fun uploadFile() = cloudRepository.uploadFile()

    suspend fun fileExists() = cloudRepository.fileExists()

    suspend fun downloadFile() = cloudRepository.downloadFile()
}