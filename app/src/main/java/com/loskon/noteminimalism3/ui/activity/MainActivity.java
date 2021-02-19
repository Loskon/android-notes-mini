package com.loskon.noteminimalism3.ui.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.backup.main.BpCloud;
import com.loskon.noteminimalism3.db.DbAdapter;
import com.loskon.noteminimalism3.helper.CheckEmptyRecyclerView;
import com.loskon.noteminimalism3.helper.MainHelper;
import com.loskon.noteminimalism3.helper.MainHelperTwo;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.MyIntent;
import com.loskon.noteminimalism3.helper.Refresh;
import com.loskon.noteminimalism3.helper.sharedpref.GetSharedPref;
import com.loskon.noteminimalism3.helper.sharedpref.MyPrefKey;
import com.loskon.noteminimalism3.helper.sharedpref.MySharedPref;
import com.loskon.noteminimalism3.ui.snackbars.MySnackbarMain;
import com.loskon.noteminimalism3.model.Note;
import com.loskon.noteminimalism3.rv.CallbackDelMode;
import com.loskon.noteminimalism3.rv.MyRecyclerViewAdapter;
import com.loskon.noteminimalism3.ui.fragments.MySettingsAppFragment;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogBottomSheet;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogColor;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogRestore;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogTrash;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogUnification;
import com.loskon.noteminimalism3.ui.preference.PrefCardView;
import com.loskon.noteminimalism3.ui.preference.PrefNumOfLines;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements CallbackDelMode, MyDialogBottomSheet.ItemClickListenerBottomNavView {

    private DbAdapter dbAdapter;
    private MyRecyclerViewAdapter rvAdapter;
    private MainHelperTwo mhAdapter;
    private MyDialogTrash myDialogTrash;
    private MyDialogUnification myDialogUnification;
    private MySnackbarMain mySnackbarMain;

    private RecyclerView recyclerView;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fabMain;
    private TextView textNumSelItem;
    private SearchView searchView;

    private boolean isTypeNotesSingle;
    private boolean isDeleteMode;
    private boolean isUpdateDate;
    private boolean isListGoUp, isOneSizeOn, isSearchOn;
    private int selNotesCategory, numOfLines;
    private int fontSize, dateFontSize, color;
    private int rangeInDays;
    private String whereClauseForMode;

    // Переменные для сохранения состояния RecyclerView
    private final static String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyColor.setDarkTheme(GetSharedPref.isDarkMode(this));
        setContentView(R.layout.activity_main);

        startSettings(savedInstanceState);
        initialiseWidgets();
        initialiseSettings();
        setCallbackForMain();
        loadSharedPref();
        cleaningFromTrash();
        setupRecyclerViewAdapter();
        switchType();
        enableSwipeToDelete();
        differentHandlers();
        setColorItem();
    }

    private void startSettings(Bundle savedInstanceState) {
        MyColor.setColorStatBarAndTaskDesc(this);

        if (savedInstanceState == null) {
            // Обнуляем состояние списка
            mBundleRecyclerViewState = null;
            MySharedPref.setInt(this, MyPrefKey.KEY_NOTES_CATEGORY, 0);
        }
    }

    private void setColorItem() {
        mhAdapter.setColorItem();
    }

    private void initialiseWidgets() {
        recyclerView =  findViewById(R.id.recyclerView);
        bottomAppBar =  findViewById(R.id.btmAppBarMain);
        fabMain =  findViewById(R.id.fabMain);
        textNumSelItem = findViewById(R.id.textView);
        searchView = findViewById(R.id.searchView);

        dbAdapter = new DbAdapter(this);
        mhAdapter = new MainHelperTwo(this, bottomAppBar, fabMain);
        myDialogTrash = new MyDialogTrash(this, dbAdapter);
        myDialogUnification = new MyDialogUnification(this);
    }

    private void initialiseSettings() {
        MainHelper.removeFlicker(recyclerView);

        mhAdapter.setVisSelectMenuItem(false);
        mhAdapter.setVisUniMenuItem(false);

        searchView.onActionViewExpanded();
        searchView.setVisibility(View.GONE);

        mhAdapter.setVisCardView(false);

        //recyclerView.setItemAnimator(new ScaleInBottomAnimator());
    }

    private void setCallbackForMain() {
        (new NoteActivity()).registerCallBackNote(this::setChangedView);
        (new PrefNumOfLines(this)).registerCallbackNumOfLines(this::changeNumOfLines);
        (new MySettingsAppFragment()).registerCallBackOneSize(this::changeOneSize);
        (new PrefCardView(this)).registerCallBackFontSize(this::changeFontSize);
        (new MyDialogRestore(this)).regCallbackRestoreNotes(this::restore);
        (new MyDialogColor()).registerCallBackColorMain(this::changeColor);
        BpCloud.regCallbackRestNotes(this::restore);
        myDialogTrash.registerCallBackTrash(this::goUpdateMethod);
        myDialogUnification.registerCallBackUni(this::unification);
    }

    public void changeNumOfLines(int numOfLines) {
        this.numOfLines = numOfLines;
        setChangedView(false);
    }

    private void changeOneSize(boolean isOneSizeOn) {
        this.isOneSizeOn = isOneSizeOn;
        setChangedView(true);
    }

    private void changeFontSize(int fontSize, int dateFontSize) {
        this.fontSize = fontSize;
        this.dateFontSize = dateFontSize;
        setChangedView(false);
    }

    private void changeColor(int color) {
        this.color = color;
        setColorItem();
        setChangedView(false);
    }

    private void restore() {
        MySharedPref.setInt(this, MyPrefKey.KEY_NOTES_CATEGORY, 0);
        cleaningFromTrash();
        setChangedView(false);
    }

    private void goUpdateMethod() {
        isUpdateDate = true;
        updateDateMethod();
    }

    private void unification() {
        rvAdapter.unificationItems();
        onClickDelete(false);
    }

    private void loadSharedPref() {
        numOfLines = GetSharedPref.getNumOfLines(this);
        isOneSizeOn = GetSharedPref.isOneSize(this);
        fontSize = GetSharedPref.getFontSize(this);
        dateFontSize = GetSharedPref.getDateFontSize(this);
        color = MyColor.getColorCustom(this);
        rangeInDays = GetSharedPref.getRangeInDays(this);
    }

    private void cleaningFromTrash() {
        dbAdapter.open();
        dbAdapter.deleteByTime(rangeInDays);
        dbAdapter.close();
    }

    private void setupRecyclerViewAdapter() {
        notesCategory();

        dbAdapter.open();
        List<Note> notes = dbAdapter.getNotes(whereClauseForMode);
        dbAdapter.close();

        rvAdapter = new MyRecyclerViewAdapter(
                this, notes, dbAdapter, selNotesCategory, isDeleteMode,
                isTypeNotesSingle, numOfLines, isOneSizeOn, fontSize, dateFontSize, color);

        mhAdapter.setHandlerSearchView(searchView, rvAdapter);
        rvAdapter.setCallbackDelMode(this);

        checkEmptyRecyclerView();
        recyclerView.setAdapter(rvAdapter);
    }

    private void differentHandlers() {
        bottomAppBarHandler();
        fabHandler();
        (new Refresh(this)).build();
    }

    private void bottomAppBarHandler() {
        // Меню bottomAppBar
        bottomAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_switch_view) {
                closeSnackBar();
                toggleTypeOfNotes();
                switchType();
                return  true;
            } else if (item.getItemId() == R.id.action_select_item) {
                rvAdapter.selectItems();
                return  true;
            } else if (item.getItemId() == R.id.action_search) {
                goSearch();
                return  true;
            } else if (item.getItemId() == R.id.action_unification) {
                myDialogUnification.call();
                return  true;
            } else
                return false;
        });

        // Кнопка навигации bottomAppBar
        bottomAppBar.setNavigationOnClickListener(v -> {
            if (isDeleteMode) {
                onClickDelete(false);
            } else {
                showBottomSheet();
            }
        });
    }

    private void showBottomSheet() {
        closeSnackBar();
        MainHelper.bottomNavViewShow(getSupportFragmentManager());
    }

    public void toggleTypeOfNotes() {
        // Переключение и сохранение вида линейный/сетка
        //isTypeNotesSingleOn = !isTypeNotesSingleOn;
        MySharedPref.setBoolean(this, MyPrefKey.KEY_TYPE_NOTES, !isTypeNotesSingle);
    }

    private void switchType() {
        // Изменение вида списка и иконки меню при переключении
        isTypeNotesSingle = GetSharedPref.isTypeSingle(this);
        mhAdapter.setTypeNotes(recyclerView, isTypeNotesSingle);
        rvAdapter.setTypeOfNotes(isTypeNotesSingle);
    }

    private void fabHandler() {
        // Обработчик клика по fab
        fabMain.setOnClickListener(v -> {
            if (isDeleteMode) {
                onClickDelete(true);
            } else {
                clickFab();
            }
        });
    }

    private void goSearch() {
        isSearchOn = !isSearchOn;
        mhAdapter.isSearchMode(searchView, isSearchOn, selNotesCategory);
        mhAdapter.changeIconFabSearch(isSearchOn, selNotesCategory);
    }

    private void clickFab() {
        if (isSearchOn) {
            goSearch();
        } else {
            if (selNotesCategory == 2) {
                myDialogTrash.call(rvAdapter.getItemCount());
            } else {
                MyIntent.intentAddNewNote(this, selNotesCategory);
            }
        }
    }

    private void notesCategory() {
        selNotesCategory = GetSharedPref.getNotesCategory(this);
        if (selNotesCategory == 0) whereClauseForMode = "del_items = 0"; // Note
        else if (selNotesCategory == 1) whereClauseForMode = "favorites = 1"; // Favorites
        else if (selNotesCategory == 2) whereClauseForMode = "del_items = 1"; // Trash
        mhAdapter.changeIconFab(selNotesCategory);
    }

    private void checkEmptyRecyclerView() {
        rvAdapter.registerAdapterDataObserver(new CheckEmptyRecyclerView(this, // Проверка на пустой список
                rvAdapter));
        (new CheckEmptyRecyclerView(this, rvAdapter)).checkEmpty(); // Дополнительная проверка на пустой список (срабатывает при сменах активити)
    }

    @Override
    public void onResume() {
        super.onResume();

        updateDateMethod();
        restoreRecyclerViewState();
    }

    private void updateDateMethod() {
        // Вызвает установку адаптера для измения содержания списка
        if (isUpdateDate) {
            isUpdateDate = false;
            setupRecyclerViewAdapter();
            if (isListGoUp) {
                // Возвращает список вверх, если создана новая заметка
                Objects.requireNonNull(recyclerView.getLayoutManager()).scrollToPosition(0);
            }
        }
    }

    private void restoreRecyclerViewState() {
        // Восстанавливаем состояние RecyclerView
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(listState);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        closeSnackBar();
        saveRecyclerViewState();
    }

    private void saveRecyclerViewState() {
        // сохраняем состояние RecyclerView
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    public void onCallbackClick(boolean isSelectionModeOn) {
        // Callback FROM SwipeRecyclerViewAdapter
        closeSnackBar();
        deleteMode(true);
    }

    @Override
    public void onCallbackClick2(boolean isSelectOneOn) {
        // Callback2 FROM SwipeRecyclerViewAdapter
        mhAdapter.setSelectIcon(isSelectOneOn);
    }

    @Override
    public void onCallbackClick3(int numSelItem) {
        textNumSelItem.setText(String.valueOf(numSelItem));
        mhAdapter.setVisUniMenuItem(numSelItem >= 2 && numSelItem <= 3);
    }

    @Override
    public void onCallbackClick4() {
        goUpdateMethod();
    }

    public void onClickDelete(boolean isDelete) {
        // IN SwipeRecyclerViewAdapter
        rvAdapter.deleteSelectedItems(isDelete);
        deleteMode(false);
    }

    private void deleteMode(boolean isDeleteModeOn) {
        // Переход в режим удаления и восстановлние из него
        this.isDeleteMode = isDeleteModeOn;
        mhAdapter.deleteMode(isDeleteModeOn, selNotesCategory, isSearchOn);
    }

    @Override
    public void onItemClickBottomNavView() {
        // Callback FROM BottomSheetDialog
        setupRecyclerViewAdapter();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Обработка нажатия кнопки назад
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isDeleteMode) {
                onClickDelete(false); // Выход из режима удаления
                return false;
            } else if (isSearchOn) {
                goSearch();
                return false;
            } else {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void enableSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START | ItemTouchHelper.END) {

                    @Override
                    public int getSwipeDirs(@NonNull RecyclerView recyclerView,
                                            @NonNull RecyclerView.ViewHolder viewHolder) {
                        if (isDeleteMode) return 0; // Отключение свайпа в режиме удаления
                        return super.getSwipeDirs(recyclerView, viewHolder);
                    }

                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        //Remove swiped item from list and notify the RecyclerView
                        int position = viewHolder.getAbsoluteAdapterPosition();
                        Note note = rvAdapter.getNotes().get(position);

                        rvAdapter.deleteItem(note, position);
                        if (selNotesCategory != 2) showSnackBar(note, position);
                    }
                };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    private void showSnackBar(Note note, int position) {
        mySnackbarMain = (new MySnackbarMain(
                this, rvAdapter, fabMain));
        mySnackbarMain.showSnackbar(note, position);
    }

    private void closeSnackBar() {
        if (mySnackbarMain != null){
            mySnackbarMain.closeSnackbar();
        }
    }

    private void setChangedView(boolean isListGoUp) {
        isUpdateDate = true;
        this.isListGoUp = isListGoUp;
    }

}