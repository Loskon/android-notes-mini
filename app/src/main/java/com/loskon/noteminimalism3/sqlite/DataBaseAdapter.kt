package com.loskon.noteminimalism3.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Синглтон БД
 */

class DataBaseAdapter(context: Context) {

    private val dbHelper: DataBaseHelper = DataBaseHelper(context.applicationContext)
    private val database: SQLiteDatabase = dbHelper.writableDatabase

    //----------------------------------------------------------------------------------------------
    fun getNotes(searchTerm: String?, category: String, sortingWay: Int): List<Note> {
        val notes: MutableList<Note> = arrayListOf()
        val whereClause: String = getWhereClause(searchTerm, category)
        val orderBy: String = getOrderBy(category, sortingWay)

        queryNotes(whereClause, orderBy).use { cursor ->
            cursor.moveToFirst()
            while (cursor.isAfterLast.not()) {
                notes.add(cursor.getNotes())
                cursor.moveToNext()
            }
        }

        return notes
    }

    private fun getWhereClause(searchTerm: String?, category: String): String {
        val categoryWhereClause: String = getCategoryWhereClause(category)

        return if (searchTerm != null) {
            NoteTable.COLUMN_TITLE + " LIKE '%$searchTerm%' AND " + categoryWhereClause
        } else {
            categoryWhereClause
        }
    }

    private fun getCategoryWhereClause(category: String): String {
        return when (category) {
            CATEGORY_ALL_NOTES -> NoteTable.COLUMN_DEL_ITEMS + " = 0"
            CATEGORY_FAVORITES -> NoteTable.COLUMN_FAVORITES + " = 1"
            CATEGORY_TRASH -> NoteTable.COLUMN_DEL_ITEMS + " = 1"
            else -> NoteTable.COLUMN_DEL_ITEMS + " = 0"
        }
    }

    private fun getOrderBy(category: String, sort: Int): String {
        return if (category == CATEGORY_TRASH) {
            NoteTable.COLUMN_DATE_DEL + " DESC" // По дате удаления
        } else {
            if (sort == 1) {
                NoteTable.COLUMN_DATE_MOD + " DESC" // По дате модификации
            } else {
                NoteTable.COLUMN_DATE + " DESC" // По дате создания
            }
        }
    }

    private fun queryNotes(whereClause: String, orderBy: String?): NoteCursorWrapper {
        val cursor: Cursor = database.query(
            NoteTable.NAME_TABLE,
            null,
            whereClause,
            null,
            null,
            null,
            orderBy
        )

        return NoteCursorWrapper(cursor)
    }

    //----------------------------------------------------------------------------------------------
    fun insert(note: Note) {
        val values: ContentValues = getContentValues(note)
        database.insert(NoteTable.NAME_TABLE, null, values)
    }

    fun insertWithIdReturn(note: Note): Long {
        val values: ContentValues = getContentValues(note)
        return database.insert(NoteTable.NAME_TABLE, null, values)
    }

    fun delete(note: Note) {
        database.delete(NoteTable.NAME_TABLE, NoteTable.COLUMN_ID + " = " + note.id, null)
    }

    fun cleanTrash() {
        database.delete(NoteTable.NAME_TABLE, NoteTable.COLUMN_DEL_ITEMS + " = " + 1, null)
    }

    fun deleteAll() {
        database.delete(NoteTable.NAME_TABLE, null, null)
    }

    fun deleteByTime(rangeInDays: Int) {
        // Перевод дня в Unix-time для корректного сложения и сравнения
        val range: Long = TimeUnit.MILLISECONDS.convert(rangeInDays.toLong(), TimeUnit.DAYS)
        database.delete(NoteTable.NAME_TABLE, getDelWhereClause(range), null)
    }

    private fun getDelWhereClause(range: Long): String {
        return NoteTable.COLUMN_DEL_ITEMS + " = " + 1 + " and " +
                Date().time + " > (" + NoteTable.COLUMN_DATE_DEL + "+" + range + ")"
    }

    fun update(note: Note) {
        val values: ContentValues = getContentValues(note)
        database.update(NoteTable.NAME_TABLE, values, NoteTable.COLUMN_ID + " = " + note.id, null)
    }

    private fun getContentValues(note: Note): ContentValues {
        return ContentValues().apply {
            put(NoteTable.COLUMN_TITLE, note.title)
            put(NoteTable.COLUMN_DATE, note.dateCreation.time)
            put(NoteTable.COLUMN_DATE_MOD, note.dateModification.time)
            put(NoteTable.COLUMN_DATE_DEL, note.dateDelete.time)
            put(NoteTable.COLUMN_FAVORITES, note.isFavorite)
            put(NoteTable.COLUMN_DEL_ITEMS, note.isDelete)
        }
    }

    //----------------------------------------------------------------------------------------------
    companion object {
        const val CATEGORY_ALL_NOTES = "category_all_notes"
        const val CATEGORY_FAVORITES = "category_favorites"
        const val CATEGORY_TRASH = "category_trash"

        private var INSTANCE: DataBaseAdapter? = null

        fun initDataBase(context: Context, rangeInDays: Int = 2) {
            if (INSTANCE == null) {
                INSTANCE = DataBaseAdapter(context).also { it.deleteByTime(rangeInDays) }
            }
        }

        fun getInstance(): DataBaseAdapter {
            return INSTANCE ?: throw Exception("Database must be initialized")
        }
    }
}