package com.loskon.noteminimalism3.base.extension.view

import android.view.Menu
import androidx.annotation.MenuRes
import androidx.core.view.get

fun Menu.setItemVisibility(@MenuRes menuItemId: Int, visible: Boolean) {
    findItem(menuItemId).isVisible = visible
}

fun Menu.setAllItemsVisibility(visible: Boolean) {
    for (i in 0 until size()) {
        this[i].isVisible = visible
    }
}