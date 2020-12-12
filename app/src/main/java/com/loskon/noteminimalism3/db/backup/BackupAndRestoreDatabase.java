package com.loskon.noteminimalism3.db.backup;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;

import static com.loskon.noteminimalism3.db.DbHelper.DATABASE_NAME;

public class BackupAndRestoreDatabase {

    public static void backupDatabase(Context context, String outFileName) {
        // Путь к базе данных
        final String inFileName = context.getDatabasePath(DATABASE_NAME).toString();

        try {

            // создать каталог для резервного копирования
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

            Toast.makeText(context, "Backup Completed", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(context, "Unable to backup database. Retry", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static void restoreDatabase(Context context, String inFileName) {

        final String outFileName = context.getDatabasePath(DATABASE_NAME).toString();

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

            Toast.makeText(context, "Import Completed", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(context, "Unable to import database. Retry", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
