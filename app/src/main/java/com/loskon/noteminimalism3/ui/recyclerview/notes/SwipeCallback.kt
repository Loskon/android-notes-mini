package com.loskon.noteminimalism3.ui.recyclerview.notes

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.commands.CommandCenter
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sqlite.DataBaseAdapter.Companion.CATEGORY_ALL_NOTES
import com.loskon.noteminimalism3.sqlite.DataBaseAdapter.Companion.CATEGORY_TRASH

/**
 * Свайп и удаление
 */

class SwipeCallback(
    private val adapter: NoteListAdapter,
    private val commandCenter: CommandCenter
) :
    ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {

    private var category: String = CATEGORY_ALL_NOTES
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

        if (category == CATEGORY_TRASH) {
            commandCenter.delete(note)
        } else {
            commandCenter.sendToTrash(note)
        }

        callback?.onSwipeDelete(note, isFavorite)
    }

    // Внешние методы
    fun setCategory(category: String) {
        this.category = category
    }

    fun blockSwiped(isDeleteMode: Boolean) {
        this.isDeleteMode = isDeleteMode
    }

    interface NoteSwipeCallback {
        fun onSwipeDelete(note: Note, hasFavStatus: Boolean)
    }

    companion object {
        private var callback: NoteSwipeCallback? = null

        fun registerCallbackNoteSwipe(callback: NoteSwipeCallback) {
            this.callback = callback
        }
    }
}