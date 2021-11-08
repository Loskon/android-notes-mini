package com.loskon.noteminimalism3.ui.recyclerview.notes

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.command.ShortsCommand
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sqlite.DateBaseAdapter.Companion.CATEGORY_ALL_NOTES
import com.loskon.noteminimalism3.sqlite.DateBaseAdapter.Companion.CATEGORY_TRASH
import java.util.*

/**
 * Обработка свайпа для списка Program
 */

class SwipeCallback(
    private val adapter: NotesListAdapter,
    private val shortsCommand: ShortsCommand
) :
    ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {

    private var notesCategory: String = CATEGORY_ALL_NOTES
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

        if (notesCategory == CATEGORY_TRASH) {
            shortsCommand.delete(note)
        } else {
            note.dateDelete = Date()
            note.isDelete = true
            note.isFavorite = false
            shortsCommand.update(note)
        }

        callback?.onSwipe(note, isFavorite)
    }

    // Внешние методы
    fun setCategory(notesCategory: String) {
        this.notesCategory = notesCategory
    }

    fun blockSwiped(isDeleteMode: Boolean) {
        this.isDeleteMode = isDeleteMode
    }

    interface CallbackSwipeUndo {
        fun onSwipe(note: Note, isFavorite: Boolean)
    }

    companion object {
        private var callback: CallbackSwipeUndo? = null

        fun listenerCallback(callback: CallbackSwipeUndo) {
            this.callback = callback
        }
    }
}