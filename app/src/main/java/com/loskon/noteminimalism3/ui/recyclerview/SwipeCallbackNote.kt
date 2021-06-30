package com.loskon.noteminimalism3.ui.recyclerview

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.model.Note2
import com.loskon.noteminimalism3.ui.recyclerview.profile.NoteListAdapter
import com.loskon.noteminimalism3.viewmodel.NoteViewModel
import com.loskon.noteminimalism3.viewmodel.NoteViewModel.Companion.CATEGORY_TRASH
import java.util.*

/**
 * Обработка свайпа для списка Program
 */

class SwipeCallbackNote(
    private val adapter: NoteListAdapter,
    private val viewModel: NoteViewModel
) :
    ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {

    private var notesCategory: String = NoteViewModel.CATEGORY_ALL_NOTES
    private var isDeleteMode: Boolean = false

    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return if (isDeleteMode) 0 else super.getSwipeDirs(
            recyclerView,
            viewHolder
        )
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position: Int = viewHolder.bindingAdapterPosition
        val note: Note2 = adapter.getItemNote(position)
        val isFav: Boolean = note.isFavorite

        if (notesCategory == CATEGORY_TRASH) {
            viewModel.delete(note)
        } else {
            note.dateDelete = Date()
            note.isDelete = true
            note.isFavorite = false
            viewModel.update(note)
        }

        swipeListener?.onItemSwipe(note, isFav ,notesCategory)
    }

    fun setCategory(category: String) {
        notesCategory = category
    }

    fun setBlockSwiped(isDelMode: Boolean) {
        isDeleteMode = isDelMode
    }



    // Callback
    interface OnItemSwipeListener {
        fun onItemSwipe(note: Note2, isFav: Boolean, category: String)
    }

    companion object {
        private var swipeListener: OnItemSwipeListener? = null

        fun setSwipeListener(swipeListener: OnItemSwipeListener) {
            this.swipeListener = swipeListener
        }
    }
}