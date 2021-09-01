package com.loskon.noteminimalism3.utils

import java.io.File

/**
 *
 */

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

fun String.outFileNameBackup(backupName: String): String {
    var string = this

    string = string.replaceForbiddenCharacters()
    string = string.replace(":", "-")

    return File.separator + "$string$backupName.db"
}