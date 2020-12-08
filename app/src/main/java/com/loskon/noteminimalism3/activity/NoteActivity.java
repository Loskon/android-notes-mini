package com.loskon.noteminimalism3.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loskon.noteminimalism3.activity.mainHelper.ColorHelper;
import com.loskon.noteminimalism3.activity.mainHelper.MainHelper;
import com.loskon.noteminimalism3.activity.noteHelper.NoteHelperTwo;
import com.loskon.noteminimalism3.model.Note;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.db.DbAdapter;

import java.util.Date;

public class NoteActivity extends AppCompatActivity {

    private DbAdapter dbAdapter;
    private Note note;

    private EditText editTitleText;
    private MaterialButton favorite_button, delete_button, more_button;
    private FloatingActionButton fabNote;

    private long noteId = 0;
    private int selNotesCategory;
    private boolean isFavItem = false;
    private boolean isDeleteItem = false;
    private boolean isListUp = false;
    private boolean isUpdateDateTame = false;
    private boolean isSaveNoteOn = true;
    private final Date nowDate = new Date();
    private Date receivedDate, setDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        // Меняем цвет статус бара
        ColorHelper.setColorStatBarAndNavView(this);

        initViewAndAdapter();
        toGetAndProcessingData();
        favoriteStatusStar();
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
            selNotesCategory = extras.getInt("selectedNoteMode");
        }

        if (noteId > 0) {
            existingNote();
        } else {
            createNewNote();
        }
    }

    private void existingNote() {
        // Получение текста, значений избранного и удаленного
        dbAdapter.open();
        note = dbAdapter.getNote(noteId);
        editTitleText.setText(note.getTitle());
        receivedDate = note.getDate();
        isFavItem = note.getFavoritesItem();
        isDeleteItem = note.getSelectItemForDel();
        dbAdapter.close();

        // Специальная перерисовка для корзины
        if (selNotesCategory == 2) redrawingForBasket();
    }

    private void redrawingForBasket() {
        favorite_button.setVisibility(View.INVISIBLE);
        more_button.setVisibility(View.INVISIBLE);
        fabNote.setImageResource(R.drawable.baseline_restore_from_trash_black_24);
        delete_button.setIcon(ResourcesCompat.getDrawable(getResources(),
                R.drawable.baseline_delete_forever_black_24, null));
    }

    private void createNewNote() {
        if (selNotesCategory == 1) {
            isFavItem = true;
        }
        more_button.setVisibility(View.GONE);
        isListUp = true;
    }

    private void favoriteStatusStar() {
        if (isFavItem) {
            favorite_button.setIcon(ResourcesCompat.getDrawable(getResources(),
                    R.drawable.baseline_star_black_24, null));
        } else {
            favorite_button.setIcon(ResourcesCompat.getDrawable(getResources(),
                    R.drawable.baseline_star_border_black_24, null));
        }
    }

    public void addNoteClick(View view){
        if (selNotesCategory == 2) {
            dbAdapter.open();
            dbAdapter.updateSelectItemForDel(note, false, note.getDate());
            dbAdapter.close();
        }
        goMainActivity();
    }

    public void deleteNoteClick(View view) {
        dbAdapter.open();
        if (selNotesCategory == 2 || noteId == 0) {
            // Удаляет навсегда не сохраненную заметку, либо заметку из мусорки
            dbAdapter.deleteNote(noteId);
        } else {
            note.setSelectItemForDel(true);
            note.setFavoritesItem(false);
            dbAdapter.updateSelectItemForDel(note, true, nowDate);
            dbAdapter.updateFavorites(note,false);
        }
        dbAdapter.close();
        isListUp = false;
        isSaveNoteOn = false;
        goMainActivity();
    }

    public void favoritesNoteClick(View view) {
        isFavItem = !isFavItem;
        favoriteStatusStar();
    }

    private void goMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("createOrDel", isListUp);
        intent.putExtra("updateDate", true);
        NoteHelperTwo.hideSoftKeyboard(this);
        (new Handler()).postDelayed(() -> {
            startActivity(intent);
        }, 50);

    }



    @Override
    protected void onPause() {
        super.onPause();
        // Защита от случайного сохранения
        if (selNotesCategory !=2 && isSaveNoteOn) saveNote();
    }

    private void saveNote() {
        String title = editTitleText.getText().toString();

        dbAdapter.open();

        if (title.trim().length() == 0) {
            dbAdapter.deleteNote(noteId); // Удаление навсегда пустой заметки
        } else {

            updateDateTime();

            note = new Note(noteId, title, setDate, nowDate,
                    isFavItem, isDeleteItem);

            if (noteId > 0) {
                dbAdapter.updateNote(note);
            } else {
                dbAdapter.addNewNote(note);
            }
        }

        dbAdapter.close();
        //if (!title.equals(note.getTitle()))
    }

    private void updateDateTime () {
        if (isUpdateDateTame || noteId == 0) {
            setDate = nowDate;  // Обновляет дату
        } else {
            setDate = receivedDate; // Оставляет старой
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goMainActivity();
    }

}