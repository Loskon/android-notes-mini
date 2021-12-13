package com.loskon.noteminimalism3.backup

import java.io.File
import java.util.*

/**
 * Сортировка файлов по дате создания
 */

class SortFileDatetime : Comparator<File?> {

    override fun compare(f1: File?, f2: File?): Int {
        return if (f1 != null && f2 != null) {
            f2.lastModified().compareTo(f1.lastModified())
        } else {
            0
        }
    }
}