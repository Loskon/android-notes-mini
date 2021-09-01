package com.loskon.noteminimalism3.ui.activities

import android.animation.ObjectAnimator
import android.util.Log
import android.view.Menu
import android.widget.GridLayout.VERTICAL
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.loskon.noteminimalism3.R
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

class WidgetHelperKt(private val activityKt: ListActivityKt) {

    companion object {
        private val TAG = "MyLogs_${WidgetHelperKt::class.java.simpleName}"

        const val ICON_FAB_ADD = "fab_icon_add"
        const val ICON_FAB_SEARCH_CLOSE = "fab_icon_search_close"
        const val ICON_FAB_DELETE = "fab_icon_delete"
    }

    private val fab: FloatingActionButton = activityKt.getFab
    private val bottomAppBar: BottomAppBar = activityKt.getBottomAppBar
    private val appBarMenu: Menu = bottomAppBar.menu

    private val color: Int = MyColor.getMyColor(activityKt)

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
            recyclerView.layoutManager = LinearLayoutManager(activityKt)
        } else {
            menuIdType = R.drawable.outline_view_agenda_black_24
            recyclerView.layoutManager = StaggeredGridLayoutManager(2, VERTICAL)
        }

        setMenuIcon(R.id.action_switch_view, menuIdType)
        setMenuIconColor()
    }

    private fun setMenuIcon(menuItem: Int, icon: Int) {
        appBarMenu.findItem(menuItem).icon = activityKt.getShortDrawable(icon)
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
            ICON_FAB_SEARCH_CLOSE -> R.drawable.baseline_search_off_black_24
            ICON_FAB_DELETE -> R.drawable.baseline_delete_black_24
            else -> throw Exception("Invalid icon")
        }

        fab.setImageDrawable(activityKt.getShortDrawable(iconIdFab))
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

    fun changeBarVisible(isVisible: Boolean) {
        if (isVisible) {
            activityKt.lifecycleScope.launch {
                delay(300L)
                bottomAppBar.performShow()
            }
        } else {
            bottomAppBar.performHide()
        }
    }
}