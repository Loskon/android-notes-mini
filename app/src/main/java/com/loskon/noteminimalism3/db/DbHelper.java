package com.loskon.noteminimalism3.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.loskon.noteminimalism3.db.NoteDbSchema.NoteTable;
import com.loskon.noteminimalism3.db.NoteDbSchema.NoteTable.Columns;

/**
 * Помощник SQLite для настройки базы данных и ее обработки
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "notes.db"; // название бд

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Table Create Statement
        db.execSQL("CREATE TABLE " + NoteTable.NAME_TABLE + "(" +
                Columns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Columns.UUID + " INTEGER, " +
                Columns.COLUMN_TITLE + " TEXT, " +
                Columns.COLUMN_DATE + " INTEGER, " +
                Columns.COLUMN_DATE_DEL + " INTEGER, " +
                Columns.COLUMN_FAVORITES + " INTEGER, " +
                Columns.COLUMN_DEL_ITEMS + " INTEGER" +
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
        //reinitialize db
        db.execSQL("DROP TABLE IF EXISTS " + NoteTable.NAME_TABLE);
        onCreate(db);
    }
}

