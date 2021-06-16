package com.loskon.noteminimalism3.ui.activities;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.note.FindLinks;
import com.loskon.noteminimalism3.auxiliary.note.MyKeyboard;
import com.loskon.noteminimalism3.auxiliary.note.NoteHelper;
import com.loskon.noteminimalism3.auxiliary.note.NoteHelperLinks;
import com.loskon.noteminimalism3.auxiliary.note.TextAssistant;
import com.loskon.noteminimalism3.auxiliary.other.AppFontManager;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.auxiliary.other.MyDate;
import com.loskon.noteminimalism3.auxiliary.other.MyIntent;
import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MySharedPref;
import com.loskon.noteminimalism3.backup.prime.BackupAuto;
import com.loskon.noteminimalism3.database.DbAdapter;
import com.loskon.noteminimalism3.model.Note;
import com.loskon.noteminimalism3.ui.sheets.SheetCustomNote;
import com.loskon.noteminimalism3.ui.snackbars.MySnackbarNoteMessage;
import com.loskon.noteminimalism3.ui.snackbars.MySnackbarNoteReset;
import com.loskon.noteminimalism3.ui.snackbars.SnackbarBuilder;
import com.loskon.noteminimalism3.ui.widgets.note.AppWidgetConfigure;
import com.loskon.noteminimalism3.ui.widgets.note.AppWidgetNoteProvider;

import java.util.Date;

import static com.loskon.noteminimalism3.auxiliary.other.RequestCode.REQUEST_CODE_PERMISSIONS;

/**
 * Класс для работы с выбранной заметкой
 */

public class NoteActivity extends AppCompatActivity {

    private DbAdapter dbAdapter;
    private Note note;
    private ActionMode actionMode;

    private FindLinks findLinks;
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

    private int position = 0;
    private long noteId = 0;
    private int selNotesCategory = 0;
    private String title, textFromDb;
    private String textDateMod = null;
    private Date receivedDate, autoBackupDate, dateMod, dateFinally;

    // false
    private boolean isAutoBackup = false;
    private boolean isNewNote = false;
    private boolean isFavItem = false;
    private boolean isListGoUp = false;
    private boolean isUpdateDate = false;
    private boolean isUpdateDateTimeWhenChanges = false;
    private boolean isShowToast = false;
    private boolean isWidget = false;
    // true
    private boolean isSaveNote = true;

    private static CallbackNote callbackNote;
    private static CallbackNoteWidget callbackNoteWidget;

    public static void regCallbackNote(CallbackNote callbackNote) {
        NoteActivity.callbackNote = callbackNote;
    }

    public static void regCallbackNoteWidget(CallbackNoteWidget callbackNoteWidget) {
        NoteActivity.callbackNoteWidget = callbackNoteWidget;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyColor.setDarkTheme(GetSharedPref.isDarkMode(this));
        AppFontManager.setFont(this);
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
            isWidget = extras.getBoolean(MyIntent.PUT_IS_WIDGET);
            selNotesCategory = extras.getInt(MyIntent.PUT_EXTRA_SEL_NOTE_CATEGORY);
            position = extras.getInt(MyIntent.PUT_EXTRA_POSITION);
        }
    }

    private void initialiseWidgets() {
        editText = findViewById(R.id.et_note_title);
        cstLayNote = findViewById(R.id.cstLayNote);
        btnFav = findViewById(R.id.btnFavNote);
        btnDel = findViewById(R.id.btnDelNote);
        btnMore = findViewById(R.id.btnMoreNote);
        fabNote = findViewById(R.id.fabNote);
        linearNote = findViewById(R.id.linLytNote);
    }

    private void initialiseAdapters() {
        dbAdapter = new DbAdapter(this);
        noteHelper = new NoteHelper(this);

        if (selNotesCategory != 2) {
            mySnackbarNoteMessage = new MySnackbarNoteMessage(this, cstLayNote);
            helperLinks = new NoteHelperLinks(this);

            if (noteId != 0) {
                helperLinks.setLinks();
                findLinks = new FindLinks(this);
            }
        } else {
            noteSbAdapter = new MySnackbarNoteReset(this, cstLayNote);
        }
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

        handlerEditTextClick();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void createNewNote() {
        // Новая заметка
        isAutoBackup = GetSharedPref.isAutoBackup(this);

        if (selNotesCategory == 1) isFavItem = true;

        isNewNote = true;
        isListGoUp = true;

        editText.requestFocus(); // for api >29
    }

    private void existingNote() {
        // Старая заметка
        isUpdateDateTimeWhenChanges = GetSharedPref.isUpdateDateTameWhenChanges(this);

        dbAdapter.open();
        note = dbAdapter.getNote(noteId);
        dbAdapter.close();

        textFromDb = getMyText();
        textDateMod = MyDate.getNowDate(note.getDateMod());

        editText.setText(textFromDb);
        receivedDate = note.getDate();
        dateMod = note.getDateMod();
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

        goClick();

        editText.setSelection(editText.getText().toString().length());
    }

    public void goClick() {
        SnackbarBuilder.close();
        helperLinks.changeColorLinks();
        editText.requestFocus();
        noteHelper.showKeyboard(true);
    }

    public void addNoteClick(View view) {
        restoreNote();
    }

    public void restoreNote() {
        if (selNotesCategory == 2) {
            if (callbackNote != null) callbackNote.onCallBackDelete(position);
            isUpdateDate = true;
            // Восстановление заметки
            dbAdapter.open();
            dbAdapter.updateItemDel(note, false, new Date(), new Date());
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

        updateWidget(true);

        if (callbackNote != null) callbackNote.onCallBackDelete(position);
        goMainActivity(true);
    }

    private void sendInTrash() {
        note.setSelectItemForDel(true);
        note.setFavoritesItem(false);
        dbAdapter.updateItemDel(note, false, new Date(), new Date());
        dbAdapter.updateFavorites(note, false);
    }

    public void favoritesNoteClick(View view) {
        isFavItem = !isFavItem;
        setFavoriteStatus();
    }

    public void moreClick(View view) {
        SnackbarBuilder.close();
        MyKeyboard.hideSoftKeyboard(this, editText);
        closeActionMode();
        editText.setSelection(editText.getSelectionEnd());
        handler.postDelayed(() -> new SheetCustomNote(this).show(textDateMod, noteId), 300);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                (new TextAssistant(this)).goSaveTextFile();
            } else {
                mySnackbarNoteMessage.show(false,
                        MySnackbarNoteMessage.MSG_TEXT_NO_PERMISSION_NOTE);
            }
        }
    }

    private void goMainActivity(boolean isButtonClick) {
        isShowToast = true;
        if (editText.getText().toString().isEmpty()) isListGoUp = false;

        if (callbackNote != null) callbackNote.onCallBack(isListGoUp);

        MyKeyboard.hideSoftKeyboard(this, editText);

        if (isWidget) {
            finish();
        } else {
            MyIntent.goMainActivityFromNote(this, isButtonClick);
        }
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

        if (callbackNoteWidget != null) {
            callbackNoteWidget.onCallBackWidget(title, noteId, MyDate.getNowDate(dateFinally));
        }
    }

    private void updateNote() {
        if (!isNewNote) updateDateNote();

        getMyNote();
        dbAdapter.updateNote(note);

        updateWidget(false);
    }

    private void updateWidget(boolean isDelete) {
        int appWidgetId = MySharedPref.getCustomInt(this, noteId);

        if (appWidgetId != -1) {
            String dateWidget = MyDate.getNowDate(dateFinally);

            if (isDelete) {
                title = getString(R.string.note_deleted);
                dateWidget = "";
            }

            AppWidgetNoteProvider.updateAppWidget(
                    this, AppWidgetManager.getInstance(this),
                    appWidgetId, title, noteId, dateWidget, isDelete);

            if (isDelete) AppWidgetConfigure.deleteTitlePref(this, appWidgetId);
        }
    }

    private void updateDateNote() {
        boolean isTitleChanged = !title.trim().equals(textFromDb.trim());
        if (isTitleChanged && isUpdateDateTimeWhenChanges) isUpdateDate = true;
        if (isTitleChanged) dateMod = new Date();
    }

    private void getMyNote() {
        dateFinally = getTime();
        if (noteId == 0) dateMod = dateFinally;
        note = new Note(noteId, title, dateFinally, dateMod, dateFinally, isFavItem);
    }

    private void callAutoBackup() {
        if (noteId % 3 == 0 && isAutoBackup && isNewNote) {
            (new BackupAuto(this)).buildBackup(isShowToast, autoBackupDate);
        }
    }

    private Date getTime() {
        if (isUpdateDate || noteId == 0) {
            return new Date();  // Обновляет дату
        } else {
            return receivedDate; // Оставляет старой
        }
    }

    @Override
    public void onActionModeStarted(ActionMode actionMode) {
        super.onActionModeStarted(actionMode);
        this.actionMode = actionMode;
    }

    public void closeActionMode() {
        if (actionMode != null) actionMode.finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        goMainActivity(false);
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

        void onCallBackDelete(int position);
    }

    public interface CallbackNoteWidget {
        void onCallBackWidget(String string, long id, String date);
    }
}