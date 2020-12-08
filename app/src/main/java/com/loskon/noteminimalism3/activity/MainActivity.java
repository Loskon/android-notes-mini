package com.loskon.noteminimalism3.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loskon.noteminimalism3.activity.mainHelper.ColorHelper;
import com.loskon.noteminimalism3.activity.mainHelper.CustomRecyclerViewEmpty;
import com.loskon.noteminimalism3.activity.mainHelper.MainHelper;
import com.loskon.noteminimalism3.activity.mainHelper.BottomSheetDialog;
import com.loskon.noteminimalism3.activity.mainHelper.Refresh;
import com.loskon.noteminimalism3.activity.mainHelper.SharedPrefHelper;
import com.loskon.noteminimalism3.rv.Callback;
import com.loskon.noteminimalism3.model.Note;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.others.RefreshView;
import com.loskon.noteminimalism3.preference.CustomPreferencesFragment;
import com.loskon.noteminimalism3.rv.CustomRecyclerViewAdapter;
import com.loskon.noteminimalism3.db.DbAdapter;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;


import java.util.List;

/**
 * Основной класс заметок
 */

public class MainActivity extends AppCompatActivity implements Callback,
        BottomSheetDialog.ItemClickListenerBottomNavView {

    private CustomRecyclerViewAdapter rvAdapter;
    private DbAdapter dbAdapter;

    private RecyclerRefreshLayout refreshLayout;
    private SwipeableRecyclerView recyclerView;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fabMain;
    private TextView textEmpty;
    private Menu appBarMenu;

    private boolean isTypeOfNotes;
    private boolean isSelectionModeOn;
    private boolean isUpdateDate;
    private boolean isListUp;
    private boolean isOneSizeOn;
    private int selNotesCategory;
    private String whereClauseForMode;

    // Переменные для сохранения состояния RecyclerView
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Меняем цвет статус бара
        ColorHelper.setColorStatBarAndNavView(this);
        // Обнуляем состояние списка
        mBundleRecyclerViewState = null;
        // Устанавливаем категорию "Note" при запуске
        SharedPrefHelper.saveInt(this,
                "selNotesCategory", 0);

        initView();
        cleaningFromTrash();
        initAdapter();
        differentHandlers();
    }

    private void initView() {
        textEmpty = findViewById(R.id.textEmpty);
        recyclerView =  findViewById(R.id.recyclerView);
        bottomAppBar =  findViewById(R.id.btmAppBarMain);
        fabMain =  findViewById(R.id.fabMain);
        refreshLayout = findViewById(R.id.refresh_layout);
        appBarMenu = bottomAppBar.getMenu();
        dbAdapter = new DbAdapter(this);
        MainHelper.removeFlicker(recyclerView);
    }

    private void cleaningFromTrash() {
        dbAdapter.open();
        dbAdapter.deleteByTime(2);
        dbAdapter.close();
    }

    private void initAdapter() {
        notesCategory();
        dbAdapter.open();
        List<Note> notes = dbAdapter.getNotes(whereClauseForMode);
        rvAdapter = new CustomRecyclerViewAdapter(this, notes,
                selNotesCategory, isSelectionModeOn);
        rvAdapter.setCallbackListenerSwipeAdapter(this);
        customHandlers();
        recyclerView.setAdapter(rvAdapter);
        dbAdapter.close();

        recyclerView.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {
                rvAdapter.swipeDeleteItem(position);
            }

            @Override
            public void onSwipedRight(int position) {
                rvAdapter.swipeDeleteItem(position);
            }
        });
    }

    private void differentHandlers() {
        bottomAppBarHandler();
        fabHandler();
        refreshHandlerAndRedrawing();
    }

    private void bottomAppBarHandler() {
        // Элементы меню bottomAppBar
        bottomAppBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_switch_view:
                        toggleTypeOfNotes();
                        switchType();
                    break;
            }
            return false;
        });

        // Кнопка навигации bottomAppBar
        bottomAppBar.setNavigationOnClickListener(v -> {
            if (isSelectionModeOn) {
                onClickDelete(false);
            } else {
                MainHelper.bottomNavViewShow(getSupportFragmentManager());
            }
        });
    }


    public void toggleTypeOfNotes() {
        // Переключение и сохранение вида линейный/сетка
        isTypeOfNotes = !isTypeOfNotes;
        SharedPrefHelper.saveBoolean(this,"isSwitchView", isTypeOfNotes);
    }

    private void switchType() {
        // Изменение вида списка и иконки меню при переключении

        isTypeOfNotes = SharedPrefHelper.loadBoolean(this,
                "isSwitchView", true);

        if (isTypeOfNotes) {
            appBarMenu.findItem(R.id.action_switch_view).
                    setIcon(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.baseline_dashboard_black_24, null));

            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        } else {


            isOneSizeOn = SharedPrefHelper.loadBoolean(this,
                    "isOneSizeOn",false);

            appBarMenu.findItem(R.id.action_switch_view).
                    setIcon(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.baseline_view_agenda_black_24, null));

            if (isOneSizeOn) {
                recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            } else {
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                        StaggeredGridLayoutManager.VERTICAL));
            }

        }
    }

    private void fabHandler() {
        // Обработчик клика по fab
        fabMain.setOnClickListener(v -> {
            if (isSelectionModeOn) {
                onClickDelete(true);
            } else {
                if (selNotesCategory == 2) {
                    // Удаление всех элементов из мусорки
                    dbAdapter.open();
                    dbAdapter.deleteAll();
                    dbAdapter.close();
                    initAdapter();
                } else {
                    Intent intent = MainHelper.newIntent(this, selNotesCategory);
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
                (new RecyclerRefreshLayout.LayoutParams(200, 300)));
        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(false);
            bottomAppBar.performShow();
        });
    }

    private void notesCategory() {
        selNotesCategory = SharedPrefHelper.loadInt(this,"selNotesCategory", 0);
        if (selNotesCategory == 0) whereClauseForMode = "del_items = 0";
        else if (selNotesCategory == 1) whereClauseForMode = "favorites = 1";
        else if (selNotesCategory == 2) whereClauseForMode = "del_items = 1";
        changeIconFab();
    }

    private void changeIconFab() {
        if (selNotesCategory == 2) {
            fabMain.setImageResource(R.drawable.baseline_delete_forever_black_24);
        } else {
            fabMain.setImageResource(R.drawable.baseline_add_black_24);
        }
    }


    private void customHandlers() {
        rvAdapter.registerAdapterDataObserver(new CustomRecyclerViewEmpty(textEmpty, // Проверка на пустой список
                rvAdapter));
        (new CustomRecyclerViewEmpty(textEmpty, rvAdapter)).checkEmpty(); // Дополнительная проверка на пустой список (срабатывает при сменах активити)
    }

    @Override
    public void onResume() {
        super.onResume();

        //rvAdapter.notifyDataSetChanged();
        switchType();
        updateDateMethod();
        restoreRecyclerViewState();

        // Сбрасывает сохрание позиции настроек
        SharedPrefHelper.saveInt(this, "index", 0);
        SharedPrefHelper.saveInt(this, "top", 0);
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
            isListUp = intent.getExtras().getBoolean("createOrDel");
        }
        // Вызвает установку адаптера
        // (необходимо для появления новой заметки в списке)
        if (isUpdateDate) {
            initAdapter ();
            intent.putExtra("updateDate", false);
            if (isListUp) {
                recyclerView.getLayoutManager().scrollToPosition(0); // Возвращает списко вверх, если создана новая заметка
            }
        }
    }

    private void restoreRecyclerViewState() {
        // Восстанавливаем состояние RecyclerView
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Очистк стека
        //if (getSupportFragmentManager().getBackStackEntryCount() > 0){
          //  getSupportFragmentManager().popBackStack();
        //}
        saveRecyclerViewState();
    }

    private void saveRecyclerViewState() {
        // сохраняем состояние RecyclerView
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    public void onCallbackClick(boolean empty) {
        // Callback FROM SwipeRecyclerViewAdapter
        deleteMode(true);
        //bottomAppBar.performShow();
    }

    private void onClickDelete(boolean isDelete) {
        // Callback IN SwipeRecyclerViewAdapter
        rvAdapter.deleteSelectedItems(isDelete);
        deleteMode(false);
    }

    private void deleteMode(boolean isDeleteModeOn) {
        // Переход в режим удаления и восстановлние из него
        isSelectionModeOn = isDeleteModeOn;
        //bottomAppBar.setHideOnScroll(!isDeleteModeOn);

        if (isDeleteModeOn) {
            if (selNotesCategory != 2) {
                fabMain.setImageResource(R.drawable.baseline_delete_black_24);
            }
            bottomAppBar.setNavigationIcon(R.drawable.baseline_navigate_before_black_24);
        } else {
            if (selNotesCategory != 2) {
                fabMain.setImageResource(R.drawable.baseline_add_black_24);
            }
            bottomAppBar.setNavigationIcon(R.drawable.baseline_menu_black_24);
        }

        for (int i = 0; i < appBarMenu.size(); i++) {
            appBarMenu.getItem(i).setVisible(!isDeleteModeOn);
        }
    }

    @Override
    public void onItemClickBottomNavView(int selNotesCategory) {
        // Callback FROM BottomSheetDialog
        //this.selNotesCategory = selNotesCategory;
        initAdapter();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Обработка нажатия кнопки назад
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isSelectionModeOn) {
                onClickDelete(false); // Выход из режима удаления
                return false;
            } else {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}