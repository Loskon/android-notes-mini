package com.loskon.noteminimalism3.db.backup;

import android.app.Activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import static com.loskon.noteminimalism3.db.DbHelper.DATABASE_NAME;

public class BackupAndRestoreDatabase {

    private  static String typeMessage;
    private  static boolean isSuccess;

    public static void backupDatabase(Activity activity, String outFileName) {
        // Путь к базе данных
        final String inFileName = activity.getDatabasePath(DATABASE_NAME).toString();

        try {
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

            typeMessage = SnackbarBackup.MSG_BACKUP_COMPLETED;
            isSuccess = true;

        } catch (Exception e) {
            e.printStackTrace();

            typeMessage = SnackbarBackup.MSG_BACKUP_NO_COMPLETED;
            isSuccess = true;
        }

        SnackbarBackup.showSnackbar(activity, isSuccess, typeMessage);
    }

    public static void restoreDatabase(Activity activity, String inFileName) {

        String outFileName = activity.getDatabasePath(DATABASE_NAME).toString();

        try {

            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();

            typeMessage = SnackbarBackup.MSG_RESTORE_COMPLETED;
            isSuccess = true;

        } catch (Exception e) {
            e.printStackTrace();

            typeMessage = SnackbarBackup.MSG_RESTORE_NO_COMPLETED;
            isSuccess = false;
        }

        SnackbarBackup.showSnackbar(activity, isSuccess, typeMessage);
    }
}
