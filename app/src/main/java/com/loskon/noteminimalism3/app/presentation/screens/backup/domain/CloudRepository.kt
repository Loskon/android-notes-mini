package com.loskon.noteminimalism3.app.presentation.screens.backup.domain

interface CloudRepository {

    suspend fun uploadFile(): Boolean

    suspend fun fileExists(): Boolean

    suspend fun downloadFile(): Boolean
}