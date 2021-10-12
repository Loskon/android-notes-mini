package com.loskon.noteminimalism3.utils

import java.text.DateFormat
import java.util.*

/**
 * Получение текущей даты и преобразование в текстовый формат
 */

class DateUtil {
    companion object {
        
        @JvmStatic
        fun getStringDate(date: Date): String {
            val day: Int
            val month: Int

            var dateString =
                DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(date)

            day = dateString.numberCharacters(0, 2)

            if (day < 2) {
                dateString = "0$dateString"
            }

            month = dateString.numberCharacters(3, 5)

            if (month < 2) {
                dateString = dateString.substring(0, 3) + "0" + dateString.substring(3)
            }

            return dateString
        }

        private fun String.numberCharacters(indexStart: Int, indexEnd: Int): Int {
            var string = this.substring(indexStart, indexEnd)

            string = string.replace("/", "")
            string = string.replace(".", "")

            return string.length
        }
    }
}

