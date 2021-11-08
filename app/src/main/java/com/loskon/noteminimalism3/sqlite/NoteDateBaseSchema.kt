package com.loskon.noteminimalism3.sqlite

/**
 * Схема базы данных, в которой хранятся имя таблицы и описание ее столбцов
 */

class NoteDateBaseSchema {

    companion object NoteTable {
        const val DATABASE_NAME = "notes.db" // название бд
        const val NAME_TABLE = "notes" // имя таблицы

        // Столбцы
        const val COLUMN_ID = "_id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DATE = "date"
        const val COLUMN_DATE_MOD = "date_mod"
        const val COLUMN_DATE_DEL = "date_del"
        const val COLUMN_FAVORITES = "favorites"
        const val COLUMN_DEL_ITEMS = "del_items"
    }
}