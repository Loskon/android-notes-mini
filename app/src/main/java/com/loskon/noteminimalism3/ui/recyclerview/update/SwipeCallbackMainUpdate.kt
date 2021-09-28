package com.loskon.noteminimalism3.ui.recyclerview.update

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.sqlite.DateBaseAdapter.Companion.CATEGORY_ALL_NOTES
import com.loskon.noteminimalism3.sqlite.DateBaseAdapter.Companion.CATEGORY_TRASH
import com.loskon.noteminimalism3.viewmodel.AppShortsCommand
import java.util.*

/**
 * Обработка свайпа для списка Program
 */

class SwipeCallbackMainUpdate(
    private val adapter: NoteListAdapterUpdate,
    private val shortsCommand: AppShortsCommand
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
        val note: Note2 = adapter.getNote(position)
        val isFavorite: Boolean = note.isFavorite

        if (notesCategory == CATEGORY_TRASH) {
            shortsCommand.delete(note)
        } else {
            note.dateDelete = Date()
            note.isDelete = true
            note.isFavorite = false
            shortsCommand.update(note)
        }

        callback?.onSwipeUpdate(note, isFavorite)
    }

    // Внешние методы
    fun setCategory(notesCategory: String) {
        this.notesCategory = notesCategory
    }

    fun blockSwiped(isDeleteMode: Boolean) {
        this.isDeleteMode = isDeleteMode
    }

    // Callback
    interface CallbackSwipeUpdate {
        fun onSwipeUpdate(note: Note2, isFavorite: Boolean)
    }

    companion object {
        private var callback: CallbackSwipeUpdate? = null

        fun callbackSwipeListener(callback: CallbackSwipeUpdate) {
            Companion.callback = callback
        }
    }
}