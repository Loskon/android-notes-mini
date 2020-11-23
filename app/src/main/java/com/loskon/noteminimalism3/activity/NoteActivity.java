package com.loskon.noteminimalism3.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
    private boolean delOrCreate = true;
    private int selectedNoteMode;
    private FloatingActionButton fabNote;
    private Note note;

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

    public void saveClick(View view){
        if (selectedNoteMode == 2) {
            dbAdapter.open();
            dbAdapter.updateSelectItemForDel(note, false, note.getDate());
            dbAdapter.close();
            delOrCreate = false;
        }
        goMainActivity();
    }

    public void deleteClick(View view) {
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
        delOrCreate = false;
        goMainActivity();
    }

    public void favoritesClick (View view) {
        favoriteItemBoolean = !favoriteItemBoolean;
        favoriteStatus();
    }

    private void goMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("delOrCreate", delOrCreate);
        intent.putExtra("updateDate", true);
        startActivity(intent);
    }

    private void save () {
        String title = editTitleText.getText().toString();
        Note note = new Note(noteId, title, new Date(),  new Date(),
                favoriteItemBoolean, selectItemForDelBoolean);
        dbAdapter.open();
        if (noteId > 0) {
            dbAdapter.updateNote(note);
        } else {
            dbAdapter.addNote(note);
        }
        dbAdapter.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Защита от случайного сохранения
        if (selectedNoteMode !=2 && delOrCreate) save();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goMainActivity();
    }

}