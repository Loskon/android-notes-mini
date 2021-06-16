package com.loskon.noteminimalism3.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.loskon.noteminimalism3.model.Note;

import java.util.Date;

import static com.loskon.noteminimalism3.database.NoteDbSchema.COLUMN_DATE;
import static com.loskon.noteminimalism3.database.NoteDbSchema.COLUMN_DATE_DEL;
import static com.loskon.noteminimalism3.database.NoteDbSchema.COLUMN_DATE_MOD;
import static com.loskon.noteminimalism3.database.NoteDbSchema.COLUMN_FAVORITES;
import static com.loskon.noteminimalism3.database.NoteDbSchema.COLUMN_ID;
import static com.loskon.noteminimalism3.database.NoteDbSchema.COLUMN_TITLE;

/**
 * Класс обертки для курсора, который передает все вызовы к реальному объекту курсора
 * Основное использование для данного класса заключается в расширении курсора, при этом
 * переопределяя только подмножество его методов
 */

public class NoteCursorWrapper extends CursorWrapper {
    // Создает обертку для курсора
    public NoteCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    // Получает курсор, обернутый этим экземпляром для извлечения данных столбцов
    public Note getNotes() {

        long  id = getLong(getColumnIndex(COLUMN_ID));
        String text = getString(getColumnIndex(COLUMN_TITLE));
        long date = getLong(getColumnIndex(COLUMN_DATE));
        long dateMod = getLong(getColumnIndex(COLUMN_DATE_MOD));
        long dateDelete = getLong(getColumnIndex(COLUMN_DATE_DEL));
        String fav = getString(getColumnIndex(COLUMN_FAVORITES));

        // ВНИМАНИЕ!!!! SQLite не имеет отдельного класса логического хранилища.
        boolean favoritesItem = fav.equals("1");

        return new Note(id, text, new Date(date),
                new Date(dateMod), new Date(dateDelete), favoritesItem);
    }
}
