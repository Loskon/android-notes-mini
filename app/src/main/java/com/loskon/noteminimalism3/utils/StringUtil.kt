package com.loskon.noteminimalism3.utils

import com.loskon.noteminimalism3.app.base.datetime.formattedString
import java.time.LocalDateTime

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
        return "${date.formattedString().replace("[./:]".toRegex(), "_")} (A)"
    }
}
