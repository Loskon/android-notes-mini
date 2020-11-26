package com.loskon.noteminimalism3.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


import com.daimajia.swipe.util.Attributes;
import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loskon.noteminimalism3.activity.mainHelper.CustomOnScrollListener;
import com.loskon.noteminimalism3.activity.mainHelper.CustomRecyclerViewEmpty;
import com.loskon.noteminimalism3.others.BottomSheetDialog;
import com.loskon.noteminimalism3.others.Callback;
import com.loskon.noteminimalism3.model.Note;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.others.RefreshView;
import com.loskon.noteminimalism3.preference.CustomPreferencesFragment;
import com.loskon.noteminimalism3.rv.SwipeRecyclerViewAdapter;
import com.loskon.noteminimalism3.db.DbAdapter;

import java.util.List;

/**
 * Основной класс заметок
 */

public class MainActivity extends AppCompatActivity implements Callback,
        BottomSheetDialog.ItemClickListenerBottomNavView {

    private SwipeRecyclerViewAdapter swipeAdapter;
    private RecyclerRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton fabMain;
    private TextView textEmpty;
    private Menu appBarMenu;
    private SharedPreferences sharedPref;
    private boolean isSwitchView, isSelectMode, isUpdateDate, createOrDel;
    private int selectedNoteMode;
    private String whereClauseForMode;
    private final Handler handler = new Handler();
    private DbAdapter dbAdapter;

    // Переменные для сохранения состояния RecyclerView
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;

    // Обратный вызов
    private Callback callbackListenerMain;

    public void setCallbackListenerMain(Callback callbackListenerMain) {
        this.callbackListenerMain = callbackListenerMain;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewAndOthersComponents();
        cleaningFromTrash();
        initAdapter ();
        differentHandlers();
    }

    public void initViewAndOthersComponents() {
        textEmpty = findViewById(R.id.textEmpty);
        recyclerView =  findViewById(R.id.recyclerView);
        bottomAppBar =  findViewById(R.id.btmAppBarMain);
        fabMain =  findViewById(R.id.fabMain);
        refreshLayout = findViewById(R.id.refresh_layout);
        appBarMenu = bottomAppBar.getMenu();
        dbAdapter = new DbAdapter(this);
    }

    private void cleaningFromTrash () {
        // Удаление по таймеру
        dbAdapter.open();
        dbAdapter.deleteByTimer(2);
        dbAdapter.close();
    }

    public void initAdapter() {
        noteViewMode();
        changeIconFab();
        loadNoteMode();
        switchView();
        dbAdapter.open();
        List<Note> notes = dbAdapter.getNotes(whereClauseForMode);
        swipeAdapter = new SwipeRecyclerViewAdapter(this, notes,
                selectedNoteMode, isSelectMode);
        swipeAdapter.setMode(Attributes.Mode.Single);
        swipeAdapter.setCallbackListenerSwipeAdapter(this);
        customHandlers();
        recyclerView.setAdapter(swipeAdapter);
        dbAdapter.close();
    }

    private void differentHandlers () {
        bottomAppBarHandler();
        fabHandler();
        refreshHandlerAndRedrawing();
    }

    private void bottomAppBarHandler() {
        // Элементы меню bottomAppBar
        bottomAppBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_switch_view:
                    swipeAdapter.closeAllItems();
                    handler.postDelayed(() -> {
                        toggleItemViewType();
                        switchView();
                    }, 200);
                    break;
            }
            return false;
        });

        // Кнопка навигации bottomAppBar
        bottomAppBar.setNavigationOnClickListener(v -> {
            if (isSelectMode) onClickDeleteOrClose(false);
            else bottomNavViewShow();
        });
    }

    public void toggleItemViewType () {
        // Переключение и сохранение вида линейный/сетка
        isSwitchView = !isSwitchView;
        saveTypeView();
    }

    private void saveTypeView() {
        sharedPref = getSharedPreferences("saveTypeView", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putBoolean("isSwitchView", isSwitchView);
        edit.apply();
    }

    private void switchView () {
        // Изменение вида списка при переключении
        if (isSwitchView) {
            appBarMenu.findItem(R.id.action_switch_view).
                    setIcon(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.baseline_dashboard_black_24, null));
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        } else {
            appBarMenu.findItem(R.id.action_switch_view).
                    setIcon(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.baseline_view_agenda_black_24, null));
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                    StaggeredGridLayoutManager.VERTICAL));
        }
    }

    private void bottomNavViewShow () {
        BottomSheetDialog bottomSheet =
                BottomSheetDialog.newInstance();
        bottomSheet.show(getSupportFragmentManager(),
                BottomSheetDialog.TAG);
    }

    private void fabHandler () {
        // Обработчик круглой кнопки
        fabMain.setOnClickListener(v -> {
            if (isSelectMode) {
                onClickDeleteOrClose(true);
            } else {
                if (selectedNoteMode == 2) {
                    // Удаление всех элементов из мусорки
                    dbAdapter.open();
                    dbAdapter.deleteAll();
                    dbAdapter.close();
                    initAdapter();
                } else {
                    Intent intent = NoteActivity.newIntent(this, selectedNoteMode);
                    startActivity(intent);
                }
            }
        });
    }

    private void refreshHandlerAndRedrawing () {
        // Высота
        refreshLayout.setRefreshTargetOffset(80);
        // С какой высоты заканчивается "анимация"
        refreshLayout.setAnimateToRefreshDuration(0);
        // Метод для настройки парамтров отображения
        refreshLayout.setRefreshView((new RefreshView(this)),
                (new RecyclerRefreshLayout.LayoutParams(200, 300)));
        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(false);
            swipeAdapter.closeAllItems();
            bottomAppBar.performShow();
        });
    }

    private void noteViewMode() {
        if (selectedNoteMode == 0) whereClauseForMode = "del_items = 0";
        else if (selectedNoteMode == 1) whereClauseForMode = "favorites = 1";
        else if (selectedNoteMode == 2) whereClauseForMode = "del_items = 1";
    }

    private void changeIconFab () {
        if (selectedNoteMode == 2) {
            fabMain.setImageResource(R.drawable.baseline_delete_forever_black_24);
        } else {
            fabMain.setImageResource(R.drawable.baseline_add_black_24);
        }
    }

    private void loadNoteMode() {
        sharedPref = this.getSharedPreferences("saveTypeView", MODE_PRIVATE);
        isSwitchView = sharedPref.getBoolean("isSwitchView", true);
    }

    private void customHandlers () {
        // Проверка на пустой список
        swipeAdapter.registerAdapterDataObserver(new
                CustomRecyclerViewEmpty(textEmpty, swipeAdapter));
        // Дополнительная проверка на пустой список (срабатывает при сменах активити)
        (new CustomRecyclerViewEmpty(textEmpty, swipeAdapter)).checkEmpty();
        // Закрытие боковых меню при скролле
        recyclerView.addOnScrollListener(new CustomOnScrollListener(swipeAdapter));
        // Закрытие бокового меню при клике на пустую область
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                if (motionEvent.getAction() != MotionEvent.ACTION_UP) {
                    return false;
                }
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if (child != null) {
                    return false;
                } else {
                    swipeAdapter.closeAllItems();
                    return true;
                }
            }
            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {}
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        swipeAdapter.notifyDataSetChanged();
        updateDateMethod();
        restoreRecyclerViewState();
        (new CustomPreferencesFragment()).savePositionSettings(getApplicationContext(), 0, 0);
    }

    private void updateDateMethod() {
        // Защита от установки адаптера при сворачивании
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            isUpdateDate = intent.getExtras().getBoolean("updateDate");
            createOrDel = intent.getExtras().getBoolean("createOrDel");
        }
        // Сохраняет состояние = false, вызвает установку адаптера = true
        // (необходимо для появления новой заметки в списке)
        if (isUpdateDate) {
            initAdapter ();
            intent.putExtra("updateDate", false);
            // Возвращает в начальную позицию, если создана новая заметка
            if (createOrDel) recyclerView.getLayoutManager().scrollToPosition(0);
        }
    }

    private void restoreRecyclerViewState () {
        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveRecyclerViewState();
    }

    private void saveRecyclerViewState () {
        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    public void onCallbackClick(boolean empty) {
        // Callback FROM SwipeRecyclerViewAdapter
        bottomAppBar.performShow();
        deleteMode(true);
    }

    private void onClickDeleteOrClose(boolean deleteOrClose) {
        // Callback IN SwipeRecyclerViewAdapter
        if (callbackListenerMain !=null) callbackListenerMain.onCallbackClick(deleteOrClose);
        deleteMode(false);
    }

    private void deleteMode(boolean startOrEnd) {
        isSelectMode = startOrEnd;
        if (startOrEnd) {
            if (selectedNoteMode != 2) fabMain.setImageResource(R.drawable.baseline_delete_black_24);
            bottomAppBar.setNavigationIcon(R.drawable.baseline_navigate_before_black_24);
        }
        else {
            if (selectedNoteMode != 2) fabMain.setImageResource(R.drawable.baseline_add_black_24);
            bottomAppBar.setNavigationIcon(R.drawable.baseline_menu_black_24);
        }
        for (int i = 0; i < appBarMenu.size(); i++) appBarMenu.getItem(i).setVisible(!startOrEnd);
        bottomAppBar.setHideOnScroll(!startOrEnd);
    }

    @Override
    public void onItemClickBottomNavView(int selectedNoteMode) {
        // Callback FROM BottomSheetDialog
        this.selectedNoteMode = selectedNoteMode;
        initAdapter();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Обработка нажатия кнопки назад
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isSelectMode) {
                // Выход из режима выбора элементов
                onClickDeleteOrClose(false);
                return false;
            } else {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}