package com.loskon.noteminimalism3.auxiliary.main;

import android.app.Activity;
import android.content.res.Configuration;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loskon.noteminimalism3.R;
import com.loskon.noteminimalism3.auxiliary.note.MyKeyboard;
import com.loskon.noteminimalism3.auxiliary.other.MyColor;
import com.loskon.noteminimalism3.ui.recyclerview.MyRecyclerViewAdapter;

/**
 * Помощник для управления элементами в MainActivity
 */

public class MainWidgetsHelper {

    private final Activity activity;
    private final Menu appBarMenu;
    private final BottomAppBar bottomAppBar;
    private final FloatingActionButton fabMain;
    private final CardView cardView;
    private final int spanCount;

    private boolean isDeleteMode = false;

    public MainWidgetsHelper(Activity activity, BottomAppBar bottomAppBar, FloatingActionButton fabMain) {
        this.activity = activity;
        this.bottomAppBar = bottomAppBar;
        this.fabMain = fabMain;
        appBarMenu = bottomAppBar.getMenu();
        cardView = activity.findViewById(R.id.cardViewMain);
        spanCount = getScreenSize();
    }

    public void setSelectIcon(boolean isNotAllSelected) {
        // Смена иконок выделения
        int menuItem = R.id.action_select_item;

        if (isNotAllSelected) {
            setMenuIcon(menuItem, R.drawable.baseline_done_black_24);
        } else {
            setMenuIcon(menuItem, R.drawable.baseline_done_all_black_24);
        }

        setColorMenuIcon();
    }

    private void setMenuIcon(int menuItem, int icon) {
        appBarMenu.findItem(menuItem)
                .setIcon(ResourcesCompat.getDrawable(activity
                        .getResources(), icon, null));
    }

    private void setColorMenuIcon() {
        MyColor.setColorMenuItem(activity, appBarMenu);
    }

    public void setTypeNotes(RecyclerView recyclerView, boolean isTypeNotesSingle) {
        // Смена вида заметок и установка Layout Manager
        int menuItem = R.id.action_switch_view;

        if (isTypeNotesSingle) {
            setMenuIcon(menuItem, R.drawable.outline_dashboard_black_24);
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        } else {
            setMenuIcon(menuItem, R.drawable.outline_view_agenda_black_24);
            recyclerView.setLayoutManager(new
                    StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL));
        }

        setColorMenuIcon();
    }

    private int getScreenSize() {
        int screenSize = activity.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        int spanCount = 2;

        if (screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            spanCount = 3;
        }

        return spanCount;
    }

    public void changeIconFabSearch(boolean isSearchMode, int selNotesCategory) {
        // Смена иконки FloatingActionButton для режима поиска
        if (isSearchMode) {
            fabMain.setImageResource(R.drawable.baseline_search_off_black_24);
        } else {
            changeIconFab(selNotesCategory);
        }

        setColorMenuIcon();
        if (isDeleteMode) setFabIconTrash();

    }

    public void changeIconFab(int selNotesCategory) {
        // Смена иконки FloatingActionButton
        if (isDeleteMode) {
            setFabIconTrash();
        } else {
            if (selNotesCategory == 2) {
                setFabIconTrash();
            } else {
                fabMain.setImageResource(R.drawable.baseline_add_black_24);
            }
        }
    }

    private void setFabIconTrash() {
        fabMain.setImageResource(R.drawable.baseline_delete_black_24);
    }

    public void isSearchMode(SearchView searchView, boolean isSearchOn) {
        // Показывть/скрывать поиск
        if (isSearchOn) {
            searchOn(searchView);
        } else {
            searchOff(searchView);
        }

        setVisSearchMenuItem(!isSearchOn);
        setVisSwitchMenuItem(!isSearchOn);
    }

    private void searchOn(SearchView searchView) {
        searchView.setVisibility(View.VISIBLE);
        searchView.setFocusableInTouchMode(true);
        searchView.requestFocus();

        EditText searchText = searchView
                .findViewById(androidx.appcompat.R.id.search_src_text);
        MyKeyboard.showSoftKeyboard(activity, searchText);

        hideBar();

    }

    private void hideBar() {
        bottomAppBar.performHide();
        bottomAppBar.setNavigationIcon(null);
    }

    private void searchOff(SearchView searchView) {
        searchView.setQuery("", false);
        searchView.setVisibility(View.GONE);

        showBar(R.drawable.baseline_menu_black_24);

        setNavIconColor();
    }

    private void showBar(int icon) {
        bottomAppBar.performShow();
        setNavIcon(icon);
    }

    private void setNavIcon(int icon) {
        bottomAppBar.setNavigationIcon(icon);
    }

    public void setVisUniMenuItem(boolean isVisibleSelect) {
        appBarMenu.findItem(R.id.action_unification).setVisible(isVisibleSelect);
    }

    public void setVisSelectMenuItem(boolean isVisibleSelect) {
        appBarMenu.findItem(R.id.action_select_item).setVisible(isVisibleSelect);
    }

    public void deleteMode(boolean isDeleteMode, int selNotesCategory, boolean isSearchMode) {
        // Переход в режим удаления с учетом режима поиска
        this.isDeleteMode = isDeleteMode;

        if (isDeleteMode) {
            showBar(R.drawable.baseline_clear_black_24);
        } else {
            if (isSearchMode) {
                hideBar();
            } else {
                setNavIcon(R.drawable.baseline_menu_black_24);
            }
        }

        changeIconFabSearch(isSearchMode, selNotesCategory);
        setNavIconColor();

        setVisSelectMenuItem(isDeleteMode);
        setVisUniMenuItem(isDeleteMode);

        if (!isSearchMode) {
            setVisSwitchMenuItem(!isDeleteMode);
            setVisSearchMenuItem(!isDeleteMode);
        }

        setVisCardView(isDeleteMode);
    }

    private void setNavIconColor() {
        MyColor.setNavIconColor(activity, bottomAppBar);
    }

    private void setVisSearchMenuItem(boolean isVisibleSelect) {
        appBarMenu.findItem(R.id.action_search).setVisible(isVisibleSelect);
    }

    private void setVisSwitchMenuItem(boolean isVisibleSelect) {
        appBarMenu.findItem(R.id.action_switch_view).setVisible(isVisibleSelect);
    }

    public void setVisCardView(boolean isVisCardView) {
        // Показывать/скрыть CardView
        if (isVisCardView) {
            cardView.setVisibility(View.VISIBLE);
        } else {
            cardView.setVisibility(View.GONE);
        }
    }

    public void setHandlerSearchView(SearchView searchView, MyRecyclerViewAdapter rvAdapter) {
        // Установка обработчика поиска
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText.trim())) {
                    rvAdapter.getFilter().filter("");
                } else {
                    rvAdapter.getFilter().filter(newText.trim());
                }
                return true;
            }
        });

    }

    public void setColorItem() {
        // Установить цвет элементов
        int color = MyColor.getMyColor(activity);
        MyColor.setColorFab(activity, fabMain);
        MyColor.setNavIconColor(activity, bottomAppBar);
        MyColor.setColorMenuItem(activity, appBarMenu);
        cardView.setCardBackgroundColor(color);
    }
}
