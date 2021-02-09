package com.loskon.noteminimalism3.backup;

import java.io.File;
import java.util.Comparator;

public class BackupSort {

    // Сортировка по дате файла
    public static class SortFileDate implements Comparator<File> {
        @Override
        public int compare(File f1, File f2) {
            return Long.compare(f2.lastModified(), f1.lastModified());
        }
    }

    // Получение списка файлов с расширением .db
    public static File[] getListFile(File folder) {
        return folder.listFiles((dir, name) -> name.endsWith(".db"));
    }
}
