package com.loskon.noteminimalism3.ui.preference;

import android.app.Activity;

import com.loskon.noteminimalism3.R;

/**
 * Помощник для Preference
 */

public class PrefHelper {

    public static int getDateFontSize(int fontSize) {
        // Изменяет размер тектса даты
        // в зависимости от размера основного текста
        if (fontSize < 18) return 12;
        else if (fontSize <= 22) return 14;
        else if (fontSize <= 26) return 16;
        else if (fontSize <= 30) return 18;
        else if (fontSize <= 34) return 20;
        else if (fontSize <= 38) return 22;
        else if (fontSize <= 42) return 24;

        return 0;
    }

    public static String getPrefSummary(Activity activity, String prefString, int prefValue) {
        // Получение текста для сводки
        String summary = "";

        if (prefString.equals(activity.getString(R.string.num_of_backup_title))) {
            summary = activity.getString(R.string.num_backup_summary);
        } else if (prefString.equals(activity.getString(R.string.retention_trash_title))) {
            summary = activity.getString(R.string.number_of_days_summary);
        }

        summary = summary +" \u2014 " + prefValue;
        return summary;
    }
}
