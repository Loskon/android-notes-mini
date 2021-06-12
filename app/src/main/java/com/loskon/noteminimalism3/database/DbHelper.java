package com.loskon.noteminimalism3.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.loskon.noteminimalism3.database.NoteDbSchema.NoteTable;
import com.loskon.noteminimalism3.database.NoteDbSchema.NoteTable.Columns;

/**
 * Помощник SQLite для настройки базы данных и ее обработки
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "notes.db"; // название бд

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        // Table Create Statement
        database.execSQL("CREATE TABLE " + NoteTable.NAME_TABLE + "(" +
                Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Columns.COLUMN_TITLE + " TEXT, " +
                Columns.COLUMN_DATE + " INTEGER, " +
                Columns.COLUMN_DATE_MOD + " INTEGER, " +
                Columns.COLUMN_DATE_DEL + " INTEGER, " +
                Columns.COLUMN_FAVORITES + " INTEGER, " +
                Columns.COLUMN_DEL_ITEMS + " INTEGER" +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,  int newVersion) {
        // Повторная инициализация базы данных
        database.execSQL("DROP TABLE IF EXISTS " + NoteTable.NAME_TABLE);
        onCreate(database);
    }
}

