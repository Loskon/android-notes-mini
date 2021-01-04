package com.loskon.noteminimalism3.helper;

import java.text.DateFormat;
import java.util.Date;

public class GetDate {

    public static String getNowDate(Date date) {
        int day;
        int month;

        String stringDate = DateFormat
                .getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(date);

        day = Integer.parseInt(stringDate.substring(0, 2)
                .replace("/", "").replace(".", ""));

        if (day < 10) {
            stringDate = "0" + stringDate;
        }

        month = Integer.parseInt(stringDate.substring(3, 5)
                .replace("/", "").replace(".", ""));

        if (month < 10) {
            stringDate = stringDate.substring(0, 3) + "0" + stringDate.substring(3);
        }

        return stringDate;
    }
}
