package com.loskon.noteminimalism3.sqlite

import android.database.Cursor
import android.database.CursorWrapper
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable
import java.util.*

/**
 * Извлечение данных столбцов
 */

class NoteCursorWrapper(cursor: Cursor) : CursorWrapper(cursor) {

    fun getNotes(): Note {
        val id: Long = getLong(getColumnIndex(NoteTable.COLUMN_ID))
        val title: String = getString(getColumnIndex(NoteTable.COLUMN_TITLE))
        val dateCreation: Long = getLong(getColumnIndex(NoteTable.COLUMN_DATE))
        val dateModification: Long = getLong(getColumnIndex(NoteTable.COLUMN_DATE_MOD))
        val dateDelete: Long = getLong(getColumnIndex(NoteTable.COLUMN_DATE_DEL))
        // ВНИМАНИЕ!!!! SQLite не имеет отдельного класса логического хранилища
        val isFavorite: Boolean = getInt(getColumnIndex(NoteTable.COLUMN_FAVORITES)) == 1
        val isDelete: Boolean = getInt(getColumnIndex(NoteTable.COLUMN_DEL_ITEMS)) == 1

        return Note(
            id,
            title,
            Date(dateCreation),
            Date(dateModification),
            Date(dateDelete),
            isFavorite,
            isDelete
        )
    }
}
