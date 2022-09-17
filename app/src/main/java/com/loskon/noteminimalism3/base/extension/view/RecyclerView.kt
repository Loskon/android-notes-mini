package com.loskon.noteminimalism3.base.extension.view

import android.widget.GridLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

fun RecyclerView.setChangeableLayoutManager(linearListType: Boolean) {
    layoutManager = if (linearListType) {
        LinearLayoutManager(context)
    } else {
        StaggeredGridLayoutManager(2, GridLayout.VERTICAL)
    }
}

fun RecyclerView.scrollToTop() = scrollToPosition(0)
