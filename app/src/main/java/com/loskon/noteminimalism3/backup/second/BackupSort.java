package com.loskon.noteminimalism3.backup.second;

import java.io.File;
import java.util.Comparator;

/**
 * Сортировка файлов по времени создания
 */

public class BackupSort {

    // Сортировка по дате файла
    public static class SortFileDate implements Comparator<File> {
        @Override
        public int compare(File f1, File f2) {
            return Long.compare(f2.lastModified(), f1.lastModified());
        }
    }
}
