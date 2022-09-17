package com.loskon.noteminimalism3.app.screens.notelist.presentation

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class NoteListSwipeCallback : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {

    private var onItemSwipeListener: ((Int) -> Unit)? = null
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
        onItemSwipeListener?.invoke(viewHolder.absoluteAdapterPosition)
    }

    fun setOnItemSwipeListener(onItemSwipeListener: ((Int) -> Unit)?) {
        this.onItemSwipeListener = onItemSwipeListener
    }

    fun blockSwipe(deleteMode: Boolean) {
        this.deleteMode = deleteMode
    }
}
