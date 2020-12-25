package com.loskon.noteminimalism3.ui.Helper;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import java.text.DateFormat;
import java.util.Date;

public class DateHelper {

    public static String getNowDate(Date date) {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT,
                DateFormat.SHORT).format(date);
    }
}
