package com.loskon.noteminimalism3.backup.second;

import android.content.Context;
import android.os.Environment;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MyPrefKey;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MySharedPref;

import java.io.File;

/**
 * Получение пути к сохранению бэкапа и текстового файла
 */

public class BackupPath {

    public static String findFullPath(String path) {
        // Дает абсолютный путь от дерева uri
        String actualPAth;
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
            actualPAth = result.substring(0, 8) + "/emulated/0/" + result.substring(17);
        } else {
            actualPAth = result.toString();
        }

        return actualPAth;
    }

    public static String getPath(Context context) {
        // Путь к месту хранения/создания папки бэкапа
        String defaultPath = String.valueOf(Environment.getExternalStorageDirectory());
        String path = MySharedPref.getString(context, MyPrefKey.KEY_SEL_DIRECTORY, defaultPath);
        path = path + File.separator + context.getString(R.string.app_name_backup);
        return path;
    }

    // Получение файлов из папки
    public static File getFolder(Context context) {
        return new File(getPath(context));
    }

    public static String getNamePath(Context context) {
        // Путь для Summary в виде текста
        String summary = getPath(context);
        summary = summary.replace("//", "/");
        summary = summary.replace("storage/", "");
        return summary;
    }
}
