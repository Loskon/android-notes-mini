package com.loskon.noteminimalism3.utils

import java.io.File

/**
 *
 */

fun File.createFolder(): Boolean {

    var isFolderCreated = true

    if (!exists()) {
        isFolderCreated = mkdir()
    }

    return isFolderCreated
}