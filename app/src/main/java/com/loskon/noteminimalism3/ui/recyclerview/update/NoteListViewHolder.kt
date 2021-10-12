package com.loskon.noteminimalism3.ui.recyclerview.update

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.databinding.ItemNoteBinding
import com.loskon.noteminimalism3.model.Note2

/**
 * Вспомогательный класс, который предоставляет доступ к View-компонентам
 */

class NoteListViewHolder(private val binding: ItemNoteBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(note: Note2) {
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
