package com.loskon.noteminimalism3.helper;

import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.Comparator;

public class RestoreHelper {

    // Сортировка по дате файла
    public static class SortFileDate implements Comparator<File> {
        @Override
        public int compare(File f1, File f2) {
            return Long.compare(f2.lastModified(), f1.lastModified());
        }
    }

    // Получени списка файлов
    public static File[] getListFile(File folder) {
        return folder.listFiles();
    }
}
