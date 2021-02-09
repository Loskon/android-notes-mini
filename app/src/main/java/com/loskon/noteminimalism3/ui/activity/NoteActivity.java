package com.loskon.noteminimalism3.ui.activity;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.backup.BackupAuto;
import com.loskon.noteminimalism3.backup.BackupPermissions;
import com.loskon.noteminimalism3.db.DbAdapter;
import com.loskon.noteminimalism3.helper.CustomMovementMethod;
import com.loskon.noteminimalism3.helper.FindLinks;
import com.loskon.noteminimalism3.helper.GetLinkify;
import com.loskon.noteminimalism3.helper.GetTextFile;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.MyIntent;
import com.loskon.noteminimalism3.helper.MyKeyboard;
import com.loskon.noteminimalism3.helper.NoteHelper;
import com.loskon.noteminimalism3.helper.sharedpref.GetSharedPref;
import com.loskon.noteminimalism3.helper.snackbars.NoteSnackbar;
import com.loskon.noteminimalism3.model.Note;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogLinks;

import java.util.Date;

import static com.loskon.noteminimalism3.backup.BackupPermissions.REQUEST_CODE_PERMISSIONS;

public class NoteActivity extends AppCompatActivity {

    private DbAdapter dbAdapter;
    private NoteSnackbar noteSbAdapter;
    private Note note;

    private BottomSheetBehavior<View> mBottomSheetBehavior;

    private EditText editTitleText;
    private MaterialButton favorite_button, delete_button, more_button;
    private FloatingActionButton fabNote;
    private LinearLayout linearNote;
    private ConstraintLayout cstLayNote;

    private long noteId = 0;
    private int selNotesCategory;
    private Date receivedDate;

    private boolean isFavItem = false;
    private boolean isDeleteItem = false;
    private boolean isListGoUp = false;
    private boolean isUpdateDateTame = false;
    private boolean isSaveNoteOn = true;
    private boolean isAutoBackupOn = false;
    private boolean isAutoBackupMessage = false;
    private boolean isEditModeOn = false;

    private static CallbackNote callbackNote;

    public void registerCallBackNote(CallbackNote callbackNote) {
        NoteActivity.callbackNote = callbackNote;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        // Меняем цвет статус бара
        MyColor.setColorStatBarAndTaskDesc(this);

        initialiseWidgets();
        toGetAndProcessingData();
        NoteHelper.favoriteStatus(this, isFavItem, favorite_button);
        handlerClickAtSpace();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void handlerClickAtSpace() {
        // Устанавливем фокус на editText при нажатии в любом месте

        linearNote.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                clickOutEditText();
            }
            return false;
        });
    }

    private void clickOutEditText() {

        if (selNotesCategory != 2) {
            int ddd = 0;
            if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                ddd = 300;
            }

            (new Handler()).postDelayed(() -> {
                editMode();

                // Ставим фокус на editText
                editTitleText.requestFocus();

                showSoft(true);

                // Ставим фокус в конце строки
                editTitleText.setSelection(editTitleText.getText().toString().length());
            }, ddd);


        } else {
            noteSbAdapter.showSnackbarReset();
        }
    }

    private void editMode() {
        int color;
        boolean isDarkMode = MyColor.isDarkMode(this);

        if (!isEditModeOn) {
            isEditModeOn = true;

            if (isDarkMode) {
                color = Color.WHITE;
            } else {
                color = Color.BLACK;
            }

            editTitleText.setLinkTextColor(color);
        }
    }

    private void showSoft(boolean isShowSoft) {
        if (isShowSoft) {
            MyKeyboard.showSoftKeyboard(NoteActivity.this, editTitleText);
        } else {
            MyKeyboard.hideSoftKeyboard(NoteActivity.this, editTitleText);
        }
        editTitleText.setCursorVisible(isShowSoft);
        setFocusLayout(!isShowSoft);
    }

    private void clickLink(){
            showSoft(false);
            editTitleText.clearFocus(); // Убираем фокус с editText
            linearNote.requestFocus(); // Делаем запрос на фокусированное состояние linearLayout
    }

    private void initialiseWidgets() {
        editTitleText = findViewById(R.id.title_text);
        cstLayNote = findViewById(R.id.cstLayNote);
        favorite_button = findViewById(R.id.favorite_button);
        delete_button = findViewById(R.id.delete_button);
        more_button = findViewById(R.id.more_button);
        fabNote = findViewById(R.id.fabNote);
        linearNote = findViewById(R.id.linearNote);
        dbAdapter = new DbAdapter(this);
        noteSbAdapter = new NoteSnackbar (this, cstLayNote, fabNote);

        View bottomSheet = findViewById(R.id.layout_bottomSheetBehavior);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        // настройка возможности скрыть элемент при свайпе вниз
        mBottomSheetBehavior.setHideable(false);
        mBottomSheetBehavior.setPeekHeight(0);

        // настройка колбэков при изменениях
        mBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                fabNote.animate().scaleX(1 - slideOffset).scaleY(1 - slideOffset).setDuration(0).start();
            }
        });


        TextView dialog_btn_open2 = bottomSheet.findViewById(R.id.tv);
        TextView dialog_btn_open = bottomSheet.findViewById(R.id.tv_close);
        dialog_btn_open2.setOnClickListener(view -> {
            String string = editTitleText.getText().toString();

            if (BackupPermissions
                    .verifyStoragePermissions(NoteActivity.this, null, true)) {
                createTextFile(string);
            }

            if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        dialog_btn_open.setOnClickListener(view -> {
            if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });



        noteSbAdapter.registerCallBackNote(this::restoreNote);

        MyColor.setColorFab(this, fabNote);
        MyColor.setColorMaterialBtn(this, favorite_button);
        MyColor.setColorMaterialBtn(this, delete_button);
        MyColor.setColorMaterialBtn(this, more_button);

        if (selNotesCategory != 2) {

            int links = GetLinkify.Web(this) | GetLinkify
                    .Mail(this) | GetLinkify.Phone(this);

            editTitleText.setAutoLinkMask(links);
            editTitleText.setLinkTextColor(MyColor.getColorCustom(this));

            editTitleText.setMovementMethod(new CustomMovementMethod() {
                @Override
                public void onLinkClick(String url) {
                    if (!isEditModeOn) {
                        clickLink();
                        handlerLinkClick(url);
                    }
                }

                @Override
                public void onEmptyClick() {
                    clickOutEditText();
                }
            });

            editTitleText.setOnLongClickListener(view -> {
                editMode();
                showSoft(true);
                editTitleText.requestFocus(); // Ставим фокус на editText
                return false;
            });
        }
    }


    private void handlerLinkClick(String url) {
        (new MyDialogLinks(this, url)).callDialog();
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
        dbAdapter.close();

        FindLinks findLinks = new FindLinks(this);

        editTitleText.setText(findLinks.replaceLinks(note.getTitle()));
        receivedDate = note.getDate();
        isFavItem = note.getFavoritesItem();
        isDeleteItem = note.getSelectItemForDel();

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
        isAutoBackupOn = GetSharedPref.isAutoBackup(this);
    }

    public void addNoteClick(View view) {
            restoreNote();
    }

    public void restoreNote() {
        if (selNotesCategory == 2) {
            isUpdateDateTame = true;
            // Восстановление заметки
            dbAdapter.open();
            dbAdapter.updateSelectItemForDel(note, false, updateDateTime(), new Date());
            //dbAdapter.updateNoteDate(note, updateDateTime());
            dbAdapter.close();
        }

        goMainActivity(true);
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
            dbAdapter.updateSelectItemForDel(note, false, updateDateTime(), new Date());
            dbAdapter.updateFavorites(note,false);
        }
        dbAdapter.close();

        isListGoUp = false;
        isSaveNoteOn = false;

        goMainActivity(true);
    }

    public void favoritesNoteClick(View view) {
        isFavItem = !isFavItem;
        NoteHelper.favoriteStatus(this, isFavItem, favorite_button);
    }

    public void otherClick(View view) {
       // String string = editTitleText.getText().toString();

        //if (BackupPermissions
            //    .verifyStoragePermissions(this, null, true)) {
            //createTextFile(string);
       // }
        MyKeyboard.hideSoftKeyboard(NoteActivity.this, editTitleText);

        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            (new Handler()).postDelayed(() -> {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }, 300);
        }
    }

    private void createTextFile(String string) {
        if (!string.trim().isEmpty()) {
            (new GetTextFile(this)).createTextFile(string);
        } else {
            noteSbAdapter.showSnackbarErrors(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String string = editTitleText.getText().toString();
                createTextFile(string);
            } else {
                noteSbAdapter.showSnackbarErrors(false);
            }
        }
    }

    private void goMainActivity(boolean isButtonClick) {
        isAutoBackupMessage = true;

        if (editTitleText.getText().toString().trim().length() == 0) {
            isListGoUp = false;
        }

        if (callbackNote != null) {
            callbackNote.callingBackNote(isListGoUp);
        }

        MyKeyboard.hideSoftKeyboard(this, editTitleText);
        MyIntent.goMainActivityFromNote(this, isButtonClick);
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
        if (title.isEmpty()) {
            dbAdapter.deleteNote(noteId); // Удаление навсегда пустой заметки
        } else {

            note = new Note(noteId, title, updateDateTime(), new Date(),
                    isFavItem, isDeleteItem);

            if (noteId > 0) {
                dbAdapter.updateNote(note);
            } else {
                noteId = dbAdapter.addNewNote(note);
                isUpdateDateTame = true;
            }

            if (noteId % 5 == 0 && isAutoBackupOn) {
                (new BackupAuto(this)).callAutoBackup(isAutoBackupMessage);
            }
        }
        dbAdapter.close();

        //if (!title.equals(note.getTitle()))
    }

    private Date updateDateTime () {
        if (isUpdateDateTame || noteId == 0) {
            return new Date();  // Обновляет дату
        } else {
            return receivedDate; // Оставляет старой
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        goMainActivity(false);
    }

    public interface CallbackNote {
        void callingBackNote(boolean isListGoUp);
    }

}