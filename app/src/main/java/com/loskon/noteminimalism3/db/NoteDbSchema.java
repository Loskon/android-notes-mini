package com.loskon.noteminimalism3.db;

/**
 * Схема базы данных, в которой храняться имя таблицы и описание ее столбцов
 */

public class NoteDbSchema {
    public static final class NoteTable {
        // Tables Name
        public static final String NAME_TABLE = "notes";

        public static final class Columns {
            // Notes Table - column name
            public static final String ID = "_id";
            public static final String UUID = "uuid";
            public static final String COLUMN_TITLE = "title";
            public static final String COLUMN_DATE = "date";
            public static final String COLUMN_FAVORITES = "favorites";
            public static final String COLUMN_DEL_ITEMS = "del_items";
            public static final String COLUMN_DATE_DEL = "date_del";
        }
    }
}

