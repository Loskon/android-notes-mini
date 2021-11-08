package com.loskon.noteminimalism3.utils

/**
 * Замена запрещенных символов в именах файлов
 */

class StringUtil {
    companion object {
        fun replaceForbiddenCharacters(oldString: String): String {
            var newString = oldString

            newString = newString.replace("\\", "_")
            newString = newString.replace("[|/*]".toRegex(), "_")
            newString = newString.replace("[\"<>«»]".toRegex(), "\'")
            newString = newString.replace("?", ".")
            newString = newString.replace("\n", " ")

            return newString
        }
    }
}