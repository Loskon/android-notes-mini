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
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


import com.daimajia.swipe.util.Attributes;
import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loskon.noteminimalism3.activity.mainHelper.OnItemTouchListenerSupport;
import com.loskon.noteminimalism3.activity.mainHelper.OnScrollListenerSupport;
import com.loskon.noteminimalism3.activity.mainHelper.RecyclerViewEmptySupport;
import com.loskon.noteminimalism3.others.BottomSheetDialog;
import com.loskon.noteminimalism3.others.Callback;
import com.loskon.noteminimalism3.model.Note;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.others.RefreshView;
import com.loskon.noteminimalism3.rv.SwipeRecyclerViewAdapter;
import com.loskon.noteminimalism3.db.DbAdapter;

import java.util.List;

/**
 * Основной класс заметок
 */

public class MainActivity extends AppCompatActivity implements Callback,
        BottomSheetDialog.ItemClickListenerBottomNavView {

    private SwipeRecyclerViewAdapter swipeAdapter;
    private RecyclerRefreshLayout mRefreshLayout;
    private RecyclerView recyclerView;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton floatingBtn;
    private TextView mTextView;
    private Menu appBarMenu;
    private SharedPreferences mSharedPref;
    private boolean isSwitchView, selectMode;
    private int noteValMode;
    private String whereClauseForMode;
    private final Handler handler = new Handler();
    private Callback listener;

    // setting the listener
    public void setListener (Callback listener)    {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        differentHandlers();
    }

    public void initView() {
        mTextView = findViewById(R.id.textView);
        recyclerView =  findViewById(R.id.recyclerView);
        bottomAppBar =  findViewById(R.id.bottomAppBar);
        floatingBtn  =  findViewById(R.id.floatingBtn);
        mRefreshLayout = findViewById(R.id.refresh_layout);
        appBarMenu = bottomAppBar.getMenu();
    }

    private void differentHandlers () {
        //recyclerView.setHasFixedSize(true);
        //bottomAppBar.setHideOnScroll(false);
        //this.setSupportActionBar(bottomAppBar);

        bottomAppBarHandler();

        floatingBtn.setOnClickListener(v -> {
            if (selectMode) {
                onClickDeleteOrClose(true);
            } else {
                Intent intent = new Intent(this, NoteActivity.class);
                //intent.putExtra("noteValMode", noteValMode);
                startActivity(intent);
            }
        });

        refreshHandler();
    }

    private void bottomAppBarHandler() {
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

        bottomAppBar.setNavigationOnClickListener(v -> {
            if (selectMode) onClickDeleteOrClose(false);
            else bottomNavViewShow();
        });
    }

    private void refreshHandler () {
        mRefreshLayout.setRefreshTargetOffset(140);
        mRefreshLayout.setAnimateToRefreshDuration(0);
        mRefreshLayout.setRefreshView((new RefreshView(this)),
                (new RecyclerRefreshLayout.LayoutParams(200, 300)));
        mRefreshLayout.setOnRefreshListener(() -> {
            mRefreshLayout.setRefreshing(false);
            swipeAdapter.closeAllItems();
            bottomAppBar.performShow();
        });
    }

    public void saveNoteView() {
        mSharedPref = this.getSharedPreferences("saveNoteView", MODE_PRIVATE);
        SharedPreferences.Editor edit = mSharedPref.edit();
        edit.putBoolean("isSwitchView", isSwitchView);
        edit.apply();
    }

    private void loadNoteView() {
        mSharedPref = getApplicationContext().getSharedPreferences("saveNoteView", MODE_PRIVATE);
        isSwitchView = mSharedPref.getBoolean("isSwitchView", true);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadNoteView();
        switchView();
        initAdapter();
    }

    public void toggleItemViewType () {
        isSwitchView = !isSwitchView;
        saveNoteView();
    }

    private void switchView () {
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

    public void initAdapter() {
        noteViewMode();
        DbAdapter adapter = new DbAdapter(this);
        adapter.open();
        List<Note> notes = adapter.getNotes(whereClauseForMode);
        swipeAdapter = new SwipeRecyclerViewAdapter(this, notes,noteValMode);
        customHandlers();
        swipeAdapter.setMode(Attributes.Mode.Single);
        swipeAdapter.setListener(this);
        recyclerView.setAdapter(swipeAdapter);
        adapter.close();
    }

    private void customHandlers () {
        // Проерка на пустой список
        swipeAdapter.registerAdapterDataObserver(new
                RecyclerViewEmptySupport(mTextView, swipeAdapter));
        // Закрытие боковых меню при скролле
        recyclerView.addOnScrollListener(new OnScrollListenerSupport(swipeAdapter));
        // Закрытие бокового меню при клике на пустую область
        recyclerView.addOnItemTouchListener(new OnItemTouchListenerSupport(swipeAdapter));
    }

    @Override
    public void onAddClick(boolean hz) {
        bottomAppBar.performShow();
        deleteMode(true);
    }

    private void bottomNavViewShow () {
        BottomSheetDialog bottomSheet =
                BottomSheetDialog.newInstance();
        bottomSheet.show(getSupportFragmentManager(),
                BottomSheetDialog.TAG);
    }

    private void onClickDeleteOrClose(boolean deleteOrNote) {
        if (listener!=null) listener.onAddClick(deleteOrNote);
        deleteMode(false);
    }

    private void deleteMode(boolean startOrEnd) {
        selectMode = startOrEnd;
        if (startOrEnd) {
            floatingBtn.setImageResource(R.drawable.baseline_delete_black_24);
            bottomAppBar.setNavigationIcon(R.drawable.baseline_navigate_before_black_24);
        }
        else {
            floatingBtn.setImageResource(R.drawable.baseline_add_black_24);
            bottomAppBar.setNavigationIcon(R.drawable.baseline_menu_black_24);
        }
        for (int i = 0; i < appBarMenu.size(); i++) appBarMenu.getItem(i).setVisible(!startOrEnd);
        bottomAppBar.setHideOnScroll(!startOrEnd);
    }

    @Override
    public void onItemClickBottomNavView(int item) {
        initAdapter();
        swipeAdapter.notifyDataSetChanged();
    }

    private void loadNoteMode() {
        mSharedPref = this.getSharedPreferences("saveNoteValModel", MODE_PRIVATE);
        noteValMode = mSharedPref.getInt("noteValMode", 0);
    }

    private void noteViewMode() {
        loadNoteMode();
        if (noteValMode == 0) whereClauseForMode = "del_items = 0";
        else if (noteValMode == 1) whereClauseForMode = "favorites = 1";
        else if (noteValMode == 2) whereClauseForMode = "del_items = 1";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        saveNoteMode ();
    }

    private void saveNoteMode() {
        mSharedPref = this.getSharedPreferences("saveNoteValModel", MODE_PRIVATE);
        SharedPreferences.Editor edit = mSharedPref.edit();
        edit.putInt("noteValMode", 0);
        edit.apply();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (selectMode) {
                onClickDeleteOrClose(false);
                return false;
            } else {
                MainActivity.this.finish();
                System.exit(0);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}