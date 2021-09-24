package com.loskon.noteminimalism3.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable
import java.util.*


/**
 * Работы с данными базы данных
 */

class DateBaseAdapter(context: Context) {

    private val dbHelper: DateBaseHelper = DateBaseHelper(context.applicationContext)

    private val database: SQLiteDatabase =
        try {
            dbHelper.writableDatabase
        } catch (ex: SQLiteException) {
            dbHelper.readableDatabase
        }

    // Получение списка заметок
    fun getNotes(whereClause: String, orderBy: String): List<Note2> {
        val notes = ArrayList<Note2>()

        queryNotes(whereClause, orderBy, null).use { cursor ->
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                notes.add(cursor.getNotes())
                cursor.moveToNext()
            }
        }

        return notes
    }

    // Получение заметки по id
    fun getNote(id: Long): Note2? {

        queryNotes(
            NoteTable.COLUMN_ID + "=?",
            null, arrayOf(id.toString())
        ).use { cursor ->
            if (cursor.count == 0) {
                return null
            }
            cursor.moveToFirst()

            return cursor.getNotes()
        }
    }

    // Получение объектов Note
    private fun queryNotes(
        whereClause: String,
        orderBy: String?,
        whereArgs: Array<String>?
    ): NoteCursorWrapperKt {
        val cursor = database.query(
            NoteTable.NAME_TABLE,
            null,
            whereClause,
            whereArgs,
            null,
            null,
            orderBy
        )

        return NoteCursorWrapperKt(cursor)
    }


    fun insert(note: Note2) {
        val values = getContentValues(note)
        database.insert(NoteTable.NAME_TABLE, null, values)
    }

    fun insertGetId(note: Note2): Long {
        val values = getContentValues(note)
        return database.insert(NoteTable.NAME_TABLE, null, values)
    }

    fun delete(note: Note2) {
        database.delete(NoteTable.NAME_TABLE, NoteTable.COLUMN_ID + "=" + note.id, null)
    }

    fun deleteById(id: Long) {
        database.delete(NoteTable.NAME_TABLE, NoteTable.COLUMN_ID + "=" + id.toString(), null)
    }

    fun update(note: Note2) {
        val values = getContentValues(note)
        database.update(NoteTable.NAME_TABLE, values, NoteTable.COLUMN_ID + "=" + note.id, null)
    }

    // Запись и обновление базы данных
    private fun getContentValues(note: Note2): ContentValues {
        val values = ContentValues()
        if (note.id != 0L) values.put(NoteTable.COLUMN_ID, note.id)
        values.put(NoteTable.COLUMN_TITLE, note.title)
        values.put(NoteTable.COLUMN_DATE, note.dateCreation.time)
        values.put(NoteTable.COLUMN_DATE_MOD, note.dateModification.time)
        values.put(NoteTable.COLUMN_DATE_DEL, note.dateDelete.time)
        values.put(NoteTable.COLUMN_FAVORITES, note.isFavorite)
        values.put(NoteTable.COLUMN_DEL_ITEMS, note.isDelete)
        values.put(NoteTable.COLUMN_CHECKED, note.isChecked)
        return values
    }

    companion object {
        private var INSTANCE: DateBaseAdapter? = null

        //
        fun initDateBase(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = DateBaseAdapter(context)
            }
        }

        //
        fun getDateBase(): DateBaseAdapter {
            return INSTANCE ?: throw Exception("DateBase must be initialized")
        }
    }
}