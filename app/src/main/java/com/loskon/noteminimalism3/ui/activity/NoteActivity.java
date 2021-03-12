package com.loskon.noteminimalism3.ui.activity;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.note.BottomSheetHelper;
import com.loskon.noteminimalism3.auxiliary.note.FindLinks;
import com.loskon.noteminimalism3.auxiliary.note.MyKeyboard;
import com.loskon.noteminimalism3.auxiliary.note.NoteHelper;
import com.loskon.noteminimalism3.auxiliary.note.NoteHelperLinks;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.auxiliary.other.MyIntent;
import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref;
import com.loskon.noteminimalism3.backup.prime.BackupAuto;
import com.loskon.noteminimalism3.db.DbAdapter;
import com.loskon.noteminimalism3.model.Note;
import com.loskon.noteminimalism3.ui.snackbars.MySnackbarNoteMessage;
import com.loskon.noteminimalism3.ui.snackbars.MySnackbarNoteReset;
import com.loskon.noteminimalism3.ui.snackbars.SnackbarBuilder;

import java.util.Date;

import static com.loskon.noteminimalism3.auxiliary.other.RequestCode.REQUEST_CODE_PERMISSIONS;

/**
 * Класс для работы с выбранной заметкой
 */

public class NoteActivity extends AppCompatActivity {

    private DbAdapter dbAdapter;
    private Note note;

    private FindLinks findLinks;
    private BottomSheetHelper bottomSheetHelper;
    private NoteHelper noteHelper;
    private NoteHelperLinks helperLinks;
    private MySnackbarNoteMessage mySnackbarNoteMessage;
    private MySnackbarNoteReset noteSbAdapter;

    private final Handler handler = new Handler();

    private ConstraintLayout cstLayNote;
    private LinearLayout linearNote;
    private MaterialButton btnFav, btnDel, btnMore;
    private FloatingActionButton fabNote;
    private EditText editText;

    private long noteId = 0;
    private Date receivedDate, autoBackupDate;
    private int selNotesCategory;
    private String title, textFromDb;

    // for new Note
    private boolean isAutoBackup = false;
    private boolean isNewNote = false;

    // for old Note
    private boolean isFavItem = false;
    private boolean isListGoUp = false;
    private boolean isUpdateDate = false;
    private boolean isUpdateDateTimeWhenChanges = false;
    private boolean isSaveNote = true;
    private boolean isShowToast = false;

    private static CallbackNote callbackNote;

    public static void regCallbackNote(CallbackNote callbackNote) {
        NoteActivity.callbackNote = callbackNote;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyColor.setDarkTheme(GetSharedPref.isDarkMode(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        MyColor.setColorStatBarAndTaskDesc(this);

        getIdAndCategory();
        initialiseWidgets();
        initialiseAdapters();
        initialiseConfigureWidgets();
        initialiseColors();
        buildNote();
        handlerOutClick();
    }

    private void getIdAndCategory() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            noteId = extras.getLong(MyIntent.PUT_EXTRA_ID);
            selNotesCategory = extras.getInt(MyIntent.PUT_EXTRA_SEL_NOTE_CATEGORY);
        }
    }

    private void initialiseWidgets() {
        editText = findViewById(R.id.title_text);
        cstLayNote = findViewById(R.id.cstLayNote);
        btnFav = findViewById(R.id.favorite_button);
        btnDel = findViewById(R.id.delete_button);
        btnMore = findViewById(R.id.more_button);
        fabNote = findViewById(R.id.fabNote);
        linearNote = findViewById(R.id.linearNote);
    }

    private void initialiseAdapters() {
        dbAdapter = new DbAdapter(this);
        noteHelper = new NoteHelper(this);

        if (selNotesCategory != 2) {
            mySnackbarNoteMessage = new MySnackbarNoteMessage(this, cstLayNote);
            bottomSheetHelper = new BottomSheetHelper(this);
            helperLinks = new NoteHelperLinks(this);
            if (noteId != 0) helperLinks.setLinks();
        } else {
            noteSbAdapter = new MySnackbarNoteReset(this, cstLayNote);
        }

        if (noteId != 0 && selNotesCategory != 2) findLinks = new FindLinks(this);
    }

    private void initialiseConfigureWidgets() {
        int fontSizeNotes = GetSharedPref.getFontSizeNote(this);
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSizeNotes);
    }

    private void setFavoriteStatus() {
        noteHelper.favoriteStatus(isFavItem, btnFav);
    }

    private void initialiseColors() {
        MyColor.setColorFab(this, fabNote);
        MyColor.setColorMaterialBtn(this, btnFav);
        MyColor.setColorMaterialBtn(this, btnDel);
        MyColor.setColorMaterialBtn(this, btnMore);
    }

    private void buildNote() {
        if (noteId == 0) {
            createNewNote();
        } else {
            existingNote();
            setSettingsNote();
        }

        if (selNotesCategory != 2) setFavoriteStatus();

        handlerScrollView();
        handlerEditTextClick();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void createNewNote() {
        // Новая заметка
        isAutoBackup = GetSharedPref.isAutoBackup(this);

        if (selNotesCategory == 1) isFavItem = true;

        isNewNote = true;
        isListGoUp = true;
    }

    private void existingNote() {
        // Старая заметка
        isUpdateDateTimeWhenChanges = GetSharedPref.isUpdateDateTameWhenChanges(this);

        dbAdapter.open();
        note = dbAdapter.getNote(noteId);
        dbAdapter.close();

        textFromDb = getMyText();

        editText.setText(textFromDb);
        receivedDate = note.getDate();
        isFavItem = note.getFavoritesItem();
    }

    private String getMyText() {
        String text;

        if (selNotesCategory == 2) {
            text = note.getTitle();
        } else {
            text = findLinks.getLinks(note.getTitle());
        }

        return text;
    }

    private void handlerEditTextClick() {
        noteHelper.handlerEditTextClick();
        if (selNotesCategory != 2) noteHelper.handlerLongEditTextClick();
    }

    private void handlerScrollView() {
        ScrollView scrollView = findViewById(R.id.scroll);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(this::hideBottomSheet);
    }

    private void setSettingsNote() {
        if (selNotesCategory == 2) {
            noteHelper.redrawingForTrash();
        } else {
            delFocusFromEditText();
        }
    }

    private void delFocusFromEditText() {
        noteHelper.setFocusableLayout(true); // Ставим фокус на linearLayout
        editText.clearFocus(); // Убираем фокус с editText
        linearNote.requestFocus(); // Делаем запрос на фокусированное состояние linearLayout
    }

    @SuppressLint("ClickableViewAccessibility")
    private void handlerOutClick() {
        linearNote.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                onOutClick();
            }
            return false;
        });
    }

    public void onOutClick() {
        if (selNotesCategory != 2) {
            eventOutClick();
        } else {
            noteSbAdapter.show();
        }
    }

    private void eventOutClick() {
        handler.removeCallbacksAndMessages(null);
        SnackbarBuilder.close();
        hideBottomSheet();

        (new Handler()).postDelayed(() -> {
            hideBottomSheet();
            helperLinks.changeColorLinks();
            editText.requestFocus();
            noteHelper.showKeyboard(true);
        }, 300);

        editText.setSelection(editText.getText().toString().length());
    }

    public void hideBottomSheet() {
        bottomSheetHelper.hideBottomSheet();
    }

    public void addNoteClick(View view) {
        restoreNote();
    }

    public void restoreNote() {
        if (selNotesCategory == 2) {
            isUpdateDate = true;
            // Восстановление заметки
            dbAdapter.open();
            dbAdapter.updateSelectItemForDel(note, false, updateDateTime(), new Date());
            dbAdapter.close();
        }

        goMainActivity(true);
    }

    public void deleteNoteClick(View view) {
        isListGoUp = false;
        isSaveNote = false;

        dbAdapter.open();
        if (selNotesCategory == 2 || noteId == 0) {
            dbAdapter.deleteNote(noteId);
        } else {
            sendInTrash();
        }
        dbAdapter.close();

        goMainActivity(true);
    }

    private void sendInTrash() {
        note.setSelectItemForDel(true);
        note.setFavoritesItem(false);
        dbAdapter.updateSelectItemForDel(note, false, updateDateTime(), new Date());
        dbAdapter.updateFavorites(note, false);
    }

    public void favoritesNoteClick(View view) {
        isFavItem = !isFavItem;
        setFavoriteStatus();
    }

    public void moreClick(View view) {
        MyKeyboard.hideSoftKeyboard(this, editText);
        handler.postDelayed(() -> {
            SnackbarBuilder.close();
            bottomSheetHelper.showBottomSheet();
        }, 300);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                bottomSheetHelper.goSaveTextFile();
            } else {
                mySnackbarNoteMessage.show(false, MySnackbarNoteMessage.MSG_TEXT_NO_PERMISSION_NOTE);
            }
        }
    }

    private void goMainActivity(boolean isButtonClick) {
        isShowToast = true;
        if (editText.getText().toString().isEmpty()) isListGoUp = false;

        callbackNote.onCallBack(isListGoUp);

        MyKeyboard.hideSoftKeyboard(this, editText);
        MyIntent.goMainActivityFromNote(this, isButtonClick);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Защита от сохранения при удалении
        if (selNotesCategory != 2 && isSaveNote) saveNote();
    }

    private void saveNote() {
        title = editText.getText().toString();

        dbAdapter.open();
        if (title.trim().isEmpty()) {
            dbAdapter.deleteNote(noteId);
        } else {
            editNote();
        }
        dbAdapter.close();
    }

    private void editNote() {
        if (noteId == 0) {
            addNewNote();
        } else {
            updateNote();
        }

        callAutoBackup();
    }

    private void addNewNote() {
        getMyNote();
        noteId = dbAdapter.addNewNote(note);

        autoBackupDate = new Date();
        isUpdateDate = true;
    }

    private void updateNote() {
        if (!isNewNote) updateDateNote();

        getMyNote();
        dbAdapter.updateNote(note);
    }

    private void updateDateNote() {
        boolean isTitleChanged = !title.trim().equals(textFromDb.trim());
        if (isTitleChanged && isUpdateDateTimeWhenChanges) isUpdateDate = true;
    }

    private void getMyNote() {
        Date date = updateDateTime();
        note = new Note(noteId, title, date, date, isFavItem);
    }

    private void callAutoBackup() {
        if (noteId % 4 == 0 && isAutoBackup && isNewNote) {
            (new BackupAuto(this)).buildBackup(isShowToast, autoBackupDate);
        }
    }

    private Date updateDateTime() {
        if (isUpdateDate || noteId == 0) {
            return new Date();  // Обновляет дату
        } else {
            return receivedDate; // Оставляет старой
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (selNotesCategory != 2) {
                if (bottomSheetHelper.isVisibleBottomSheet()) {
                    hideBottomSheet();
                    return false;
                } else {
                    goMainActivity(false);
                    return true;
                }
            } else {
                goMainActivity(false);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public NoteHelper getNoteHelper() {
        return noteHelper;
    }

    public LinearLayout getLinearNote() {
        return linearNote;
    }

    public EditText getEditText() {
        return editText;
    }

    public long getNoteId() {
        return noteId;
    }

    public FloatingActionButton getFabNote() {
        return fabNote;
    }

    public MySnackbarNoteMessage getMySnackbarNoteMessage() {
        return mySnackbarNoteMessage;
    }

    public NoteHelperLinks getHelperLinks() {
        return helperLinks;
    }

    public int getSelNotesCategory() {
        return selNotesCategory;
    }

    public MySnackbarNoteReset getNoteSbAdapter() {
        return noteSbAdapter;
    }

    public MaterialButton getBtnFav() {
        return btnFav;
    }

    public MaterialButton getBtnDel() {
        return btnDel;
    }

    public MaterialButton getBtnMore() {
        return btnMore;
    }

    public interface CallbackNote {
        void onCallBack(boolean isListGoUp);
    }
}