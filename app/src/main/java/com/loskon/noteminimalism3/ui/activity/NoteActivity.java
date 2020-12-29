package com.loskon.noteminimalism3.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.MyIntent;
import com.loskon.noteminimalism3.helper.MyKeyboard;
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
    private LinearLayout linearNote;

    private long noteId = 0;
    private int selNotesCategory;
    private boolean isFavItem = false;
    private boolean isDeleteItem = false;
    private boolean isListGoUp = false;
    private boolean isUpdateDateTame = false;
    private boolean isSaveNoteOn = true;
    private final Date nowDate = new Date();
    private Date receivedDate, setDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        // Меняем цвет статус бара
        MyColor.setColorStatBarAndTaskDesc(this);

        initialiseWidgets();
        toGetAndProcessingData();
        favoriteStatus();
        handlerClickAtSpace();
    }

    private void handlerClickAtSpace() {
        // Устанавливем фокус на editText при нажатии в любом месте
        linearNote.setOnClickListener(v -> {
            if (selNotesCategory != 2) {
                setFocusLayout(false);
                // Ставим фокус на editText
                editTitleText.requestFocus();
                // Вызов клавиатуры
                MyKeyboard.showSoftKeyboard(this, editTitleText);
                // Ставим фокус в конце строки
                editTitleText.setSelection(editTitleText.getText().toString().trim().length());
            } else {
                Snackbar.make(
                        findViewById(R.id.cstLayoutNote),
                        "Заметка находится в корзине",
                        Snackbar.LENGTH_SHORT)
                        .setAnchorView(fabNote)
                        .setAction("Восстановить", view -> {
                            restoreNote();
                        })
                        .show();
            }
        });
    }

    private void initialiseWidgets() {
        editTitleText = findViewById(R.id.title_text);
        favorite_button = findViewById(R.id.favorite_button);
        delete_button = findViewById(R.id.delete_button);
        more_button = findViewById(R.id.more_button);
        fabNote = findViewById(R.id.fabNote);
        linearNote = findViewById(R.id.linearNote);
        dbAdapter = new DbAdapter(this);

        MyColor.setColorFab(this, fabNote);
        MyColor.setColorMaterialBtn(this, favorite_button);
        MyColor.setColorMaterialBtn(this, delete_button);
        MyColor.setColorMaterialBtn(this, more_button);
        //EditTextTint.setCursorDrawableColor(editTitleText, Color.RED);
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
        // Получение
        dbAdapter.open();

        note = dbAdapter.getNote(noteId);

        editTitleText.setText(note.getTitle());
        receivedDate = note.getDate();
        isFavItem = note.getFavoritesItem();
        isDeleteItem = note.getSelectItemForDel();

        dbAdapter.close();

        if (selNotesCategory == 2) redrawingForTrash();

        setFocusLayout(true); // Ставим фокус на linearLayout
        editTitleText.clearFocus(); // Убираем фокус с editText
        linearNote.requestFocus(); // Делаем запрос на фокусированное состояние linearLayout
    }

    private void setFocusLayout (boolean isFocusOn) {
        linearNote.setFocusable(isFocusOn);
        linearNote.setFocusableInTouchMode(isFocusOn);
    }

    private void redrawingForTrash() {
        // Изменение вида заметки для мусорки
        editTitleText.setEnabled(false); // Запрещаем что либо нажимать

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
        isListGoUp = true;
    }

    private void favoriteStatus() {
        if (isFavItem) {
            favorite_button.setIcon(ResourcesCompat.getDrawable(getResources(),
                    R.drawable.baseline_star_black_24, null));
        } else {
            favorite_button.setIcon(ResourcesCompat.getDrawable(getResources(),
                    R.drawable.baseline_star_border_black_24, null));
        }
    }

    public void addNoteClick(View view) {
            restoreNote();
    }

    private void restoreNote() {
        if (selNotesCategory == 2) {
            // Восстановление заметки
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
            // Отправляет заметку в корзину
            note.setSelectItemForDel(true);
            note.setFavoritesItem(false);
            dbAdapter.updateSelectItemForDel(note, true, nowDate);
            dbAdapter.updateFavorites(note,false);
        }
        dbAdapter.close();

        isListGoUp = false;
        isSaveNoteOn = false;

        goMainActivity();
    }

    public void favoritesNoteClick(View view) {
        isFavItem = !isFavItem;
        favoriteStatus();
    }

    public void otherClick(View view) {
           Toast.makeText(this, "Клик", Toast.LENGTH_SHORT).show();
    }

    private void goMainActivity() {
        MyIntent.goMainActivityFromNote(this, isListGoUp);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Защита от сохранения при удалении
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
                //callbackNote.callingBackNote(note);
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