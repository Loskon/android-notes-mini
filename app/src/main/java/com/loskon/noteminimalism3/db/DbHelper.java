package com.loskon.noteminimalism3.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.loskon.noteminimalism3.db.NoteDbSchema.NoteTable;

/**
 * Помощник SQLite для настройки базы данных и ее обработки
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notes2345678901112.db"; // название бд

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + NoteTable.NAME_TABLE + "(" +
                NoteTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NoteTable.Cols.UUID + " INTEGER, " +
                NoteTable.Cols.COLUMN_TITLE + " TEXT, " +
                NoteTable.Cols.COLUMN_DATE + " INTEGER, " +
                NoteTable.Cols.COLUMN_DATE_DEL + " INTEGER, " +
                NoteTable.Cols.COLUMN_FAVORITES + " INTEGER, " +
                NoteTable.Cols.COLUMN_DEL_ITEMS + " INTEGER" +
                ")"
        );

        // добавление начальных данных
        //db.execSQL("INSERT INTO " + UserTable.NAME + " (" +
             //   UserTable.Cols.TEXT + ", " +
              //  UserTable.Cols.DATE + ") " +
               // "VALUES ('Том Смит', 1981);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NoteTable.NAME_TABLE);
        onCreate(db);
    }
}

