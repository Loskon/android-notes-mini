package com.loskon.noteminimalism3.ui.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.loskon.noteminimalism3.R;
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
import com.loskon.noteminimalism3.helper.snackbars.MainSnackbar;
import com.loskon.noteminimalism3.model.Note;
import com.loskon.noteminimalism3.rv.CallbackDelMode;
import com.loskon.noteminimalism3.rv.MyRecyclerViewAdapter;
import com.loskon.noteminimalism3.ui.activity.settingsapp.MySettingsAppFragment;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogBottomSheet;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogColor;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogRestore;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogTrash;
import com.loskon.noteminimalism3.ui.preference.PrefCardView;
import com.loskon.noteminimalism3.ui.preference.PrefNumOfLines;

import java.util.List;
import java.util.Objects;

/**
 * Основной класс заметок
 */

public class  MainActivity extends AppCompatActivity implements CallbackDelMode,
        MyDialogBottomSheet.ItemClickListenerBottomNavView, PrefNumOfLines.callbackNumOfLines,
        MySettingsAppFragment.CallbackOneSize, PrefCardView.CallbackFontSize, NoteActivity.CallbackNote,
        MyDialogRestore.CallbackRestoreNotes, MyDialogColor.CallbackColorMain {

    private MyRecyclerViewAdapter rvAdapter;
    private DbAdapter dbAdapter;
    private MainHelperTwo mhAdapter;

    private RecyclerView recyclerView;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fabMain;
    private TextView textNumSelItem;
    private CardView cardView;
    private Menu appBarMenu;
    private Snackbar snackbarMain;
    private SearchView searchView;

    private boolean isTypeNotesSingleOn;
    private boolean isDeleteMode = false;
    private boolean isUpdateDate;
    private boolean isListGoUp, isOneSizeOn, isSearchOn;
    private int selNotesCategory, numOfLines;
    private int fontSize, dateFontSize, color;
    private String whereClauseForMode;

    // Переменные для сохранения состояния RecyclerView
    private final static String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyColor.setDarkTheme(GetSharedPref.isDarkModeOn(this));
        setContentView(R.layout.activity_main);

        MyColor.setColorStatBarAndTaskDesc(this);

        if (savedInstanceState == null) {
            // Обнуляем состояние списка
            mBundleRecyclerViewState = null;
            // Устанавливаем категорию "Note" при запуске
            MySharedPref.setInt(this, MyPrefKey.KEY_NOTES_CATEGORY, 0);
        }

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

    private void setColorItem() {
        MyColor.setColorFab(this, fabMain);
        MyColor.setNavIconColor(this, bottomAppBar);
        MyColor.setColorMenuIcon(this, appBarMenu);
        cardView.setCardBackgroundColor(color);
    }

    private void initialiseWidgets() {
        cardView = findViewById(R.id.card_view_main);
        recyclerView =  findViewById(R.id.recyclerView);
        bottomAppBar =  findViewById(R.id.btmAppBarMain);
        fabMain =  findViewById(R.id.fabMain);
        textNumSelItem = findViewById(R.id.textView);
        searchView = findViewById(R.id.searchView);

        appBarMenu = bottomAppBar.getMenu();

        dbAdapter = new DbAdapter(this);
        mhAdapter = new MainHelperTwo(this, appBarMenu, bottomAppBar, fabMain);
    }

    private void initialiseSettings() {
        MainHelper.removeFlicker(recyclerView);

        mhAdapter.setVisSelMenuItem(false);

        searchView.onActionViewExpanded();
        searchView.setVisibility(View.GONE);

        cardView.setVisibility(View.GONE);

        //recyclerView.setItemAnimator(new ScaleInBottomAnimator());
    }

    private void setCallbackForMain() {
        (new NoteActivity()).registerCallBackNote(this);
        (new PrefNumOfLines(this)).registerCallbackNumOfLines(this);
        (new MySettingsAppFragment()).registerCallBackOneSize(this);
        (new PrefCardView(this)).registerCallBackFontSize(this);
        (new MyDialogRestore(this)).regCallbackRestoreNotes(this);
        (new MyDialogColor()).registerCallBackColorMain(this);
        (new MyDialogTrash(this, dbAdapter)).registerCallBackTrash(() -> {
            isUpdateDate = true;
            updateDateMethod();
        });
    }

    private void loadSharedPref() {
        numOfLines = GetSharedPref.getNumOfLines(this);
        isOneSizeOn = GetSharedPref.isOneSize(this);
        fontSize = GetSharedPref.getFontSize(this);
        dateFontSize = GetSharedPref.getDateFontSize(this);
        color = MyColor.getColorCustom(this);
    }

    private void cleaningFromTrash() {
        dbAdapter.open();
        dbAdapter.deleteByTime(2);
        dbAdapter.close();
    }

    private void setupRecyclerViewAdapter() {
        notesCategory();

        dbAdapter.open();
        List<Note> notes = dbAdapter.getNotes(whereClauseForMode);
        dbAdapter.close();

        rvAdapter = new MyRecyclerViewAdapter(
                this, notes, dbAdapter, selNotesCategory, isDeleteMode,
                isTypeNotesSingleOn, numOfLines, isOneSizeOn, fontSize, dateFontSize, color);

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
            }
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
        MySharedPref.setBoolean(this, MyPrefKey.KEY_TYPE_NOTES, !isTypeNotesSingleOn);
    }

    private void switchType() {
        // Изменение вида списка и иконки меню при переключении
        isTypeNotesSingleOn = GetSharedPref.isTypeSingle(this);
        mhAdapter.setTypeNotes(recyclerView, isTypeNotesSingleOn);
        rvAdapter.setTypeOfNotes(isTypeNotesSingleOn);
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
        mhAdapter.isSearchMode(searchView, isSearchOn);
        mhAdapter.changeIconFabSearch(isSearchOn, selNotesCategory);
    }

    private void clickFab() {
        if (isSearchOn) {
            goSearch();
        } else {
            if (selNotesCategory == 2) {
                (new MyDialogTrash(this, dbAdapter)).call(rvAdapter.getItemCount());
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
    }

    private void onClickDelete(boolean isDelete) {
        // IN SwipeRecyclerViewAdapter
        rvAdapter.deleteSelectedItems(isDelete);
        deleteMode(false);
    }

    private void deleteMode(boolean isDeleteModeOn) {
        // Переход в режим удаления и восстановлние из него
        this.isDeleteMode = isDeleteModeOn;

        mhAdapter.deleteMode(isDeleteModeOn, selNotesCategory, isSearchOn, cardView);
    }

    @Override
    public void onItemClickBottomNavView(int selNotesCategory) {
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

    private MainSnackbar mainSnackbar;

    private void showSnackBar(Note note, int position) {
        mainSnackbar = (new MainSnackbar(
                this, rvAdapter, fabMain));
        snackbarMain = mainSnackbar.showSnackbar(note, position);
        snackbarMain.show();
    }

    private void closeSnackBar() {
        if (snackbarMain != null) {
            mainSnackbar.closeSnackbar();
        }
    }

    @Override
    public void callingBackNumOfLines(int numOfLines) {
        this.numOfLines = numOfLines;
        setChangedView(false);
    }

    @Override
    public void callingBackOneSize(boolean isOneSizeOn) {
        this.isOneSizeOn = isOneSizeOn;
        setChangedView(true);
    }

    @Override
    public void callingBackFontSize(int fontSize, int dateFontSize) {
        this.fontSize = fontSize;
        this.dateFontSize = dateFontSize;
        setChangedView(false);
    }

    @Override
    public void callingBackNote(boolean isListGoUp) {
        setChangedView(isListGoUp);
    }

    @Override
    public void callingRestoreNotes() {
        setChangedView(false);
    }

    @Override
    public void callingBackColorMain(int color) {
        this.color = color;
        setColorItem();
        setChangedView(false);
    }

    private void setChangedView(boolean isListGoUp) {
        isUpdateDate = true;
        this.isListGoUp = isListGoUp;
    }


}