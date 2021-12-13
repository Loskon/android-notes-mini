package com.loskon.noteminimalism3.files

import java.io.File

/**
 * Проверка существования папки
 */

class CheckCreatedFile {
    companion object {

        fun hasCreated(folder: File): Boolean {
            var hasFileCreated = true

            if (!folder.exists()) {
                hasFileCreated = folder.mkdirs()
            }

            return hasFileCreated
        }
    }
}