package com.loskon.noteminimalism3.base.extension.contentresolver

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns

fun ContentResolver.getFileName(fileUri: Uri): String? {
    var name: String? = null
    val cursor = query(fileUri, null, null, null, null)

    if (cursor != null) {
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        name = cursor.getString(nameIndex)
        cursor.close()
    }

    return name
}