package com.loskon.noteminimalism3.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.loskon.noteminimalism3.model.Note;
import com.loskon.noteminimalism3.db.NoteDbSchema.NoteTable;

/**
 * Данный класс позволяте взамодействовать с БД
 */

public class DbAdapter {

    private final DbHelper mDbHelper;
    private SQLiteDatabase mDatabase;

    public DbAdapter(Context context) {
        mDbHelper = new DbHelper(context.getApplicationContext());
    }

    public void open() {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }

    public List<Note> getNotes(String whereClause) {
        // Получить все заметки
        ArrayList<Note> notes = new ArrayList<>();

        try (NoteCursorWrapper cursor = queryCrimes(whereClause, null)) {
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
                NoteTable.Columns.ID + "=?",
                new String[]{String.valueOf(id)}
        )) {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getNotes();
        }
    }

    public void addNewNote(Note note) {
        // Добавить новую заметку
        ContentValues values = getContentValues(note);
        mDatabase.insert(NoteTable.NAME_TABLE, null, values);
    }

    public void deleteNote(long id) {
        // Удалить заметку
        mDatabase.delete(NoteTable.NAME_TABLE,
                NoteTable.Columns.ID + "=?",
                new String[]{String.valueOf(id)}
        );
    }

    public void updateNote(Note note) {
        // Обновить заметку
        ContentValues values = getContentValues(note);
        mDatabase.update(NoteTable.NAME_TABLE, values,
                NoteTable.Columns.ID + "=?",
                new String[]{String.valueOf(note.getId())});
    }

    public void updateFavorites(Note note, boolean isFavItem) {
        // Добавить/удалить избранное
        ContentValues values = getContentValues(note);
        values.put(NoteTable.Columns.COLUMN_FAVORITES, isFavItem);
        mDatabase.update(NoteTable.NAME_TABLE, values,
                NoteTable.Columns.ID + "=?",
                new String[]{String.valueOf(note.getId())});
    }

    public void updateSelectItemForDel(Note note, boolean isDelItem, Date dateDelete) {
        // Добавить/удалить корзина
        ContentValues values = getContentValues(note);
        values.put(NoteTable.Columns.COLUMN_DEL_ITEMS, isDelItem);
        values.put(NoteTable.Columns.COLUMN_DATE_DEL, dateDelete.getTime());
        mDatabase.update(NoteTable.NAME_TABLE, values,
                NoteTable.Columns.ID + "=?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteByTime(int rangeInDays) {
        // Удаление заметки с помощью сравнения дат

        // Перевод дня в Unix-time для корректного сложения и сравнения
        long range = TimeUnit.MILLISECONDS.convert(rangeInDays, TimeUnit.DAYS);
        mDatabase.delete(NoteTable.NAME_TABLE,
                NoteTable.Columns.COLUMN_DEL_ITEMS + " = " + 1
                 + " and " + (new Date()).getTime() + " > (" +
                NoteTable.Columns.COLUMN_DATE_DEL + "+" + range + ")", null);
    }

    public void deleteAll() {
        // Удалить все заметки из корзины
        mDatabase.delete(NoteTable.NAME_TABLE,
                NoteTable.Columns.COLUMN_DEL_ITEMS + " = " + 1, null);
    }

    private static ContentValues getContentValues(Note note) {
        ContentValues values = new ContentValues();
        if (note.getId() != 0) values.put(NoteTable.Columns.ID, note.getId());
        values.put(NoteTable.Columns.COLUMN_TITLE, note.getTitle());
        values.put(NoteTable.Columns.COLUMN_DATE, note.getDate().getTime());
        values.put(NoteTable.Columns.COLUMN_DATE_DEL, note.getDateDelete().getTime());
        values.put(NoteTable.Columns.COLUMN_FAVORITES, note.getFavoritesItem());
        values.put(NoteTable.Columns.COLUMN_DEL_ITEMS, note.getSelectItemForDel());
        return values;
    }

    private NoteCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                NoteTable.NAME_TABLE,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                "_id DESC" // Обратная сортировка по _id
        );
        return new NoteCursorWrapper(cursor);
    }

}
