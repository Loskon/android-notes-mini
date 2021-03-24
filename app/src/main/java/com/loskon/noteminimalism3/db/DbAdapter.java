package com.loskon.noteminimalism3.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.loskon.noteminimalism3.db.NoteDbSchema.NoteTable;
import com.loskon.noteminimalism3.db.NoteDbSchema.NoteTable.Columns;
import com.loskon.noteminimalism3.model.Note;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Данный класс позволяте взамодействовать с БД
 */

public class DbAdapter {

    private final DbHelper dbHelper;
    private SQLiteDatabase sqlDatabase;

    public DbAdapter(Context context) {
        dbHelper = new DbHelper(context.getApplicationContext());
    }

    public void open() {
        sqlDatabase = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<Note> getNotes(String whereClause, String orderBy) {
        // Получить все заметки
        ArrayList<Note> notes = new ArrayList<>();

        try (NoteCursorWrapper cursor = queryCrimes(whereClause, orderBy, null)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                notes.add(cursor.getNotes());
                cursor.moveToNext();
            }
        }

        return notes;
    }

    public Note getNote(long id) {
        // Получить заметку по id
        try (NoteCursorWrapper cursor = queryCrimes(
                Columns.ID + "=?",
                null,
                new String[]{String.valueOf(id)}
        )) {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getNotes();
        }
    }

    public int addNewNote(Note note) {
        // Добавить новую заметку
        ContentValues values = getContentValues(note);
        return (int) sqlDatabase.insert(NoteTable.NAME_TABLE, null, values);
    }

    public void deleteNote(long id) {
        // Удалить заметку
        sqlDatabase.delete(NoteTable.NAME_TABLE,
                Columns.ID + "=?",
                new String[]{String.valueOf(id)}
        );
    }

    public void updateNote(Note note) {
        // Обновить заметку
        ContentValues values = getContentValues(note);
        sqlDatabase.update(NoteTable.NAME_TABLE, values,
                Columns.ID + "=?",
                new String[]{String.valueOf(note.getId())});
    }

    public void updateFavorites(Note note, boolean isFavItem) {
        // Добавить/удалить избранное
        ContentValues values = getContentValues(note);
        values.put(Columns.COLUMN_FAVORITES, isFavItem);
        sqlDatabase.update(NoteTable.NAME_TABLE, values,
                Columns.ID + "=?",
                new String[]{String.valueOf(note.getId())});
    }

    public void updateItemDel(Note note, boolean isDelItem,
                              Date date, Date dateDelete) {
        // Добавить/удалить корзина
        ContentValues values = getContentValues(note);
        values.put(Columns.COLUMN_DEL_ITEMS, isDelItem);
        values.put(Columns.COLUMN_DATE, date.getTime());
        values.put(Columns.COLUMN_DATE_DEL, dateDelete.getTime());
        sqlDatabase.update(NoteTable.NAME_TABLE, values,
                Columns.ID + "=?",
                new String[]{String.valueOf(note.getId())});
    }

    public void updateTitle(Note note, String title) {
        // Добавить/удалить избранное
        ContentValues values = getContentValues(note);
        values.put(Columns.COLUMN_DEL_ITEMS, false);
        values.put(Columns.COLUMN_TITLE, title);
        values.put(Columns.COLUMN_DATE, (new Date()).getTime());
        sqlDatabase.update(NoteTable.NAME_TABLE, values,
                Columns.ID + "=?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteByTime(int rangeInDays) {
        // Удаление заметки с помощью сравнения дат

        // Перевод дня в Unix-time для корректного сложения и сравнения
        long range = TimeUnit.MILLISECONDS.convert(rangeInDays, TimeUnit.DAYS);
        sqlDatabase.delete(NoteTable.NAME_TABLE,
                Columns.COLUMN_DEL_ITEMS + " = " + 1
                 + " and " + (new Date()).getTime() + " > (" +
                Columns.COLUMN_DATE_DEL + "+" + range + ")", null);
    }

    public void deleteAll() {
        // Удалить все заметки из корзины
        sqlDatabase.delete(NoteTable.NAME_TABLE,
                Columns.COLUMN_DEL_ITEMS + " = " + 1, null);
    }

    private static ContentValues getContentValues(Note note) {
        ContentValues values = new ContentValues();
        if (note.getId() != 0) values.put(NoteTable.Columns.ID, note.getId());
        values.put(Columns.COLUMN_TITLE, note.getTitle());
        values.put(Columns.COLUMN_DATE, note.getDate().getTime());
        values.put(Columns.COLUMN_DATE_MOD, note.getDateMod().getTime());
        values.put(Columns.COLUMN_DATE_DEL, note.getDateDelete().getTime());
        values.put(Columns.COLUMN_FAVORITES, note.getFavoritesItem());
        values.put(Columns.COLUMN_DEL_ITEMS, note.getSelectItemForDel());
        return values;
    }

    private NoteCursorWrapper queryCrimes(String whereClause, String orderBy, String[] whereArgs) {
        Cursor cursor = sqlDatabase.query(
                NoteTable.NAME_TABLE,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                orderBy
        );
        return new NoteCursorWrapper(cursor);
    }
}
