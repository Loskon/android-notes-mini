package com.loskon.noteminimalism3.sqlite

import android.content.ContentValues
import android.content.Context
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sqlite.NoteDatabaseSchema.NoteTable
import java.util.Date
import java.util.concurrent.TimeUnit

class DatabaseAdapterNew(context: Context) {

    private val dbHelper = DatabaseHelper(context.applicationContext)
    private val database = dbHelper.writableDatabase

    fun getNotes(): List<Note> {
        return arrayListOf<Note>().apply {
            getNoteCursorWrapper().use { cursor ->
                cursor.moveToFirst()
                while (cursor.isAfterLast.not()) {
                    add(cursor.getNotes())
                    cursor.moveToNext()
                }
            }
        }.toList()
    }

    private fun getNoteCursorWrapper(): NoteCursorWrapper {
        return NoteCursorWrapper(
            database.query(
                NoteTable.NAME_TABLE,
                null,
                null,
                null,
                null,
                null,
                null
            )
        )
    }

    fun insert(note: Note) {
        database.insert(NoteTable.NAME_TABLE, null, getContentValues(note))
    }

    fun delete(note: Note) {
        database.delete(NoteTable.NAME_TABLE, NoteTable.COLUMN_ID + " = " + note.id, null)
    }

    fun update(note: Note) {
        database.update(NoteTable.NAME_TABLE, getContentValues(note), NoteTable.COLUMN_ID + " = " + note.id, null)
    }

    fun insertWithIdReturn(note: Note): Long {
        return database.insert(NoteTable.NAME_TABLE, null, getContentValues(note))
    }

    fun cleanTrash() {
        database.delete(NoteTable.NAME_TABLE, NoteTable.COLUMN_DEL_ITEMS + " = " + 1, null)
    }

    fun deleteAll() {
        database.delete(NoteTable.NAME_TABLE, null, null)
    }

    fun deleteByTime(days: Int) {
        val dayRange = TimeUnit.MILLISECONDS.convert(days.toLong(), TimeUnit.DAYS)
        database.delete(NoteTable.NAME_TABLE, getDelWhereClause(dayRange), null)
    }

    private fun getDelWhereClause(dayRange: Long): String {
        return NoteTable.COLUMN_DEL_ITEMS + "=" + 1 + "and" +
            Date().time + ">(" + NoteTable.COLUMN_DATE_DEL + "+" + dayRange + ")"
    }

    private fun getContentValues(note: Note): ContentValues {
        return ContentValues().apply {
            put(NoteTable.COLUMN_TITLE, note.title)
            put(NoteTable.COLUMN_DATE, note.createdDate.nano)
            put(NoteTable.COLUMN_DATE_MOD, note.modifiedDate.nano)
            put(NoteTable.COLUMN_DATE_DEL, note.deletedDate.nano)
            put(NoteTable.COLUMN_FAVORITES, note.isFavorite)
            put(NoteTable.COLUMN_DEL_ITEMS, note.isDeleted)
        }
    }

    //----------------------------------------------------------------------------------------------
    companion object {
        const val CATEGORY_ALL_NOTES = "category_all_notes"
        const val CATEGORY_FAVORITES = "category_favorites"
        const val CATEGORY_TRASH = "category_trash"

        private var INSTANCE: DatabaseAdapterNew? = null

        fun initDataBase(context: Context, rangeInDays: Int = 2) {
            if (INSTANCE == null) {
                INSTANCE = DatabaseAdapterNew(context).also { it.deleteByTime(rangeInDays) }
            }
        }

        fun getInstance(): DatabaseAdapterNew {
            return INSTANCE ?: throw Exception("Database must be initialized")
        }
    }
}