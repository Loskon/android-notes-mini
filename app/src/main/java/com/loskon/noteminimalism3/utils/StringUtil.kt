package com.loskon.noteminimalism3.utils

import java.util.*

/**
 * Замена запрещенных символов в строках
 */

class StringUtil {
    companion object {

        fun replaceForbiddenCharacters(oldString: String): String {
            var newString = oldString

            newString = newString.replace("\\", "_")
            newString = newString.replace("[.|/*:]".toRegex(), "_")
            newString = newString.replace("[\"<>«»]".toRegex(), "\'")
            newString = newString.replace("?", ".")
            newString = newString.replace("\n", " ")

            return newString
        }

        fun replaceForbiddenCharacters(date: Date): String {
            var name: String = DateUtil.getStringDate(date)
            name = name.replace("[./:]".toRegex(), "_")
            return "$name (A)"
        }
    }
}