package com.loskon.noteminimalism3.sqlite

import android.content.ContentValues
import android.content.Context
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sqlite.NoteDatabaseSchema.NoteTable
import java.time.LocalDateTime
import java.time.ZoneOffset
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

    fun getNote(id: Long): Note {
        getNoteCursorWrapper2(NoteTable.COLUMN_ID + "=?", arrayOf(id.toString())).use { cursor ->
            cursor.moveToFirst()
            return cursor.getNotes()
        }
    }

    private fun getNoteCursorWrapper2(s: String, arrayOf: Array<String>): NoteCursorWrapper {
        return NoteCursorWrapper(
            database.query(
                NoteTable.NAME_TABLE,
                null,
                s,
                arrayOf,
                null,
                null,
                null
            )
        )
    }

    fun insert(note: Note) {
        database.insert(NoteTable.NAME_TABLE, null, getContentValues(note))
    }

    fun update(note: Note) {
        database.update(NoteTable.NAME_TABLE, getContentValues(note), NoteTable.COLUMN_ID + " = " + note.id, null)
    }

    fun delete(note: Note) {
        database.delete(NoteTable.NAME_TABLE, NoteTable.COLUMN_ID + " = " + note.id, null)
    }

    fun insertGetId(note: Note): Long {
        return database.insert(NoteTable.NAME_TABLE, null, getContentValues(note))
    }

    fun updateAll(list: List<Note>) {
        list.forEach { note ->
            database.update(NoteTable.NAME_TABLE, getContentValues(note), NoteTable.COLUMN_ID + " = " + note.id, null)
        }
    }

    fun cleanTrash() {
        database.delete(NoteTable.NAME_TABLE, NoteTable.COLUMN_DEL_ITEMS + " = " + 1, null)
    }

    fun deleteAll() {
        database.delete(NoteTable.NAME_TABLE, null, null)
    }

    fun deleteAll(list: List<Note>) {
        val idArray = list.map { it.id }.toTypedArray()
        val ids = idArray.joinToString(separator = ",")
        database.delete(NoteTable.NAME_TABLE, NoteTable.COLUMN_ID + " IN (" + ids + ")", null)
    }

    fun deleteByTime(days: Int) {
        val dayRange = TimeUnit.MILLISECONDS.convert(days.toLong(), TimeUnit.DAYS)
        database.delete(NoteTable.NAME_TABLE, getDelWhereClause(dayRange), null)
    }

    private fun getDelWhereClause(dayRange: Long): String {
        return NoteTable.COLUMN_DEL_ITEMS + " = " + 1 + " and " + LocalDateTime.now().toInstant(ZoneOffset.UTC)
            .toEpochMilli() + " > (" + NoteTable.COLUMN_DATE_DEL + "+" + dayRange + ")"
    }

    private fun getContentValues(note: Note): ContentValues {
        return ContentValues().apply {
            put(NoteTable.COLUMN_TITLE, note.title)
            put(NoteTable.COLUMN_DATE, note.createdDate.toInstant(ZoneOffset.UTC).toEpochMilli())
            put(NoteTable.COLUMN_DATE_MOD, note.modifiedDate.toInstant(ZoneOffset.UTC).toEpochMilli())
            put(NoteTable.COLUMN_DATE_DEL, note.deletedDate.toInstant(ZoneOffset.UTC).toEpochMilli())
            put(NoteTable.COLUMN_FAVORITES, note.isFavorite)
            put(NoteTable.COLUMN_DEL_ITEMS, note.isDeleted)
        }
    }
}