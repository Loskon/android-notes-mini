package com.loskon.noteminimalism3.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loskon.noteminimalism3.model.Note;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.db.DbAdapter;

import java.util.Date;

public class NoteActivity extends AppCompatActivity {

    private EditText editTitleText;
    private MaterialButton favorite_button, delete_button, more_button;
    private DbAdapter dbAdapter;
    private long noteId = 0;
    private boolean favoriteItemBoolean = false;
    private boolean selectItemForDelBoolean = false;
    private boolean createOrDel = true;
    private boolean isUpdateDateTame = false;
    private int selectedNoteMode;
    private FloatingActionButton fabNote;
    private Note note;
    private final Date newDate = new Date();
    private Date receivedDate, setDate;

    public static Intent newIntent(Context context, int selectedNoteMode) {
        Intent intent = new Intent(context, NoteActivity.class);
        intent.putExtra("selectedNoteMode", selectedNoteMode);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        initViewAndAdapter();
        toGetAndProcessingData();
        favoriteStatus();
    }

    private void initViewAndAdapter() {
        editTitleText = findViewById(R.id.title_text);
        favorite_button = findViewById(R.id.favorite_button);
        delete_button = findViewById(R.id.delete_button);
        more_button = findViewById(R.id.more_button);
        fabNote = findViewById(R.id.fabNote);
        dbAdapter = new DbAdapter(this);
    }

    private void toGetAndProcessingData () {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            noteId = extras.getLong("id");
            selectedNoteMode = extras.getInt("selectedNoteMode");
        }

        if (noteId > 0) {
            existingNote();
        } else {
            newNote();
        }
    }

    private void existingNote() {
        // Получение текста, значений избранного и удаленного
        dbAdapter.open();
        note = dbAdapter.getNote(noteId);
        editTitleText.setText(note.getTitle());
        receivedDate = note.getDate();
        favoriteItemBoolean = note.getFavoritesItem();
        selectItemForDelBoolean = note.getSelectItemForDel();
        dbAdapter.close();

        // Специальная перерисовка для корзины
        if (selectedNoteMode == 2) redrawingForBasket();
    }

    private void redrawingForBasket() {
        favorite_button.setVisibility(View.GONE);
        more_button.setVisibility(View.GONE);
        fabNote.setImageResource(R.drawable.baseline_restore_from_trash_black_24);
        delete_button.setIcon(ResourcesCompat.getDrawable(getResources(),
                R.drawable.baseline_delete_forever_black_24, null));
    }

    private void newNote () {
        if (selectedNoteMode == 1) {
            favoriteItemBoolean = true;
        }
        more_button.setVisibility(View.GONE);
    }

    private void favoriteStatus() {
        if (favoriteItemBoolean) {
            favorite_button.setIcon(ResourcesCompat.getDrawable(getResources(),
                    R.drawable.baseline_star_black_24, null));
        } else {
            favorite_button.setIcon(ResourcesCompat.getDrawable(getResources(),
                    R.drawable.baseline_star_border_black_24, null));
        }
    }

    public void addNoteClick(View view){
        if (selectedNoteMode == 2) {
            dbAdapter.open();
            dbAdapter.updateSelectItemForDel(note, false, note.getDate());
            dbAdapter.close();
            createOrDel = false;
        }
        goMainActivity();
    }

    public void deleteNoteClick(View view) {
        dbAdapter.open();
        if (selectedNoteMode == 2 || noteId == 0) {
            // Удаляет навсегда не сохраненную заметку, либо заметку из мусорки
            dbAdapter.deleteNote(noteId);
        } else {
            note.setSelectItemForDel(true);
            note.setFavoritesItem(false);
            dbAdapter.updateSelectItemForDel(note, true, newDate);
            dbAdapter.updateFavorites(note,false);
        }
        dbAdapter.close();
        createOrDel = false;
        goMainActivity();
    }

    public void favoritesNoteClick(View view) {
        favoriteItemBoolean = !favoriteItemBoolean;
        favoriteStatus();
    }

    private void goMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("createOrDel", createOrDel);
        intent.putExtra("updateDate", true);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Защита от случайного сохранения
        if (selectedNoteMode !=2 && createOrDel) saveNote();
    }

    private void saveNote() {
        String title = editTitleText.getText().toString();
        dbAdapter.open();
        if (title.trim().length() == 0) {
            // Удаление навсегда пустой заметки
            dbAdapter.deleteNote(noteId);
        } else {
            updateDateTime();
            note = new Note(noteId, title, setDate, newDate,
                    favoriteItemBoolean, selectItemForDelBoolean);
            if (noteId > 0) {
                dbAdapter.updateNote(note);
            } else {
                dbAdapter.addNote(note);
            }
        }
        dbAdapter.close();
        //if (!title.equals(note.getTitle()))
    }

    private void updateDateTime () {
        if (isUpdateDateTame || noteId == 0) {
            // Обновляет дату
            setDate = newDate;
        } else {
            // Оставляет старой
            setDate = receivedDate;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goMainActivity();
    }

}