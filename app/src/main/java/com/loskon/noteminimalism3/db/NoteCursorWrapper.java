package com.loskon.noteminimalism3.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.loskon.noteminimalism3.model.Note;
import com.loskon.noteminimalism3.db.NoteDbSchema.NoteTable;

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

        long  id = getLong(getColumnIndex(NoteTable.Cols.ID));
        String text = getString(getColumnIndex(NoteTable.Cols.COLUMN_TITLE));
        String date = getString(getColumnIndex(NoteTable.Cols.COLUMN_DATE));
        String fav = getString(getColumnIndex(NoteTable.Cols.COLUMN_FAVORITES));
        String select = getString(getColumnIndex(NoteTable.Cols.COLUMN_DEL_ITEMS));

        // ATTENTION!!!! SQLite does not have a separate Boolean storage class.
        boolean favoritesItem = fav.equals("1");
        boolean selectItemForDel = select.equals("1");

        return new Note(id, text, date, favoritesItem, selectItemForDel);
    }
}
