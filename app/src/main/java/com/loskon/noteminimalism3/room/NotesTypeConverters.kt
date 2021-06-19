package com.loskon.noteminimalism3.room

import androidx.room.TypeConverter
import java.util.*

/**
 *
 */

class NotesTypeConverters {

    @TypeConverter
    fun formatDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date? {
        return millisSinceEpoch?.let {
            Date(it)
        }
    }
}
