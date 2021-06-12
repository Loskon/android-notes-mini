package com.loskon.noteminimalism3.ui.preferences;

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

        return 14;
    }

    public static String getSummaryRange(Activity activity, int rangeInDays) {
        String summary = activity.getString(R.string.number_of_days_summary);

        summary = summary +" \u2014 " + rangeInDays;

        return summary;
    }
}
