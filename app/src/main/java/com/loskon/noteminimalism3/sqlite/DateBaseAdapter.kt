package com.loskon.noteminimalism3.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable
import java.util.*
import java.util.concurrent.TimeUnit


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
    fun getNotes(noteCategory: String, sortingWay: Int): List<Note2> {
        val notes: ArrayList<Note2> = ArrayList<Note2>()
        val whereClause: String = getWhereClause(noteCategory)
        val orderBy: String = getOrderBy(noteCategory, sortingWay)

        queryNotes(whereClause, orderBy, null).use { cursor ->
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                notes.add(cursor.getNotes())
                cursor.moveToNext()
            }
        }

        return notes
    }

    // Выбор категории
    private fun getWhereClause(category: String): String =
        when (category) {
            CATEGORY_ALL_NOTES -> "del_items = 0"
            CATEGORY_FAVORITES -> "favorites = 1"
            CATEGORY_TRASH -> "del_items = 1"
            else -> throw Exception("Invalid category value")
        }


    // Выбор способа сортировки
    private fun getOrderBy(noteCategory: String, sort: Int): String {
        return if (noteCategory == CATEGORY_TRASH) {
            NoteTable.COLUMN_DATE_DEL + " DESC" // Date of deletion
        } else {
            if (sort == 1) {
                NoteTable.COLUMN_DATE_MOD + " DESC" // Modification
            } else {
                NoteTable.COLUMN_DATE + " DESC" // Create
            }
        }
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
    ): NoteCursorWrapperUpdate {
        val cursor = database.query(
            NoteTable.NAME_TABLE,
            null,
            whereClause,
            whereArgs,
            null,
            null,
            orderBy
        )

        return NoteCursorWrapperUpdate(cursor)
    }

    // insert
    fun insert(note: Note2) {
        val values = getContentValues(note)
        database.insert(NoteTable.NAME_TABLE, null, values)
    }

    fun insertGetId(note: Note2): Long {
        val values = getContentValues(note)
        return database.insert(NoteTable.NAME_TABLE, null, values)
    }

    // delete
    fun delete(note: Note2) {
        database.delete(NoteTable.NAME_TABLE, NoteTable.COLUMN_ID + "=" + note.id, null)
    }

    fun deleteById(id: Long) {
        database.delete(NoteTable.NAME_TABLE, NoteTable.COLUMN_ID + "=" + id.toString(), null)
    }

    fun deleteByTime(rangeInDays: Int) {
        // Перевод дня в Unix-time для корректного сложения и сравнения
        val range = TimeUnit.MILLISECONDS.convert(rangeInDays.toLong(), TimeUnit.DAYS)
        database.delete(
            NoteTable.NAME_TABLE,
            NoteTable.COLUMN_DEL_ITEMS + " = " + 1
                    + " and " + Date().time + " > (" +
                    NoteTable.COLUMN_DATE_DEL + "+" + range + ")", null
        )
    }

    // update
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

        //
        fun deleteNotesByTime(rangeInDays: Int) {
            INSTANCE?.deleteByTime(rangeInDays)
        }

        const val CATEGORY_ALL_NOTES = "category_all_notes"
        const val CATEGORY_FAVORITES = "category_favorites"
        const val CATEGORY_TRASH = "category_trash"
    }
}