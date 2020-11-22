package com.loskon.noteminimalism3.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.loskon.noteminimalism3.model.Note;
import com.loskon.noteminimalism3.db.NoteDbSchema.NoteTable;

/**
 * Данный класс позволяте взамодействовать с БД
 */

public class DbAdapter {

    private final DbHelper mDbHelper;
    private SQLiteDatabase mDatabase;

    public DbAdapter(Context context){
        mDbHelper = new DbHelper(context.getApplicationContext());
    }

    public void open(){
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close(){
        mDbHelper.close();
    }

    public List<Note> getNotes(String whereClause) {
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
        try (NoteCursorWrapper cursor = queryCrimes(
                NoteTable.Cols.ID + "=?",
                new String[]{String.valueOf(id)}
        )) {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getNotes();
        }
    }

    public void addNote(Note note) {
        ContentValues values = getContentValues(note);
        mDatabase.insert(NoteTable.NAME_TABLE, null, values);
    }

    public void deleteNote(long id) {
        mDatabase.delete(
                NoteTable.NAME_TABLE,
                NoteTable.Cols.ID + "=?",
                new String[] {String.valueOf(id)}
        );
    }

    public void updateNote(Note note) {
        ContentValues values = getContentValues(note);
        mDatabase.update(NoteTable.NAME_TABLE, values,
                NoteTable.Cols.ID + "=?",
                new String[]{String.valueOf(note.getId())});
    }

    public void updateFavorites(Note note, boolean favBoolean) {
        ContentValues values = getContentValues(note);
        values.put(NoteTable.Cols.COLUMN_FAVORITES, favBoolean);
        mDatabase.update(NoteTable.NAME_TABLE, values,
                NoteTable.Cols.ID + "=?",
                new String[]{String.valueOf(note.getId())});
    }

    public void updateSelectItemForDel(Note note, boolean selectBoolean) {
        ContentValues values = getContentValues(note);
        values.put(NoteTable.Cols.COLUMN_DEL_ITEMS, selectBoolean);
        mDatabase.update(NoteTable.NAME_TABLE, values,
                NoteTable.Cols.ID + "=?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteByTimer (int rangeInDays) {
        rangeInDays = -2;
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar range = Calendar.getInstance();
        range.add(Calendar.DAY_OF_MONTH, rangeInDays);
        //db.delete(DBHelper.TABLE, DBHelper.COLUMN_TRASH + "=" + 1 +  " and " + DBHelper.COLUMN_TIMER
               // + " < '" + dateFormat.format(range.getTime()) + "'",null);
    }

    private static ContentValues getContentValues (Note note) {
        ContentValues values = new ContentValues();
        if (note.getId() != 0) values.put(NoteTable.Cols.ID, note.getId());
        values.put(NoteTable.Cols.COLUMN_TITLE, note.getTitle());
        values.put(NoteTable.Cols.COLUMN_DATE, note.getDate());
        values.put(NoteTable.Cols.COLUMN_FAVORITES, note.getFavoritesItem());
        values.put(NoteTable.Cols.COLUMN_DEL_ITEMS, note.getSelectItemForDel());
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
