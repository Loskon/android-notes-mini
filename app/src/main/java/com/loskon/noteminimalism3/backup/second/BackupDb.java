package com.loskon.noteminimalism3.backup.second;

import android.content.Context;

import com.loskon.noteminimalism3.ui.snackbars.MySnackbarBackup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.loskon.noteminimalism3.database.DbHelper.DATABASE_NAME;

/**
 * Создание файла бэкапа и его восстановление
 */

public class BackupDb {

    private final Context activity;

    private String typeMessage;
    private boolean isSuccess = false;

    public BackupDb(Context activity) {
        this.activity = activity;
    }

    public void backupDatabase(boolean isAutoBackup, String outFileName) {
        // Backup
        try {
            buildDatabaseFile(getDbPath(), outFileName);
            typeMessage = MySnackbarBackup.MSG_BACKUP_COMPLETED;
            isSuccess = true;
        } catch (Exception exception) {
            exception.printStackTrace();
            typeMessage = MySnackbarBackup.MSG_BACKUP_FAILED;
        }

        if (!isAutoBackup) showSnackbar();
    }

    public void restoreDatabase(String inFileName) {
        // Restore
        try {
            buildDatabaseFile(inFileName, getDbPath());
            typeMessage = MySnackbarBackup.MSG_RESTORE_COMPLETED;
            isSuccess = true;
        } catch (Exception exception) {
            exception.printStackTrace();
            typeMessage = MySnackbarBackup.MSG_RESTORE_FAILED;
        }

        showSnackbar();
    }

    // Путь к базе данных
    private String getDbPath() {
        return activity.getDatabasePath(DATABASE_NAME).toString();
    }

    private void buildDatabaseFile(String inFileName, String outFileName) throws IOException {
        // создать файл
        File dbFile = new File(inFileName);
        FileInputStream fis = new FileInputStream(dbFile);

        // Путь к внешней резервной копии
        OutputStream output = new FileOutputStream(outFileName);

        // Передача байтов из входного файла в выходной файл
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        // Закройте потоки
        output.flush();
        output.close();
        fis.close();
    }

    private void showSnackbar() {
        MySnackbarBackup.showSnackbar(activity, isSuccess, typeMessage);
    }
}
