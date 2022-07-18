package com.loskon.noteminimalism3.app.base.extension.view

import android.os.SystemClock
import android.view.MenuItem
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.MenuRes
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import com.google.android.material.bottomappbar.BottomAppBar

fun BottomAppBar.setDebounceNavigationClickListener(debounceTime: Long = 600L, onClick: () -> Unit) {
    setNavigationOnClickListener(object : View.OnClickListener {

        private var lastClickTime: Long = 0

        override fun onClick(view: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) {
                return
            } else {
                onClick()
            }

            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

fun BottomAppBar.setDebounceMenuItemClickListener(debounceTime: Long = 600L, onClick: (item: MenuItem) -> Unit) {
    setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener {

        private var lastClickTime: Long = 0

        override fun onMenuItemClick(item: MenuItem): Boolean {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) {
                return false
            } else {
                onClick(item)
            }

            lastClickTime = SystemClock.elapsedRealtime()
            return true
        }
    })
}

fun BottomAppBar.setNavigationIconColor(@ColorInt color: Int) {
    navigationIcon?.mutate()?.setTint(color)
}

fun BottomAppBar.setMenuItemsColor(@ColorInt color: Int) {
    for (i in 0 until menu.size()) {
        menu[i].icon.setTint(color)
    }
}

fun BottomAppBar.setAllItemsColor(@ColorInt color: Int) {
    setNavigationIconColor(color)
    setMenuItemsColor(color)
}

fun BottomAppBar.setMenuItemVisibility(@MenuRes menuItemId: Int, visible: Boolean) {
    menu.findItem(menuItemId).isVisible = visible
}

fun BottomAppBar.setAllMenuItemsVisibility(visible: Boolean) {
    for (i in 0 until menu.size()) {
        menu[i].isVisible = visible
    }
}
