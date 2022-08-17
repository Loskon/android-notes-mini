package com.loskon.noteminimalism3.app.presentation.screens.notelist.presentation

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loskon.noteminimalism3.app.base.datetime.formatString
import com.loskon.noteminimalism3.app.base.extension.view.setBackgroundTintColorKtx
import com.loskon.noteminimalism3.app.base.extension.view.setDebounceClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setShortLongClickListener
import com.loskon.noteminimalism3.app.base.extension.view.setSoftVisibleKtx
import com.loskon.noteminimalism3.app.base.extension.view.setTextSizeKtx
import com.loskon.noteminimalism3.databinding.ItemNoteNewBinding
import com.loskon.noteminimalism3.model.Note
import com.loskon.noteminimalism3.sharedpref.AppPreference
import com.loskon.noteminimalism3.viewbinding.viewBinding

class NoteListAdapter : RecyclerView.Adapter<NoteListAdapter.NoteListViewHolder>() {

    private var list: List<Note> = emptyList()

    private var onItemClickListener: ((Note, Int) -> Unit)? = null
    private var onItemLongClickListener: ((Note, Int) -> Unit)? = null

    private var linearListType: Boolean = true
    private var hasOneSizeCards: Boolean = false
    private var color: Int = 0
    private var titleFontSize: Int = 0
    private var dateFontSize: Int = 0
    private var numberLines: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder {
        hasOneSizeCards = AppPreference.hasOneSizeCards(parent.context)
        color = AppPreference.getColor(parent.context)
        titleFontSize = AppPreference.getTitleFontSize(parent.context)
        dateFontSize = AppPreference.getDateFontSize(parent.context)
        numberLines = AppPreference.getNumberLines(parent.context)
        return NoteListViewHolder(parent.viewBinding(ItemNoteNewBinding::inflate))
    }

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {
        val note = list[position]

        with(holder.binding) {
            tvCardNoteTitle.text = note.title.trim()
            tvCardNoteDate.text = note.createdDate.formatString()
            viewFavorite.setSoftVisibleKtx(note.isFavorite)
            root.setDebounceClickListener { onItemClickListener?.invoke(note, position) }
            root.setShortLongClickListener { onItemLongClickListener?.invoke(note, position) }

            tvCardNoteTitle.setTextSizeKtx(titleFontSize)
            tvCardNoteTitle.maxLines = numberLines
            tvCardNoteTitle.minLines = getMinLines()
            tvCardNoteDate.setTextSizeKtx(dateFontSize)
            viewFavorite.setBackgroundTintColorKtx(color)
            root.strokeColor = if (note.isChecked) color else Color.TRANSPARENT
        }
    }

    override fun getItemCount(): Int = list.size

    private fun getMinLines(): Int {
        return if (linearListType) {
            MAX_LINES
        } else {
            if (hasOneSizeCards) numberLines else MAX_LINES
        }
    }

    fun getItems(): List<Note> = list

    fun getNote(position: Int): Note = list[position]

    @SuppressLint("NotifyDataSetChanged")
    fun updateNoteList(list: List<Note>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: ((Note, Int) -> Unit)?) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: ((Note, Int) -> Unit)?) {
        this.onItemLongClickListener = onItemLongClickListener
    }

    fun setLinearList(hasLinearList: Boolean) {
        this.linearListType = hasLinearList
    }

    class NoteListViewHolder(val binding: ItemNoteNewBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private const val MAX_LINES = 1
    }
}