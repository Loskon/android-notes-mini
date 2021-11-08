package com.loskon.noteminimalism3.ui.recyclerview.notes

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.databinding.RowNoteBinding
import com.loskon.noteminimalism3.model.Note

/**
 * Вспомогательный класс, который предоставляет доступ к View-компонентам
 */

class NotesListViewHolder(private val binding: RowNoteBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(note: Note) {
        binding.note = note
        binding.executePendingBindings() // Используется, чтобы биндинг не откладывался
    }

    // Getters
    val linearLayout: LinearLayout
        get() {
            return binding.linearLayoutList
        }

    val title: TextView
        get() {
            return binding.tvCardNoteTitle
        }

    val date: TextView
        get() {
            return binding.tvCardNoteDate
        }

    val viewFavorite: View
        get() {
            return binding.viewFavorite
        }
}
