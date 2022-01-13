package com.loskon.noteminimalism3.files

import java.io.File

/**
 * Фильтр для файлов с расширением .db
 */

object BackupFilter {
    fun getListDateBaseFiles(folder: File?): Array<File>? {
        return folder?.listFiles { _, name: String ->
            name.lowercase().endsWith(".db")
        }
    }
}
