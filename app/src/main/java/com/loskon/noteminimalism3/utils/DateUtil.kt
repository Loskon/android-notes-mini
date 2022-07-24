package com.loskon.noteminimalism3.utils

import java.text.DateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Date

/**
 * Получение текущей даты и преобразование в текстовый формат
 */

object DateUtil {

    @JvmStatic
    fun getStringDate(date: LocalDateTime): String {
        return date.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT))
        //return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(date)
    }

    fun getTimeNowWithBrackets(): String {
        val date = Date()
        return " (" + DateFormat.getTimeInstance(DateFormat.SHORT).format(date) + ")"
    }
}


