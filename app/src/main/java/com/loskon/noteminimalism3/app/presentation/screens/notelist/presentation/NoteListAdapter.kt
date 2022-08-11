package com.loskon.noteminimalism3.app.presentation.screens.notelist.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.app.base.datetime.formatString
import com.loskon.noteminimalism3.app.base.extension.view.setBackgroundTintColor
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setShortLongClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setSoftVisibleKtx
import com.loskon.noteminimalism3.databinding.ItemNoteNewBinding
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.viewbinding.viewBinding

class NoteListAdapter : RecyclerView.Adapter<NoteListAdapter.NoteListViewHolder>() {

    private var list: List<Note> = emptyList()

    private var onItemClickListener: ((Note) -> Unit)? = null
    private var onItemLongClickListener: ((Note) -> Unit)? = null

    private var hasLinearList: Boolean = true
    private var color: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder {
        color = AppPreference.getColor(parent.context)
        return NoteListViewHolder(parent.viewBinding(ItemNoteNewBinding::inflate))
    }

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {
        val note = list[position]

        with(holder.binding) {
            tvCardNoteTitle.text = note.title.trim()
            tvCardNoteDate.text = note.createdDate.formatString()
            viewFavorite.setBackgroundTintColor(color)
            viewFavorite.setSoftVisibleKtx(note.isFavorite)
            root.setDebounceClickListener { onItemClickListener?.invoke(note) }
            root.setShortLongClickListener { onItemLongClickListener?.invoke(note) }
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateNoteList(list: List<Note>) {
        this.list = list
        notifyItemRangeChanged(0, itemCount)
    }

    fun setOnItemClickListener(onItemClickListener: ((Note) -> Unit)?) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: ((Note) -> Unit)?) {
        this.onItemLongClickListener = onItemLongClickListener
    }

    fun setLinearList(hasLinearList: Boolean) {
        this.hasLinearList = hasLinearList
    }

    class NoteListViewHolder(val binding: ItemNoteNewBinding) : RecyclerView.ViewHolder(binding.root)
}