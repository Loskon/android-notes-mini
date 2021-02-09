package com.loskon.noteminimalism3.helper;

import java.text.DateFormat;
import java.util.Date;

public class GetDate {

    public static String getNowDate(Date date) {
        int day;
        int month;

        String stringDate = DateFormat
                .getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(date);

        day = getNumOfString(stringDate, 0, 2);

        if (day < 2) {
            stringDate = "0" + stringDate;
        }

        month = getNumOfString(stringDate, 3, 5);

        if (month < 2) {
            stringDate = stringDate.substring(0, 3) + "0" + stringDate.substring(3);
        }

        return stringDate;
    }

    private static int getNumOfString(String stringDate , int indexStart, int indexEnd) {
        return Integer.parseInt(String.valueOf(stringDate.substring(indexStart, indexEnd)
                .replace("/", "").replace(".", "").length()));
    }
}
