package com.loskon.noteminimalism3.ui.recyclerview.notes

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.databinding.RowNoteBinding
import com.loskon.noteminimalism3.model.Note

/**
 * Доступ к View-компонентам
 */

class NoteViewHolder(private val binding: RowNoteBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(note: Note) {
        binding.note = note
        binding.executePendingBindings() // Используется, чтобы биндинг не откладывался
    }

    // Getters
    val linearLayout: LinearLayout
        get() {
            return binding.linearLayoutRowNote
        }

    val viewFavorite: View
        get() {
            return binding.viewFavorite
        }

    val title: TextView
        get() {
            return binding.tvCardNoteTitle
        }

    val date: TextView
        get() {
            return binding.tvCardNoteDate
        }

    val card: CardView
        get() {
            return binding.cardViewNote
        }
}
