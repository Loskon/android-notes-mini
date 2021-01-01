package com.loskon.noteminimalism3.helper;

import android.app.Activity;
import android.content.Context;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.db.backup.BackupAndRestoreDatabase;
import com.loskon.noteminimalism3.db.backup.MyPath;

import java.io.File;
import java.util.Date;

public class BackupHelper {

    public static void autoBackup(Activity activity) {

        File folder = getFolder(activity, MyPath.loadPath(activity));

        boolean success = true;

        // exists определяет, существует ли файл или каталог,
        // обозначаемый абстрактным именем файла, или нет.
        if (!folder.exists()) {
            success = folder.mkdirs(); // Создать папку
        }

        if (success) {
            // File name
            String outFileName = MyPath.loadPath(activity) +
                    File.separator + activity.getResources()
                    .getString(R.string.app_name) + File.separator;

            String titleText = GetDate.getNowDate(new Date());

            titleText = ReplaceText.replaceForSaveTittle(titleText);

            String outText = outFileName + titleText + " (A)" + ".db";

            BackupAndRestoreDatabase.backupDatabase(activity, outText);

            BackupLimiter.purgeLogFiles(folder); // Удаление лишних файлов
        }
    }

    private static File getFolder(Context context, String path) {
        return new File(path +
                File.separator + context.getResources().getString(R.string.app_name));
    }
}
