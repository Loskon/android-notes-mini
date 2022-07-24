package com.loskon.noteminimalism3.utils

import java.time.LocalDateTime

/**
 * Замена запрещенных символов в строках
 */

object StringUtil {

    fun replaceForbiddenCharacters(oldString: String): String {
        var newString = oldString

        newString = newString.replace("\\", "_")
        newString = newString.replace("[.|/*:]".toRegex(), "_")
        newString = newString.replace("[\"<>«»]".toRegex(), "\'")
        newString = newString.replace("?", ".")
        newString = newString.replace("\n", " ")

        return newString
    }

    fun replaceForbiddenCharacters(date: LocalDateTime): String {
        var name: String = DateUtil.getStringDate(date)
        name = name.replace("[./:]".toRegex(), "_")
        return "$name (A)"
    }
}
