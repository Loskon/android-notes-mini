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

    private lateinit var swipeListener: NoteSwipeListener

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
        val position: Int = viewHolder.absoluteAdapterPosition
        val note: Note = adapter.getNote(position)
        val isFavorite: Boolean = note.isFavorite
        swipeListener.onNoteSwipe(note, isFavorite)
    }

    //--- MainActivity -----------------------------------------------------------------------------
    fun blockSwiped(isDeleteMode: Boolean) {
        this.isDeleteMode = isDeleteMode
    }

    //--- interface ---------------------------------------------------------------------------------
    interface NoteSwipeListener {
        fun onNoteSwipe(note: Note, hasFavStatus: Boolean)
    }

    fun registerNoteSwipeListener(swipeListener: NoteSwipeListener) {
        this.swipeListener = swipeListener
    }
}
