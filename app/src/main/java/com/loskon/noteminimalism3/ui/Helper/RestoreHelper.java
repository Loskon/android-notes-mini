package com.loskon.noteminimalism3.ui.Helper;

import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.Comparator;

public class RestoreHelper {

    //sorts based on the files name
    public static class SortFileDate implements Comparator<File> {
        @Override
        public int compare(File f1, File f2) {
            return Long.compare(f2.lastModified(), f1.lastModified());
        }
    }

    public static void purgeLogFiles(File[] logFiles){
        long oldestDate = Long.MAX_VALUE;
        File oldestFile = null;
        if (logFiles != null && logFiles.length >11){
            //delete oldest files after there is more than 500 log files
            for(File file: logFiles){
                if(file.lastModified() < oldestDate){
                    oldestDate = file.lastModified();
                    oldestFile = file;
                }
            }

            if(oldestFile != null){
                SQLiteDatabase.deleteDatabase(new File(oldestFile.getPath()));
                //oldestFile.deleteOnExit();
            }
        }
    }

    public static File[] getListFile(File folder) {
        return folder.listFiles();
    }
}
