package com.loskon.noteminimalism3.utils

import java.text.DateFormat
import java.util.*

/**
 * Получение текущей даты и преобразование в текстовый формат
 */

object DateUtil {

    @JvmStatic
    fun getStringDate(date: Date): String {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(date)
    }

    fun getTimeNowWithBrackets(): String {
        val date = Date()
        return " (" + DateFormat.getTimeInstance(DateFormat.SHORT).format(date) + ")"
    }
}


