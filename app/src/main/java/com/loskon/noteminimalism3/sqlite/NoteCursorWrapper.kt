package com.loskon.noteminimalism3.sqlite

import android.database.Cursor
import android.database.CursorWrapper
import com.loskon.noteminimalism3.app.base.datetime.toLocalDateTime
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sqlite.NoteDatabaseSchema.NoteTable

/**
 * Извлечение данных столбцов
 */

class NoteCursorWrapper(cursor: Cursor) : CursorWrapper(cursor) {

    fun getNotes(): Note {
        val id = getLong(getColumnIndex(NoteTable.COLUMN_ID))
        val title = getString(getColumnIndex(NoteTable.COLUMN_TITLE))
        val dateCreation = getLong(getColumnIndex(NoteTable.COLUMN_DATE))
        val dateModification = getLong(getColumnIndex(NoteTable.COLUMN_DATE_MOD))
        val dateDelete = getLong(getColumnIndex(NoteTable.COLUMN_DATE_DEL))
        val isFavorite = getInt(getColumnIndex(NoteTable.COLUMN_FAVORITES)) == 1
        val isDelete = getInt(getColumnIndex(NoteTable.COLUMN_DEL_ITEMS)) == 1

        return Note(
            id = id,
            title = title,
            createdDate = dateCreation.toLocalDateTime(),
            modifiedDate = dateModification.toLocalDateTime(),
            deletedDate = dateDelete.toLocalDateTime(),
            isFavorite = isFavorite,
            isDeleted = isDelete
        )
    }
}