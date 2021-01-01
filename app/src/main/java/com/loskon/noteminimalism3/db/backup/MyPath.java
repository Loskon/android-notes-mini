package com.loskon.noteminimalism3.db.backup;

import android.content.Context;
import android.os.Environment;

import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.helper.sharedpref.MyPrefKey;
import com.loskon.noteminimalism3.helper.sharedpref.MySharedPreference;

import java.io.File;

/**
 *
 */

public class MyPath {

    // эта функция дает абсолютный путь от дерева uri
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

    // Путь к месту хранения/создания папки
    public static String loadPath(Context context) {
        return MySharedPreference.loadString(context,
                MyPrefKey.KEY_SEL_DIRECTORY, String
                        .valueOf(Environment.getExternalStorageDirectory()));
    }

    // Путь для вывода в виде текста
    public static String loadPathString(Context context) {
        String pathString = loadPath(context)
                + File.separator + context.getResources().getString(R.string.app_name);
        pathString = pathString.replace("//", "/");
        pathString = pathString.replace("storage/", "");
        return pathString;
    }


}
