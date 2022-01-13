package com.loskon.noteminimalism3.files

import java.io.File

/**
 * Проверка существования папки и ее создание
 */

object CheckCreatedFile {
    fun hasCreated(folder: File): Boolean {
        var hasFileCreated = true

        if (!folder.exists()) {
            hasFileCreated = folder.mkdirs()
        }

        return hasFileCreated
    }
}
