package com.loskon.noteminimalism3.helper;

import java.text.DateFormat;
import java.util.Date;

public class GetDate {

    public static String getNowDate(Date date) {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT,
                DateFormat.SHORT).format(date);
    }
}
