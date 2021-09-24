package com.loskon.noteminimalism3.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable.COLUMN_CHECKED
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable.COLUMN_DATE
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable.COLUMN_DATE_DEL
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable.COLUMN_DATE_MOD
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable.COLUMN_DEL_ITEMS
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable.COLUMN_FAVORITES
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable.COLUMN_ID
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable.COLUMN_TITLE
import com.loskon.noteminimalism3.sqlite.NoteDateBaseSchema.NoteTable.NAME_TABLE

/**
 * Основной класс по работе с базой данных
 */

@Database(entities = [Note2::class], version = 2, exportSchema = false)
@TypeConverters(NotesTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}

const val TABLE_TMP = "notes_tmp"

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {

        database.execSQL(
            "CREATE TABLE $TABLE_TMP (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    COLUMN_TITLE + " TEXT NOT NULL DEFAULT '', " +
                    COLUMN_DATE + " INTEGER NOT NULL DEFAULT 0, " +
                    COLUMN_DATE_MOD + " INTEGER NOT NULL DEFAULT 0, " +
                    COLUMN_DATE_DEL + " INTEGER NOT NULL DEFAULT 0, " +
                    COLUMN_FAVORITES + " INTEGER NOT NULL DEFAULT 0, " +
                    COLUMN_DEL_ITEMS + " INTEGER NOT NULL DEFAULT 0" +
                    ")"
        )

        database.execSQL(
            "INSERT INTO $TABLE_TMP (" +
                    COLUMN_ID + ", " +
                    COLUMN_TITLE + ", " +
                    COLUMN_DATE + ", " +
                    COLUMN_DATE_MOD + ", " +
                    COLUMN_DATE_DEL + ", " +
                    COLUMN_FAVORITES + ", " +
                    COLUMN_DEL_ITEMS + ")" +
                    " SELECT " +
                    COLUMN_ID + ", " +
                    COLUMN_TITLE + ", " +
                    COLUMN_DATE + ", " +
                    COLUMN_DATE_MOD + ", " +
                    COLUMN_DATE_DEL + ", " +
                    COLUMN_FAVORITES + ", " +
                    COLUMN_DEL_ITEMS +
                    " FROM $NAME_TABLE"
        )

        database.execSQL("DROP TABLE $NAME_TABLE")
        database.execSQL("ALTER TABLE $TABLE_TMP RENAME TO $NAME_TABLE")
        database.execSQL("ALTER TABLE $NAME_TABLE ADD COLUMN $COLUMN_CHECKED INTEGER NOT NULL DEFAULT 0")
    }
}