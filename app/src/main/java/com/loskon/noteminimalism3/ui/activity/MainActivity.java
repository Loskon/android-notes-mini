package com.loskon.noteminimalism3.ui.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.main.MainSomeHelper;
import com.loskon.noteminimalism3.auxiliary.main.MainWidgetsHelper;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.auxiliary.other.MyIntent;
import com.loskon.noteminimalism3.auxiliary.sharedpref.GetSharedPref;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MyPrefKey;
import com.loskon.noteminimalism3.auxiliary.sharedpref.MySharedPref;
import com.loskon.noteminimalism3.backup.prime.BpCloud;
import com.loskon.noteminimalism3.db.DbAdapter;
import com.loskon.noteminimalism3.db.NoteDbSchema.NoteTable.Columns;
import com.loskon.noteminimalism3.model.Note;
import com.loskon.noteminimalism3.rv.adapter.MyRecyclerViewAdapter;
import com.loskon.noteminimalism3.rv.other.CallbackDelMode;
import com.loskon.noteminimalism3.rv.other.CheckEmptyRecyclerView;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogColor;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogRestore;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogSort;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogTrash;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogUnification;
import com.loskon.noteminimalism3.ui.fragments.MyBottomSheet;
import com.loskon.noteminimalism3.ui.fragments.MySettingsAppFragment;
import com.loskon.noteminimalism3.ui.preference.MyPrefCardView;
import com.loskon.noteminimalism3.ui.preference.MyPrefNumOfLines;
import com.loskon.noteminimalism3.ui.snackbars.MySnackbarMain;
import com.loskon.noteminimalism3.ui.snackbars.SnackbarBuilder;

import java.util.List;
import java.util.Objects;

import jp.wasabeef.recyclerview.animators.ScaleInTopAnimator;

/**
 * Основной класс для работы со списком заметок
 */

public class MainActivity extends AppCompatActivity
        implements CallbackDelMode, MyBottomSheet.ItemClickListenerBottomNavView {

    private DbAdapter dbAdapter;
    private MyRecyclerViewAdapter rvAdapter;
    private MainWidgetsHelper widgetsHelper;
    private MyDialogTrash myDialogTrash;
    private MySnackbarMain mySnackbarMain;

    private RecyclerView recyclerView;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fabMain;
    private TextView textNumSelItem;
    private SearchView searchView;
    private CoordinatorLayout coordLytMain;

    private boolean isTypeNotesSingle;
    private boolean isDeleteMode;
    private boolean isUpdateDate;
    private boolean isListGoUp, isOneSizeOn, isSearchMode;
    private int selNotesCategory, numOfLines;
    private int fontSize, dateFontSize, color;
    private int rangeInDays;
    private String whereClauseForMode, orderBy;

    // Переменные для сохранения состояния RecyclerView
    private final static String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyColor.setDarkTheme(GetSharedPref.isDarkMode(this));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyColor.setColorStatBarAndTaskDesc(this);

        initialiseStartSettings(savedInstanceState);
        initialiseWidgets();
        initialiseConfigureWidgets();
        setCallbackForMain();
        loadSharedPref();
        cleaningFromTrash();
        setupRecyclerViewAdapter();
        switchType();
        enableSwipeToDelete();
        differentHandlers();
        setColorItem();
    }

    private void initialiseStartSettings(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            // Обнуляем состояние списка
            mBundleRecyclerViewState = null;
            setNotesCategory();
        }
    }

    private void setNotesCategory() {
        MySharedPref.setInt(this, MyPrefKey.KEY_NOTES_CATEGORY, 0);
    }

    private void setColorItem() {
        widgetsHelper.setColorItem();
    }

    private void initialiseWidgets() {
        recyclerView =  findViewById(R.id.recyclerView);
        bottomAppBar =  findViewById(R.id.btmAppBarMain);
        coordLytMain = findViewById(R.id.coordLytMain);
        fabMain =  findViewById(R.id.fabMain);
        textNumSelItem = findViewById(R.id.tv_font_size_title);
        searchView = findViewById(R.id.searchView);

        dbAdapter = new DbAdapter(this);
        widgetsHelper = new MainWidgetsHelper(this, bottomAppBar, fabMain);
        myDialogTrash = new MyDialogTrash(this, dbAdapter, fabMain, coordLytMain);
    }

    private void initialiseConfigureWidgets() {
        widgetsHelper.setVisSelectMenuItem(false);
        widgetsHelper.setVisUniMenuItem(false);

        searchView.onActionViewExpanded();
        searchView.setVisibility(View.GONE);

        widgetsHelper.setVisCardView(false);

        MainSomeHelper.removeFlicker(recyclerView);
        recyclerView.setItemAnimator(new ScaleInTopAnimator());
    }

    private void setCallbackForMain() {
        NoteActivity.regCallbackNote(this::setChangedView);
        MyPrefNumOfLines.regCallbackNumOfLines(this::changeNumOfLines);
        MySettingsAppFragment.regCallbackOneSize(this::changeOneSize);
        MyPrefCardView.regCallbackFontSize(this::changeFontSize);
        MyDialogRestore.regCallbackResNotes(this::restore);
        MyDialogColor.regCallbackMain(this::changeColor);
        BpCloud.regCallbackCloud(this::restore);
        MyDialogSort.regCallBackNavIcon(this::goUpdateMethodTop);
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
        setNotesCategory();
        cleaningFromTrash();
        setChangedView(false);
    }

    public void goUpdateMethod() {
        isUpdateDate = true;
        updateDateMethod();
    }

    public void goUpdateMethodTop() {
        isListGoUp = true;
        isUpdateDate = true;
        updateDateMethod();
    }

    public void unification() {
        try {
            rvAdapter.onUnificationItems();
        } catch (Exception exception) {
            exception.printStackTrace();
            SnackbarBuilder.makeSnackbar(this, coordLytMain,
                    getString(R.string.unknown_error), bottomAppBar, false);
        }

        onClickDelete(false);
    }

    private void loadSharedPref() {
        numOfLines = GetSharedPref.getNumOfLines(this);
        isOneSizeOn = GetSharedPref.isOneSize(this);
        fontSize = GetSharedPref.getFontSize(this);
        dateFontSize = GetSharedPref.getDateFontSize(this);
        color = MyColor.getMyColor(this);
        rangeInDays = GetSharedPref.getRangeInDays(this);
    }

    private void cleaningFromTrash() {
        dbAdapter.open();
        dbAdapter.deleteByTime(rangeInDays);
        dbAdapter.close();
    }

    private void setupRecyclerViewAdapter() {
        notesCategory();
        notesSort();

        dbAdapter.open();
        List<Note> notes = dbAdapter.getNotes(whereClauseForMode, orderBy);
        dbAdapter.close();

        rvAdapter = new MyRecyclerViewAdapter(
                this, notes, dbAdapter);
        rvAdapter.initSettings(selNotesCategory, isDeleteMode, isTypeNotesSingle,
                numOfLines, isOneSizeOn, fontSize, dateFontSize, color);
        rvAdapter.initStrokeInDp();

        widgetsHelper.setHandlerSearchView(searchView, rvAdapter);
        rvAdapter.regCallbackDelMode(this);

        checkEmptyRecyclerView();
        recyclerView.setAdapter(rvAdapter);
    }

    private void differentHandlers() {
        bottomAppBarHandler();
        fabHandler();
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
                rvAdapter.onSelectAllItems();
                return  true;
            } else if (item.getItemId() == R.id.action_search) {
                goSearch();
                return  true;
            } else if (item.getItemId() == R.id.action_unification) {
                (new MyDialogUnification(this)).call();
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
        MainSomeHelper.bottomNavViewShow(getSupportFragmentManager());
    }

    public void toggleTypeOfNotes() {
        // Переключение и сохранение вида линейный/сетка
        //isTypeNotesSingleOn = !isTypeNotesSingleOn;
        MySharedPref.setBoolean(this, MyPrefKey.KEY_TYPE_NOTES, !isTypeNotesSingle);
    }

    private void switchType() {
        // Изменение вида списка и иконки меню при переключении
        isTypeNotesSingle = GetSharedPref.isTypeSingle(this);
        widgetsHelper.setTypeNotes(recyclerView, isTypeNotesSingle);
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
        isSearchMode = !isSearchMode;
        widgetsHelper.isSearchMode(searchView, isSearchMode);
        widgetsHelper.changeIconFabSearch(isSearchMode, selNotesCategory);
        closeSnackBar();
    }

    private void clickFab() {
        if (isSearchMode) {
            goSearch();
        } else {
            if (selNotesCategory == 2) {
                myDialogTrash.call(rvAdapter.getItemCount());
            } else {
                MyIntent.addNewNote(this, selNotesCategory);
            }
        }
    }

    private void notesCategory() {
        selNotesCategory = GetSharedPref.getNotesCategory(this);

        if (selNotesCategory == 0) whereClauseForMode = "del_items = 0"; // Note
        else if (selNotesCategory == 1) whereClauseForMode = "favorites = 1"; // Favorites
        else if (selNotesCategory == 2) whereClauseForMode = "del_items = 1"; // Trash

        widgetsHelper.changeIconFabSearch(isSearchMode, selNotesCategory);
    }

    private void notesSort() {
        int sort = GetSharedPref.getSort(this);

        if (sort == R.id.rb_sort_creation) orderBy = Columns.COLUMN_DATE + " DESC"; // Create
        else if (sort == R.id.rb_sort_modification) orderBy = Columns.COLUMN_DATE_MOD + " DESC"; // Modification

        if (selNotesCategory == 2) orderBy = Columns.COLUMN_DATE_DEL + " DESC"; // Date of deletion
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
                if (recyclerView.getLayoutManager() != null) {
                    recyclerView.getLayoutManager().scrollToPosition(0);
                }
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
        if (recyclerView.getLayoutManager() != null)  {
            Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
            mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
        }
    }

    @Override
    public void onCallBackSelMode(boolean isSelMode) {
        // Callback FROM SwipeRecyclerViewAdapter
        closeSnackBar();
        deleteMode(true);
    }

    @Override
    public void onCallBackNotAllSelected(boolean isNotAllSelected) {
        // Callback2 FROM SwipeRecyclerViewAdapter
        widgetsHelper.setSelectIcon(isNotAllSelected);
    }

    @Override
    public void onCallBackNumSel(int numSelItem) {
        textNumSelItem.setText(String.valueOf(numSelItem));
        if (selNotesCategory != 2) {
            widgetsHelper.setVisUniMenuItem(numSelItem >= 2 && numSelItem <= 3);
        } else {
            widgetsHelper.setVisUniMenuItem(false);
        }
    }

    @Override
    public void onCallBackUni() {
        goUpdateMethod();
    }

    public void onClickDelete(boolean isDelete) {
        // IN SwipeRecyclerViewAdapter
        rvAdapter.onExitFromDeleteMode(isDelete);
        deleteMode(false);
    }

    private void deleteMode(boolean isDeleteModeOn) {
        // Переход в режим удаления и восстановлние из него
        this.isDeleteMode = isDeleteModeOn;
        widgetsHelper.deleteMode(isDeleteModeOn, selNotesCategory, isSearchMode);
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
            } else if (isSearchMode) {
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

                        rvAdapter.onDeleteItem(note, position);
                        if (selNotesCategory != 2) showSnackBar(note, position);
                    }
                };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    private void showSnackBar(Note note, int position) {
        mySnackbarMain = (new MySnackbarMain(this, rvAdapter, fabMain, coordLytMain));
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