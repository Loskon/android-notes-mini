package com.loskon.noteminimalism3.ui.recyclerview.profile

import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.databinding.ItemProfileBinding
import com.loskon.noteminimalism3.model.Note2

/**
 *
 */

class NoteListViewHolder(private val binding: ItemProfileBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(note: Note2) {
        binding.profile = note
        binding.executePendingBindings() // Используется, чтобы биндинг не откладывался
    }

    val getLinearLayout: LinearLayout
        get() {
            return binding.linearLayoutList
        }
}
