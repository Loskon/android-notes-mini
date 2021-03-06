package com.loskon.noteminimalism3.backup.second;

import java.io.File;

/**
 * Помощник для BackupActivity
 */

public class BackupHelper {

    // Получение списка файлов с расширением .db
    public static File[] getListFile(File folder) {
        return folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".db"));
    }

}
