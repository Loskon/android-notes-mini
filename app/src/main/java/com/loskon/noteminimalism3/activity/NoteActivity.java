package com.loskon.noteminimalism3.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loskon.noteminimalism3.model.Note;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.db.DbAdapter;

import java.util.Date;

public class NoteActivity extends AppCompatActivity {

    private EditText nameBox;
    private Button saveButton;
    private Button button;
    private DbAdapter dbAdapter;
    private long noteId = 0;
    private boolean favoriteItemBoolean = false;
    private boolean selectItemForDelBoolean = false;
    //private boolean isUpdateDate = true;
    private int selectedNoteMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        initViewAndAdapter();
        toGetData();
        favoriteStatus();
    }

    private void initViewAndAdapter() {
        nameBox =  findViewById(R.id.name);
        saveButton =  findViewById(R.id.saveButton);
        button =  findViewById(R.id.button);
        dbAdapter = new DbAdapter(this);
    }

    private void toGetData () {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            noteId = extras.getLong("id");
            selectedNoteMode = extras.getInt("selectedNoteMode");
        }
        // если 0, то добавление
        if (noteId > 0) {
            // получаем элемент по id из бд
            dbAdapter.open();
            Note note = dbAdapter.getNote(noteId);
            nameBox.setText(note.getTitle());
            favoriteItemBoolean = note.getFavoritesItem();
            selectItemForDelBoolean = note.getSelectItemForDel();
            dbAdapter.close();
        }
    }

    public void favoriteStatus() {
        if (favoriteItemBoolean) button.setText("В избранном");
        else button.setText("Нет");
    }

    public void save(View view){
        String title = nameBox.getText().toString();
        Note note = new Note(noteId, title, new Date(),  new Date(),
                favoriteItemBoolean, selectItemForDelBoolean);

        dbAdapter.open();
        if (noteId > 0) {
            dbAdapter.updateNote(note);
        } else {
            dbAdapter.addNote(note);
        }
        dbAdapter.close();
        goMainActivity();
    }

    public void deleteClick(View view){
        dbAdapter.open();
        dbAdapter.deleteNote(noteId);
        dbAdapter.close();
        goMainActivity();
    }

    public void favoritesClick (View view) {
        favoriteItemBoolean = !favoriteItemBoolean;
        favoriteStatus();
    }

    private void goMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("updateDate", true);
        startActivity(intent);
    }
}