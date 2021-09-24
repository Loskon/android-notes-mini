package com.loskon.noteminimalism3.database;

import static com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.COLUMN_DATE;
import static com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.COLUMN_DATE_DEL;
import static com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.COLUMN_DATE_MOD;
import static com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.COLUMN_DEL_ITEMS;
import static com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.COLUMN_FAVORITES;
import static com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.COLUMN_ID;
import static com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.COLUMN_TITLE;
import static com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.DATABASE_NAME;
import static com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NAME_TABLE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Помощник SQLite для настройки базы данных и ее обработки
 */

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        // Table Create Statement
        database.execSQL("CREATE TABLE " + NAME_TABLE + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DATE + " INTEGER, " +
                COLUMN_DATE_MOD + " INTEGER, " +
                COLUMN_DATE_DEL + " INTEGER, " +
                COLUMN_FAVORITES + " INTEGER, " +
                COLUMN_DEL_ITEMS + " INTEGER" +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        // Повторная инициализация базы данных
        database.execSQL("DROP TABLE IF EXISTS " + NAME_TABLE);
        onCreate(database);
    }
}

