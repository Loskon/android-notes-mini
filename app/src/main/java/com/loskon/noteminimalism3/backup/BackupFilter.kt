package com.loskon.noteminimalism3.backup

import java.io.File

/**
 * Фильтр для файлов с расширением .db
 */

class BackupFilter {

    companion object {

        fun getListDateBaseFile(folder: File?): Array<File>? {
            return folder?.listFiles { _, name: String ->
                name.lowercase().endsWith(".db")
            }
        }
    }
}