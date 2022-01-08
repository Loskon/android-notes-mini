package com.loskon.noteminimalism3.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable.DATABASE_NAME

/**
 * Настройка БД
 */

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE " + NoteTable.NAME_TABLE + "(" +
                    NoteTable.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NoteTable.COLUMN_TITLE + " TEXT, " +
                    NoteTable.COLUMN_DATE + " INTEGER, " +
                    NoteTable.COLUMN_DATE_MOD + " INTEGER, " +
                    NoteTable.COLUMN_DATE_DEL + " INTEGER, " +
                    NoteTable.COLUMN_FAVORITES + " INTEGER, " +
                    NoteTable.COLUMN_DEL_ITEMS + " INTEGER" +
                    ")"
        )
    }

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        database.execSQL("DROP TABLE IF EXISTS " + NoteTable.NAME_TABLE)
        onCreate(database)
    }
}