package com.loskon.noteminimalism3.ui.activities

import android.animation.ObjectAnimator
import android.util.Log
import android.view.Menu
import android.widget.GridLayout.VERTICAL
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.R.drawable.baseline_navigate_next_black_24
import com.loskon.noteminimalism3.auxiliary.other.MyColor
import com.loskon.noteminimalism3.utils.getShortDrawable
import com.loskon.noteminimalism3.utils.menuIconColor
import com.loskon.noteminimalism3.utils.setFabColor
import com.loskon.noteminimalism3.utils.setNavigationIconColor
import com.loskon.noteminimalism3.viewmodel.NoteViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Помощник для управления элементами активити
 */

private val TAG = "MyLogs_${WidgetHelperList::class.java.simpleName}"

class WidgetHelperList(private val activity: ListActivity) {

    companion object {
        const val ICON_FAB_ADD = "fab_icon_add"
        const val ICON_FAB_NAVIGATE_NEXT = "fab_icon_navigate_next"
        const val ICON_FAB_SEARCH_CLOSE = "fab_icon_search_close"
        const val ICON_FAB_DELETE = "fab_icon_delete"
    }

    private val layout: CoordinatorLayout = activity.findViewById(R.id.coord_layout_list)
    private val fab: FloatingActionButton = activity.findViewById(R.id.fab_list)
    private val bottomAppBar: BottomAppBar = activity.findViewById(R.id.bottom_app_bar_list)
    private val appBarMenu: Menu = bottomAppBar.menu

    private val color: Int = MyColor.getMyColor(activity)

    init {
        Log.d(TAG, "initialization")
        bottomAppBar.setNavigationIconColor(color)
        fab.setFabColor(color)
        setMenuIconColor()
    }


    // Menu
    fun setMenuIconColor() {
        appBarMenu.menuIconColor(color)
    }

    fun setVisibleUnification(isVisible: Boolean) {
        setVisibleMenuItem(R.id.action_unification, isVisible)
    }

    fun setVisibleSelect(isVisible: Boolean) {
        setVisibleMenuItem(R.id.action_select_item, isVisible)
    }

    fun setVisibleList(isVisible: Boolean) {
        setVisibleMenuItem(R.id.action_switch_view, isVisible)
        setVisibleMenuItem(R.id.action_search, isVisible)
        setMenuIconColor()
    }

    private fun setVisibleMenuItem(menuId: Int, isVisible: Boolean) {
        appBarMenu.findItem(menuId).isVisible = isVisible
    }

    fun setSelectIcon(isSelectOne: Boolean) {
        val menuIdSelect: Int = if (isSelectOne) {
            R.drawable.baseline_done_black_24
        } else {
            R.drawable.baseline_done_all_black_24
        }

        setMenuIcon(R.id.action_select_item, menuIdSelect)
        setMenuIconColor()
    }

    fun setTypeNotes(recyclerView: RecyclerView, isTypeNotesSingle: Boolean) {
        val menuIdType: Int

        if (isTypeNotesSingle) {
            menuIdType = R.drawable.outline_dashboard_black_24
            recyclerView.layoutManager = LinearLayoutManager(activity)
        } else {
            menuIdType = R.drawable.outline_view_agenda_black_24
            recyclerView.layoutManager = StaggeredGridLayoutManager(2, VERTICAL)
        }

        setMenuIcon(R.id.action_switch_view, menuIdType)
        setMenuIconColor()
    }

    private fun setMenuIcon(menuItem: Int, icon: Int) {
        appBarMenu.findItem(menuItem).icon = activity.getShortDrawable(icon)
    }

    fun startAnimateFab() {
        ObjectAnimator.ofFloat(fab, "rotation", 0f, 360f)
            .setDuration(300).start()
    }


    // Bottom App Bar
    fun setNavigationIcon(isDel: Boolean) {
        val navIconId: Int = if (isDel) {
            R.drawable.baseline_menu_black_24
        } else {
            R.drawable.baseline_close_black_24
        }

        bottomAppBar.setNavigationIcon(navIconId)
        bottomAppBar.setNavigationIconColor(color)
    }


    // Fab
    fun setIconFab(icon: String) {
        val iconIdFab: Int = when (icon) {
            ICON_FAB_ADD -> R.drawable.baseline_add_black_24
            ICON_FAB_NAVIGATE_NEXT -> baseline_navigate_next_black_24
            ICON_FAB_SEARCH_CLOSE -> R.drawable.baseline_search_off_black_24
            ICON_FAB_DELETE -> R.drawable.baseline_delete_black_24
            else -> throw Exception("Invalid icon")
        }

        fab.setImageDrawable(activity.getShortDrawable(iconIdFab))
    }

    fun setIconFab(category: String, isSearch: Boolean) {
        if (isSearch) {
            setIconFab(ICON_FAB_SEARCH_CLOSE)
            changeBarVisible(false)
        } else {
            if (category == NoteViewModel.CATEGORY_TRASH) {
                setIconFab(ICON_FAB_DELETE)
            } else {
                setIconFab(ICON_FAB_ADD)
            }
        }
    }

    fun setVisibleWidgets(isVisible: Boolean) {
        activity.lifecycleScope.launch {
            delay(300L)
            changeIconFabBetweenFragments(isVisible)
            changeBarVisible(isVisible)
        }
    }

    private fun changeIconFabBetweenFragments(isIconAdd: Boolean) {
        if (isIconAdd) {
            setIconFab(ICON_FAB_ADD)
        } else {
            setIconFab(ICON_FAB_NAVIGATE_NEXT)
        }
    }

    fun changeBarVisible(isVisible: Boolean) {
        if (isVisible) {
            bottomAppBar.performShow()
        } else {
            bottomAppBar.performHide()
        }
    }


    // Getters
    val getCoordLayout: CoordinatorLayout
        get() {
            return layout
        }

    val getFab: FloatingActionButton
        get() {
            return fab
        }

    val getBottomAppBar: BottomAppBar
        get() {
            return bottomAppBar
        }
}