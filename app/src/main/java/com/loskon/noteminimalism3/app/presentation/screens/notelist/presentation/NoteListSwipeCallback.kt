package com.loskon.noteminimalism3.app.presentation.screens.notelist.presentation

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class NoteListSwipeCallback : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {

    private var onItemSwipeListener: ((viewHolder: RecyclerView.ViewHolder) -> Unit)? = null
    private var deleteMode: Boolean = false

    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return if (deleteMode) 0 else super.getSwipeDirs(recyclerView, viewHolder)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onItemSwipeListener?.invoke(viewHolder)
    }

    fun setOnItemSwipeListener(onItemSwipeListener: ((viewHolder: RecyclerView.ViewHolder) -> Unit)?) {
        this.onItemSwipeListener = onItemSwipeListener
    }

    fun blockSwipe(deleteMode: Boolean) {
        this.deleteMode = deleteMode
    }
}
