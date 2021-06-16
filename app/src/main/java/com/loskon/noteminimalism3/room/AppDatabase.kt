package com.loskon.noteminimalism3.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.loskon.noteminimalism3.model.Note2

/**
 * Основной класс по работе с базой данных
 */

@Database(entities = [Note2::class], version = 2, exportSchema = false)
@TypeConverters(CrimeTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
                CREATE TABLE new_Song (
                    _id INTEGER PRIMARY KEY NOT NULL,
                    title TEXT NOT NULL DEFAULT '',
                    date INTEGER NOT NULL DEFAULT 0,
                    date_mod INTEGER NOT NULL DEFAULT 0,
                    date_del INTEGER NOT NULL DEFAULT 0,
                    favorites INTEGER NOT NULL DEFAULT 0,
                    del_items INTEGER NOT NULL DEFAULT 0
                )
                """.trimIndent()
        )
        database.execSQL(
            """
                INSERT INTO new_Song (_id, title, date,date_mod, date_del,favorites,del_items)
                SELECT _id, title, date,date_mod, date_del,favorites,del_items   FROM notes
                """.trimIndent()
        )
        database.execSQL("DROP TABLE notes")
        database.execSQL("ALTER TABLE new_Song RENAME TO notes")
    }
}