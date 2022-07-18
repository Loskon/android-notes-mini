package com.loskon.noteminimalism3.app.presentation.screens.notelist

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.databinding.ItemNoteNewBinding
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.utils.DateUtil
import com.loskon.noteminimalism3.viewbinding.viewBinding

class NoteListAdapter : RecyclerView.Adapter<NoteListAdapter.NoteListViewHolder>() {

    private var list: List<Note> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder {
        return NoteListViewHolder(parent.viewBinding(ItemNoteNewBinding::inflate))
    }

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {
        val note = list[position]

        with(holder.binding) {
            tvCardNoteTitle.text = note.title.trim()
            tvCardNoteDate.text = DateUtil.getStringDate(note.dateCreation)
            root.setDebounceClickListener { }
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateNoteList(newList: List<Note>) {
        list = newList
        notifyItemRangeChanged(0, itemCount)
    }

    class NoteListViewHolder(val binding: ItemNoteNewBinding) : RecyclerView.ViewHolder(binding.root)
}