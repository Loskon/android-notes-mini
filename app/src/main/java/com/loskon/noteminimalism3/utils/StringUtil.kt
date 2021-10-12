package com.loskon.noteminimalism3.utils

/**
 *
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


// Замена запрещенных символов в именах файлов
fun String.replaceForbiddenCharacters(): String {
    var string = this

    string = string.replace("\\", "_")
    string = string.replace("[|/*]".toRegex(), "_")
    string = string.replace("[\"<>«»]".toRegex(), "\'")
    string = string.replace("?", ".")
    string = string.replace("\n", " ")

    return string
}