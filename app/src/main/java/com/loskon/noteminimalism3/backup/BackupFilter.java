package com.loskon.noteminimalism3.backup;

import java.io.File;

/**
 * Фильтр для получения файлов с расширением .db
 */

public class BackupFilter {
    public static File[] getListFile(File folder) {
        return folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".db"));
    }
}
