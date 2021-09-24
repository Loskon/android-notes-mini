package com.loskon.noteminimalism3.sqlite

import android.database.Cursor
import android.database.CursorWrapper
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable
import java.util.*

/**
 * Обертка для курсора
 */

class NoteCursorWrapperKt(cursor: Cursor?) : CursorWrapper(cursor) {

    fun getNotes(): Note2 {
        val id = getLong(getColumnIndex(NoteTable.COLUMN_ID))
        val text = getString(getColumnIndex(NoteTable.COLUMN_TITLE))
        val date = getLong(getColumnIndex(NoteTable.COLUMN_DATE))
        val dateMod = getLong(getColumnIndex(NoteTable.COLUMN_DATE_MOD))
        val dateDelete = getLong(getColumnIndex(NoteTable.COLUMN_DATE_DEL))
        // ВНИМАНИЕ!!!! SQLite не имеет отдельного класса логического хранилища
        val favoritesItem = getInt(getColumnIndex(NoteTable.COLUMN_FAVORITES)) == 1

        return Note2(id, text, Date(date), Date(dateMod), Date(dateDelete), favoritesItem)
    }
}
