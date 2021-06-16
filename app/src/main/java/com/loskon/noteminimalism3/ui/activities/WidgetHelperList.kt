package com.loskon.noteminimalism3.ui.activities

import android.view.Menu
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.loskon.noteminimalism3.R
import com.loskon.noteminimalism3.auxiliary.other.MyColor
import com.loskon.noteminimalism3.utils.getShortDrawable
import com.loskon.noteminimalism3.utils.menuIconColor
import com.loskon.noteminimalism3.utils.setNavigationIconColor

/**
 *
 */

class WidgetHelperList(private val activity: ListActivity) {

    private val layout: CoordinatorLayout = activity.findViewById(R.id.coord_layout_list)
    private val fab: FloatingActionButton = activity.findViewById(R.id.fab_list)
    private val bottomAppBar: BottomAppBar = activity.findViewById(R.id.bottom_app_bar_list)
    private val appBarMenu: Menu = bottomAppBar.menu

    private val color: Int = MyColor.getMyColor(activity)

    init {
        bottomAppBar.setNavigationIconColor(color)
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
        setVisibleMenuItem(R.id.search_view, isVisible)
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
            recyclerView.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }

        setMenuIcon(R.id.action_switch_view, menuIdType)
        setMenuIconColor()
    }

    private fun setMenuIcon(menuItem: Int, icon: Int) {
        appBarMenu.findItem(menuItem).icon = activity.getShortDrawable(icon)
    }

    // Widgets
    fun setIconFab(isIconAdd: Boolean) {
        val iconIdFab: Int = if (isIconAdd) {
            R.drawable.baseline_add_black_24
        } else {
            R.drawable.baseline_navigate_next_black_48
        }

        fab.setImageDrawable(activity.getShortDrawable(iconIdFab))
    }

    fun setBottomBarVisible(isVisible: Boolean) {
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