package com.loskon.noteminimalism3.backup.second;

import android.app.Activity;

import java.io.File;

/**
 * Создание папок
 */

public class AppFolder {

    public static boolean createBackupFolder(Activity activity) {
        // Проверка и создание папки для бэкапа
        File folder = BackupPath.getFolder(activity);

        boolean isFolderCreated = true;

        if (!folder.exists()) {
            isFolderCreated = folder.mkdirs();
        }

        return isFolderCreated;
    }

    public static boolean createTextFilesFolder(File file) {
        // Проверка и создание папки для текстового файла
        boolean isFolderCreated = true;

        if (!file.exists()) {
            isFolderCreated = file.mkdir();
        }

        return isFolderCreated;
    }
}
