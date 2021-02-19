package com.loskon.noteminimalism3.backup.second;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.helper.sharedpref.MyPrefKey;
import com.loskon.noteminimalism3.helper.sharedpref.MySharedPref;

import java.io.File;

/**
 *
 */

public class BackupPath {

    // Дает абсолютный путь от дерева uri
    public static String findFullPath(String path) {
        String actualResult;
        int index = 0;

        path = path.substring(5);

        StringBuilder result = new StringBuilder("/storage");

        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) != ':') {
                result.append(path.charAt(i));
            } else {
                index = ++i;
                result.append('/');
                break;
            }
        }

        for (int i = index; i < path.length(); i++) {
            result.append(path.charAt(i));
        }

        if (result.substring(9, 16).equalsIgnoreCase("primary")) {
            actualResult = result.substring(0, 8) + "/emulated/0/" + result.substring(17);
        } else {
            actualResult = result.toString();
        }

        return actualResult;
    }

    // Путь к месту хранения/создания папки бэкапа
    public static String loadPath(Context context) {
        String defaultPath = String.valueOf(Environment.getExternalStorageDirectory());
        String path = MySharedPref.getString(context, MyPrefKey.KEY_SEL_DIRECTORY, defaultPath);
        path = path + File.separator + context.getString(R.string.app_name_backup);
        return path;
    }

    // Получени файлов из папки
    public static File getFolder(Context context) {
        return new File(BackupPath.loadPath(context));
    }

    // Путь для вывода в виде текста
    public static String getSummary(Context context) {
        String pathForSummary = loadPath(context);
        pathForSummary = pathForSummary.replace("//", "/");
        pathForSummary = pathForSummary.replace("storage/", "");
        return pathForSummary;
    }

    public static boolean createNoteFolder(Activity activity) {
        File folder = getFolder(activity);

        boolean isFolderCreated = true;

        if (!folder.exists()) {
            isFolderCreated = folder.mkdirs();
        }

        return isFolderCreated;
    }

    public static boolean createTextFolder(File file) {

        boolean isFolderTextCreated = true;

        if (!file.exists()) {
            isFolderTextCreated = file.mkdir();
        }

        return isFolderTextCreated;
    }

    public static String getPathToFiles(Activity activity) {
        return BackupPath.loadPath(activity) + File.separator;
    }
}
