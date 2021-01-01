package com.loskon.noteminimalism3.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.loskon.noteminimalism3.helper.MyColor;
import com.loskon.noteminimalism3.helper.CheckEmptyRecyclerView;
import com.loskon.noteminimalism3.helper.MainHelper;
import com.loskon.noteminimalism3.helper.MyIntent;
import com.loskon.noteminimalism3.ui.dialogs.MyDialogBottomSheet;
import com.loskon.noteminimalism3.helper.sharedpref.MySharedPreference;
import com.loskon.noteminimalism3.rv.CallbackDelMode;
import com.loskon.noteminimalism3.model.Note;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.others.RefreshView;
import com.loskon.noteminimalism3.rv.MyRecyclerViewAdapter;
import com.loskon.noteminimalism3.db.DbAdapter;
import com.loskon.noteminimalism3.helper.sharedpref.MyPrefKey;
import com.loskon.noteminimalism3.ui.preference.PrefCardView;
import com.loskon.noteminimalism3.ui.preference.PrefNumOfLines;


import java.util.List;
import java.util.Objects;

import jp.wasabeef.recyclerview.animators.ScaleInBottomAnimator;

/**
 * Основной класс заметок
 */

public class  MainActivity extends AppCompatActivity implements CallbackDelMode,
        MyDialogBottomSheet.ItemClickListenerBottomNavView, PrefNumOfLines.callbackNumOfLines,
        SettingsAppActivity.CallbackOneSize, PrefCardView.CallbackFontSize {

    private MyRecyclerViewAdapter rvAdapter;
    private DbAdapter dbAdapter;

    private RecyclerRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fabMain;
    private TextView textEmpty;
    private TextView textNumSelItem;
    private CardView cardView;
    private Menu appBarMenu;
    private Snackbar snackbarMain;
    private CountDownTimer countDownTimer;

    private boolean isTypeNotesSingleOn;
    private boolean isDeleteModeOn;
    private boolean isUpdateDate;
    private boolean isListGoUp, isOneSizeOn;
    private int selNotesCategory, numOfLines, fontSize, dateFontSize;
    private String whereClauseForMode;

    // Переменные для сохранения состояния RecyclerView
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Меняем цвет статус бара
        MyColor.setColorStatBarAndTaskDesc(this);
        // Обнуляем состояние списка
        mBundleRecyclerViewState = null;
        // Устанавливаем категорию "Note" при запуске
        MySharedPreference.saveInt(this,
                MyPrefKey.KEY_SEL_CATEGORY, 0);

        initialiseWidgets();
        initialiseSettings();
        cleaningFromTrash();
        setupRecyclerViewAdapter();
        enableSwipeToDelete();
        differentHandlers();
    }

    private void initialiseWidgets() {
        textEmpty = findViewById(R.id.textEmpty);
        cardView = findViewById(R.id.card_view_main);
        recyclerView =  findViewById(R.id.recyclerView);
        bottomAppBar =  findViewById(R.id.btmAppBarMain);
        fabMain =  findViewById(R.id.fabMain);
        refreshLayout = findViewById(R.id.refreshLayout);
        textNumSelItem = findViewById(R.id.textView);

        appBarMenu = bottomAppBar.getMenu();
        dbAdapter = new DbAdapter(this);
    }

    private void initialiseSettings() {
        MainHelper.removeFlicker(recyclerView);
        cardView.setVisibility(View.GONE);
        recyclerView.setItemAnimator(new ScaleInBottomAnimator());
        setVisibleSelectItem(false);

        (new PrefNumOfLines(this)).registerCallbackNumOfLines(this);
        numOfLines = MySharedPreference.loadInt(this,
                MyPrefKey.KEY_NUM_OF_LINES, 3);

        (new SettingsAppActivity()).registerCallBackOneSize(this);
        isOneSizeOn = MySharedPreference.loadBoolean(this,
                MyPrefKey.KEY_ONE_SIZE, false);

        (new PrefCardView(this)).registerCallBackFontSize(this);
        fontSize = MySharedPreference
                .loadInt(this, MyPrefKey.KEY_TITLE_FONT_SIZE, 18);
        dateFontSize = MySharedPreference
                .loadInt(this, MyPrefKey.KEY_DATE_FONT_SIZE, 14);
    }

    private void setVisibleSelectItem(boolean isVisibleSelect) {
        appBarMenu.findItem(R.id.action_select_item).setVisible(isVisibleSelect);
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
                this, notes, dbAdapter, selNotesCategory,
                isDeleteModeOn, isTypeNotesSingleOn, numOfLines, isOneSizeOn,
                fontSize, dateFontSize);

        rvAdapter.setCallbackDelMode(this);
        checkEmptyRecyclerView();
        recyclerView.setAdapter(rvAdapter);
    }

    private void differentHandlers() {
        bottomAppBarHandler();
        fabHandler();
        refreshHandlerAndRedrawing();
    }

    private void bottomAppBarHandler() {
        // Элементы меню bottomAppBar
        bottomAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_switch_view) {
                closeSnackBar();
                toggleTypeOfNotes();
                switchType();
                return  true;
            }
            if (item.getItemId() == R.id.action_select_item) {
                rvAdapter.selectAll();
                return  true;
            }
            return false;
        });

        // Кнопка навигации bottomAppBar
        bottomAppBar.setNavigationOnClickListener(v -> {
            if (isDeleteModeOn) {
                onClickDelete(false);
            } else {
                closeSnackBar();
                MainHelper.bottomNavViewShow(getSupportFragmentManager());
            }
        });
    }


    public void toggleTypeOfNotes() {
        // Переключение и сохранение вида линейный/сетка
        isTypeNotesSingleOn = !isTypeNotesSingleOn;
        MySharedPreference.saveBoolean(this, MyPrefKey.KEY_TYPE_NOTES, isTypeNotesSingleOn);
    }

    private void switchType() {
        // Изменение вида списка и иконки меню при переключении

        isTypeNotesSingleOn = MySharedPreference.loadBoolean(this,
                MyPrefKey.KEY_TYPE_NOTES, true);

        if (isTypeNotesSingleOn) {
            appBarMenu.findItem(R.id.action_switch_view).
                    setIcon(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.baseline_dashboard_black_24, null));

            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        } else {
            appBarMenu.findItem(R.id.action_switch_view).
                    setIcon(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.baseline_view_agenda_black_24, null));

            recyclerView.setLayoutManager(new
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }

        rvAdapter.setTypeOfNotes(isTypeNotesSingleOn);

        MyColor.setColorMenuIcon(this, appBarMenu);
    }

    private void fabHandler() {
        // Обработчик клика по fab
        fabMain.setOnClickListener(v -> {
            if (isDeleteModeOn) {
                onClickDelete(true);
            } else {
                if (selNotesCategory == 2) {
                    // Удаление всех элементов из мусорки
                    dbAdapter.open();
                    dbAdapter.deleteAll();
                    dbAdapter.close();
                    //initAdapter();
                } else {
                    Intent intent = MyIntent.intentAddNewNote(this, selNotesCategory);
                    startActivity(intent);
                }
            }
        });
    }

    private void refreshHandlerAndRedrawing () {
        refreshLayout.setRefreshTargetOffset((int)
                getResources().getDimension(R.dimen.height_refresh_layout));
        // С какой высоты заканчивается "анимация"
        refreshLayout.setAnimateToRefreshDuration(0);
        // Метод для настройки парамтров отображения
        refreshLayout.setRefreshView((new RefreshView(this)),
                (new RecyclerRefreshLayout.LayoutParams(0, 0)));
        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(false);
            bottomAppBar.performShow();
        });
    }

    private void notesCategory() {
        selNotesCategory = MySharedPreference.loadInt(this,"selNotesCategory", 0);
        if (selNotesCategory == 0) whereClauseForMode = "del_items = 0"; // Note
        else if (selNotesCategory == 1) whereClauseForMode = "favorites = 1"; // Favorites
        else if (selNotesCategory == 2) whereClauseForMode = "del_items = 1"; // Trash
        changeIconFab();
    }

    private void changeIconFab() {
        if (selNotesCategory == 2) {
            fabMain.setImageResource(R.drawable.baseline_delete_forever_black_24);
        } else {
            fabMain.setImageResource(R.drawable.baseline_add_black_24);
        }
    }

    private void checkEmptyRecyclerView() {
        rvAdapter.registerAdapterDataObserver(new CheckEmptyRecyclerView(textEmpty, // Проверка на пустой список
                rvAdapter));
        (new CheckEmptyRecyclerView(textEmpty, rvAdapter)).checkEmpty(); // Дополнительная проверка на пустой список (срабатывает при сменах активити)
    }

    @Override
    public void onResume() {
        super.onResume();

        MyColor.setColorFab(this, fabMain);
        MyColor.setNavigationIconColor(this, bottomAppBar);

       // rvAdapter.notifyDataSetChanged();
        switchType();
        updateDateMethod();
        restoreRecyclerViewState();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); // Необходим для получения значаения переменных из NoteActivity
    }

    private void updateDateMethod() {
        // Защита от установки адаптера при сворачивании
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            isUpdateDate = intent.getExtras().getBoolean("updateDate");
            isListGoUp = intent.getExtras().getBoolean("createOrDel");
        }
        // Вызвает установку адаптера
        // (необходимо для появления новой заметки в списке)
        if (isUpdateDate) {
            setupRecyclerViewAdapter();
            isUpdateDate = false;
            intent.putExtra("updateDate", false);
            if (isListGoUp) {
                Objects.requireNonNull(recyclerView.getLayoutManager()).scrollToPosition(0); // Возвращает списко вверх, если создана новая заметка
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
        // Очистк стека
        //if (getSupportFragmentManager().getBackStackEntryCount() > 0){
          //  getSupportFragmentManager().popBackStack();
        //}
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
        if (isSelectOneOn) {
            MainHelper.setMenuIcon(this, appBarMenu,
                    R.id.action_select_item, R.drawable.baseline_done_black_24);
        } else {
            MainHelper.setMenuIcon(this, appBarMenu,
                    R.id.action_select_item, R.drawable.baseline_done_all_black_24);
        }

        MyColor.setColorMenuIcon(this, appBarMenu);
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
        this.isDeleteModeOn = isDeleteModeOn;
        //bottomAppBar.setHideOnScroll(!isDeleteModeOn);

        if (isDeleteModeOn) {
            cardView.setVisibility(View.VISIBLE);
            cardView.setCardBackgroundColor(MyColor.getColorCustom(this));
            if (selNotesCategory != 2) {
                fabMain.setImageResource(R.drawable.baseline_delete_black_24);
            }
            bottomAppBar.setNavigationIcon(R.drawable.baseline_navigate_before_black_24);
        } else {
            cardView.setVisibility(View.GONE);
            if (selNotesCategory != 2) {
                fabMain.setImageResource(R.drawable.baseline_add_black_24);
            }
            bottomAppBar.setNavigationIcon(R.drawable.baseline_menu_black_24);
        }

        for (int i = 0; i < 2; i++) {
            appBarMenu.getItem(i).setVisible(!isDeleteModeOn);
        }



        setVisibleSelectItem(isDeleteModeOn);

        MyColor.setNavigationIconColor(this, bottomAppBar);
    }

    @Override
    public void onItemClickBottomNavView(int selNotesCategory) {
        // Callback FROM BottomSheetDialog
        //this.selNotesCategory = selNotesCategory;
        //rvAdapter.notifyDataSetChanged();
        setupRecyclerViewAdapter();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Обработка нажатия кнопки назад
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isDeleteModeOn) {
                onClickDelete(false); // Выход из режима удаления
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
                        if (isDeleteModeOn) return 0; // Отключение свайпа в режиме удаления
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

    private void showSnackBar2(Note note, int position) {

                //View view = snack.getView();
        //            TextView tv = (TextView) view
        //                    .findViewById(android.support.design.R.id.snackbar_text);
        //            tv.setTextColor(Color.WHITE);//change textColor
        //
        //            TextView tvAction = (TextView) view
        //                    .findViewById(android.support.design.R.id.snackbar_action);
        //            tvAction.setTextSize(16);
        //            tvAction.setTextColor(Color.WHITE);
    }

    @SuppressLint("InflateParams")
    private void showSnackBar(Note note, int position) {
        if (countDownTimer != null) {
            countDownTimer.cancel(); // Сброс таймера
        }

        CoordinatorLayout coordinatorLayout = findViewById(R.id.coord_layout_main);

        snackbarMain = Snackbar.make(coordinatorLayout, "", Snackbar.LENGTH_INDEFINITE);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbarMain.getView();

        snackbarMain.setAnchorView(fabMain);

        LayoutInflater objLayoutInflater = (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View snackView = objLayoutInflater.inflate(R.layout.custom_snackbar, null);

        Button btnSnackbar =  snackView.findViewById(R.id.snackbar_btn);
        TextView textSnackbar =  snackView.findViewById(R.id.snackbar_text_title);
        ProgressBar progressBar =  snackView.findViewById(R.id.snackbar_progress_bar);
        TextView textProgress =  snackView.findViewById(R.id.snackbar_text_progress);

        textSnackbar.setText(getString(R.string.snackbar_main_text_add_trash));
        progressBar.setProgress(0);
        progressBar.setMax(10000);

        btnSnackbar.setTextColor(MyColor.getColorCustom(this));
        btnSnackbar.setOnClickListener(v -> {
            rvAdapter.resetItem(note, position);
            recyclerView.scrollToPosition(position);
            closeSnackBar();
        });

        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar,
                "progress", 10000);
        animation.setDuration(3900); // 4 second
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();

        countDownTimer = new CountDownTimer(4 * 1000, 100) {
            @Override
            public void onTick(long leftTimeInMilliseconds) {
                long seconds = leftTimeInMilliseconds / 1000;
                textProgress.setText(String.valueOf(seconds));
            }

            @Override
            public void onFinish() {
                snackbarMain.dismiss();
            }
        }.start();

        layout.addView(snackView, 0);
        snackbarMain.show();
    }

    private void closeSnackBar() {
        if (snackbarMain != null && countDownTimer != null) {
            snackbarMain.dismiss();
            countDownTimer.cancel();
        }
    }

    @Override
    public void callingBackNumOfLines(int numOfLines) {
        this.numOfLines = numOfLines;
        setChangedView();
    }

    @Override
    public void callingBackOneSize(boolean isOneSizeOn) {
        this.isOneSizeOn = isOneSizeOn;
        setChangedView();
    }

    @Override
    public void callingBackFontSize(int fontSize, int dateFontSize) {
        this.fontSize = fontSize;
        this.dateFontSize = dateFontSize;
        setChangedView();
    }

    private void setChangedView() {
        isUpdateDate = true;
        updateDateMethod();
    }


}