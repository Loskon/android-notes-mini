package com.loskon.noteminimalism3.activity.noteHelper;

import com.loskon.noteminimalism3.db.DbAdapter;
import com.loskon.noteminimalism3.model.Note;

public class NoteAdapter {

    private final DbAdapter dbAdapter;
    private boolean createOrDel;
    private final int selectedNoteMode;
    private final Note note;
    private final long noteId;

    public NoteAdapter(DbAdapter dbAdapter, boolean createOrDel, int selectedNoteMode,Note note, long noteId) {
        this.dbAdapter = dbAdapter;
        this.createOrDel = createOrDel;
        this.selectedNoteMode = selectedNoteMode;
        this.note = note;
        this.noteId = noteId;
    }

    public void addNoteClickSupport(){
        if (selectedNoteMode == 2) {
            dbAdapter.open();
            dbAdapter.updateSelectItemForDel(note, false, note.getDate());
            dbAdapter.close();
            createOrDel = false;
        }
    }

    public void deleteNoteClickSupport() {
        dbAdapter.open();
        // Удаляет не созданную заметку, либо заметку из мусорки
        if (selectedNoteMode == 2 || noteId == 0) {
            dbAdapter.deleteNote(noteId);
        } else {
            note.setSelectItemForDel(true);
            note.setFavoritesItem(false);
            dbAdapter.updateSelectItemForDel(note, true, note.getDate());
            dbAdapter.updateFavorites(note,false);
        }
        dbAdapter.close();
    }
}
