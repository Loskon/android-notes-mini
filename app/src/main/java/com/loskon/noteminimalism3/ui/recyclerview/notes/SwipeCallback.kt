package com.loskon.noteminimalism3.ui.recyclerview.notes

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.model.Note

/**
 * Свайп и удаление
 */

class SwipeCallback(
    private val adapter: NoteRecyclerAdapter
) :
    ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {

    private var isDeleteMode: Boolean = false

    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return if (isDeleteMode) 0 else
            super.getSwipeDirs(recyclerView, viewHolder)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position: Int = viewHolder.bindingAdapterPosition
        val note: Note = adapter.getNote(position)
        val isFavorite: Boolean = note.isFavorite
        callback?.onNoteSwipe(note, isFavorite)
    }

    //--- MainActivity -----------------------------------------------------------------------------
    fun blockSwiped(isDeleteMode: Boolean) {
        this.isDeleteMode = isDeleteMode
    }

    //--- interface ---------------------------------------------------------------------------------
    interface NoteSwipeCallback {
        fun onNoteSwipe(note: Note, hasFavStatus: Boolean)
    }

    companion object {
        private var callback: NoteSwipeCallback? = null

        fun registerCallbackNoteSwipe(callback: NoteSwipeCallback) {
            this.callback = callback
        }
    }
}