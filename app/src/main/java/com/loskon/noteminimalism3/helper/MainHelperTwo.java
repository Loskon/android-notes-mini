package com.loskon.noteminimalism3.helper;

import android.app.Activity;
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
import com.loskon.noteminimalism3.rv.MyRecyclerViewAdapter;

public class MainHelperTwo {

    private final Activity activity;
    private final Menu appBarMenu;
    private final BottomAppBar bottomAppBar;
    private final FloatingActionButton fabMain;

    public MainHelperTwo(Activity activity, Menu appBarMenu, BottomAppBar bottomAppBar, FloatingActionButton fabMain) {
        this.activity = activity;
        this.appBarMenu = appBarMenu;
        this.bottomAppBar = bottomAppBar;
        this.fabMain = fabMain;
    }

    public void setVisSelMenuItem(boolean isVisibleSelect) {
        appBarMenu.findItem(R.id.action_select_item).setVisible(isVisibleSelect);
    }

    public void setSelectIcon(boolean isSelectOneOn) {
        if (isSelectOneOn) {
            setMenuIcon(R.drawable.baseline_done_black_24);
        } else {
            setMenuIcon(R.drawable.baseline_done_all_black_24);
        }

        setColorMenuIcon();
    }

    private void setColorMenuIcon() {
        MyColor.setColorMenuIcon(activity, appBarMenu);
    }

    private void setMenuIcon(int icon) {
        appBarMenu.findItem(R.id.action_select_item)
                .setIcon(ResourcesCompat.getDrawable(activity
                        .getResources(), icon, null));
    }

    public void setTypeNotes(RecyclerView recyclerView, boolean isTypeNotesSingleOn) {
        if (isTypeNotesSingleOn) {
            appBarMenu.findItem(R.id.action_switch_view).
                    setIcon(ResourcesCompat.getDrawable(activity.getResources(),
                            R.drawable.baseline_dashboard_black_24, null));

            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        } else {
            appBarMenu.findItem(R.id.action_switch_view).
                    setIcon(ResourcesCompat.getDrawable(activity.getResources(),
                            R.drawable.baseline_view_agenda_black_24, null));

            recyclerView.setLayoutManager(new
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }

        setColorMenuIcon();
    }

    public void changeIconFabSearch(boolean isSearchOn, int selNotesCategory) {
        if (isSearchOn) {
            fabMain.setImageResource(R.drawable.baseline_search_off_black_24);
        } else {
            changeIconFab(selNotesCategory);
        }
        setColorMenuIcon();
    }

    public void changeIconFab(int selNotesCategory) {
        if (selNotesCategory == 2) {
            fabMain.setImageResource(R.drawable.baseline_delete_black_24);
        } else {
            fabMain.setImageResource(R.drawable.baseline_add_black_24);
        }
    }

    public void isSearchMode(SearchView searchView, boolean isSearchOn) {
        if (isSearchOn) {

            searchView.setVisibility(View.VISIBLE);
            searchView.setFocusableInTouchMode(true);
            searchView.requestFocus();

            EditText searchText = searchView
                    .findViewById(androidx.appcompat.R.id.search_src_text);
            MyKeyboard.showSoftKeyboard(activity, searchText);

            bottomAppBar.setNavigationIcon(null);
            bottomAppBar.performHide();

        } else {
            searchView.setQuery("", false);
            searchView.setVisibility(View.GONE);

            bottomAppBar.performShow();
            bottomAppBar.setNavigationIcon(R.drawable.baseline_menu_black_24);
            setNavIconColor();
        }

        setVisSearchMenuItem(!isSearchOn);
        setVisSwitchMenuItem(!isSearchOn);
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

    public void deleteMode(boolean isDeleteModeOn, int selNotesCategory,
                           boolean isSearchOn, CardView cardView) {

        if (isDeleteModeOn) {

            cardView.setVisibility(View.VISIBLE);

            if (selNotesCategory != 2) {
                fabMain.setImageResource(R.drawable.baseline_delete_black_24);
            }

            bottomAppBar.performShow();

            bottomAppBar.setNavigationIcon(R.drawable.baseline_clear_black_24);
            setNavIconColor();

        } else {

            cardView.setVisibility(View.GONE);

            if (selNotesCategory != 2) {
                if (isSearchOn) {
                    fabMain.setImageResource(R.drawable.baseline_search_off_black_24);
                } else {
                    fabMain.setImageResource(R.drawable.baseline_add_black_24);
                }
            }

            if (isSearchOn) {
                bottomAppBar.setNavigationIcon(null);
                bottomAppBar.performHide();
            } else {
                bottomAppBar.setNavigationIcon(R.drawable.baseline_menu_black_24);
                setNavIconColor();
            }
        }

        setVisSelMenuItem(isDeleteModeOn);

        if (!isSearchOn) {
            setVisSwitchMenuItem(!isDeleteModeOn);
            setVisSearchMenuItem(!isDeleteModeOn);
        }
    }

    public void setHandlerSearchView(SearchView searchView, MyRecyclerViewAdapter rvAdapter) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            // Нажатие кнопки поиска на клавиатуре работает хероово
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            // Ввод текста в поисковой строке
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
}
